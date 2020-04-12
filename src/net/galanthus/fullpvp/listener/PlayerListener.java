package net.galanthus.fullpvp.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import net.galanthus.fullpvp.FullPvP;
import net.galanthus.fullpvp.clans.ClanHandler;
import net.galanthus.fullpvp.utilities.ColorText;

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
        	FullPvP.getPlugin().getEconomyManager().addBalance(player.getKiller().getUniqueId(), FullPvP.getPlugin().getConfig().getInt("DeathMessage.Money"));
    		player.getKiller().sendMessage(ColorText.translate(FullPvP.getPlugin().getConfig().getString("DeathMessage.Killer").replace("%playername%", player.getName()).replace("%money%", "" + FullPvP.getPlugin().getConfig().getInt("DeathMessage.Money"))));
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
