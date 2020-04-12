package net.galanthus.fullpvp.listener;

import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.Sound;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;

import net.galanthus.fullpvp.FullPvP;
import net.galanthus.fullpvp.configuration.LocationFile;
import net.galanthus.fullpvp.utilities.ColorText;
import net.galanthus.fullpvp.utilities.InventoryMaker;
import net.galanthus.fullpvp.utilities.ItemMaker;

import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;


import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class ChestListener implements Listener {
	
    private LocationFile location;
    
    public ChestListener(final FullPvP plugin) {
    	this.location = LocationFile.getConfig();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.CHEST) {
            for (final String claim : this.location.getConfigurationSection("Claims").getKeys(false)) {
                final CuboidSelection selection = new CuboidSelection(Bukkit.getWorld(this.location.getString("Claims." + claim + ".world")), this.getLocation(claim, "cornerA"), this.getLocation(claim, "cornerB"));
                final boolean isPvP = this.location.getBoolean("Claims." + claim + ".pvp");
                if (!isPvP && selection.contains(event.getClickedBlock().getLocation())) {
                    event.setCancelled(true);
                    player.openInventory(new InventoryMaker(null, 5, "&e" + player.getName() + " Chest")
                    		.setItem(4, new ItemMaker(Material.LEATHER_HELMET).create())
                    		.setItem(13, new ItemMaker(Material.CHAINMAIL_CHESTPLATE).create())
                    		.setItem(22, new ItemMaker(Material.LEATHER_LEGGINGS).create())
                    		.setItem(31, new ItemMaker(Material.GOLD_BOOTS).create())
                    		.setItem(20, new ItemMaker(Material.STONE_SWORD).create())
                    		.setItem(30, new ItemMaker(Material.SNOW_BALL, 8).create())
                    		.setItem(24, new ItemMaker(Material.COOKED_BEEF, 8).create())
                    		.setItem(32, new ItemMaker(Material.SNOW_BALL, 8).create())
                    		.setItem(36, new ItemMaker(Material.ANVIL).displayName("&cUpgrade Chest").lore("&7Click here to upgrade your chest.").create())
                    		.setItem(44, new ItemMaker(Material.DIAMOND_PICKAXE).create())
                    		.create());
                    player.playSound(player.getLocation(), Sound.CHEST_OPEN, 1.0f, 1.0f);
                }
            }
        }
    }
    
    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent event) {
        final Player player = (Player)event.getPlayer();
        if (event.getInventory().getName().equals(ColorText.translate("&e" + player.getName() + " Chest"))) {
            player.playSound(player.getLocation(), Sound.CHEST_CLOSE, 1.0f, 1.0f);
        }
    }
    
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        final Player player = (Player)event.getWhoClicked();
        final int slot = event.getSlot();
        if (event.getInventory().getName().equals(ColorText.translate("&e" + player.getName() + " Chest"))) {
            if(slot == 36) {
            	event.setCancelled(true);
            	player.sendMessage(ColorText.translate("&cThis feature coming soon."));
            	return;
            }
        }
    }
    
    
    public Location getLocation(final String town, final String corner) {
        final World world = Bukkit.getWorld((String)LocationFile.getConfig().get("Claims." + town + ".world"));
        final double x = this.location.getDouble("Claims." + town + "." + corner + ".x");
        final double y = this.location.getDouble("Claims." + town + "." + corner + ".y");
        final double z = this.location.getDouble("Claims." + town + "." + corner + ".z");
        return new Location(world, x, y, z);
    }
}
