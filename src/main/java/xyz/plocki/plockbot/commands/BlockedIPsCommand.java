package xyz.plocki.plockbot.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.plocki.plockbot.PlockBot;
import xyz.plocki.plockbot.util.IPGetter;
import xyz.plocki.plockbot.util.manager.LanguageManager;

public class BlockedIPsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (player.hasPermission("plockbot.blockedips")) {
                player.sendMessage(PlockBot.prefix + IPGetter.blockedIPs.size() + " IPs are blocked.");
            } else {
                player.sendMessage(PlockBot.prefix + new LanguageManager().getNoPermissionsMessage());
            }
        }
        return false;
    }
}