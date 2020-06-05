package net.bfcode.fullpvp.claim;

import org.bukkit.World;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;

import net.bfcode.fullpvp.FullPvP;
import net.bfcode.fullpvp.configuration.LocationFile;
import net.bfcode.fullpvp.utilities.ColorText;

import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
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
                if (isPvP) {
                    continue;
                }
                player.sendMessage(ColorText.translate("&7* &eEntrando a Zona Segura"));
                player.setHealth(((Damageable)player).getMaxHealth());
                player.setFoodLevel(20);
                player.setFireTicks(0);
            }
            else {
                if (!selection.contains(event.getFrom()) || selection.contains(event.getTo())) {
                    continue;
                }
                if(isPvP) {
                	continue;
                }
                player.sendMessage(ColorText.translate("&7* &eSaliendo de Zona Segura"));
            }
        }
    }
    
    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        for (final String claim : this.location.getConfigurationSection("Claims").getKeys(false)) {
            final CuboidSelection selection = new CuboidSelection(Bukkit.getWorld(this.location.getString("Claims." + claim + ".world")), this.getLocation(claim, "cornerA"), this.getLocation(claim, "cornerB"));
            final boolean isPvP = this.location.getBoolean("Claims." + claim + ".pvp");
            if(event.getEntity() instanceof Player) {
	            if(selection.contains(event.getEntity().getLocation()) && !isPvP) {
	            	event.setCancelled(true);
	            }
            }
        }
    }
    
    @EventHandler
    public void onEnderpearl(final PlayerTeleportEvent event) {
        for (final String claim : this.location.getConfigurationSection("Claims").getKeys(false)) {
            final CuboidSelection selection = new CuboidSelection(Bukkit.getWorld(this.location.getString("Claims." + claim + ".world")), this.getLocation(claim, "cornerA"), this.getLocation(claim, "cornerB"));
            final boolean isPvP = this.location.getBoolean("Claims." + claim + ".pvp");
            if(event.getCause().equals(TeleportCause.ENDER_PEARL)) {
	            if(!selection.contains(event.getFrom()) && selection.contains(event.getTo()) && !isPvP) {
	            	FullPvP.getPlugin().getEnderpearlListener().removeCooldown(event.getPlayer());
	                event.getPlayer().getInventory().addItem(new ItemStack[] { new ItemStack(Material.ENDER_PEARL) });
	            	event.setCancelled(true);
	            	event.getPlayer().sendMessage(ColorText.translate("&cNo puedes perlear a una zona sin PvP!"));
	            }	
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
        final int x = location.getInt("Claims." + town + "." + corner + ".x");
        final int y = location.getInt("Claims." + town + "." + corner + ".y");
        final int z = location.getInt("Claims." + town + "." + corner + ".z");
        return new Location(world, x, y, z);
    }
}
