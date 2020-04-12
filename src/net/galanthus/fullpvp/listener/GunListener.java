package net.galanthus.fullpvp.listener;

import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.Sound;
import org.bukkit.entity.Fireball;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import net.galanthus.fullpvp.FullPvP;
import net.galanthus.fullpvp.handler.GunHandler;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class GunListener implements Listener {
    public GunListener(final FullPvP plugin) {
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
    }
    
	@EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (!GunHandler.isHoeGun(player)) {
            return;
        }
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_AIR) {
            final Fireball fireball = (Fireball) player.launchProjectile(Fireball.class);
            player.playSound(player.getLocation(), Sound.GHAST_CHARGE, 1.0f, 1.0f);
            fireball.setFireTicks(1000);
            fireball.setIsIncendiary(false);
            fireball.setYield(5.0f);
            fireball.setFallDistance(100.0f);
            this.decrementItemInHand(player);
        }
    }
    
    private void decrementItemInHand(final Player player) {
        final ItemStack itemStack = player.getItemInHand();
        if (itemStack.getAmount() <= 1) {
            player.setItemInHand(new ItemStack(Material.AIR, 1));
        }
        else {
            itemStack.setAmount(itemStack.getAmount() - 1);
        }
    }
    
    @EventHandler
    public void onExplode(final EntityExplodeEvent event) {
        event.setCancelled(true);
    }
}
