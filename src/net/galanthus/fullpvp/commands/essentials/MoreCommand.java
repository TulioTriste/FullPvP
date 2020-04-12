package net.galanthus.fullpvp.commands.essentials;

import org.bukkit.inventory.ItemStack;

import net.galanthus.fullpvp.commands.BaseCommand;
import net.galanthus.fullpvp.utilities.ColorText;
import net.galanthus.fullpvp.utilities.Ints;

import org.bukkit.Material;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class MoreCommand extends BaseCommand {
    public MoreCommand() {
        super("more", "Sets your item to its maximum amount.");
        this.setUsage("/(command)");
    }
    
    @Override
    public boolean isPlayerOnlyCommand() {
        return true;
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command is only executable for players.");
            return true;
        }
        if (!sender.hasPermission("fullpvp.command.more")) {
			sender.sendMessage(ColorText.translate("&cYou don't have permission to execute this command."));
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
