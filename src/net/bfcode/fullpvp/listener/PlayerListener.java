package net.bfcode.fullpvp.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import net.bfcode.fullpvp.FullPvP;
import net.bfcode.fullpvp.clans.ClanHandler;
import net.bfcode.fullpvp.utilities.ColorText;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class PlayerListener implements Listener {
	
	private FullPvP plugin;
	
    public PlayerListener(final FullPvP plugin) {
    	this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerDeath(final PlayerDeathEvent event) {
        if (event.getEntity() instanceof Player && event.getEntity().getKiller() instanceof Player) {
            final Player player = event.getEntity();
        	if(this.plugin.getTournamentManager().isInTournament(player)) {
        		return;
        	}
        	FullPvP.getInstance().getEconomyManager().addBalance(player.getKiller().getUniqueId(), FullPvP.getInstance().getConfig().getInt("Death.Money"));
        	player.sendMessage(ColorText.translate(FullPvP.getInstance().getConfig().getString("Death.Player-Message")
        			.replace("{killer}", player.getKiller().getName())));
    		player.getKiller().sendMessage(ColorText.translate(FullPvP.getInstance().getConfig().getString("Death.Killer-Message")
    				.replace("{player}", player.getName())
    				.replace("{money}", String.valueOf(FullPvP.getInstance().getConfig().getInt("Death.Money")))));
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void setTotalKills(PlayerDeathEvent event) {
    	Player player = event.getEntity();
    	if(ClanHandler.hasClan(player)) {
    		if(ClanHandler.getTotalKills(ClanHandler.getClan(player)) == 0) {
    			return;
    		}
    		if(ClanHandler.getTotalKills(ClanHandler.getClan(player)) > 0) {
    			ClanHandler.setTotalKills(ClanHandler.getClan(player), ClanHandler.getTotalKills(ClanHandler.getClan(player)) - 1);
    		}
    	}
    	if(player.getKiller() instanceof Player) {
    		Player killer = player.getKiller();
    		if(ClanHandler.hasClan(killer)) {
    			ClanHandler.setTotalKills(ClanHandler.getClan(killer), ClanHandler.getTotalKills(ClanHandler.getClan(killer)) + 1);
    		}
    	}
    }
}
