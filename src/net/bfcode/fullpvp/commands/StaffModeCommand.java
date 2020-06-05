package net.bfcode.fullpvp.commands;

import java.util.ArrayList; 
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import net.bfcode.fullpvp.configuration.MessagesFile;
import net.bfcode.fullpvp.listener.VanishListener;
import net.bfcode.fullpvp.staffmode.StaffItems;
import net.bfcode.fullpvp.utilities.ColorText;
import net.bfcode.fullpvp.utilities.Utils;


public class StaffModeCommand implements CommandExecutor, Listener {
	
	public static ArrayList<Player> modMode = new ArrayList<Player>();
	public static ArrayList<UUID> Staff = new ArrayList<UUID>();
	public static ArrayList<Player> teleportList = new ArrayList<Player>();
	public static HashMap<String, ItemStack[]> armorContents = new HashMap<>();
	public static HashMap<String, ItemStack[]> inventoryContents = new HashMap<>();
	
	static StaffModeCommand instance = new StaffModeCommand();

	public static StaffModeCommand getInstance() {
		return instance;
	}

	public static boolean isMod(Player player) {
		return Staff.contains(player.getUniqueId());
	}

	public static boolean enterMod(final Player p) {
	final ItemStack leggings = new ItemStack(Material.CHAINMAIL_LEGGINGS);
	leggings.addEnchantment(Enchantment.DURABILITY, 3);
		modMode.add(p);
		Staff.add(p.getUniqueId());
		StaffItems.saveInventory(p);
		VanishListener.getInstance().setVanish(p, true);
		p.getInventory().clear();
		p.getInventory().setHelmet(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setLeggings(leggings);
		p.getInventory().setBoots(null);
		p.setExp(0.0F);
		p.setAllowFlight(true);
		p.setGameMode(GameMode.CREATIVE);
		StaffItems.modItems(p);
		p.sendMessage(ColorText.translate(MessagesFile.getConfig().getString("StaffMode-Enable")));
		p.updateInventory();
		return true;
	}

	public static boolean leaveMod(final Player p) {
		modMode.remove(p);
		Staff.remove(p.getUniqueId());
		p.getInventory().clear();
		StaffItems.loadInventory(p);
		p.setAllowFlight(false);
		VanishListener.getInstance().setVanish(p, false);
		p.sendMessage(ColorText.translate(MessagesFile.getConfig().getString("StaffMode-Enable")));
		p.setGameMode(GameMode.SURVIVAL);
		p.updateInventory();
		return true;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!sender.hasPermission("fullpvp.command.staffmode")) {
				sender.sendMessage(Utils.NO_PERMISSION);
				return true;
			}
			if (args.length < 1) {
				if (!(sender instanceof Player)) {
					sender.sendMessage(Utils.MUST_BE_PLAYER);
					return true;
				}
				if (modMode.contains(sender)) {
					leaveMod((Player) sender);
					return true;
				}
				enterMod((Player) sender);
				return true;
			}
			if (!sender.hasPermission("fullpvp.command.staffmode.argument.others")) {
				sender.sendMessage(Utils.NO_PERMISSION);
				return true;
			}
			Player t = Bukkit.getPlayer(args[0]);
			if (t == null) {
				sender.sendMessage(Utils.PLAYER_NOT_FOUND);
				return true;
			}
			if (modMode.contains(t)) {
				leaveMod(t);
				sender.sendMessage(ColorText.translate(MessagesFile.getConfig().getString("StaffMode-Enable-Otherplayer").replace("{player}", t.getName())));
				return true;
			}
			enterMod(t);
			sender.sendMessage(ColorText.translate(MessagesFile.getConfig().getString("StaffMode-Disable-Otherplayer").replace("{player}", t.getName())));
			return true;
	}
}
