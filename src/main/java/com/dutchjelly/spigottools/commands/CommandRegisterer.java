package com.dutchjelly.spigottools.commands;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.logging.Level;

import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

//Credits to https://bukkit.org/members/elchilen0.90636997/ for posing a
//tutorial of this on the bukkit forums.
class CommandRegisterer {

    public static PluginCommand registerCommand(JavaPlugin plugin, String[] aliases){
        PluginCommand cmd = plugin.getCommand(aliases[0]);
        if(cmd != null)
            return cmd;
        cmd = makeServerCommand(plugin, aliases[0]);
        cmd.setAliases(Arrays.asList(aliases));
        getServerCommandMapping(plugin).register(plugin.getName(), cmd);
        return cmd;
    }

    private static PluginCommand makeServerCommand(JavaPlugin plugin, String label){
        try{
            //The constructor looks like this: "protected PluginCommand(String name, Plugin owner)"
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);
            return constructor.newInstance(label, plugin);
        }catch(Exception e){
            //TODO add proper logger to show error message
            plugin.getLogger().log(Level.WARNING, "Error with initialising a command with label " + label + ".");
        }
        return null;
    }

    private static CommandMap getServerCommandMapping(JavaPlugin plugin){
        try{
            Field commandMapField = SimplePluginManager.class.getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            return (CommandMap)commandMapField.get(plugin.getServer().getPluginManager());
        }catch(Exception e){
            //TODO add proper logger to show error message
            plugin.getLogger().log(Level.WARNING, "Error getting the server command mapping.");
        }
        return null;
    }
}
