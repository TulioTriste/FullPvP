package net.galanthus.fullpvp.destroythecore;

import java.util.Iterator;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;

import net.galanthus.fullpvp.FullPvP;
import net.galanthus.fullpvp.utilities.ColorText;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class DTCListener implements Listener
{
    DTCFile dtcFile;
    
    public DTCListener(final FullPvP plugin) {
        this.dtcFile = DTCFile.getConfig();
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
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
        final Player player = event.getPlayer();
        final Block block = event.getBlock();
        final Iterator<String> iterator = this.dtcFile.getConfigurationSection("CurrentDTC").getKeys(false).iterator();
        if (iterator.hasNext()) {
            final String dtc = iterator.next();
            if (block.getLocation().getBlockX() == this.dtcFile.getInt("DTC." + dtc + ".X") && block.getLocation().getBlockY() == this.dtcFile.getInt("DTC." + dtc + ".Y") && block.getLocation().getBlockZ() == this.dtcFile.getInt("DTC." + dtc + ".Z") && block.getType().equals((Object)Material.OBSIDIAN)) {
                event.setCancelled(true);
                DTCHandler.decrementPoints(dtc);
                Bukkit.broadcastMessage(ColorText.translate(FullPvP.getPlugin().getConfig().getString("DestroyTheCore.Destroyed").replace("%playername%", player.getName()).replace("%destroythecore%", dtc)).replace("%points%", "" + DTCHandler.getDTCPoints(dtc)));
                if (DTCHandler.getDTCPoints(dtc) == 0) {
                    DTCHandler.setDTCEvent(dtc, false);
                    Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getConsoleSender(), "cr givekey " + player.getName() + " Event 2");
                    Bukkit.broadcastMessage(ColorText.translate(FullPvP.getPlugin().getConfig().getString("DestroyTheCore.DestroyedCore").replace("%playername%", player.getName()).replace("%destroythecore%", dtc)));
                }
            }
            return;
        }
        if (player.getGameMode().equals((Object)GameMode.CREATIVE) && player.isOp()) {
            return;
        }
        event.setCancelled(true);
    }
}
