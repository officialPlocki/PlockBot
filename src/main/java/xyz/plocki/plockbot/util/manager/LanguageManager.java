package xyz.plocki.plockbot.util.manager;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class LanguageManager {

    private YamlConfiguration yml;

    public LanguageManager() {
        File file = new File("plugins/PlockBot/language.yml");
        yml = YamlConfiguration.loadConfiguration(file);
        if(!file.exists()) {
            yml.set("kickVPN", "Please disable your VPN.");
            yml.set("kickBadName", "Your Name contains not allowed words(?).");
            yml.set("kickBlockedIP", "Your IP is blocked. If this is a mistake, please contact the support.");
            yml.set("kickRejoin", "Your IP is now verified. Please rejoin. To bypass this next time, please add the server to your Serverlist.");
            yml.set("kickSimName", "Your name is too similar to a name of an another player.");
            yml.set("noPermissions", "You don't have enough permissions.");
            try {
                yml.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getKickBadNameMessage() {
        return yml.getString("kickBadName");
    }

    public String getKickVBlockedIPMessage() {
        return yml.getString("kickBlockedIP");
    }

    public String getKickRejoinMessage() {
        return yml.getString("kickRejoin");
    }

    public String getKickSimNameMessage() {
        return yml.getString("kickSimName");
    }

    public String getKickVPNMessage() {
        return yml.getString("kickVPN");
    }

    public String getNoPermissionsMessage() {
        return yml.getString("noPermissions");
    }

}
