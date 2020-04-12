package net.galanthus.fullpvp.claim;

import org.bukkit.World;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;

import net.galanthus.fullpvp.FullPvP;
import net.galanthus.fullpvp.configuration.LocationFile;
import net.galanthus.fullpvp.utilities.ColorText;

import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class ClaimListener implements Listener {
    LocationFile location;
    
    public ClaimListener(final FullPvP plugin) {
        this.location = LocationFile.getConfig();
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onClaimEntering(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final Location from = event.getFrom();
        final Location to = event.getTo();
        if (from.getBlockX() == to.getBlockX() && from.getBlockY() == to.getBlockY() && from.getBlockZ() == to.getBlockZ()) {
            return;
        }
        for (final String claim : this.location.getConfigurationSection("Claims").getKeys(false)) {
            final CuboidSelection selection = new CuboidSelection(Bukkit.getWorld(this.location.getString("Claims." + claim + ".world")), this.getLocation(claim, "cornerA"), this.getLocation(claim, "cornerB"));
            final boolean isPvP = this.location.getBoolean("Claims." + claim + ".pvp");
            if (selection.contains(event.getTo()) && !selection.contains(event.getFrom())) {
                player.sendMessage(ColorText.translate("&eNow Entering: &c" + (isPvP ? claim : ("&a" + claim)) + " &e(" + (isPvP ? "&cPvP" : "&aSafeZone") + "&e)"));
                if (isPvP) {
                    continue;
                }
                player.setHealth(((Damageable)player).getMaxHealth());
                player.setFoodLevel(20);
                player.setFireTicks(0);
            }
            else {
                if (!selection.contains(event.getFrom()) || selection.contains(event.getTo())) {
                    continue;
                }
                player.sendMessage(ColorText.translate("&eNow leaving: &c" + (isPvP ? claim : ("&a" + claim)) + " &e(" + (isPvP ? "&cPvP" : "&aSafeZone") + "&e)"));
            }
        }
    }
    
    @EventHandler
    public void onEntityDamage(final EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            for (final String claim : this.location.getConfigurationSection("Claims").getKeys(false)) {
                final CuboidSelection selection = new CuboidSelection(Bukkit.getWorld(this.location.getString("Claims." + claim + ".world")), this.getLocation(claim, "cornerA"), this.getLocation(claim, "cornerB"));
                final boolean isPvP = this.location.getBoolean("Claims." + claim + ".pvp");
                final Player player = (Player)event.getEntity();
                if (selection.contains(player.getLocation()) && !isPvP) {
                    event.setCancelled(true);
                }
                if (selection.contains(event.getDamager().getLocation()) && !isPvP) {
                    event.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler
    public void onEntityDamage(final EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            for (final String claim : this.location.getConfigurationSection("Claims").getKeys(false)) {
                final CuboidSelection selection = new CuboidSelection(Bukkit.getWorld(this.location.getString("Claims." + claim + ".world")), this.getLocation(claim, "cornerA"), this.getLocation(claim, "cornerB"));
                final boolean isPvP = this.location.getBoolean("Claims." + claim + ".pvp");
                final Player player = (Player)event.getEntity();
                if (selection.contains(player.getLocation()) && !isPvP) {
                    event.setCancelled(true);
                }
            }
        }
    }
    
    public Location getLocation(final String town, final String corner) {
        final LocationFile location = this.location;
        final World world = Bukkit.getWorld((String)LocationFile.getConfig().get("Claims." + town + ".world"));
        final double x = location.getDouble("Claims." + town + "." + corner + ".x");
        final double y = location.getDouble("Claims." + town + "." + corner + ".y");
        final double z = location.getDouble("Claims." + town + "." + corner + ".z");
        return new Location(world, x, y, z);
    }
}
