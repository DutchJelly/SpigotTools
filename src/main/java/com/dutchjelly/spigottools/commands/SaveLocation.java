package com.dutchjelly.spigottools.commands;

import com.dutchjelly.spigottools.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SaveLocation implements ICommand {
    @Override
    public String[] getPath() {
        return new String[]{"sl", "savelocation", "location.save"};
    }

    @Override
    public String[] getPermissions() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Saves a location for you.";
    }

    @Override
    public void handlePlayerCommand(Player p, List<String> args) {
        if(args.size() != 1) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&ePlease give a name for this location to save it."));
            return;
        }

        if(!Main.getPlugin().saveLocation(p.getUniqueId(), args.get(0), p.getLocation())) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eThat name is not unique."));
            return;
        }

        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2Saved your location: " + Main.getPlugin().toLocationString(p.getLocation())));
    }

    @Override
    public void handleConsoleCommand(CommandSender sender, List<String> args) {
        sender.sendMessage("That command is not meant for console senders.");
    }
}
