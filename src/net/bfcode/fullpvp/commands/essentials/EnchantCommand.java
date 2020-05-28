package net.bfcode.fullpvp.commands.essentials;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.bfcode.fullpvp.commands.BaseCommand;
import net.bfcode.fullpvp.utilities.BaseConstants;
import net.bfcode.fullpvp.utilities.BukkitUtils;
import net.bfcode.fullpvp.utilities.ColorText;
import net.bfcode.fullpvp.utilities.Ints;

public class EnchantCommand extends BaseCommand implements CommandExecutor {
    public EnchantCommand() {
        super("enchant", "Adds enchantment to items.");
        this.setUsage("/(command) <enchantment> <level> [playerName]");
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
    	if (!sender.hasPermission("fullpvp.command.enchant")) {
			sender.sendMessage(ColorText.translate("&cYou don't have permission to execute this command."));
			return true;
		}
        if (args.length < 2) {
            sender.sendMessage(this.getUsage());
            return true;
        }
        Player target;
        if (args.length > 2 && sender.hasPermission("fullpvp.command.enchant.others")) {
            target = BukkitUtils.playerWithNameOrUUID(args[2]);
        }
        else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(this.getUsage(label));
                return true;
            }
            target = (Player)sender;
        }
        if (target == null || !BaseCommand.canSee(sender, target)) {
            sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
            return true;
        }
        final Enchantment enchantment = Enchantment.getByName(args[0]);
        if (enchantment == null) {
            sender.sendMessage(ChatColor.RED + "No enchantment named '" + args[0] + "' found.");
            return true;
        }
        final ItemStack stack = target.getItemInHand();
        if (stack == null || stack.getType() == Material.AIR) {
            sender.sendMessage(ChatColor.RED + target.getName() + " is not holding an item.");
            return true;
        }
        final Integer level = Ints.tryParse(args[1]);
        if (level == null) {
            sender.sendMessage(ChatColor.RED + "'" + args[1] + "' is not a number.");
            return true;
        }
        final int maxLevel = enchantment.getMaxLevel();
        if (level > maxLevel && !sender.hasPermission("fullpvp.command.enchant.abovemaxlevel")) {
            sender.sendMessage(ChatColor.RED + "The maximum enchantment level for " + enchantment.getName() + " is " + maxLevel + '.');
            return true;
        }
        if (!enchantment.canEnchantItem(stack) && !sender.hasPermission("fullpvp.command.enchant.anyitem")) {
            sender.sendMessage(ChatColor.RED + "Enchantment " + enchantment.getName() + " cannot be applied to that item.");
            return true;
        }
        stack.addUnsafeEnchantment(enchantment, (int)level);
        String itemName;
        try {
            itemName = CraftItemStack.asNMSCopy(stack).getName();
        }
        catch (Error ex) {
            itemName = stack.getType().name();
        }
        Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Enchanted " + enchantment.getName() + " at level " + level + " onto " + itemName + " of " + target.getName() + '.');
        return true;
    }
    
    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        switch (args.length) {
            case 1: {
                final Enchantment[] enchantments = Enchantment.values();
                final ArrayList<String> results = new ArrayList<String>(enchantments.length);
                for (final Enchantment enchantment : enchantments) {
                    results.add(enchantment.getName());
                }
                return BukkitUtils.getCompletions(args, results);
            }
            case 3: {
                return null;
            }
            default: {
                return Collections.emptyList();
            }
        }
    }
}
