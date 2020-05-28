package net.bfcode.fullpvp.handler;

import java.util.HashMap;

import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.bfcode.fullpvp.FullPvP;
import net.bfcode.fullpvp.utilities.ColorText;
import net.bfcode.fullpvp.utilities.ItemMaker;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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
            if (kills == 39) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "key give " + killer.getName() + " TierThree 3");
                sendMessageToPlayers(killer.getDisplayName() + " &ehas gotten the &cTierThree &ekillstreak! &7[x39]", "&6III TierThree");
            }
            if (kills == 36) {
                FullPvP.getPlugin().getEconomyManager().setBalance(killer.getUniqueId(), FullPvP.getPlugin().getEconomyManager().getBalance(killer.getUniqueId()) + 5000);
                sendMessageToPlayers(killer.getDisplayName() + " &ehas gotten the the &bMoney &ekillstreak! &7[x36]", "&+5000 Balance");
            }
            if (kills == 33) {
                killer.getInventory().addItem(new ItemStack[] { new ItemMaker(Material.DIAMOND_SWORD).enchant(Enchantment.DAMAGE_ALL, 5).displayName("&9KillStreak Sword &7[x33]").enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.FIRE_ASPECT, 2).create() });
                sendMessageToPlayers(killer.getDisplayName() + " &ehas gotten the &bDiamond Sword &ekillstreak! &7[x33]", "&7Sharpness Level V");
            }
            if (kills == 30) {
                killer.getInventory().addItem(new ItemStack[] { new ItemMaker(Material.DIAMOND_BOOTS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.PROTECTION_FALL, 4).create() });
                sendMessageToPlayers(killer.getDisplayName() + " &ehas gotten the &bDiamond Boots &ekillstreak! &7[x30]", "&7Protection Level V");
            }
            if (kills == 27) {
                killer.getInventory().addItem(new ItemStack[] { new ItemMaker(Material.DIAMOND_LEGGINGS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5).enchant(Enchantment.DURABILITY, 3).create() });
                sendMessageToPlayers(killer.getDisplayName() + " &ehas gotten the &bDiamond Leggings &ekillstreak! &7[x27]", "&7Protection Level V");
            }
            if (kills == 24) {
                killer.getInventory().addItem(new ItemStack[] { new ItemMaker(Material.DIAMOND_CHESTPLATE).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5).enchant(Enchantment.DURABILITY, 3).create() });
                sendMessageToPlayers(killer.getDisplayName() + " &ehas gotten the &bDiamond Chestplate &ekillstreak! &7[x24]", "&7Protection Level V");
            }
            if (kills == 21) {
                killer.getInventory().addItem(new ItemStack[] { new ItemMaker(Material.DIAMOND_HELMET).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5).enchant(Enchantment.DURABILITY, 3).create() });
                sendMessageToPlayers(killer.getDisplayName() + " &ehas gotten the &bDiamond Helmet &ekillstreak! &7[x21]", "&7Protection Level V");
            }
            if (kills == 18) {
                GunHandler.getHoeGun(killer, 1);
                sendMessageToPlayers(killer.getDisplayName() + " &ehas gotten the &9Hoe Gun &ekillstreak! &7[x18]", "&7Gun Level I");
            }
            if (kills == 15) {
                killer.getInventory().addItem(new ItemStack[] { new ItemMaker(Material.BOW).enchant(Enchantment.ARROW_KNOCKBACK, 2).enchant(Enchantment.DURABILITY, 3).create() });
                sendMessageToPlayers(killer.getDisplayName() + " &ehas gotten the &aBow &ekillstreak! &7[x15]", "&7Punch Level II");
            }
            if (kills == 12) {
                killer.getInventory().addItem(new ItemStack[] { new ItemMaker(Material.GOLDEN_APPLE, 8).data((short)1).create() });
                sendMessageToPlayers(killer.getDisplayName() + " &ehas gotten the &6Golden Apple &ekillstreak! &7[x12]", "&7XVIII Gapples");
            }
            if (kills == 9) {
                sendMessageToPlayers(killer.getDisplayName() + " &ehas gotten the &cStrenght &ekillstreak! &7[x9]", "&7Level II");
            }
            if (kills == 6) {
                killer.getInventory().addItem(new ItemStack[] { new ItemMaker(Material.ENDER_PEARL, 4).create() });
                sendMessageToPlayers(killer.getDisplayName() + " &ehas gotten the &aEnderpearls &ekillstreak! &7[x6]", "&7IV Enderpearls");
            }
            if (kills == 3) {
                killer.getInventory().addItem(new ItemStack[] { new ItemMaker(Material.EMERALD, 3).create() });
                sendMessageToPlayers(killer.getDisplayName() + " &ehas gotten the &aEmeralds &ekillstreak! &7[x3]", "&7III Emeralds");
            }
        }
        else {
            KillStreakHandler.count.put(name, 1);
            killer.playSound(killer.getLocation(), Sound.ANVIL_USE, 1.0f, 1.0f);
        }
    }
    
	private static void sendMessageToPlayers(final String message, final String tooltip) {
        for (final Player online : Bukkit.getServer().getOnlinePlayers()) {
        	online.sendMessage(ColorText.translate(message));
        }
    }
    
    static {
        KillStreakHandler.count = new HashMap<String, Integer>();
    }
}
