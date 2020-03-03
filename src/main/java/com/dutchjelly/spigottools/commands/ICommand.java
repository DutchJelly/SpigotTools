package com.dutchjelly.spigottools.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public interface ICommand {

    String[] getPath();
    String[] getPermissions();
    String getDescription();

    void handlePlayerCommand(Player p, List<String> args);
    void handleConsoleCommand(CommandSender sender, List<String> args);

}
