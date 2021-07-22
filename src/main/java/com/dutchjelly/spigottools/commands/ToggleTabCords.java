package com.dutchjelly.spigottools.commands;

import com.dutchjelly.spigottools.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ToggleTabCords implements ICommand {
    @Override
    public String[] getPath() {
        return new String[]{"togglecords", "ttc", "toggletabcords", "hide"};
    }

    @Override
    public String[] getPermissions() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Toggles between showing your cords in the tab list and hiding them.";
    }

    @Override
    public void handlePlayerCommand(Player p, List<String> args) {
        if(Main.getPlugin().toggleHiding(p)) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&',"&dYou are now &2hidden&d."));
        } else {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&dEveryone can see your cords now."));
        }
    }

    @Override
    public void handleConsoleCommand(CommandSender sender, List<String> args) {
        sender.sendMessage("This command is not for console senders.");
    }
}
