package xyz.plocki.plockbot.util;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import xyz.plocki.plockbot.PlockBot;
import xyz.plocki.plockbot.util.manager.ConfigManager;

import java.util.ArrayList;
import java.util.List;

public class VerboseHandler {

    public static List<Player> verbosePlayers = new ArrayList<>();

    public void verbose(String ip) {
        ChatMessageType place = new ConfigManager().getVerbosePlace();
        verbosePlayers.forEach(all -> all.spigot().sendMessage(place, TextComponent.fromLegacyText(PlockBot.prefix + "Die IP §c" + ip + " §7wurde geblockt.")));
    }

}
