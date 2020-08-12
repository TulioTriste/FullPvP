package net.panda.fullpvp.listener;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.block.SignChangeEvent;

import net.panda.fullpvp.FullPvP;

import org.bukkit.event.Listener;

public class ColouredSignListener implements Listener {
	
	public ColouredSignListener(final FullPvP plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
	}
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onSignCreate(final SignChangeEvent event) {
        final Player player = event.getPlayer();
        if (player != null && player.hasPermission("fullpvp.sign.colour")) {
            final String[] lines = event.getLines();
            for (int i = 0; i < lines.length; ++i) {
                event.setLine(i, ChatColor.translateAlternateColorCodes('&', lines[i]));
            }
        }
    }
}
