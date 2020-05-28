package net.bfcode.fullpvp.handler;

import java.util.Set;
import org.bukkit.permissions.Permissible;

import net.bfcode.fullpvp.utilities.ColorText;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.ChatColor;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;

public class CommandHandler {
    public static void broadcastCommandMessage(final CommandSender source, final String message) {
        broadcastCommandMessage(source, message, true);
    }
    
    public static void broadcastCommandMessage(final CommandSender source, final String message, final boolean sendToSource) {
        final String result = source.getName() + ": " + message;
        if (source instanceof BlockCommandSender) {
            final BlockCommandSender blockCommandSender = (BlockCommandSender)source;
            if (blockCommandSender.getBlock().getWorld().getGameRuleValue("commandBlockOutput").equalsIgnoreCase("false")) {
                Bukkit.getConsoleSender().sendMessage(ColorText.translate(result));
                return;
            }
        }
        else if (source instanceof CommandMinecart) {
            final CommandMinecart commandMinecart = (CommandMinecart)source;
            if (commandMinecart.getWorld().getGameRuleValue("commandBlockOutput").equalsIgnoreCase("false")) {
                Bukkit.getConsoleSender().sendMessage(ColorText.translate(result));
                return;
            }
        }
        final Set<Permissible> users = (Set<Permissible>)Bukkit.getPluginManager().getPermissionSubscriptions("fullpvp.broadcast.command");
        final String colored = ChatColor.GRAY.toString() + ChatColor.ITALIC + "[Alert] " + result;
        if (sendToSource && !(source instanceof ConsoleCommandSender)) {
            source.sendMessage(ColorText.translate(message));
        }
        for (final Permissible user : users) {
            if (user instanceof CommandSender) {
                final CommandSender target = (CommandSender)user;
                if (target instanceof ConsoleCommandSender) {
                    target.sendMessage(ColorText.translate(result));
                }
                else {
                    if (target == source) {
                        continue;
                    }
                    target.sendMessage(ColorText.translate(colored));
                }
            }
        }
    }
}
