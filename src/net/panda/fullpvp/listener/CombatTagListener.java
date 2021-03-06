package net.panda.fullpvp.listener;

import org.bukkit.event.EventPriority;
import org.bukkit.Bukkit;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import net.panda.fullpvp.FullPvP;
import net.panda.fullpvp.utilities.ColorText;

import java.util.HashMap;
import org.bukkit.entity.Player;
import java.util.Map;

import org.bukkit.event.Listener;

public class CombatTagListener implements Listener {
    private FullPvP fullPvP;
    private Map<Player, Long> combatTagCooldownMap;
    private int combatTagCooldownTime;
    
    public CombatTagListener(final FullPvP fullpvp) {
        this.combatTagCooldownMap = new HashMap<Player, Long>();
        this.fullPvP = fullpvp;
    }
    
    public void init() {
        this.fullPvP.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this.fullPvP);
        this.combatTagCooldownTime = this.fullPvP.getConfig().getInt("Combat-Tag.Time");
    }
    
    public void putCooldown(final Player player, final long cooldown) {
        if (!this.combatTagCooldownMap.containsKey(player)) {
            this.combatTagCooldownMap.put(player, cooldown);
        }
        else {
            final long localValue = this.combatTagCooldownMap.get(player);
            this.combatTagCooldownMap.put(player, Math.max(cooldown, localValue));
        }
    }
    
    public void removeCooldown(final Player player) {
        this.combatTagCooldownMap.remove(player);
    }
    
    public boolean hasCooldown(final Player player) {
        if (!this.combatTagCooldownMap.containsKey(player)) {
            return false;
        }
        final long localValue = this.combatTagCooldownMap.get(player);
        return localValue > System.currentTimeMillis();
    }
    
    public long getMillisecondsLeft(final Player player) {
        if (!this.hasCooldown(player)) {
            return -1L;
        }
        return this.combatTagCooldownMap.get(player) - System.currentTimeMillis();
    }
    
    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final Player localPlayer = event.getPlayer();
        if(this.hasCooldown(localPlayer)) {
            localPlayer.setHealth(0.0);
            Bukkit.broadcastMessage(ColorText.translate(this.fullPvP.getConfig().getString("Combat-Tag.Disconnect"))
            		.replace("{player}", localPlayer.getName()));
        }
        this.removeCooldown(localPlayer);
    }
    
    @EventHandler
    public void onPlayerKick(final PlayerKickEvent event) {
        final Player localPlayer = event.getPlayer();
        if(this.hasCooldown(localPlayer)) {
            localPlayer.setHealth(0.0);
            Bukkit.broadcastMessage(ColorText.translate(this.fullPvP.getConfig().getString("Combat-Tag.Disconnect"))
            		.replace("{player}", localPlayer.getName()));
        }
        this.removeCooldown(localPlayer);
    }
    
    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent event) {
        final Player localPlayer = event.getEntity().getPlayer();
        this.removeCooldown(localPlayer);
    }
    
	public Player getPlayer(final Entity event) {
        if (event instanceof Player) {
            return (Player)event;
        }
        if (event instanceof Projectile) {
            final Projectile localProjectile = (Projectile)event;
            if (localProjectile.getShooter() != null && localProjectile.getShooter() instanceof Player) {
                return (Player)localProjectile.getShooter();
            }
        }
        return null;
    }
    
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if(FullPvP.getInstance().getTournamentManager().isInTournament(event.getEntity().getUniqueId())) {
        	return;
        }
        final Player localDamager = this.getPlayer(event.getDamager());
        final Player localDamaged = this.getPlayer(event.getEntity());
        if (localDamager != null && localDamaged != null && localDamaged != localDamager) {
            if (!this.hasCooldown(localDamager)) {
                localDamager.sendMessage(ColorText.translate(this.fullPvP.getConfig().getString("Combat-Tag.Message")
                		.replace("{time}", String.valueOf(this.fullPvP.getConfig().getInt("Combat-Tag.Time")))));
            }
            if (!this.hasCooldown(localDamaged)) {
            	localDamaged.sendMessage(ColorText.translate(this.fullPvP.getConfig().getString("Combat-Tag.Message")
                		.replace("{time}", String.valueOf(this.fullPvP.getConfig().getInt("Combat-Tag.Time")))));
            }
            this.putCooldown(localDamager, System.currentTimeMillis() + 1000 * this.combatTagCooldownTime);
            this.putCooldown(localDamaged, System.currentTimeMillis() + 1000 * this.combatTagCooldownTime);
        }
        return;
    }
    
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
    	Player player = event.getPlayer();
    	if(this.hasCooldown(player)) {
    		for(String commands : FullPvP.getInstance().getConfig().getStringList("Block-Commands-In-Combat")) {
    			if(event.getMessage().contains(commands)) {
        			event.setCancelled(true);
        			player.sendMessage(ColorText.translate(this.fullPvP.getConfig().getString("Combat-Tag.Block-Command")));
        		}
    		}
    	}
    }
}
