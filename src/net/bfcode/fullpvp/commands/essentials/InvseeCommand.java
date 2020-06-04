package net.bfcode.fullpvp.commands.essentials;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import net.bfcode.fullpvp.FullPvP;
import net.bfcode.fullpvp.staffmode.StaffPriority;
import net.bfcode.fullpvp.utilities.BaseConstants;
import net.bfcode.fullpvp.utilities.BukkitUtils;
import net.bfcode.fullpvp.utilities.ColorText;
import net.bfcode.fullpvp.utilities.Utils;

public class InvseeCommand implements CommandExecutor, Listener {
	
    private final InventoryType[] types;
    private final Map<InventoryType, Inventory> inventories;
    
    public InvseeCommand(final FullPvP plugin) {
        this.types = new InventoryType[] { InventoryType.BREWING, InventoryType.CHEST, InventoryType.DISPENSER, InventoryType.ENCHANTING, InventoryType.FURNACE, InventoryType.HOPPER, InventoryType.PLAYER, InventoryType.WORKBENCH };
        this.inventories = new EnumMap<InventoryType, Inventory>(InventoryType.class);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
    	if (!sender.hasPermission("fullpvp.command.invsee")) {
			sender.sendMessage(Utils.NO_PERMISSION);
			return true;
		}
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <playerName>");
            return true;
        }
        if (!(sender instanceof Player)) {
            final Player target = BukkitUtils.playerWithNameOrUUID(args[0]);
            if (target == null) {
                sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
                return true;
            }
            sender.sendMessage(ChatColor.YELLOW + "This players inventory contains: ");
            for (final ItemStack items : target.getInventory().getContents()) {
                if (items != null) {
                    sender.sendMessage(ChatColor.AQUA + items.getType().toString().replace("_", "").toLowerCase() + ": " + items.getAmount());
                }
            }
            return true;
        }
        else {
            final Player player = (Player)sender;
            Inventory inventory = null;
            final InventoryType[] types = this.types;
            final int length2 = types.length;
            int j = 0;
            while (j < length2) {
                final InventoryType type = types[j];
                if (!type.name().equalsIgnoreCase(args[0])) {
                    ++j;
                }
                else {
                    final Inventory inventoryRevert = Bukkit.createInventory((InventoryHolder)player, type);
                    inventory = this.inventories.putIfAbsent(type, inventoryRevert);
                    if (inventory != null) {
                        break;
                    }
                    inventory = inventoryRevert;
                    break;
                }
            }
            if (inventory == null) {
                final Player target2 = BukkitUtils.playerWithNameOrUUID(args[0]);
                if (sender.equals(target2)) {
                    sender.sendMessage(ChatColor.RED + "You cannot check the inventory of yourself.");
                    return true;
                }
                if (target2 == null) {
                    sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
                    return true;
                }
                final StaffPriority selfPriority = StaffPriority.of(player);
                if (StaffPriority.of(target2).isMoreThan(selfPriority)) {
                    sender.sendMessage(ChatColor.RED + "You do not have access to check the inventory of that player.");
                    return true;
                }
                inventory = (Inventory)target2.getInventory();
            }
            player.openInventory(inventory);
            return true;
        }
    }
    
	public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length != 1) {
            return Collections.emptyList();
        }
        final InventoryType[] values = InventoryType.values();
        final ArrayList<String> results = new ArrayList<String>(values.length);
        final Player senderPlayer = (sender instanceof Player) ? (Player) sender : null;
        for (final Player target : Bukkit.getOnlinePlayers()) {
            if (senderPlayer != null && !senderPlayer.canSee(target)) {
                continue;
            }
            results.add(target.getName());
        }
        return BukkitUtils.getCompletions(args, results);
    }
}
