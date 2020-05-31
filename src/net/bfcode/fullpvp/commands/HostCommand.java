package net.bfcode.fullpvp.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;

import net.bfcode.fullpvp.FullPvP;
import net.bfcode.fullpvp.configuration.LocationFile;
import net.bfcode.fullpvp.listener.VanishListener;
import net.bfcode.fullpvp.utilities.ColorText;
import net.bfcode.fullpvp.utilities.ItemMaker;
import net.bfcode.fullpvp.utilities.ItemStackBuilder;
import net.bfcode.fullpvp.utilities.Messager;

public class HostCommand implements CommandExecutor, Listener {
	
	public Inventory inventory;
	private static String hostTitle;

    static {
    	hostTitle = ColorText.translate(FullPvP.getPlugin().getConfig().getString("Host-Menu.Title"));
    }
    
    public HostCommand() {
        this.inventory = Bukkit.createInventory(null, FullPvP.getPlugin().getConfig().getInt("Host-Menu.Slots"), hostTitle);
    }
    
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ColorText.translate("&cDebes ser un jugador para ejecutar este comando."));
            return true;
        }
        final Player player = (Player)sender;
        if(StaffModeCommand.isMod(player) || VanishListener.isVanished(player)) {
        	Messager.player(player, "&cNo puedes hostear eventos en staff-mode.");
        	return true;
        }
        LocationFile location = LocationFile.getConfig();
        for (final String claim : location.getConfigurationSection("Claims").getKeys(false)) {
            final CuboidSelection selection = new CuboidSelection(Bukkit.getWorld(location.getString("Claims." + claim + ".world")), HostCommand.this.getLocation(claim, "cornerA"), HostCommand.this.getLocation(claim, "cornerB"));
            final boolean isPvP = location.getBoolean("Claims." + claim + ".pvp");
            if(selection.contains(player.getLocation()) && !isPvP && !StaffModeCommand.isMod(player)) {
        		player.sendMessage(ColorText.translate("&cNo puedes hostear Eventos en Zonas con PvP"));
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
        	for(int i = 1; i <= FullPvP.getPlugin().getConfig().getInt("Host-Menu.Slots") + 1; ++i) {
        		if(event.getRawSlot() == i && player.hasPermission(FullPvP.getPlugin().getConfig().getString("Host-Menu.items." + i + ".Permission"))) {
        			player.performCommand("/" + FullPvP.getPlugin().getConfig().getString("Host-Menu.items." + i + ".Start-Command"));
        		}
        	}
            event.setCancelled(true);	
        }
    }
    
    public void HostGUI(Player player) {
		FileConfiguration config = FullPvP.getPlugin().getConfig();

    	for(int i = 1; i <= config.getConfigurationSection("Host-Menu.items").getKeys(true).size() + 1; ++i) {
            HostCommand.this.inventory.setItem(config.getInt("Host-Menu.items." + i + ".slot"), new ItemStackBuilder(Material.COBBLESTONE).setName(config.getString("Host-Menu.items." + i + ".Name")).addLore((config.getStringList("Host-Menu.items." + i + ".Lore"))).build());
    	}

		player.openInventory(HostCommand.this.inventory);
    	
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
