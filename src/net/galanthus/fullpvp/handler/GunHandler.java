package net.galanthus.fullpvp.handler;

import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.galanthus.fullpvp.utilities.ColorText;
import net.galanthus.fullpvp.utilities.ItemMaker;

public class GunHandler
{
    public static ItemStack getHoeGun() {
        return new ItemMaker(Material.GOLD_HOE).data((short)32).displayName("&9Hoe Gun &7[Level I]").create();
    }
    
    public static void getHoeGun(final Player player, final int amount) {
        player.getInventory().addItem(new ItemStack[] { new ItemMaker(Material.GOLD_HOE, amount).data((short)32).displayName("&9Hoe Gun &7[Level I]").create() });
    }
    
    public static boolean isHoeGun(final Player player) {
        return player.getItemInHand().getType() == Material.GOLD_HOE && player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ColorText.translate("&9Hoe Gun &7[Level I]"));
    }
}
