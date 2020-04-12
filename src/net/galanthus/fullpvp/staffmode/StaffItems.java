package net.galanthus.fullpvp.staffmode;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.galanthus.fullpvp.commands.StaffModeCommand;

public class StaffItems {

	@SuppressWarnings("deprecation")
	public static void modItems(Player p) {
		Inventory inv = p.getInventory();

		inv.clear();

		ItemStack compass = new ItemStack(Material.COMPASS);
		ItemStack book = new ItemStack(Material.BOOK);
		ItemStack tp = new ItemStack(Material.WATCH);
		ItemStack vanish = new ItemStack(351, 1, (short) 10);
		ItemStack freeze = new ItemStack(Material.PACKED_ICE);
		ItemStack online = new ItemStack(Material.GHAST_TEAR);

		ItemMeta compassMeta = compass.getItemMeta();
		ItemMeta bookMeta = book.getItemMeta();
		ItemMeta eggMeta = tp.getItemMeta();
		ItemMeta vanishMeta = vanish.getItemMeta();
		ItemMeta freezeMeta = freeze.getItemMeta();
		ItemMeta onlineMeta = online.getItemMeta();
		

		compassMeta.setDisplayName("§eTeleporter");
		bookMeta.setDisplayName("§eInventory");
		eggMeta.setDisplayName("§eRandom TP");
		vanishMeta.setDisplayName("§eVanish: §aOn");
		freezeMeta.setDisplayName("§eFreeze");
		onlineMeta.setDisplayName("§eTop");

		compass.setItemMeta(compassMeta);
		book.setItemMeta(bookMeta);
		tp.setItemMeta(eggMeta);
		vanish.setItemMeta(vanishMeta);
		freeze.setItemMeta(freezeMeta);
		online.setItemMeta(onlineMeta);

		inv.setItem(0, compass);
		inv.setItem(1, book);
		inv.setItem(4, freeze);
		inv.setItem(7, tp);
		inv.setItem(8, vanish);
		inv.setItem(2, online);
	}

	public static void saveInventory(Player p) {
		StaffModeCommand.armorContents.put(p.getName(), p.getInventory().getArmorContents());
		StaffModeCommand.inventoryContents.put(p.getName(), p.getInventory().getContents());
	}

	public static void loadInventory(Player p) {
		p.getInventory().clear();

		p.getInventory().setContents((ItemStack[]) StaffModeCommand.inventoryContents.get(p.getName()));
		p.getInventory().setArmorContents((ItemStack[]) StaffModeCommand.armorContents.get(p.getName()));

		StaffModeCommand.inventoryContents.remove(p.getName());
		StaffModeCommand.armorContents.remove(p.getName());
	}

}






