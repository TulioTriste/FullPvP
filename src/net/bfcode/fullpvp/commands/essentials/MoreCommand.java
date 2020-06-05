package net.bfcode.fullpvp.commands.essentials;

import org.bukkit.inventory.ItemStack;

import net.bfcode.fullpvp.utilities.Ints;
import net.bfcode.fullpvp.utilities.Utils;

import org.bukkit.Material;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MoreCommand implements CommandExecutor {
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.MUST_BE_PLAYER);
            return true;
        }
        if (!sender.hasPermission("fullpvp.command.more")) {
			sender.sendMessage(Utils.NO_PERMISSION);
			return true;
		}
        final Player player = (Player)sender;
        final ItemStack stack = player.getItemInHand();
        if (stack == null || stack.getType() == Material.AIR) {
            sender.sendMessage(ChatColor.RED + "You are not holding any item.");
            return true;
        }
        Integer amount;
        if (args.length > 0) {
            amount = Ints.tryParse(args[0]);
            if (amount == null) {
                sender.sendMessage(ChatColor.RED + "'" + args[0] + "' is not a number.");
                return true;
            }
            if (amount <= 0) {
                sender.sendMessage(ChatColor.RED + "Item amounts must be positive.");
                return true;
            }
        }
        else {
            final int curAmount = stack.getAmount();
            if (curAmount >= (amount = stack.getMaxStackSize())) {
                sender.sendMessage(ChatColor.RED + "You already have the maximum amount: " + amount + '.');
                return true;
            }
        }
        stack.setAmount((int)amount);
        return true;
    }
}
