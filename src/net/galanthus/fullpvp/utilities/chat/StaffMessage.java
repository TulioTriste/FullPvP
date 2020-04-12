package net.galanthus.fullpvp.utilities.chat;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.permissions.Permissible;

public class StaffMessage
{
    public static void broadcastFilter(final CommandSender source, final String message) {
        broadcastFilter(source, message, true);
    }
    
    public static void broadcastFilter(final CommandSender source, final String message, final boolean sendToSource) {
        final String result = source.getName() + " " + message;
        if (source instanceof BlockCommandSender) {
            final BlockCommandSender blockCommandSender = (BlockCommandSender)source;
            if (blockCommandSender.getBlock().getWorld().getGameRuleValue("commandBlockOutput").equalsIgnoreCase("false")) {
                Bukkit.getConsoleSender().sendMessage(result);
                return;
            }
        }
        else if (source instanceof CommandMinecart) {
            final CommandMinecart commandMinecart = (CommandMinecart)source;
            if (commandMinecart.getWorld().getGameRuleValue("commandBlockOutput").equalsIgnoreCase("false")) {
                Bukkit.getConsoleSender().sendMessage(result);
                return;
            }
        }
        final Set<Permissible> users = (Set<Permissible>)Bukkit.getPluginManager().getPermissionSubscriptions("bukkit.broadcast.staff");
        final String colored = ChatColor.GOLD + "" + ChatColor.ITALIC + "[Filter] " + ChatColor.GRAY + "" + ChatColor.ITALIC + result;
        if (sendToSource && !(source instanceof ConsoleCommandSender)) {
            source.sendMessage(message);
        }
        for (final Permissible user : users) {
            if (user instanceof CommandSender) {
                final CommandSender target = (CommandSender)user;
                if (target instanceof ConsoleCommandSender) {
                    target.sendMessage(result);
                }
                else {
                    if (target == source) {
                        continue;
                    }
                    target.sendMessage(colored);
                }
            }
        }
    }
}
