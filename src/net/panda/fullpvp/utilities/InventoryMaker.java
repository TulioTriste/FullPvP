package net.panda.fullpvp.utilities;

import java.io.IOException;
import java.io.InputStream;
import org.bukkit.util.io.BukkitObjectInputStream;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
import java.io.OutputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.Bukkit;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.Inventory;

public class InventoryMaker {
    private Inventory Inventory;
    
    public InventoryMaker(final InventoryHolder owner, final int rows) {
        this.Inventory = Bukkit.createInventory(owner, rows * 9);
    }
    
    public InventoryMaker(final InventoryHolder owner, final InventoryType type) {
        this.Inventory = Bukkit.createInventory(owner, type);
    }
    
    public InventoryMaker(final InventoryHolder owner, final int rows, final String title) {
        this.Inventory = Bukkit.createInventory(owner, rows * 9, ColorText.translate(title));
    }
    
    public InventoryMaker(final InventoryHolder owner, final InventoryType type, final String title) {
        this.Inventory = Bukkit.createInventory(owner, type, ColorText.translate(title));
    }
    
    public InventoryMaker setItem(final int slot, final ItemStack stack) {
        this.Inventory.setItem(slot, stack);
        return this;
    }
    
    public InventoryMaker setItems(final Map<Integer, ItemStack> items) {
        int is = 0;
        for (final ItemStack is2 : items.values()) {
            if (is2 != null) {
                this.Inventory.setItem(is, is2);
            }
            ++is;
        }
        return this;
    }
    
    public InventoryMaker addItem(final ItemStack stack) {
        this.Inventory.addItem(new ItemStack[] { stack });
        return this;
    }
    
    public String toStack() {
        try {
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            final BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream((OutputStream)outputStream);
            dataOutput.writeInt(this.Inventory.getSize());
            for (int i = 0; i < this.Inventory.getSize(); ++i) {
                dataOutput.writeObject((Object)this.Inventory.getItem(i));
            }
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        }
        catch (Exception e) {
            throw new IllegalStateException("Error: Unable to save item stacks!", e);
        }
    }
    
    @SuppressWarnings("null")
	public Map<Integer, ItemStack> fromStack(final String f) {
        final Map<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();
        try {
            final ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(f));
            final BukkitObjectInputStream dataInput = new BukkitObjectInputStream((InputStream)inputStream);
            for (int size = dataInput.readInt(), i = 0; i < size; ++i) {
                items.put(i, (ItemStack)dataInput.readObject());
            }
            dataInput.close();
        }
        catch (IOException | ClassNotFoundException ex3) {
            final Exception ex2 = null;
            final Exception ex = ex2;
            ex.printStackTrace();
        }
        return items;
    }
    
    public Inventory create() {
        return this.Inventory;
    }
}
