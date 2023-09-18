package xyz.plocki.plockbot.util;

import org.bukkit.Bukkit;
import xyz.plocki.plockbot.PlockBot;
import xyz.plocki.plockbot.util.manager.BlockedIPsManager;
import xyz.plocki.plockbot.util.manager.ConfigManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class IPGetter {

    public static List<String> blockedIPs = new ArrayList<>();

    public static void getJsonIPs() throws IOException {
        ArrayList<String> ips = new ArrayList<>();
        File proxyList = new File("plugins/PlockBot/proxylist.txt");
        if(!proxyList.exists()) {
            try {
                proxyList.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Scanner in = new Scanner(proxyList);
        while (in.hasNext()) {
            String s = in.next();
            if (s.contains(":")) {
                ips.add(s.split(":")[0]);
            } else {
                ips.add(s);
            }
        }
        ips.forEach(ip -> {
            if (!new BlockedIPsManager().isBlocked(ip)) {
                if (new ConfigManager().autoScrapeMessageIsEnabled()) {
                    Bukkit.getConsoleSender().sendMessage("§a§lAUTO-" + PlockBot.prefix + "IP " + ip + " from ProxyList was added.");
                }
                try {
                    new BlockedIPsManager().blockIp(ip);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}