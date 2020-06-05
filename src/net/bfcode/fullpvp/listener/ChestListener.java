package net.bfcode.fullpvp.listener;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.Location;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.Sound;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;

import net.bfcode.fullpvp.FullPvP;
import net.bfcode.fullpvp.configuration.LocationFile;
import net.bfcode.fullpvp.utilities.ColorText;
import net.bfcode.fullpvp.utilities.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import java.util.ArrayList;
import java.util.List;

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
                    
                    Inventory inventory = Bukkit.createInventory(null, 45, ColorText.translate("&e" + player.getName() + " Chest"));

            		List<String> test = new ArrayList<>(FullPvP.getInstance().getConfig().getConfigurationSection("Spawn-Chest-Refill").getKeys(false));
            		
                    for(int i = 0; i < test.size(); ++i) {
                    	String name = test.get(i);
                    	int n = Integer.parseInt(name);
                    	FileConfiguration config = FullPvP.getInstance().getConfig();
                    	inventory.setItem(n -1, new ItemBuilder(Material.valueOf(config.getString("Spawn-Chest-Refill." + n + ".Material").toUpperCase()), config.getInt("Spawn-Chest-Refill." + n + ".Amount")).data((short) config.getInt("Spawn-Chest-Refill." + n + ".Value")).build());
                    }
                    
                    player.openInventory(inventory);

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
    
    
    public Location getLocation(final String town, final String corner) {
        final World world = Bukkit.getWorld((String)LocationFile.getConfig().get("Claims." + town + ".world"));
        final double x = this.location.getDouble("Claims." + town + "." + corner + ".x");
        final double y = this.location.getDouble("Claims." + town + "." + corner + ".y");
        final double z = this.location.getDouble("Claims." + town + "." + corner + ".z");
        return new Location(world, x, y, z);
    }
}
