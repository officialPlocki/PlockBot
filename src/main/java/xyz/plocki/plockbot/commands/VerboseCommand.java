package xyz.plocki.plockbot.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.plocki.plockbot.PlockBot;
import xyz.plocki.plockbot.util.VerboseHandler;
import xyz.plocki.plockbot.util.manager.LanguageManager;

import javax.annotation.Nonnull;

public class VerboseCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String s, @Nonnull String[] strings) {
        if(commandSender instanceof Player player) {
            if(player.hasPermission("plockbot.verbose")) {
                if(VerboseHandler.verbosePlayers.contains(player)) {
                    VerboseHandler.verbosePlayers.remove(player);
                    player.sendMessage(PlockBot.prefix + "Verbose messages are now disabled.");
                } else {
                    VerboseHandler.verbosePlayers.add(player);
                    player.sendMessage(PlockBot.prefix + "Verbose messages are now enabled.");
                }
            } else {
                player.sendMessage(PlockBot.prefix + new LanguageManager().getNoPermissionsMessage());
            }
        }
        return false;
    }
}
