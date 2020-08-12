package net.panda.fullpvp.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import net.panda.fullpvp.FullPvP;
import net.panda.fullpvp.commands.StaffModeCommand;

public class VanishListener implements Listener {
	
	public static Map<Player, Player> examineTasks = new HashMap<Player, Player>();
	static VanishListener instance = new VanishListener();

	public VanishListener() {
		Bukkit.getPluginManager().registerEvents(this, FullPvP.getInstance());
	}

	public static VanishListener getInstance() {
		return instance;
	}

	private static ArrayList<Player> Vanish = new ArrayList<Player>();

	public static boolean isVanished(Player p) {
		return Vanish.contains(p);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		for (Player online : Bukkit.getOnlinePlayers()) {
			if (isVanished(online)) {
				if (player.hasPermission("fullpvp.command.vanish")) {
					player.showPlayer(online);
				} else {
					player.hidePlayer(online);
				}
			}
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			return;
		}
		Player p = (Player) e.getEntity();
		if (VanishListener.isVanished(p)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if (VanishListener.isVanished(e.getPlayer()) || StaffModeCommand.isMod(e.getPlayer())) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onDrop(PlayerPickupItemEvent e) {
		if (VanishListener.isVanished(e.getPlayer())) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			Player player = (Player) event.getDamager();
			if (VanishListener.isVanished(player)) {
				event.setCancelled(true);
			}
		}
	}

	public void setVanish(Player p, boolean b) {
		if (b == true) {
			Vanish.add(p);
			for (Player online : Bukkit.getOnlinePlayers()) {
				if (online.hasPermission("fullpvp.command.vanish")) {
					online.showPlayer(p);
				} else {
					online.hidePlayer(p);
				}
			}
		}
		if (b == false) {
			Vanish.remove(p);
			for (Player online : Bukkit.getOnlinePlayers()) {
				online.showPlayer(p);
			}
		}
	}

	public ArrayList<Player> listInVanish() {
		return Vanish;
	}

}
