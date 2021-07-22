package com.dutchjelly.spigottools.commands;

import com.dutchjelly.spigottools.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class FindBert implements ICommand {
    @Override
    public String[] getPath() {
        return new String[] {"find", "cords", "findbert"};
    }

    @Override
    public String[] getPermissions() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void handlePlayerCommand(Player p, List<String> args) {
        handleConsoleCommand(p, args);
    }

    @Override
    public void handleConsoleCommand(CommandSender sender, List<String> args) {
        if(args.size() != 1) {
            sender.sendMessage(ChatColor.YELLOW + "Please only use one name as an argument.");
            return;
        }
        Player found = Main.getPlugin().getServer().getOnlinePlayers().stream().filter(x -> args.get(0).equalsIgnoreCase(x.getName())).findFirst().orElse(null);
        Main plugin = Main.getPlugin();
        if(plugin.getHiding().contains(found.getUniqueId())) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&dThat player's coordinates are &2hidden&d."));
            return;
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&dPlayer " + args.get(0) + " is at &2" + found.getLocation().getBlockX() + "&d, &2" + found.getLocation().getBlockY() + "&d, &2" + found.getLocation().getBlockZ() + "&d. You can also see this in the tab list."));
    }
}
