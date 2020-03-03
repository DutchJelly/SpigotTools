package com.dutchjelly.spigottools.testing;

import com.dutchjelly.spigottools.commands.CustomCommandHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class ToolsTest extends JavaPlugin{


    @Override
    public void onEnable(){
        CustomCommandHandler cmdHandler = CustomCommandHandler.Init(this);
        cmdHandler.register(new SuperCommand());
    }
}
