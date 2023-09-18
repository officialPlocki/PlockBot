package xyz.plocki.plockbot.listeners;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.ricecode.similarity.JaroWinklerStrategy;
import net.ricecode.similarity.StringSimilarityService;
import net.ricecode.similarity.StringSimilarityServiceImpl;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import xyz.plocki.plockbot.PlockBot;
import xyz.plocki.plockbot.util.VerboseHandler;
import xyz.plocki.plockbot.util.manager.BlockedIPsManager;
import xyz.plocki.plockbot.util.manager.WhitelistManager;
import xyz.plocki.plockbot.util.manager.ConfigManager;
import xyz.plocki.plockbot.util.manager.LanguageManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConnectionListener implements Listener {

    private final List<String> names = new ArrayList<>();
    private static final Map<String, Long> lastConnection = new HashMap<>();
    private static final Map<String, Boolean> hasPinged = new HashMap<>();
    private static final List<String> bypassTemp = new ArrayList<>();

    @EventHandler
    public void onPing(ServerListPingEvent event) {
        hasPinged.put(event.getAddress().getHostAddress(), true);
    }

    private boolean isVPN(String ip) throws IOException {
        StringBuilder jsonS = new StringBuilder();
        URL url = new URL("http://ip-api.com/json/" + ip +"?fields=proxy");
        URLConnection conn = url.openConnection();
        conn.connect();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;

        while((inputLine = in.readLine()) != null) {
            jsonS.append(inputLine);
        }
        Gson gson = new Gson();
        JsonObject jsonObject= gson.fromJson(jsonS.toString(), JsonObject.class);
        boolean b = jsonObject.get("proxy").getAsBoolean();
        in.close();
        return b;
    }

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        if(WhitelistManager.isWhitelisted(event.getUniqueId().toString())) {
            return;
        }
        if(Integer.parseInt(event.getName()) != 0) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, PlockBot.prefix + new LanguageManager().getKickBadNameMessage());
        }
        /*AtomicBoolean b = new AtomicBoolean(false);
        new ConfigManager().getBadNameContains().forEach(s -> {
            if(event.getName().contains(s)) {
                b.set(true);
            }
        });
        if(b.get()) {
            try {
                new BlockedIPsManager().blockIp(event.getAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
            new VerboseHandler().verbose(event.getAddress().getHostAddress());
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, PlockBot.prefix + new LanguageManager().getKickBadNameMessage());
        }*/
        if(lastConnection.get(event.getAddress().getHostAddress()) != null) {
            if((System.currentTimeMillis() - lastConnection.get(event.getAddress().getHostAddress())) <= 200) {
                try {
                    new BlockedIPsManager().blockIp(event.getAddress().getHostAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                new VerboseHandler().verbose(event.getAddress().getHostAddress());
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, PlockBot.prefix + new LanguageManager().getKickVBlockedIPMessage());
            }
        }
        if(new BlockedIPsManager().isBlocked(event.getAddress().getHostAddress())) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, PlockBot.prefix + new LanguageManager().getKickVBlockedIPMessage());
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        StringSimilarityService service = new StringSimilarityServiceImpl(new JaroWinklerStrategy());
        if(names.size() != 0) {
            names.forEach(s -> {
                if(service.score(s, event.getPlayer().getName()) >= new ConfigManager().getSimilarityScore()) {
                    event.getPlayer().kickPlayer(PlockBot.prefix + new LanguageManager().getKickSimNameMessage());
                }
            });
        }
        new Thread(() -> {
            if(new ConfigManager().antiVPNIsEnabled()) {
                try {
                    if(isVPN(Objects.requireNonNull(event.getPlayer().getAddress()).getAddress().getHostAddress())) {
                        new BlockedIPsManager().blockIp(event.getPlayer().getAddress().getAddress().getHostAddress());
                        new VerboseHandler().verbose(event.getPlayer().getAddress().getAddress().getHostAddress());
                        event.getPlayer().kickPlayer(PlockBot.prefix + new LanguageManager().getKickVPNMessage());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        names.add(event.getPlayer().getName());
        lastConnection.put(Objects.requireNonNull(event.getPlayer().getAddress()).getAddress().getHostAddress(), System.currentTimeMillis());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        names.remove(event.getPlayer().getName());
        VerboseHandler.verbosePlayers.remove(event.getPlayer());
    }

}
