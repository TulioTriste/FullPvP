package net.bfcode.fullpvp.commands.tournaments.arguments;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.bfcode.fullpvp.commands.StaffModeCommand;
import net.bfcode.fullpvp.listener.VanishListener;
import net.bfcode.fullpvp.tournaments.Tournament;
import net.bfcode.fullpvp.utilities.ColorText;
import net.bfcode.fullpvp.utilities.CommandArgument;
import net.bfcode.fullpvp.utilities.InventoryMaker;
import net.bfcode.fullpvp.utilities.ItemMaker;
import net.bfcode.fullpvp.utilities.Messager;

public class TournamentHostArgument extends CommandArgument implements Listener {	
	
	@SuppressWarnings("unused")
	private Tournament tournament;
    private static String lastLineSumo;
    private static String lastLineFFA;
    
    public TournamentHostArgument() {
        super("host", "Host an events/tournament");
        this.permission = "tournament.default";
    }
    
    public String getUsage(final String label) {
        return '/' + label + ' ' + this.getName();
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ColorText.translate("&cYou must be player to execute this command."));
            return true;
        }
        Player player = (Player)sender;
        if(StaffModeCommand.isMod(player) || VanishListener.isVanished(player)) {
        	Messager.player(player, "&cYou cannot host with mod mode or vanish!");
        } 
        else {
        	Page1(player);
            return true;
        }
        return true;
    }
    
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        if(event.getInventory().getTitle().contains("Host's")) {
        	if(event.getClickedInventory() == null || event.getInventory() != event.getClickedInventory()) {
        		return;
        	}
        	if(event.getRawSlot() == 0 && player.hasPermission("tournament.sumo")) {
        		player.performCommand("tournament create 16 sumo");
        		player.closeInventory();
        		return;
        	}
        	else if(event.getRawSlot() == 1 && player.hasPermission("tournament.ffa")) {
        		player.performCommand("tournament create 24 ffa");
        		player.closeInventory();
        		return;
        	}
            event.setCancelled(true);	
        }
    }
    
    public void Page1(Player player) {
	    if(player.hasPermission("tournament.sumo")) {
	   		 lastLineSumo = ColorText.translate("&aYou can select this event");
	   	} else {
	   		 lastLineSumo = ColorText.translate("&cYou do not have permissions to select this event");
	   	}
		if(player.hasPermission("tournament.ffa")) {
			 lastLineFFA = ColorText.translate("&aYou can select this event");
		} else {
			 lastLineFFA = ColorText.translate("&cYou do not have permissions to select this event");
		}
		ItemStack NONE = new ItemMaker(Material.STAINED_GLASS_PANE).data((short)7).displayName("&f ").create();
		player.openInventory(new InventoryMaker(null, 1, "Host's")
        		.setItem(0, new ItemMaker(Material.RAW_FISH).displayName("&bSumo").lore(
        				"&7&m-----------------------------------",
                		"&6Description about this Tournament&7:",
                		" &8* &7You mustn't be knocked from the platform.",
                		"  &7The last man standing wins the tournament.",
                		"",
                		"&6Tournament Rewards&7:",
                		" &8+&f2 &7Event Keys",
                		"",
                		"&aClick here to start this tournament &bSumo",
                		"&7&m-----------------------------------",
                		lastLineSumo).create())
        		.setItem(1, new ItemMaker(Material.DIAMOND_SWORD).displayName("&dFFA").lore(
        				"&7&m-----------------------------------",
                		"&6Description about this Tournament&7:",
                		" &8* &7The entire event will be spawned into a platform!",
                		"  &7Last player to be standing wins the match!",
                		"",
                		"&6Tournament Rewards&7:",
                		" &8+&f2 &7Event Keys",
                		"",
                		"&aClick here to start this tournament &dFFA",
                		"&7&m-----------------------------------",
                		lastLineFFA).create())
        		.setItem(2, NONE)
        		.setItem(3, NONE)
        		.setItem(4, NONE)
        		.setItem(5, NONE)
        		.setItem(6, NONE)
        		.setItem(7, NONE)
        		.setItem(8, NONE)
        		.create());
    }
}