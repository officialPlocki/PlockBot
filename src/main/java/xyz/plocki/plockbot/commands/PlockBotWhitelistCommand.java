package xyz.plocki.plockbot.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.plocki.plockbot.PlockBot;
import xyz.plocki.plockbot.util.manager.WhitelistManager;
import xyz.plocki.plockbot.util.manager.LanguageManager;

import javax.annotation.Nonnull;

public class PlockBotWhitelistCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if(sender instanceof Player player) {
            if(player.hasPermission("plockbot.whitelist")) {
                if(args.length != 2) {
                    player.sendMessage(PlockBot.prefix + "Please use /plockbotwhitelist <add, remove> <UUID>");
                } else {
                    if(args[0].equalsIgnoreCase("add")) {
                        player.sendMessage(PlockBot.prefix + "The UUID " + args[0] + " were added to the whitelist.");
                        WhitelistManager.addWhitelist(args[1]);
                    } else if(args[0].equalsIgnoreCase("remove")) {
                        player.sendMessage(PlockBot.prefix + "The UUID " + args[0] + " was removed from the whitelist.");
                        WhitelistManager.removeWhitelist(args[1]);
                    } else {
                        player.sendMessage(PlockBot.prefix + "Please use /plockbotwhitelist <add, remove> <UUID>");
                    }
                }
            } else {
                player.sendMessage(PlockBot.prefix + new LanguageManager().getNoPermissionsMessage());
            }
        }
        return false;
    }

}
