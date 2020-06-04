package net.bfcode.fullpvp.commands;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.bfcode.fullpvp.utilities.ColorText;
import net.bfcode.fullpvp.utilities.Utils;

public class RefundCommand implements CommandExecutor {
	
	public static HashMap<UUID, ItemStack[]> PlayerInventoryContents;
    public static HashMap<UUID, ItemStack[]> PlayerArmorContents;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		final Player p = (Player)sender;
		if (!p.hasPermission("fullpvp.command.refund")) {
			p.sendMessage(Utils.NO_PERMISSION);
			return true;
		}
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /refund <player>");
            return true;
        }
        if (Bukkit.getPlayer(args[0]) == null) {
            p.sendMessage(ChatColor.RED + "Player isn't online.");
            return true;
        }
        final Player target = Bukkit.getPlayer(args[0]);
        if (PlayerInventoryContents.containsKey(target.getUniqueId())) {
            target.getInventory().setContents((ItemStack[])PlayerInventoryContents.get(target.getUniqueId()));
            target.getInventory().setArmorContents((ItemStack[])PlayerArmorContents.get(target.getUniqueId()));
            Command.broadcastCommandMessage((CommandSender)p, ChatColor.YELLOW + "Returned " + target.getName() + "'s items.");
            PlayerArmorContents.remove(target.getUniqueId());
            PlayerInventoryContents.remove(target.getUniqueId());
            return true;
        }
        p.sendMessage(ChatColor.RED + "Inventory not found. (Already rolled back?)");
		return true;
	}
	
	static {
        PlayerInventoryContents = new HashMap<UUID, ItemStack[]>();
        PlayerArmorContents = new HashMap<UUID, ItemStack[]>();
    }
}
