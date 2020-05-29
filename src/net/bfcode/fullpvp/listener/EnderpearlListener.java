package net.bfcode.fullpvp.listener;

import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.bfcode.fullpvp.FullPvP;
import net.bfcode.fullpvp.configuration.LocationFile;
import net.bfcode.fullpvp.utilities.ColorText;
import net.bfcode.fullpvp.utilities.Utils;

import org.bukkit.event.Event;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.HashMap;

import org.bukkit.event.Listener;

public class EnderpearlListener implements Listener {
	
	private LocationFile location;
    private FullPvP fullPvP;
    private HashMap<Player, Long> enderpearlCooldownMap;
    private int enderpearlCooldownTime;
    
    public EnderpearlListener(final FullPvP fullPvP) {
        this.enderpearlCooldownMap = new HashMap<Player, Long>();
        this.fullPvP = fullPvP;
        this.location = LocationFile.getConfig();
    }
    
    public void init() {
        this.enderpearlCooldownTime = this.fullPvP.getConfig().getInt("Ender-Pearl.Time");
        Bukkit.getServer().getPluginManager().registerEvents(this, this.fullPvP);
    }
    
    public void putCooldown(final Player player, final long paramLong) {
        final long localLong = System.currentTimeMillis() + paramLong * 1000L;
        this.enderpearlCooldownMap.put(player, localLong);
    }
    
    public void removeCooldown(final Player player) {
        this.enderpearlCooldownMap.remove(player);
    }
    
    public boolean hasCooldown(final Player player) {
        if (!this.enderpearlCooldownMap.containsKey(player)) {
            return false;
        }
        final long localValue = this.enderpearlCooldownMap.get(player);
        return localValue > System.currentTimeMillis();
    }
    
    public long getMillisecondsLeft(final Player player) {
        if (!this.hasCooldown(player)) {
            return -1L;
        }
        return this.enderpearlCooldownMap.get(player) - System.currentTimeMillis();
    }
    
    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent playerQuitEvent) {
        final Player localPlayer = playerQuitEvent.getPlayer();
        this.removeCooldown(localPlayer);
    }
    
    @EventHandler
    public void onPlayerKick(final PlayerKickEvent playerKickEvent) {
        final Player localPlayer = playerKickEvent.getPlayer();
        this.removeCooldown(localPlayer);
    }
    
    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent playerDeathEvent) {
        final Player localPlayer = playerDeathEvent.getEntity().getPlayer();
        this.removeCooldown(localPlayer);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(final PlayerInteractEvent playerInteractEvent) {
        final Action localAction = playerInteractEvent.getAction();
        if (localAction == Action.RIGHT_CLICK_AIR || localAction == Action.RIGHT_CLICK_BLOCK) {
            final Material localMaterial = playerInteractEvent.getMaterial();
            final Player localPlayer = playerInteractEvent.getPlayer();
            if (localMaterial == Material.ENDER_PEARL && localPlayer.getGameMode() != GameMode.CREATIVE) {
                if (this.hasCooldown(localPlayer)) {
                    playerInteractEvent.setUseItemInHand(Event.Result.DENY);
                    localPlayer.sendMessage(ColorText.translate(this.fullPvP.getConfig().getString("Ender-Pearl.Message.Remaining")
                    		.replace("{time}", String.valueOf(Utils.getDecimalFormat().format(this.getMillisecondsLeft(localPlayer) / 1000.0)))));
                }
                else {
                    new BukkitRunnable() {
                        public void run() {
                            final ItemStack localItemStack = localPlayer.getItemInHand();
                            if (localItemStack != null && localItemStack.getType() == Material.ENDER_PEARL && localPlayer.getGameMode() != GameMode.CREATIVE) {
                                if (EnderpearlListener.this.hasCooldown(localPlayer)) {
                                    final ItemMeta localItemMeta = localItemStack.getItemMeta();
                                    localItemMeta.setDisplayName(ColorText.translate(EnderpearlListener.this.fullPvP.getConfig().getString("Ender-Pearl.Name.Remaining")
                                    		.replace("{time}", String.valueOf(Utils.getDecimalFormat().format(EnderpearlListener.this.getMillisecondsLeft(localPlayer) / 1000.0)))));
                                    localItemStack.setItemMeta(localItemMeta);
                                }
                                else if (localItemStack.getItemMeta().hasDisplayName()) {
                                    this.cancel();
                                    final ItemMeta localItemMeta = localItemStack.getItemMeta();
                                    localItemMeta.setDisplayName(ColorText.translate(EnderpearlListener.this.fullPvP.getConfig().getString("Ender-Pearl.Name.Ended")));
                                    localItemStack.setItemMeta(localItemMeta);
                                    localPlayer.sendMessage(ColorText.translate(EnderpearlListener.this.fullPvP.getConfig().getString("Ender-Pearl.Message.Ended")));
                                }
                            }
                        }
                    }.runTaskTimer(this.fullPvP, 1L, 1L);
                    this.putCooldown(localPlayer, this.enderpearlCooldownTime);
                }
            }
        }
    }
    
    @EventHandler
    public void onInventoryClickEvent(final InventoryClickEvent event) {
        final ItemStack localItemStack = event.getCurrentItem();
        if (localItemStack != null && localItemStack.getType() == Material.ENDER_PEARL) {
            final ItemMeta localItemMeta = event.getCurrentItem().getItemMeta();
            localItemMeta.setDisplayName((String)null);
            event.getCurrentItem().setItemMeta(localItemMeta);
        }
    }
    
    @EventHandler
    public void onPlayerItemHeldEvent(final PlayerItemHeldEvent playerItemHeldEvent) {
        final ItemStack localItemStack = playerItemHeldEvent.getPlayer().getInventory().getItem(playerItemHeldEvent.getPreviousSlot());
        if (localItemStack != null && localItemStack.getType() == Material.ENDER_PEARL && localItemStack.getItemMeta() != null) {
            final ItemMeta localItemMeta = localItemStack.getItemMeta();
            localItemMeta.setDisplayName((String)null);
            localItemStack.setItemMeta(localItemMeta);
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
