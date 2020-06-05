package net.bfcode.fullpvp.commands.essentials;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.bfcode.fullpvp.utilities.Utils;

public class RenameCommand implements CommandExecutor {
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
            return true;
        }
        if (!sender.hasPermission("fullpvp.command.rename")) {
			sender.sendMessage(Utils.NO_PERMISSION);
			return true;
		}
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <newItemName>");
            return true;
        }
        final Player player = (Player)sender;
        final ItemStack stack = player.getItemInHand();
        if (stack == null || stack.getType() == Material.AIR) {
            sender.sendMessage(ChatColor.RED + "You are not holding anything.");
            return true;
        }
        final ItemMeta meta = stack.getItemMeta();
        String oldName = meta.getDisplayName();
        if (oldName != null) {
            oldName = oldName.trim();
        }
        final String newName = (args[0].equalsIgnoreCase("none") || args[0].equalsIgnoreCase("null")) ? null : ChatColor.translateAlternateColorCodes('&', StringUtils.join((Object[])args, ' ', 0, args.length));
        if (oldName == null && newName == null) {
            sender.sendMessage(ChatColor.RED + "Your held item already has no name.");
            return true;
        }
        if (oldName != null && oldName.equals(newName)) {
            sender.sendMessage(ChatColor.RED + "Your held item is already named this.");
            return true;
        }
        if(stack.getType() == Material.TRIPWIRE_HOOK) {
        	return true;
        }
        meta.setDisplayName(newName);
        stack.setItemMeta(meta);
        if (newName == null) {
            sender.sendMessage(ChatColor.YELLOW + "Removed name of held item from " + oldName + '.');
            return true;
        }
        sender.sendMessage(ChatColor.YELLOW + "Renamed item in hand from " + ((oldName == null) ? "no name" : oldName) + ChatColor.YELLOW + " to " + newName + ChatColor.YELLOW + '.');
        return true;
    }
    
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        return Collections.emptyList();
    }
}
