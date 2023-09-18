package xyz.plocki.plockbot.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.plocki.plockbot.PlockBot;
import xyz.plocki.plockbot.util.manager.BlockedIPsManager;

import javax.annotation.Nonnull;
import java.io.IOException;

public class PlockBotCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if(sender instanceof Player player) {
            if(player.hasPermission("plockbot.command")) {
                if(args.length == 0 || args.length >= 3) {
                    player.sendMessage(PlockBot.prefix + "Please use /plockbot <unblockIPs, unblockIP> <IP>!");
                } else {
                    if(args.length == 1) {
                        if(args[0].equalsIgnoreCase("unblockips")) {
                            player.sendMessage(PlockBot.prefix + "Cleared all blocked IPs.");
                            new BlockedIPsManager().reset();
                        } else {
                            player.sendMessage(PlockBot.prefix + "Please use /plockbot <unblockIPs, unblockIP> <IP>!");
                        }
                    } else {
                        if(args[0].equalsIgnoreCase("unblockip")) {
                            try {
                                new BlockedIPsManager().unblockIp(args[1]);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            player.sendMessage(PlockBot.prefix + "The IP " + args[1] + " was unblocked (if they was blocked).");
                        } else {
                            player.sendMessage(PlockBot.prefix + "Please use /plockbot <unblockIPs, unblockIP> <IP>!");
                        }
                    }
                }
            } else {
                player.sendMessage(PlockBot.prefix + "This server is protected by PlockBot from plocki.");
            }
        }
        return false;
    }

}
