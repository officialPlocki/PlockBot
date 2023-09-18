package xyz.plocki.plockbot.util.manager;

import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {

    private final YamlConfiguration yml;

    public ConfigManager() {
        File file = new File("plugins/PlockBot/config.yml");
        yml = YamlConfiguration.loadConfiguration(file);
        if(!file.exists()) {
            List<String> urls = new ArrayList<>();
            List<String> badNameList = new ArrayList<>();
            urls.add("http://plockbot.plocki.xyz/ips-to-block.list");
            badNameList.add("cipher");
            badNameList.add("dropbot");
            yml.set("autoScrape", true);
            yml.set("scrapeMessage", true);
            yml.set("antiVPN", true);
            yml.set("prefix", "§b§lPlock§c§lBot §8» §7");
            yml.set("similarityScore", 0.95D);
            yml.set("DO_NOT_CHANGE", "#Verboseplaces are CHAT and ACTION_BAR.");
            yml.set("verbosePlace", "ACTION_BAR");
            yml.set("autoIPUnblock", false);
            yml.set("ipUnblockMessage", "All ips were unblocked.");
            yml.set("scrapeURLs", urls);
            yml.set("badNameContains", badNameList);
            try {
                yml.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            urls.clear();
        }
    }

    public boolean autoScrapeIsEnabled() {
        return yml.getBoolean("autoScrape");
    }

    public boolean autoScrapeMessageIsEnabled() {
        return yml.getBoolean("scrapeMessage");
    }

    public boolean antiVPNIsEnabled() {
        return yml.getBoolean("antiVPN");
    }

    public String getPrefix() {
        return yml.getString("prefix");
    }

    public double getSimilarityScore() {
        return yml.getDouble("similarityScore");
    }

    public ChatMessageType getVerbosePlace() {
        return ChatMessageType.valueOf(yml.getString("verbosePlace"));
    }

    public boolean autoIPUnblockIsEnabled() {
        return yml.getBoolean("autoIPUnblock");
    }

    public String getIPUnblockMessage() {
        return yml.getString("ipUnblockMessage");
    }

    public List<String> getScrapeURLs() {
        return yml.getStringList("scrapeURLs");
    }

    public List<String> getBadNameContains() { return yml.getStringList("badNameContains"); }

}
