package net.bfcode.fullpvp.commands;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import net.bfcode.fullpvp.utilities.ColorText;
import net.bfcode.fullpvp.utilities.Ints;
import net.bfcode.fullpvp.utilities.Utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class StatsManagerCommand implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.MUST_BE_PLAYER);
            return true;
        }
        final Player player = (Player)sender;
        if (!player.hasPermission("fullpvp.command.statsmanager")) {
            player.sendMessage(Utils.NO_PERMISSION);
            return true;
        }
        if (args.length < 4) {
            this.getUsage((CommandSender)player);
        }
        else if (args[0].equalsIgnoreCase("kills")) {
            if (args[1].equalsIgnoreCase("add")) {
                final Player target = Bukkit.getPlayer(args[2]);
                if (!Utils.isOnline((CommandSender)player, target)) {
                    Utils.PLAYER_NOT_FOUND((CommandSender)player, args[2]);
                    return true;
                }
                final Integer amount = Ints.tryParse(args[3]);
                if (amount == null) {
                    player.sendMessage(ColorText.translate("&c'" + args[1] + "' is not a valid number."));
                    return true;
                }
                if (amount <= 0) {
                    player.sendMessage(ColorText.translate("&cThe amount must be positive."));
                    return true;
                }
                target.incrementStatistic(Statistic.PLAYER_KILLS, amount);
                player.sendMessage(ColorText.translate("&eAdded " + amount + " kills to " + target.getName() + " account."));
            }
            else if (args[1].equalsIgnoreCase("remove")) {
                final Player target = Bukkit.getPlayer(args[2]);
                if (!Utils.isOnline((CommandSender)player, target)) {
                    Utils.PLAYER_NOT_FOUND((CommandSender)player, args[2]);
                    return true;
                }
                final Integer amount = Ints.tryParse(args[3]);
                if (amount == null) {
                    player.sendMessage(ColorText.translate("&c'" + args[1] + "' is not a valid number."));
                    return true;
                }
                if (amount <= 0) {
                    player.sendMessage(ColorText.translate("&cThe amount must be positive."));
                    return true;
                }
                if (target.getStatistic(Statistic.PLAYER_KILLS) < amount) {
                    player.sendMessage(ColorText.translate("&c" + target.getName() + " don't have " + amount + " kills."));
                    return true;
                }
                target.decrementStatistic(Statistic.PLAYER_KILLS, amount);
                player.sendMessage(ColorText.translate("&eRemoved " + amount + " kills from " + target.getName() + " account."));
            }
            else {
                player.sendMessage(ColorText.translate("&cStatsManager sub-command '" + args[1] + "' not found."));
            }
        }
        else if (args[0].equalsIgnoreCase("deaths")) {
            if (args[1].equalsIgnoreCase("add")) {
                final Player target = Bukkit.getPlayer(args[2]);
                if (!Utils.isOnline((CommandSender)player, target)) {
                    Utils.PLAYER_NOT_FOUND((CommandSender)player, args[2]);
                    return true;
                }
                final Integer amount = Ints.tryParse(args[3]);
                if (amount == null) {
                    player.sendMessage(ColorText.translate("&c'" + args[1] + "' is not a valid number."));
                    return true;
                }
                if (amount <= 0) {
                    player.sendMessage(ColorText.translate("&cThe amount must be positive."));
                    return true;
                }
                target.incrementStatistic(Statistic.DEATHS, amount);
                player.sendMessage(ColorText.translate("&eAdded " + amount + " deaths to " + target.getName() + " account."));
            }
            else if (args[1].equalsIgnoreCase("remove")) {
                final Player target = Bukkit.getPlayer(args[2]);
                if (!Utils.isOnline((CommandSender)player, target)) {
                    Utils.PLAYER_NOT_FOUND((CommandSender)player, args[2]);
                    return true;
                }
                final Integer amount = Ints.tryParse(args[3]);
                if (amount == null) {
                    player.sendMessage(ColorText.translate("&c'" + args[1] + "' is not a valid number."));
                    return true;
                }
                if (amount <= 0) {
                    player.sendMessage(ColorText.translate("&cThe amount must be positive."));
                    return true;
                }
                final int deaths = target.getStatistic(Statistic.DEATHS);
                if (deaths < amount) {
                    player.sendMessage(ColorText.translate("&c" + target.getName() + " don't have " + amount + " deaths."));
                    return true;
                }
                target.decrementStatistic(Statistic.DEATHS, amount);
                player.sendMessage(ColorText.translate("&eRemoved " + amount + " deaths from " + target.getName() + " account."));
            }
            else {
                player.sendMessage(ColorText.translate("&cStatsManager sub-command '" + args[1] + "' not found."));
            }
        }
        else {
            player.sendMessage(ColorText.translate("&cStatsManager sub-command '" + args[0] + "' not found."));
        }
        return true;
    }
    
    private void getUsage(final CommandSender sender) {
        sender.sendMessage(ColorText.translate("&7&m------------------------------"));
        sender.sendMessage(ColorText.translate("&6StatsManager Command: &7(Page 1 of 1)"));
        sender.sendMessage("");
        sender.sendMessage(ColorText.translate("&e/statsmanager <kills> <add|remove> <playerName> <amount>"));
        sender.sendMessage(ColorText.translate("&7Add or remove kills to|from player"));
        sender.sendMessage("");
        sender.sendMessage(ColorText.translate("&e/statsmanager <deaths> <add|remove> <playerName> <amount>"));
        sender.sendMessage(ColorText.translate("&7Add or remove deaths to|from player"));
        sender.sendMessage(ColorText.translate("&7&m------------------------------"));
    }
}
