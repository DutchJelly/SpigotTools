package com.dutchjelly.spigottools.files;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class ConfigHandler {

    private JavaPlugin plugin;

    private Map<File, FileConfiguration> cache;

    File dataFolder;

    private ConfigHandler(){
        cache = new HashMap<>();
        plugin.getDataFolder();
    }

    public static ConfigHandler init(JavaPlugin plugin){
        ConfigHandler handler = new ConfigHandler();
        handler.plugin = plugin;
        return handler;
    }

    public void ensureFilesExist(List<String> files){
        files.forEach(x -> ensureFileExists(x));
    }

    public void ensureFileExists(String file){

    }

    public <T extends ConfigurationSerializable> T getObject(String file, String path){
        return null;
    }

    public void save(String file, Object item){

    }

    private void ensureCreated(File file, boolean dir){
        if(!file.exists()){
            if(dir){
                file.mkdir();
            }else{
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    plugin.getLogger().log(Level.WARNING, "Failed to create a new file.");
                }
            }
        }
    }

}
