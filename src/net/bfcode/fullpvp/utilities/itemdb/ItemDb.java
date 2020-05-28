package net.bfcode.fullpvp.utilities.itemdb;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ItemDb
{
    void reloadItemDatabase();
    
    ItemStack getPotion(final String p0);
    
    ItemStack getPotion(final String p0, final int p1);
    
    ItemStack getItem(final String p0);
    
    ItemStack getItem(final String p0, final int p1);
    
    String getName(final ItemStack p0);
    
    @Deprecated
    String getPrimaryName(final ItemStack p0);
    
    String getNames(final ItemStack p0);
    
    List<ItemStack> getMatching(final Player p0, final String[] p1);
}
