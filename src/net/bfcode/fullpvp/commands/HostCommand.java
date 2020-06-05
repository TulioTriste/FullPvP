package net.bfcode.fullpvp.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;

import net.bfcode.fullpvp.FullPvP;
import net.bfcode.fullpvp.configuration.LocationFile;
import net.bfcode.fullpvp.configuration.MessagesFile;
import net.bfcode.fullpvp.listener.VanishListener;
import net.bfcode.fullpvp.utilities.ColorText;
import net.bfcode.fullpvp.utilities.ItemStackBuilder;

public class HostCommand implements CommandExecutor, Listener {

	private static String hostTitle;

	public HostCommand() {
        FullPvP.getInstance().getServer().getPluginManager().registerEvents(this, FullPvP.getInstance());
    }

    static {
    	hostTitle = ColorText.translate(FullPvP.getInstance().getConfig().getString("Host-Menu.Title"));
    }
    
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ColorText.translate("&cDebes ser un jugador para ejecutar este comando."));
            return true;
        }
        final Player player = (Player)sender;
        MessagesFile messages = MessagesFile.getConfig();
        if(!player.hasPermission("fullpvp.command.host")) {
        	player.sendMessage(ColorText.translate(messages.getString("No-Permission")));
        	return true;
        }
        if(StaffModeCommand.isMod(player) || VanishListener.isVanished(player)) {
        	player.sendMessage(ColorText.translate(messages.getString("Host-with-StaffMode")));
        	return true;
        }
        LocationFile location = LocationFile.getConfig();
        for (final String claim : location.getConfigurationSection("Claims").getKeys(false)) {
            final CuboidSelection selection = new CuboidSelection(Bukkit.getWorld(location.getString("Claims." + claim + ".world")), HostCommand.this.getLocation(claim, "cornerA"), HostCommand.this.getLocation(claim, "cornerB"));
            if(!selection.contains(player.getLocation())) {
        		player.sendMessage(ColorText.translate(messages.getString("Host-in-noZone")));
        		return true;
            }
        }
    	HostGUI(player);
        return true;
    }
    
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        if(event.getInventory().getTitle().equals(hostTitle)) {
        	if(event.getClickedInventory() == null || event.getInventory() != event.getClickedInventory()) {
        		return;
        	}
    		ConfigurationSection config = FullPvP.getInstance().getConfig().getConfigurationSection("Host-Menu.items");
        	for(int i = 1; i <= config.getKeys(false).size(); ++i) {
        		int clickslot = config.getInt("." + i + ".Slot");
        		if(event.getRawSlot() == clickslot && player.hasPermission(FullPvP.getInstance().getConfig().getString("Host-Menu.items." + i + ".Permission"))) {
        			player.performCommand(FullPvP.getInstance().getConfig().getString("Host-Menu.items." + i + ".Start-Command"));
        		}
        	}
            event.setCancelled(true);	
        }
    }
    
    public void HostGUI(Player player) {
    	Inventory inventory = Bukkit.createInventory(null, FullPvP.getInstance().getConfig().getInt("Host-Menu.Slots"), hostTitle);
		ConfigurationSection config = FullPvP.getInstance().getConfig().getConfigurationSection("Host-Menu.items");

    	for(int i = 1; i <= config.getKeys(false).size(); ++i) {
            inventory.setItem(config.getInt("." + i + ".Slot"), new ItemStackBuilder(Material.valueOf(config.getString("." + i + ".Material"))).setName(ColorText.translate(config.getString("." + i + ".Name"))).addLore((config.getStringList("." + i + ".Lore"))).build());
    	}

		player.openInventory(inventory);
    	
    }
    
    public Location getLocation(final String town, final String corner) {
        LocationFile location = LocationFile.getConfig();
        final LocationFile location1 = location;
        final World world = Bukkit.getWorld((String)LocationFile.getConfig().get("Claims." + town + ".world"));
        final double x = location1.getDouble("Claims." + town + "." + corner + ".x");
        final double y = location1.getDouble("Claims." + town + "." + corner + ".y");
        final double z = location1.getDouble("Claims." + town + "." + corner + ".z");
        return new Location(world, x, y, z);
    }
}
