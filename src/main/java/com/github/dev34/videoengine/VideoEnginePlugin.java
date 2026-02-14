package com.github.dev34.videoengine;

import com.github.dev34.videoengine.commands.VideoEngineCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class VideoEnginePlugin extends JavaPlugin {

    public static VideoEnginePlugin plugin;

    @Override
    public void onEnable() {
        plugin = this;
        Bukkit.getPluginCommand("videoengine").setExecutor(new VideoEngineCommand());
        getLogger().info(getName() + " has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info(getName() + " has been disabled!");
    }

    public static boolean isPUA(String charCode){
        if (charCode != null && !charCode.isEmpty()){
            if (charCode.codePointCount(0, charCode.length()) == 1) {
                int codePoint = charCode.codePointAt(0);

                return (codePoint >= 0xE000 && codePoint <= 0xF8FF) ||
                        (codePoint >= 0xF0000 && codePoint <= 0xFFFFD) ||
                        (codePoint >= 0x100000 && codePoint <= 0x10FFFD);
            }
            return false;
        }
        return false;
    }

    public static String colorize(String str){
        return ChatColor.translateAlternateColorCodes('&', str);
    }

}
