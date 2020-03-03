package com.dutchjelly.spigottools.testing;

import com.dutchjelly.spigottools.commands.ICommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SuperCommand implements ICommand {

    @Override
    public String[] getPath() {
        return new String[]{"hello.jelle","hoi.jelle"};
    }

    @Override
    public String[] getPermissions() {
        return new String[0];
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void handlePlayerCommand(Player p, List<String> args) {
        p.sendMessage("hello " + String.join(" ", args));
    }

    @Override
    public void handleConsoleCommand(CommandSender sender, List<String> args) {

    }
}
