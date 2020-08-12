package net.panda.fullpvp.handler;

import org.bukkit.Bukkit;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import net.panda.fullpvp.FullPvP;
import net.panda.fullpvp.configuration.LocationFile;
import net.panda.fullpvp.utilities.ColorText;

import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;
import java.util.HashMap;
import org.bukkit.event.Listener;

public class SpawnHandler extends Handler implements Listener {
    private HashMap<UUID, SpawnTask> spawnTasks;
    private HashMap<UUID, Long> warmup;
    
    public SpawnHandler(final FullPvP FullPvP) {
        super(FullPvP);
        this.spawnTasks = new HashMap<UUID, SpawnTask>();
        this.warmup = new HashMap<UUID, Long>();
    }
    
    @Override
    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }
    
    @Override
    public void disable() {
        this.spawnTasks.clear();
        this.warmup.clear();
    }
    
    public void applyWarmup(final Player player) {
        this.warmup.put(player.getUniqueId(), System.currentTimeMillis() + FullPvP.getInstance().getConfig().getInt("Spawn.Time") * 1000);
    }
    
    public boolean isActive(final Player player) {
        return this.warmup.containsKey(player.getUniqueId()) && System.currentTimeMillis() < this.warmup.get(player.getUniqueId());
    }
    
    public long getMillisecondsLeft(final Player player) {
        if (this.warmup.containsKey(player.getUniqueId())) {
            return Math.max(this.warmup.get(player.getUniqueId()) - System.currentTimeMillis(), 0L);
        }
        return 0L;
    }
    
    public void createSpawn(final Player player) {
        final SpawnTask SpawnTask = new SpawnTask(player);
        SpawnTask.runTaskLater((Plugin)FullPvP.getInstance(), (long)(FullPvP.getInstance().getConfig().getInt("Spawn.Time") * 20));
        this.applyWarmup(player);
        this.spawnTasks.put(player.getUniqueId(), SpawnTask);
    }
    
    public void removeSpawn(final Player player) {
        if (this.spawnTasks.containsKey(player.getUniqueId())) {
            this.spawnTasks.get(player.getUniqueId()).cancel();
            this.spawnTasks.remove(player.getUniqueId());
        }
    }
    
    public HashMap<UUID, SpawnTask> getSpawnTasks() {
        return this.spawnTasks;
    }
    
    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent playerMoveEvent) {
        final Player player = playerMoveEvent.getPlayer();
        final Location from = playerMoveEvent.getFrom();
        final Location to = playerMoveEvent.getTo();
        if (from.getPitch() != to.getPitch() || from.getYaw() != to.getYaw()) {
            return;
        }
        if (this.spawnTasks.containsKey(player.getUniqueId()) && (from.getBlockX() != to.getBlockX() || from.getBlockZ() != to.getBlockZ())) {
            this.spawnTasks.get(player.getUniqueId()).cancel();
            this.spawnTasks.remove(player.getUniqueId());
            player.sendMessage(ColorText.translate(FullPvP.getInstance().getConfig().getString("Spawn.Teleport-Cancelled")));
        }
    }
    
    @EventHandler
    public void onPlayerDamage(final EntityDamageEvent entityDamageEvent) {
        final Entity entity = entityDamageEvent.getEntity();
        if (entity instanceof Player) {
            final Player player = (Player)entity;
            if (this.spawnTasks.containsKey(player.getUniqueId())) {
                this.spawnTasks.get(player.getUniqueId()).cancel();
                this.spawnTasks.remove(player.getUniqueId());
                player.sendMessage(ColorText.translate(FullPvP.getInstance().getConfig().getString("Spawn.Teleport-Cancelled")));
            }
        }
    }
    
    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent playerQuitEvent) {
        final Player player = playerQuitEvent.getPlayer();
        if (this.spawnTasks.containsKey(player.getUniqueId())) {
            this.spawnTasks.get(player.getUniqueId()).cancel();
            this.spawnTasks.remove(player.getUniqueId());
        }
    }
    
    @EventHandler
    public void onPlayerQuit(final PlayerKickEvent playerKickEvent) {
        final Player player = playerKickEvent.getPlayer();
        if (this.spawnTasks.containsKey(player.getUniqueId())) {
            this.spawnTasks.get(player.getUniqueId()).cancel();
            this.spawnTasks.remove(player.getUniqueId());
        }
    }
    
    public class SpawnTask extends BukkitRunnable {
        private Player player;
        
        public SpawnTask(final Player player) {
            this.player = player;
        }
        public void run() {
            this.player.setMetadata("SpawnCommand", (MetadataValue)new FixedMetadataValue(FullPvP.getInstance(), Boolean.TRUE));
            final LocationFile locations = LocationFile.getConfig();
            final int x = locations.getInt("Locations.Spawn.X");
            final int y = locations.getInt("Locations.Spawn.Y");
            final int z = locations.getInt("Locations.Spawn.Z");
            final String world = locations.getString("Locations.Spawn.World");
            final Location location = new Location(Bukkit.getWorld(world), (double)x, (double)y, (double)z);
            this.player.teleport(location);
            this.player.sendMessage(ColorText.translate(FullPvP.getInstance().getConfig().getString("Spawn.Teleported")));
            SpawnHandler.this.spawnTasks.remove(this.player.getUniqueId());
            if (FullPvP.getInstance().getConfig().getBoolean("Spawn.Sound.Enabled")) {
                String sound = FullPvP.getInstance().getConfig().getString("Spawn.Sound.Type");
                if (sound == null) {
                    return;
                }
                this.player.playSound(this.player.getLocation(), sound, 1.0f, 1.0f);
            }
        }
    }
}
