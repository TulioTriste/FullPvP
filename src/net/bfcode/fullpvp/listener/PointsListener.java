package net.bfcode.fullpvp.listener;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.bfcode.fullpvp.configuration.PointsFile;
import net.bfcode.fullpvp.utilities.ColorText;

public class PointsListener implements Listener {
	
	@EventHandler
    public void onUserJoin(PlayerJoinEvent event) {
    	Player player = event.getPlayer();
    	PointsFile points = PointsFile.getConfig();
    	UUID UUID = player.getUniqueId();
    	if(!player.hasPlayedBefore()) {
    		points.set("users." + UUID + ".name", player.getName());
    		points.set("users." + UUID + ".points", 0);
    		points.save();
    		points.reload();
    		player.sendMessage(ColorText.translate("&aYour profile has been successfully created."));
    		return;
    	}
    	return;
    }
	
	@EventHandler
	public void onUserLeft(PlayerQuitEvent event) {
		PointsFile points = PointsFile.getConfig();
		points.save();
		points.reload();
		return;
	}
	
	@EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity() instanceof Player && event.getEntity().getKiller() instanceof Player) {
            Player player = event.getEntity();
            PointsFile points = PointsFile.getConfig();
            UUID UUID = player.getKiller().getUniqueId();
            points.set("users." + UUID + ".points", points.getInt("users." + UUID + ".points") + 1);
            points.save();
    		points.reload();
    		player.getKiller().sendMessage(ColorText.translate("&eYour received &61 RP &efor killed " + player.getName() + "."));
        }
    }
}
