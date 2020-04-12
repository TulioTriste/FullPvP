package net.galanthus.fullpvp.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.projectiles.ProjectileSource;

import net.galanthus.fullpvp.FullPvP;
import net.galanthus.fullpvp.utilities.ColorText;

import org.bukkit.entity.Entity;

import java.text.DecimalFormat;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class ArrowListener implements Listener {
    public ArrowListener(final FullPvP plugin) {
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
    }
    
    @EventHandler
    public void onEntityDamage(final EntityDamageByEntityEvent event) {
        final Entity entity = event.getEntity();
        final Entity damager = event.getDamager();
        if (entity instanceof Player && damager instanceof Arrow) {
            final Arrow arrow = (Arrow)damager;
			final ProjectileSource source = (ProjectileSource)arrow.getShooter();
            if (source instanceof Player) {
                final Player damaged = (Player)entity;
                final Player shooter = (Player)source;
                final double health = ((Damageable)damaged).getHealth() / 2.0;
                final DecimalFormat decimalFormat = new DecimalFormat("#.#");
                if (damaged.equals(shooter)) {
                    shooter.sendMessage(ColorText.translate("&6Your Health: &f" + decimalFormat.format(health) + "&4\u2764"));
                    return;
                }
                shooter.sendMessage(ColorText.translate("&6" + damaged.getName() + "'s Health: &f" + decimalFormat.format(health) + "&4\u2764"));
            }
        }
    }
}
