package net.bfcode.fullpvp.utilities;

import org.bukkit.enchantments.Enchantment;
import java.util.List;
import java.util.ArrayList;
import com.google.common.base.Preconditions;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.ItemStack;

public class ItemMaker {

    private ItemStack stack;
    private ItemMeta meta;
    
    public ItemMaker(final Material material) {
        this(material, 1);
    }
    
    public ItemMaker(final Material material, final int amount) {
        this(material, amount, (byte)0);
    }
    
    public ItemMaker(final ItemStack stack) {
        Preconditions.checkNotNull(stack, "ItemStack not found.");
        this.stack = stack;
    }
    
    public ItemMaker(final Material material, final int amount, final byte data) {
        Preconditions.checkNotNull(material, "Material not found.");
        Preconditions.checkArgument(amount > 0, "Amount must be positive");
        this.stack = new ItemStack(material, amount, data);
    }
    
    public ItemMaker displayName(final String name) {
        if (this.meta == null) {
            this.meta = this.stack.getItemMeta();
        }
        this.meta.setDisplayName(ColorText.translate(name));
        return this;
    }
    
    public ItemMaker lore(final String... strings) {
        final List<String> loreArray = new ArrayList<>();
        for (final String loreBit : strings) {
            loreArray.add(loreBit.replace("&", "§"));
        }
        this.meta.setLore(loreArray);
        return this;
    }
    
	public ItemMaker lore(final List<String> strings) {
        final List<String> loreArray = new ArrayList<>();
        for (final String loreBit : strings) {
            loreArray.add(ColorText.translate(loreBit));
        }
        this.meta.setLore(loreArray);
        return this;
    }
    
    public ItemMaker enchant(final Enchantment enchantment, final int level) {
        return this.setEnchant(enchantment, level, true);
    }
    
    public ItemMaker setEnchant(final Enchantment enchantment, final int level, final boolean unsafe) {
        if (unsafe && level >= enchantment.getMaxLevel()) {
            this.stack.addUnsafeEnchantment(enchantment, level);
        }
        else {
            this.stack.addEnchantment(enchantment, level);
        }
        return this;
    }
    
    public ItemMaker data(final short data) {
        this.stack.setDurability(data);
        return this;
    }
    
    public ItemStack create() {
        if (this.meta != null) {
            this.stack.setItemMeta(this.meta);
        }
        return this.stack;
    }
}
