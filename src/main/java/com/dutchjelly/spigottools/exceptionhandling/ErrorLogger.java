package com.dutchjelly.spigottools.exceptionhandling;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ErrorLogger {

    Logger logger;
    String pluginName;

    public ErrorLogger(JavaPlugin plugin){
        logger = plugin.getLogger();
        pluginName = plugin.getName();
    }

    public ErrorLogger(Logger logger, String pluginName){
        this.logger = logger;
        this.pluginName = pluginName;
    }

    public void log(String message){
        logger.log(Level.WARNING, "an error has occurred in " + pluginName + ": " + message);
    }

    public void log(String message, Exception e){
        logger.log(Level.WARNING, "an error has occurred in " + pluginName + ": " + message);
        logger.log(Level.WARNING, "exception: " + e.getMessage());
    }


}
