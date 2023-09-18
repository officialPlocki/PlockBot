package xyz.plocki.plockbot.util.manager;

import org.bukkit.configuration.file.YamlConfiguration;
import xyz.plocki.plockbot.util.IPGetter;

import java.io.*;
import java.util.ArrayList;

public class BlockedIPsManager {

    public static void init() throws IOException {
        File blockedIPsFile = new File("plugins/PlockBot/blockedips.list");
        if(!blockedIPsFile.exists()) {
            blockedIPsFile.createNewFile();
        }
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(blockedIPsFile);
        if(!yml.isSet("blockedIPs")) {
            yml.set("blockedIPs", new ArrayList<String>());
            yml.save(blockedIPsFile);
        }
        IPGetter.blockedIPs.addAll(yml.getStringList("blockedIPs"));
    }

    public boolean isBlocked(String ip) {
        return IPGetter.blockedIPs.contains(ip);
    }

    public void blockIp(String ip) throws IOException {
        IPGetter.blockedIPs.add(ip);
    }

    public void reset() {
        IPGetter.blockedIPs.clear();
    }

    public void unblockIp(String ip) throws IOException {
        IPGetter.blockedIPs.remove(ip);
    }

    public static void unload() throws IOException {
        File blockedIPsFile = new File("plugins/PlockBot/blockedips.list");
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(blockedIPsFile);
        yml.set("blockedIPs", IPGetter.blockedIPs);
        yml.save(blockedIPsFile);
    }

}
