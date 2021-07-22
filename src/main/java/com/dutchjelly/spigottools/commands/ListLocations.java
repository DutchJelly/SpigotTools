package com.dutchjelly.spigottools.commands;

import com.dutchjelly.spigottools.Main;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class ListLocations implements ICommand {
    @Override
    public String[] getPath() {
        return new String[] {"ll", "listlocations", "locations.list"};
    }

    @Override
    public String[] getPermissions() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Lists all your saved locations.";
    }

    @Override
    public void handlePlayerCommand(Player p, List<String> args) {
        Map<String, Location> saved = Main.getPlugin().getSavedLocations(p.getUniqueId());
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&dThese are your saved locations: "));
        saved.forEach((x,y) -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', "   &d" + x + "   &2" + Main.getPlugin().toLocationString(y).replace("@", "&d@&2"))));
    }

    @Override
    public void handleConsoleCommand(CommandSender sender, List<String> args) {
        sender.sendMessage("That command is not meant for console senders.");
    }
}
