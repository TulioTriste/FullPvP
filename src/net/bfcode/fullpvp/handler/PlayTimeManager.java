package net.bfcode.fullpvp.handler;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import gnu.trove.map.hash.TObjectLongHashMap;
import net.bfcode.fullpvp.utilities.Config;

public class PlayTimeManager implements Listener {
    private final TObjectLongHashMap<UUID> totalPlaytimeMap;
    private final TObjectLongHashMap<UUID> sessionTimestamps;
    private final Config config;
    
    public PlayTimeManager(final JavaPlugin plugin) {
        this.totalPlaytimeMap = new TObjectLongHashMap<UUID>();
        this.sessionTimestamps = new TObjectLongHashMap<UUID>();
        this.config = new Config(plugin, "play-times");
        this.reloadPlaytimeData();
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        this.sessionTimestamps.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final UUID uuid = event.getPlayer().getUniqueId();
        this.totalPlaytimeMap.put(uuid, this.getTotalPlayTime(uuid));
        this.sessionTimestamps.remove(uuid);
    }
    
	private void reloadPlaytimeData() {
        final Object object = this.config.get("playing-times");
        if (object instanceof MemorySection) {
            final MemorySection section = (MemorySection)object;
            for (final Object id : section.getKeys(false)) {
                this.totalPlaytimeMap.put(UUID.fromString((String)id), this.config.getLong("playing-times." + id, 0L));
            }
        }
        final long millis = System.currentTimeMillis();
        for (final Player target : Bukkit.getServer().getOnlinePlayers()) {
            this.sessionTimestamps.put(target.getUniqueId(), millis);
        }
    }
    
	public void savePlaytimeData() {
        for (final Player player : Bukkit.getServer().getOnlinePlayers()) {
            this.totalPlaytimeMap.put(player.getUniqueId(), this.getTotalPlayTime(player.getUniqueId()));
        }
        this.totalPlaytimeMap.forEachEntry((uuid, l) -> {
            this.config.set("playing-times." + uuid.toString(), l);
            return true;
        });
        this.config.save();
    }
    
    private long getSessionPlayTime(final UUID uuid) {
        final long session = this.sessionTimestamps.get(uuid);
        return (session != this.sessionTimestamps.getNoEntryValue()) ? (System.currentTimeMillis() - session) : 0L;
    }
    
    private long getPreviousPlayTime(final UUID uuid) {
        final long stamp = this.totalPlaytimeMap.get(uuid);
        return (stamp == this.totalPlaytimeMap.getNoEntryValue()) ? 0L : stamp;
    }
    
    public long getTotalPlayTime(final UUID uuid) {
        return this.getSessionPlayTime(uuid) + this.getPreviousPlayTime(uuid);
    }
}
