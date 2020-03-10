package com.dutchjelly.spigottools.files;

import com.dutchjelly.spigottools.exceptionhandling.ErrorLogger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

public class FileManager extends ErrorLogger{

    private String pluginName;

    protected Logger logger;
    protected File datafolder;
    private Map<String, File> cachedFiles;
    private Map<File, String> cachedFileContents;
    private Map<File, FileConfiguration> cachedFileConfigurations;



    public FileManager(JavaPlugin plugin){
        super(plugin);
        pluginName = plugin.getName();
        datafolder = ensureCreated(plugin.getDataFolder());
        logger = plugin.getLogger();
        cachedFiles = new HashMap<>();
        cachedFileContents = new HashMap<>();
        cachedFileConfigurations = new HashMap<>();
    }

    public File getFile(String name){
        if(cachedFiles.containsKey(name.toLowerCase())){
            return cachedFiles.get(name);
        }
        File file = ensureCreated(new File(datafolder, name));
        if(file != null)
            cachedFiles.put(name, file);
        return file;
    }

    public String getFileContent(String fileName){
        File file = getFile(fileName);
        if(file == null){
            return null;
        }
        return getFileContent(file);
    }

    public String getFileContent(File file){

        if(cachedFileContents.containsKey(file)){
            return cachedFileContents.get(file);
        }
        String content = "";
        try (Scanner scanner = new Scanner(file)){
            while(scanner.hasNextLine()){
                content += scanner.nextLine() + "\n";
            }
        } catch(FileNotFoundException e) {
            log("Can't read from file " + file.getName() + ".", e);
        }
        if(content.endsWith("\n")){
            content = content.substring(0, content.length()-1);
        }
        cachedFileContents.put(file, content);
        return content;
    }

    public boolean saveFileContent(String content, File file){
        String val;
        if((val = cachedFileContents.get(file)) != null){
            if(val == content)
                return true;
            cachedFileContents.remove(file);
        }
        cachedFileContents.put(file, content);
        OutputStream os = null;
        try{
            os = new FileOutputStream(file);
            os.write(content.getBytes(), 0, content.length());
        }catch (Exception e){
            log("Error writing to " + file.getName(), e);
            return false;
        } finally{
            try{
                if(os != null)
                    os.close();
            }catch(Exception e){
                log("Error closing the file " + file.getName(), e);
                return false;
            }
        }
        return true;
    }

    public FileConfiguration getConfig(String fileName){
        File file = getFile(fileName);
        if(file == null){
            return null;
        }
        return getConfig(file);
    }

    public FileConfiguration getConfig(File file){

        if(cachedFileConfigurations.containsKey(file)){
            return cachedFileConfigurations.get(file);
        }
        FileConfiguration fconfig = YamlConfiguration.loadConfiguration(file);
        if(fconfig == null){
            log("Failed to load the file configuration of " + file.getName() + ".");
            return null;
        }
        cachedFileConfigurations.put(file, fconfig);
        return fconfig;
    }

    public boolean saveConfig(FileConfiguration config, File file){
        if(!cachedFileConfigurations.containsKey(file)){
            cachedFileConfigurations.put(file, config);
        }
        //NOTE: We never have to update the cached configs when saving because the
        //FileConfiguration objects are always passed by reference.
        try {
            config.save(file);
        } catch (IOException e) {
            log("Can't save config for " + file.getName() + ".", e);
            return false;
        }
        return true;
    }

    private File ensureCreated(File file){
        if(file.exists())
            return file;
        if(file.isFile()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                log("Couldn't create a file named " + file.getName() + ".", e);
                return null;
            }
        }
        else if(file.isDirectory()){
            if(!file.mkdir()){
                log("Failed to create a directory named " + file.getName() + ".");
                return null;
            }
        }
        return file;
    }
}
