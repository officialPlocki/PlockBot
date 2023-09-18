package xyz.plocki.plockbot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.plocki.plockbot.commands.*;
import xyz.plocki.plockbot.listeners.ConnectionListener;
import xyz.plocki.plockbot.util.IPGetter;
import xyz.plocki.plockbot.util.LogFilter;
import xyz.plocki.plockbot.util.manager.BlockedIPsManager;
import xyz.plocki.plockbot.util.manager.WhitelistManager;
import xyz.plocki.plockbot.util.manager.ConfigManager;
import xyz.plocki.plockbot.util.manager.LanguageManager;

import java.io.IOException;
import java.util.*;

public class PlockBot extends JavaPlugin {

    public static String prefix = "§b§lPlock§c§lBot §8» §7";
    public static PlockBot instance;

    @Override
    public void onEnable() {
        instance = this;
        prefix = new ConfigManager().getPrefix();
        new LanguageManager();
        ((Logger) LogManager.getRootLogger()).addFilter(new LogFilter());
        Bukkit.getPluginManager().registerEvents(new ConnectionListener(), this);
        Objects.requireNonNull(getCommand("verbose")).setExecutor(new VerboseCommand());
        Objects.requireNonNull(getCommand("plockbot")).setExecutor(new PlockBotCommand());
        Objects.requireNonNull(getCommand("pb")).setExecutor(new PlockBotCommand());
        Objects.requireNonNull(getCommand("pbw")).setExecutor(new PlockBotWhitelistCommand());
        Objects.requireNonNull(getCommand("plockbotwhitelist")).setExecutor(new PlockBotWhitelistCommand());
        Objects.requireNonNull(getCommand("bips")).setExecutor(new BlockedIPsCommand());
        Objects.requireNonNull(getCommand("blockedips")).setExecutor(new BlockedIPsCommand());
        WhitelistManager.load();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            Map<String, Integer> map = new HashMap<>();
            Bukkit.getOnlinePlayers().forEach(player -> {
                if(map.containsKey(Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress())) {
                    player.kickPlayer("");
                } else {
                    map.put(player.getAddress().getAddress().getHostAddress(), 1);
                }
            });
            map.clear();
        }, 0, 20*5);
        if(new ConfigManager().autoIPUnblockIsEnabled()) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
                new BlockedIPsManager().reset();
                Bukkit.getConsoleSender().sendMessage(prefix + new ConfigManager().getIPUnblockMessage());
            }, 0, 20*5);
        }
        try {
            BlockedIPsManager.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bukkit.getScheduler().scheduleAsyncDelayedTask(this, () -> {
            try {
                IPGetter.getJsonIPs();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 20);
    }

    @Override
    public void onDisable() {
        WhitelistManager.save();
        try {
            BlockedIPsManager.unload();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
