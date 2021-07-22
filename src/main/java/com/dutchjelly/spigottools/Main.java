package com.dutchjelly.spigottools;

import com.dutchjelly.spigottools.commands.*;
import com.dutchjelly.spigottools.exceptionhandling.ErrorLogger;
import com.dutchjelly.spigottools.util.Pair;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.stream.Collectors;

public class Main extends JavaPlugin implements Listener {

    private static final String LOCATION_SAVE_PREFIX = "saved-locations";

    private static Main plugin;
    private ErrorLogger errorLogger;
    private CustomCommandHandler cmdHandler;
    private List<UUID> hiding = new ArrayList<>();
    private Map<UUID, Map<String, Location>> savedLocations = new HashMap<>();

    public static Main getPlugin() {
        return plugin;
    }

    public List<UUID> getHiding() {
        return hiding;
    }

    @Override
    public void onEnable() {
        plugin = this;

        errorLogger = new ErrorLogger(this);

        cmdHandler = CustomCommandHandler.Init(this);
        cmdHandler.register(new FindBert());
        cmdHandler.register(new ToggleTabCords());
        cmdHandler.register(new DeleteLocation());
        cmdHandler.register(new SaveLocation());
        cmdHandler.register(new ListLocations());


        if(getConfig().contains("hiding"))
            hiding.addAll(getConfig().getStringList("hiding").stream().map(x -> UUID.fromString(x)).collect(Collectors.toList()));

        if(getConfig().contains(LOCATION_SAVE_PREFIX))
            getConfig().getConfigurationSection(LOCATION_SAVE_PREFIX).getKeys(false).forEach(x ->
                    //Yes.. it's a bit strange that the deserialize method uses getConfig() and a pId because
                    //this reduces the modularity as it now depends on a fixed config path... :P
                    savedLocations.put(UUID.fromString(x), deserializeSavedLocations(UUID.fromString(x)))
            );


        getServer().getScheduler().runTaskTimer(this, () -> {

            for(Player p : getServer().getOnlinePlayers()) {
                updatePlayerTabCords(p);
            }
        }, 0L, 20L);


        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        updatePlayerTabCords(e.getPlayer());
    }

    //@return if the player is now hidden
    public boolean toggleHiding(Player p) {
        boolean hidden;
        if(hiding.contains(p.getUniqueId())){
            hiding.remove(p.getUniqueId());
            hidden = false;
        } else {
            hiding.add(p.getUniqueId());
            hidden = true;
        }
        getConfig().set("hiding", hiding);
        saveConfig();
        return hidden;
    }

    public void updatePlayerTabCords(Player p) {
        if(hiding.contains(p.getUniqueId())) {
            p.setPlayerListName(ChatColor.translateAlternateColorCodes('&',
                    p.getDisplayName() + "  &2@&r[&d?&r,&d?&r,&d?&r]"));
        } else {
            p.setPlayerListName(ChatColor.translateAlternateColorCodes('&',
                    p.getName() + "  &2@&r[&d" + p.getLocation().getBlockX() + "&r,&d" + p.getLocation().getBlockY() + "&r,&d" + p.getLocation().getBlockZ() + "&r]"));
        }
    }

    public Location getSavedLocation(UUID pId, String locationName) {
        return savedLocations.getOrDefault(pId, new HashMap<>()).getOrDefault(locationName, null);
    }

    public Map<String, Location> getSavedLocations(UUID pId) {
        return savedLocations.getOrDefault(pId, new HashMap<>());
    }

    public boolean saveLocation(UUID pId, String name, Location location) {
        if(!savedLocations.containsKey(pId)) {
            savedLocations.put(pId, new HashMap<String, Location>(){{put(name,location);}});
        } else {
            if(savedLocations.get(pId).containsKey(name)) {
                //location already exists
                return false;
            }
            savedLocations.get(pId).put(name, location);
        }

        //update config
        getConfig().set(LOCATION_SAVE_PREFIX + "." + pId.toString(), serializeSavedLocations(pId));
        saveConfig();
        return true;
    }

    public boolean deleteLocation(UUID pId, String name) {
        if(!savedLocations.containsKey(pId) || !savedLocations.get(pId).containsKey(name)) return false;

        savedLocations.get(pId).remove(name);

        //update config
        getConfig().set(LOCATION_SAVE_PREFIX + "." + pId.toString(), serializeSavedLocations(pId));
        saveConfig();
        return true;
    }

    public String toLocationString(Location location) {
        return new StringBuilder()
                .append(location.getBlockX())
                .append(',').append(location.getBlockY())
                .append(',').append(location.getBlockZ())
                .append(" @").append(location.getWorld().getName()).toString();
    }

    public Location fromLocationString(String locationString) {
        String[] locationStringComponents = locationString.split(",| @");
        if(locationStringComponents.length != 4) {
            errorLogger.log("Invalid location format: " + locationString);
            return new Location(getServer().getWorlds().get(0), 0,0,0);
        }

        return new Location(
                getServer().getWorld(locationStringComponents[3]),
                Integer.valueOf(locationStringComponents[0]),
                Integer.valueOf(locationStringComponents[1]),
                Integer.valueOf(locationStringComponents[2])
        );
    }

    private List<String> serializeSavedLocations(UUID playerId) {
        if(!savedLocations.containsKey(playerId)) return new ArrayList<>();

        return savedLocations.get(playerId).entrySet().stream().map(
                x -> x.getKey() + ":" + toLocationString(x.getValue())
        ).collect(Collectors.toList());
    }

    private Map<String, Location> deserializeSavedLocations(UUID playerId) {
        if(!getConfig().contains(LOCATION_SAVE_PREFIX + "." + playerId.toString())) return new HashMap<>();
        List<String> configValues = getConfig().getStringList(LOCATION_SAVE_PREFIX + "." + playerId.toString());

        Map<String, Location> deserialized = new HashMap<>();
        for (String configValue : configValues) {
            String[] splitConfigValue = configValue.split(":", 2);
            deserialized.put(splitConfigValue[0], fromLocationString(splitConfigValue[1]));
        }
        return deserialized;
    }
}
