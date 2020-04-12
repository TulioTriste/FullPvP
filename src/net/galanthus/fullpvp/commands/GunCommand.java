package net.galanthus.fullpvp.commands;

import org.bukkit.entity.Player;

import net.galanthus.fullpvp.handler.GunHandler;
import net.galanthus.fullpvp.utilities.ColorText;
import net.galanthus.fullpvp.utilities.Ints;
import net.galanthus.fullpvp.utilities.Utils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class GunCommand implements CommandExecutor {
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!sender.hasPermission(Utils.PERMISSION + "gun")) {
            sender.sendMessage(Utils.NO_PERMISSION);
            return true;
        }
        if (args.length < 1) {
            this.getUsage(sender);
        }
        else if (args[0].equalsIgnoreCase("give")) {
            if (args.length < 3) {
                this.getUsage(sender);
                return true;
            }
            final Player target = Bukkit.getPlayer(args[1]);
            if (!Utils.isOnline(sender, target)) {
                Utils.PLAYER_NOT_FOUND(sender, args[1]);
                return true;
            }
            final Integer amount = Ints.tryParse(args[2]);
            if (amount == null) {
                sender.sendMessage(ColorText.translate("&c'" + args[2] + "' is not a valid number."));
                return true;
            }
            if (amount <= 0) {
                sender.sendMessage(ColorText.translate("&cThe amount must be positive."));
                return true;
            }
            if (amount > 64) {
                sender.sendMessage(ColorText.translate("&cMax amount is 64."));
                return true;
            }
            GunHandler.getHoeGun(target, amount);
            sender.sendMessage(ColorText.translate("&eSuccessfully given x" + amount + " &9Hoe Gun &eto &6" + target.getName() + "&e."));
            target.sendMessage(ColorText.translate("&eYou have received x" + amount + " &9Hoe Gun&e."));
        }
        else if (args[0].equalsIgnoreCase("giveall")) {
            if (args.length < 2) {
                this.getUsage(sender);
                return true;
            }
            final Integer amount2 = Ints.tryParse(args[1]);
            if (amount2 == null) {
                sender.sendMessage(ColorText.translate("&c'" + args[1] + "' is not a valid number."));
                return true;
            }
            if (amount2 <= 0) {
                sender.sendMessage(ColorText.translate("&cThe amount must be positive."));
                return true;
            }
            if (amount2 > 64) {
                sender.sendMessage(ColorText.translate("&cMax amount is 64."));
                return true;
            }
            for (final Player online : Bukkit.getServer().getOnlinePlayers()) {
                GunHandler.getHoeGun(online, amount2);
                sender.sendMessage(ColorText.translate("&eSuccessfully given x" + amount2 + " &9Hoe Gun &eto &6all online players&e."));
                online.sendMessage(ColorText.translate("&eYou have received x" + amount2 + " &9Hoe Gun&e."));
            }
        }
        else {
            sender.sendMessage(ColorText.translate("&cGun sub-command '" + args[0] + "' not found."));
        }
        return true;
    }
    
    private void getUsage(final CommandSender sender) {
        sender.sendMessage(ColorText.translate("&7&m------------------------------"));
        sender.sendMessage(ColorText.translate("&6Gun Commands"));
        sender.sendMessage("");
        sender.sendMessage(ColorText.translate("&e/gun give <playerName> <amount> &7- &fGive gun(s) to player."));
        sender.sendMessage(ColorText.translate("&e/gun giveall <amount> &7- &fGive gun(s) to all online players."));
        sender.sendMessage(ColorText.translate("&7&m------------------------------"));
    }
}
