package net.bfcode.fullpvp.commands;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.bfcode.fullpvp.utilities.ColorText;
import net.bfcode.fullpvp.utilities.Ints;
import net.bfcode.fullpvp.utilities.ItemMaker;
import net.bfcode.fullpvp.utilities.Utils;

import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class HeadLootCommand implements CommandExecutor {
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!sender.hasPermission("fullpvp.command.headloot")) {
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
                sender.sendMessage(ColorText.translate("&cAmount must be positive."));
                return true;
            }
            if (amount > 64) {
                sender.sendMessage(ColorText.translate("&cMax amount is 64."));
                return true;
            }
            this.giveHeadLoot(target, amount);
            sender.sendMessage(ColorText.translate("&eSuccessfully given x" + amount + " HeadLoot to &6" + target.getName() + "&e."));
            target.sendMessage(ColorText.translate("&eYou have received x" + amount + " HeadLoot."));
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
                sender.sendMessage(ColorText.translate("&cAmount must be positive."));
                return true;
            }
            if (amount2 > 64) {
                sender.sendMessage(ColorText.translate("&cMax amount is 64."));
                return true;
            }
            for (final Player online : Bukkit.getOnlinePlayers()) {
                this.giveHeadLoot(online, amount2);
                sender.sendMessage(ColorText.translate("&eSuccessfully given x" + amount2 + " HeadLoot to &6all online players&e."));
                online.sendMessage(ColorText.translate("&eYou have received x" + amount2 + " HeadLoot's."));
            }
        }
        else {
            sender.sendMessage(ColorText.translate("&cHeadLoot sub-command '" + args[0] + "' not found."));
        }
        return true;
    }
    
    private void giveHeadLoot(final Player player, final int amount) {
        player.getInventory().addItem(new ItemStack[] { new ItemMaker(Material.SKULL_ITEM, amount).data((short)3).displayName("&6Head Loot &7(Right Click)").lore("&7Right-Click to receive a loot.").create() });
    }
    
    private void getUsage(final CommandSender sender) {
        sender.sendMessage(ColorText.translate("&7&m------------------------------"));
        sender.sendMessage(ColorText.translate("&6HeadLoot Commands"));
        sender.sendMessage("");
        sender.sendMessage(ColorText.translate("&e/headloot <give> <playerName> <amount> &7- Give a HeadLoot(s) to a player."));
        sender.sendMessage(ColorText.translate("&e/headloot <giveall> <amount> &7- &fGive a HeadLoot(s) to all online players."));
        sender.sendMessage(ColorText.translate("&7&m------------------------------"));
    }
}
