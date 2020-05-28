package net.bfcode.fullpvp.destroythecore;

import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.bfcode.fullpvp.utilities.ColorText;
import net.bfcode.fullpvp.utilities.ItemMaker;

import java.util.Set;

public class DTCHandler
{
    public static DTCFile dtcFile;
    
    public static void createDTC(final String DTC, final int points) {
        DTCHandler.dtcFile.set("DTC." + DTC + ".X", (Object)DTCHandler.dtcFile.getInt("CurrentSelect.X"));
        DTCHandler.dtcFile.set("DTC." + DTC + ".Y", (Object)DTCHandler.dtcFile.getInt("CurrentSelect.Y"));
        DTCHandler.dtcFile.set("DTC." + DTC + ".Z", (Object)DTCHandler.dtcFile.getInt("CurrentSelect.Z"));
        DTCHandler.dtcFile.set("DTC." + DTC + ".Active", (Object)false);
        DTCHandler.dtcFile.set("DTC." + DTC + ".Points", (Object)points);
        DTCHandler.dtcFile.set("DTC." + DTC + ".PointsLeft", (Object)0);
        DTCHandler.dtcFile.set("CurrentSelect.X", (Object)null);
        DTCHandler.dtcFile.set("CurrentSelect.Y", (Object)null);
        DTCHandler.dtcFile.set("CurrentSelect.Z", (Object)null);
        saveFile();
    }
    
    public static void deleteDTC(final String DTC) {
        DTCHandler.dtcFile.set("DTC." + DTC, (Object)null);
        saveFile();
    }
    
    public static void decrementPoints(final String DTC) {
        DTCHandler.dtcFile.set("DTC." + DTC + ".PointsLeft", (Object)(DTCHandler.dtcFile.getInt("DTC." + DTC + ".PointsLeft") - 1));
        saveFile();
    }
    
    public static int getDTCPoints(final String DTC) {
        return DTCHandler.dtcFile.getInt("DTC." + DTC + ".PointsLeft");
    }
    
    public static Set<String> getDTCActiveList() {
        return (Set<String>)DTCHandler.dtcFile.getConfigurationSection("CurrentDTC").getKeys(false);
    }
    
    public static int getDTCListSize() {
        final Set<String> list = (Set<String>)DTCHandler.dtcFile.getConfigurationSection("CurrentDTC").getKeys(false);
        return list.size();
    }
    
    public static int getDTCLocX(final String DTC) {
        return DTCHandler.dtcFile.getInt("CurrentDTC." + DTC + ".X");
    }
    
    public static int getDTCLocY(final String DTC) {
        return DTCHandler.dtcFile.getInt("CurrentDTC." + DTC + ".Y");
    }
    
    public static int getDTCLocZ(final String DTC) {
        return DTCHandler.dtcFile.getInt("CurrentDTC." + DTC + ".Z");
    }
    
    public static boolean isSet() {
        return DTCHandler.dtcFile.isSet("CurrentDTC");
    }
    
    public static boolean isStarted(final String DTC) {
        return DTCHandler.dtcFile.getBoolean("DTC." + DTC + ".Active");
    }
    
    public static void setDTCEvent(final String DTC, final boolean b) {
        if (b) {
            DTCHandler.dtcFile.set("DTC." + DTC + ".Active", (Object)true);
            DTCHandler.dtcFile.set("DTC." + DTC + ".PointsLeft", (Object)DTCHandler.dtcFile.getInt("DTC." + DTC + ".Points"));
            DTCHandler.dtcFile.set("CurrentDTC." + DTC + ".X", (Object)DTCHandler.dtcFile.getInt("DTC." + DTC + ".X"));
            DTCHandler.dtcFile.set("CurrentDTC." + DTC + ".Y", (Object)DTCHandler.dtcFile.getInt("DTC." + DTC + ".Y"));
            DTCHandler.dtcFile.set("CurrentDTC." + DTC + ".Z", (Object)DTCHandler.dtcFile.getInt("DTC." + DTC + ".Z"));
            saveFile();
        }
        else {
            DTCHandler.dtcFile.set("DTC." + DTC + ".Active", (Object)false);
            DTCHandler.dtcFile.set("DTC." + DTC + ".PointsLeft", (Object)0);
            DTCHandler.dtcFile.set("CurrentDTC." + DTC, (Object)null);
            saveFile();
        }
    }
    
    public static Set<String> getDTCList() {
        return (Set<String>)DTCHandler.dtcFile.getConfigurationSection("DTC").getKeys(false);
    }
    
    public static boolean isAlreadyCreated(final String DTC) {
        return DTCHandler.dtcFile.contains("DTC." + DTC);
    }
    
    public static void setCurrentSelection(final int x, final int y, final int z) {
        DTCHandler.dtcFile.set("CurrentSelect.X", (Object)x);
        DTCHandler.dtcFile.set("CurrentSelect.Y", (Object)y);
        DTCHandler.dtcFile.set("CurrentSelect.Z", (Object)z);
        saveFile();
    }
    
    public static boolean isSetSelection() {
        return DTCHandler.dtcFile.isSet("CurrentSelect.X") && DTCHandler.dtcFile.isSet("CurrentSelect.Y") && DTCHandler.dtcFile.isSet("CurrentSelect.Z");
    }
    
    public static ItemStack getDTCWand() {
        return new ItemMaker(Material.GOLD_SPADE).displayName("&aDTC &7(Wand)").lore("&7Right-Click at obsidian block to set the block of the event.").create();
    }
    
    public static boolean isDTCWand(final Player player) {
        return player.getItemInHand().getType() == Material.GOLD_SPADE && player.getItemInHand().getItemMeta().hasLore() && player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ColorText.translate("&aDTC &7(Wand)"));
    }
    
    private static void saveFile() {
        DTCHandler.dtcFile.save();
        DTCHandler.dtcFile.reload();
    }
    
    static {
        DTCHandler.dtcFile = DTCFile.getConfig();
    }
}
