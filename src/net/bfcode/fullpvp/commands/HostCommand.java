package net.bfcode.fullpvp.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;

import net.bfcode.fullpvp.configuration.LocationFile;
import net.bfcode.fullpvp.listener.VanishListener;
import net.bfcode.fullpvp.utilities.ColorText;
import net.bfcode.fullpvp.utilities.InventoryMaker;
import net.bfcode.fullpvp.utilities.ItemMaker;
import net.bfcode.fullpvp.utilities.Messager;

public class HostCommand implements CommandExecutor, Listener {
	
	private static String lastLineSumo;
    private static String lastLineFFA;

	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ColorText.translate("&cDebes ser un jugador para ejecutar este comando."));
            return true;
        }
        final Player player = (Player)sender;
        if(StaffModeCommand.isMod(player) || VanishListener.isVanished(player)) {
        	Messager.player(player, "&cNo puedes hostear eventos en staff-mode.");
        }
        LocationFile location = LocationFile.getConfig();
        for (final String claim : location.getConfigurationSection("Claims").getKeys(false)) {
            final CuboidSelection selection = new CuboidSelection(Bukkit.getWorld(location.getString("Claims." + claim + ".world")), HostCommand.this.getLocation(claim, "cornerA"), HostCommand.this.getLocation(claim, "cornerB"));
            final boolean isPvP = location.getBoolean("Claims." + claim + ".pvp");
            if(selection.contains(player.getLocation()) && !StaffModeCommand.isMod(player)) {
            	if(!isPvP) {
            		player.sendMessage(ColorText.translate("&cNo puedes hostear Eventos en Zonas con PvP"));
            		return true;
            	}
            	return true;
            }
            return true;
        }
    	HostGUI(player);
        return true;
    }
    
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        if(event.getInventory().getTitle().equals("Host's")) {
        	if(event.getClickedInventory() == null || event.getInventory() != event.getClickedInventory()) {
        		return;
        	}
        	if(event.getRawSlot() == 11 && player.hasPermission("tournament.sumo")) {
        		player.performCommand("tournament create 24 sumo");
        	}
        	else if(event.getRawSlot() == 15 && player.hasPermission("tournament.ffa")) {
        		player.performCommand("tournament create 24 ffa");
        	}
            event.setCancelled(true);	
        }
    }
    
    public void HostGUI(Player player) {
	    if(player.hasPermission("tournament.sumo")) {
	   		 lastLineSumo = ColorText.translate("&aPuedes hostear este evento!");
	   	} else {
	   		 lastLineSumo = ColorText.translate("&cNo tienes permisos para hostear este evento!");
	   	}
		if(player.hasPermission("tournament.ffa")) {
			 lastLineFFA = ColorText.translate("&aPuedes hostear este evento!");
		} else {
			 lastLineFFA = ColorText.translate("&cNo tienes permisos para hostear este evento!");
		}
		ItemStack NONE = new ItemMaker(Material.STAINED_GLASS_PANE).data((short)7).displayName(" ").create();
		player.openInventory(new InventoryMaker(null, 3, "Host's")
				.setItem(0, NONE)
				.setItem(1, NONE)
				.setItem(2, NONE)
				.setItem(3, NONE)
				.setItem(4, NONE)
				.setItem(5, NONE)
				.setItem(6, NONE)
				.setItem(7, NONE)
				.setItem(8, NONE)
				.setItem(9, NONE)
        		.setItem(10, NONE)
        		.setItem(11, new ItemMaker(Material.RAW_FISH).displayName("&2&lSumo").lore(
        				"&7&m-----------------------------------",
                		"&eDescripción sobre este evento&7:",
                		" &8* &7No debes ser tirado de la plataforma!",
                		" &8* &7El ultimo que queda en pie gana!",
                		"",
                		"&ePremios del evento&7:",
                		" &8+&f2 &7Event Key",
                		"",
                		"&eClick para hostear el evento de &2Sumo",
                		"&7&m-----------------------------------",
                		lastLineSumo).create())
        		.setItem(12, NONE)
        		.setItem(13, NONE)
        		.setItem(14, NONE)
        		.setItem(15, new ItemMaker(Material.DIAMOND_SWORD).displayName("&2&lFFA").lore(
        				"&7&m-----------------------------------",
                		"&eDescripción sobre este evento&7:",
                		" &8* &7Tienes que combatir contra los demas enemigos!",
                		" &8* &7El ultimo que queda en pie gana!",
                		"",
                		"&ePremios del evento&7:",
                		" &8+&f2 &7Event Key",
                		"",
                		"&eClick para hostear el evento de &2FFA",
                		"&7&m-----------------------------------",
                		lastLineFFA).create())
        		.setItem(16, NONE)
        		.setItem(17, NONE)
        		.setItem(18, NONE)
        		.setItem(19, NONE)
        		.setItem(20, NONE)
        		.setItem(21, NONE)
        		.setItem(22, NONE)
        		.setItem(23, NONE)
        		.setItem(24, NONE)
        		.setItem(25, NONE)
        		.setItem(26, NONE)
        		.create());
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
