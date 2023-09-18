package xyz.plocki.plockbot.util.manager;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WhitelistManager {

    private static final List<String> whitelist = new ArrayList<>();

    public static boolean isWhitelisted(String uuid) {
        return whitelist.contains(uuid);
    }

    public static void addWhitelist(String uuid) {
        whitelist.add(uuid);
    }

    public static void removeWhitelist(String uuid) {
        whitelist.remove(uuid);
    }

    public static void save() {
        File file = new File("plugins/PlockBot/whitelisteduuids.list");
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
        yml.set("uuids", new ArrayList<>(whitelist));
        try {
            yml.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            yml.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        File file = new File("plugins/PlockBot/whitelisteduuids.list");
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
        if(yml.isSet("uuids")) {
            whitelist.addAll(yml.getStringList("uuids"));
        }
    }
}
