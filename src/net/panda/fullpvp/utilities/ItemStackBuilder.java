package net.panda.fullpvp.utilities;

import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.*;
import java.util.*;
import org.bukkit.enchantments.*;

public class ItemStackBuilder
{
    private ItemStack itemStack;
    private ItemMeta itemMeta;
    
    public ItemStackBuilder(final Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = this.itemStack.getItemMeta();
    }
    
    public ItemStackBuilder amount(final int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }
    
    public ItemStackBuilder setName(final String displayName) {
        this.itemMeta.setDisplayName(displayName);
        return this;
    }
    
    public ItemStackBuilder color(final int hex) {
        if (this.itemMeta instanceof LeatherArmorMeta) {
            ((LeatherArmorMeta)this.itemMeta).setColor(Color.fromRGB(hex));
        }
        return this;
    }
    
    public ItemStackBuilder addLore(final String... strings) {
        final List<String> loreArray = new ArrayList<String>();
        for (final String loreBit : strings) {
            loreArray.add(ChatColor.WHITE + loreBit);
        }
        this.itemMeta.setLore(loreArray);
        return this;
    }
    
    public ItemStackBuilder addLore(final List<String> strings) {
        final List<String> loreArray = new ArrayList<String>();
        for (final String loreBit : strings) {
            loreArray.add(ChatColor.translateAlternateColorCodes('&', loreBit));
        }
        this.itemMeta.setLore(loreArray);
        return this;
    }
    
    public ItemStackBuilder enchant(final Enchantment enchanement, final int level, final boolean ignoreLevelRestriction) {
        this.itemMeta.addEnchant(enchanement, level, ignoreLevelRestriction);
        return this;
    }
    
    public ItemStackBuilder durability(final int durability) {
        this.itemStack.setDurability((short)durability);
        return this;
    }
    
    public ItemStack build() {
        final ItemStack clonedStack = this.itemStack.clone();
        clonedStack.setItemMeta(this.itemMeta.clone());
        return clonedStack;
    }
}
