package net.panda.fullpvp.utilities;

import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import java.util.UUID;
import java.util.HashMap;

public class Cooldowns
{
    private static HashMap<String, HashMap<UUID, Long>> cooldown;
    
    public static void clearCooldowns() {
        Cooldowns.cooldown.clear();
    }
    
    public static void createCooldown(final String k) {
        if (Cooldowns.cooldown.containsKey(k)) {
            throw new IllegalArgumentException("Cooldown doesn't exists.");
        }
        Cooldowns.cooldown.put(k, new HashMap<UUID, Long>());
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "Cooldown '" + k + "' has been successfully created!");
    }
    
    public static HashMap<UUID, Long> getCooldownMap(final String k) {
        if (Cooldowns.cooldown.containsKey(k)) {
            return Cooldowns.cooldown.get(k);
        }
        return null;
    }
    
    public static void addCooldown(final String k, final Player p, final int seconds) {
        if (!Cooldowns.cooldown.containsKey(k)) {
            throw new IllegalArgumentException(String.valueOf(k) + " doesn't exists.");
        }
        final long next = System.currentTimeMillis() + seconds * 1000L;
        Cooldowns.cooldown.get(k).put(p.getUniqueId(), next);
    }
    
    public static boolean isOnCooldown(final String k, final Player p) {
        return Cooldowns.cooldown.containsKey(k) && Cooldowns.cooldown.get(k).containsKey(p.getUniqueId()) && System.currentTimeMillis() <= Cooldowns.cooldown.get(k).get(p.getUniqueId());
    }
    
    public static int getCooldownForPlayerInt(final String k, final Player p) {
        return (int)((Cooldowns.cooldown.get(k).get(p.getUniqueId()) - System.currentTimeMillis()) / 1000L);
    }
    
    public static long getCooldownForPlayerLong(final String k, final Player p) {
        return Cooldowns.cooldown.get(k).get(p.getUniqueId()) - System.currentTimeMillis();
    }
    
    public static void removeCooldown(final String k, final Player p) {
        if (!Cooldowns.cooldown.containsKey(k)) {
            throw new IllegalArgumentException(String.valueOf(k) + " doesn't exists.");
        }
        Cooldowns.cooldown.get(k).remove(p.getUniqueId());
    }
    
    static {
        Cooldowns.cooldown = new HashMap<String, HashMap<UUID, Long>>();
    }
}
