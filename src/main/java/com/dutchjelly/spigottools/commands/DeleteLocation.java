package com.dutchjelly.spigottools.commands;

import com.dutchjelly.spigottools.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class DeleteLocation implements ICommand {
    @Override
    public String[] getPath() {
        return new String[]{"dl", "deletelocation", "location.delete"};
    }

    @Override
    public String[] getPermissions() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Deletes a location that you've saved.";
    }

    @Override
    public void handlePlayerCommand(Player p, List<String> args) {
        if(args.size() != 1) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&ePlease specify the name of the location to delete."));
            return;
        }

        if(!Main.getPlugin().deleteLocation(p.getUniqueId(), args.get(0))) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eThat location isn't in the saved list."));
            return;
        }

        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2Deleted your location " + args.get(0)));
    }

    @Override
    public void handleConsoleCommand(CommandSender sender, List<String> args) {
        sender.sendMessage("That command is not meant for console senders.");
    }
}
