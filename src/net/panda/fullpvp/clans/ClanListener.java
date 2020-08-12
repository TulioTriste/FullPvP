package net.panda.fullpvp.clans;

import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.panda.fullpvp.FullPvP;
import net.panda.fullpvp.tournaments.TournamentManager;
import net.panda.fullpvp.utilities.ColorText;

import org.bukkit.event.EventHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class ClanListener implements Listener {
	
    public ClanListener(final FullPvP plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onEntityDamage(final EntityDamageByEntityEvent event) {
    	TournamentManager tournamentManager = FullPvP.getInstance().getTournamentManager();
    	Player player = (Player) event.getEntity();
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player && ClanHandler.isMember((Player)event.getEntity(), ClanHandler.getClan((Player)event.getDamager()))) {
        	if (tournamentManager.isInTournament(player.getUniqueId())) {
        		event.setCancelled(false);
        		return;
        	}
            event.setCancelled(true);
            ((Player)event.getDamager()).sendMessage(ColorText.translate("&cYou can' hurt to " + ((Player)event.getEntity()).getName() + '.'));
            return;
        }
    }
}
