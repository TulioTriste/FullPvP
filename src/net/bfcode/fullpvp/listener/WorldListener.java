package net.bfcode.fullpvp.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import net.bfcode.fullpvp.FullPvP;
import net.bfcode.fullpvp.utilities.PlayerUtil;


public class WorldListener implements Listener {

	public WorldListener(final FullPvP plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (player.hasPermission("fullpvp.break.bypass")) {
			event.setCancelled(false);
			return;
		}
		event.setCancelled(true);
		return;
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (player.hasPermission("fullpvp.place.bypass")) {
			event.setCancelled(false);
			return;
		}
		event.setCancelled(true);
		return;
	}
	
	@EventHandler
	public void onLeave(PlayerKickEvent event) {
		PlayerUtil.allowMovement(event.getPlayer());
	}
	
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {
	      event.setCancelled(true); 
	}

	@EventHandler
	public void onWeatherChange(WeatherChangeEvent event) {
	    if (event.toWeatherState()) {
	    	event.setCancelled(true); 
	    }
	}
}
