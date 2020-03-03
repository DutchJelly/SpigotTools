package com.dutchjelly.spigottools.commands;

import javafx.util.Pair;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomCommandHandler implements TabCompleter, CommandExecutor{

    private JavaPlugin plugin;
    private List<ICommand> registered;

    private CustomCommandHandler(){}

    public static CustomCommandHandler Init(JavaPlugin plugin){
        CustomCommandHandler handler = new CustomCommandHandler();
        handler.plugin = plugin;
        handler.registered = new ArrayList<>();
        return handler;
    }

    public void register(ICommand cmd){
        List<String> aliases = new ArrayList<>();
        for(String path : cmd.getPath()){
            if(path.contains(".")){
                aliases.add(path.substring(0, path.indexOf('.')));
            } else{
                aliases.add(path);
            }
        }
        PluginCommand pCmd = CommandRegisterer.registerCommand(plugin, aliases.toArray(new String[aliases.size()]));
        pCmd.setExecutor(this);
        pCmd.setTabCompleter(this);
        registered.add(cmd);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command c, String label, String[] argsArray) {
        List<String> tabCompletions = new ArrayList<>();
        List<String> args = getArgsList(label, argsArray);
        String commandPrefix = String.join(".", args).toLowerCase();
        int substrIndex = commandPrefix.lastIndexOf('.')+1;

        System.out.println(commandPrefix);

        for(ICommand cmd : registered){
            if(!hasPermission(cmd, sender))
                continue;

            for(String cmdPath : cmd.getPath()){
                if(cmdPath.startsWith(commandPrefix)){
                    String completionPath = cmdPath.substring(substrIndex);
                    if(completionPath.contains("."))
                        tabCompletions.add(completionPath.substring(0, completionPath.indexOf('.')));
                    else
                        tabCompletions.add(completionPath);
                }
            }
        }
        return tabCompletions;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] argsArray) {
        List<String> args = getArgsList(label, argsArray);
        Pair<ICommand,List<String>> matchingCommandPair = findCommand(args);
        if(matchingCommandPair == null){
            sender.sendMessage("Command not found.");
            return true;
        }
        ICommand matchingCommand = matchingCommandPair.getKey();
        List<String> leftoverArgs = matchingCommandPair.getValue();

        if(!hasPermission(matchingCommand, sender)){
            sender.sendMessage("You don't have permissions to execute that command.");
            return true;
        }

        if(sender instanceof Player){
            matchingCommand.handlePlayerCommand((Player)sender, leftoverArgs);
        }else{
            matchingCommand.handleConsoleCommand(sender, leftoverArgs);
        }
        return true;
    }

    private boolean hasPermission(ICommand cmd, CommandSender sender){
        if(cmd.getPermissions().length == 0)
            return true;
        for(String perm : cmd.getPermissions()){
            if(sender.hasPermission(perm))
                return true;
        }
        return false;
    }

    private List<String> getArgsList(String label, String[] args){
        List<String> allArgs = new ArrayList<>();
        allArgs.add(label);
        allArgs.addAll(Arrays.asList(args));
        return allArgs;
    }

    private Pair<ICommand,List<String>> findCommand(List<String> args){
        for(ICommand cmd : registered){
            for(String aliasPath : cmd.getPath()){
                int i;
                String[] splitPath = aliasPath.split("\\.");
                for(i = 0; i < splitPath.length; i++){
                    if(i >= args.size())
                        break;
                    if(!args.get(i).equalsIgnoreCase(splitPath[i]))
                        break;
                }
                if(i == splitPath.length){
                    List<String> leftoverArgs = new ArrayList<>();
                    if(i < args.size()){
                        leftoverArgs = args.subList(i, args.size());
                    }
                    return new Pair(cmd,leftoverArgs);
                }

            }
        }
        return null;
    }
}
