package net.panda.fullpvp.handler;

import java.util.HashMap;

import org.bukkit.Sound;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.panda.fullpvp.configuration.MessagesFile;

import java.util.Map;

public class KillStreakHandler {
    private static Map<String, Integer> count;
    
    public static int getKillStreakCount(final Player player) {
        final int kills = KillStreakHandler.count.get(player.getName());
        return kills;
    }
    
    public static Integer clearKillStreak(final Player player) {
        return KillStreakHandler.count.put(player.getName(), 0);
    }
    
    public static void addKillStreak(final Player killer) {
        final String name = killer.getName();
        if (KillStreakHandler.count.containsKey(name)) {
            int kills = KillStreakHandler.count.get(name);
            ++kills;
            KillStreakHandler.count.put(name, kills);
            MessagesFile messages = MessagesFile.getConfig();
            if (kills == 65) {
                for(String msg : messages.getStringList("Kill-Streak-Rewards." + kills)) {
                	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), msg.replace("{player}", killer.getName()).replace("{kills}", kills + ""));
                }
            }
            if (kills == 60) {
                for(String msg : messages.getStringList("Kill-Streak-Rewards." + kills)) {
                	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), msg.replace("{player}", killer.getName()).replace("{kills}", kills + ""));
                }
            }
            if (kills == 55) {
                for(String msg : messages.getStringList("Kill-Streak-Rewards." + kills)) {
                	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), msg.replace("{player}", killer.getName()).replace("{kills}", kills + ""));
                }
            }
            if (kills == 50) {
                for(String msg : messages.getStringList("Kill-Streak-Rewards." + kills)) {
                	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), msg.replace("{player}", killer.getName()).replace("{kills}", kills + ""));
                }
            }
            if (kills == 45) {
                for(String msg : messages.getStringList("Kill-Streak-Rewards." + kills)) {
                	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), msg.replace("{player}", killer.getName()).replace("{kills}", kills + ""));
                }
            }
            if (kills == 40) {
                for(String msg : messages.getStringList("Kill-Streak-Rewards." + kills)) {
                	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), msg.replace("{player}", killer.getName()).replace("{kills}", kills + ""));
                }
            }
            if (kills == 35) {
                for(String msg : messages.getStringList("Kill-Streak-Rewards." + kills)) {
                	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), msg.replace("{player}", killer.getName()).replace("{kills}", kills + ""));
                }
            }
            if (kills == 30) {
                for(String msg : messages.getStringList("Kill-Streak-Rewards." + kills)) {
                	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), msg.replace("{player}", killer.getName()).replace("{kills}", kills + ""));
                }
            }
            if (kills == 25) {
                for(String msg : messages.getStringList("Kill-Streak-Rewards." + kills)) {
                	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), msg.replace("{player}", killer.getName()).replace("{kills}", kills + ""));
                }
            }
            if (kills == 20) {
                for(String msg : messages.getStringList("Kill-Streak-Rewards." + kills)) {
                	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), msg.replace("{player}", killer.getName()).replace("{kills}", kills + ""));
                }
            }
            if (kills == 15) {
                for(String msg : messages.getStringList("Kill-Streak-Rewards." + kills)) {
                	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), msg.replace("{player}", killer.getName()).replace("{kills}", kills + ""));
                }
            }
            if (kills == 10) {
                for(String msg : messages.getStringList("Kill-Streak-Rewards." + kills)) {
                	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), msg.replace("{player}", killer.getName()).replace("{kills}", kills + ""));
                }
            }
            if (kills == 5) {
                for(String msg : messages.getStringList("Kill-Streak-Rewards." + kills)) {
                	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), msg.replace("{player}", killer.getName()).replace("{kills}", kills + ""));
                }
            }
        }
        else {
            KillStreakHandler.count.put(name, 1);
            killer.playSound(killer.getLocation(), Sound.ANVIL_USE, 1.0f, 1.0f);
        }
    }
    
    static {
        KillStreakHandler.count = new HashMap<String, Integer>();
    }
}
