package net.panda.fullpvp.destroythecore;

import java.util.Iterator;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;

import net.panda.fullpvp.FullPvP;
import net.panda.fullpvp.configuration.MessagesFile;
import net.panda.fullpvp.utilities.ColorText;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class DTCListener implements Listener {
	
    DTCFile dtcFile;
    
    public DTCListener(final FullPvP plugin) {
        this.dtcFile = DTCFile.getConfig();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onPlayerSelect(final BlockBreakEvent event) {
        final Player player = event.getPlayer();
        final Block block = event.getBlock();
        final int x = event.getBlock().getX();
        final int y = event.getBlock().getY();
        final int z = event.getBlock().getZ();
        if (DTCHandler.isDTCWand(player)) {
            if (block.getType() == Material.OBSIDIAN) {
                event.setCancelled(true);
                DTCHandler.setCurrentSelection(x, y, z);
                player.sendMessage(ColorText.translate("&eYou have select the core &a(&c" + x + "&a, &c" + y + "&a, &c" + z + "&a)"));
            }
            else {
                event.setCancelled(true);
                player.sendMessage(ColorText.translate("&cYou can select only Obsidian Block."));
            }
        }
    }
    
    @EventHandler
    public void onBlockBreak(final BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

	    if(!this.dtcFile.getConfigurationSection("DTC").getKeys(false).isEmpty()) {
            Iterator<String> iterator = this.dtcFile.getConfigurationSection("CurrentDTC").getKeys(false).iterator();
            if (iterator.hasNext()) {
                final String dtc = iterator.next();
                if (block.getLocation().getBlockX() == this.dtcFile.getInt("DTC." + dtc + ".X") && 
                	block.getLocation().getBlockY() == this.dtcFile.getInt("DTC." + dtc + ".Y") && 
                	block.getLocation().getBlockZ() == this.dtcFile.getInt("DTC." + dtc + ".Z") && 
                	block.getType().equals(Material.OBSIDIAN)) {
                    event.setCancelled(true);
                    MessagesFile messages = MessagesFile.getConfig();
                    DTCHandler.decrementPoints(dtc);
                    Bukkit.broadcastMessage(ColorText.translate(messages.getString("Destroy-The-Core.Destroyed")
                    		.replace("{player}", player.getName())
                    		.replace("{dtc}", dtc))
                    		.replace("{points}", String.valueOf(DTCHandler.getDTCPoints(dtc))));
                    if (DTCHandler.getDTCPoints(dtc) == 0) {
                        DTCHandler.setDTCEvent(dtc, false);
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), FullPvP.getInstance().getConfig().getString("DTC-Reward")
                        		.replace("{player}", player.getName()));
                        Bukkit.broadcastMessage(ColorText.translate(messages.getString("Destroy-The-Core.Destroyed-Core")
                        		.replace("{player}", player.getName())
                        		.replace("{dtc}", dtc)));
                    }
                }
                return;
            }
	    }
        if (player.getGameMode().equals(GameMode.CREATIVE) && player.isOp()) {
            return;
        }
    }
}
