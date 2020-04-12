package net.galanthus.fullpvp.koth;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;

import net.galanthus.fullpvp.FullPvP;
import net.galanthus.fullpvp.configuration.LocationFile;
import net.galanthus.fullpvp.utilities.ColorText;

public class KothListener implements Listener {
	
	LocationFile location;
    
    public KothListener(final FullPvP plugin) {
        this.location = LocationFile.getConfig();
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onKothEntering(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final Location from = event.getFrom();
        final Location to = event.getTo();
        if (from.getBlockX() == to.getBlockX() && from.getBlockY() == to.getBlockY() && from.getBlockZ() == to.getBlockZ()) {
            return;
        }
        for (final String koth : this.location.getConfigurationSection("KOTHS").getKeys(false)) {
            final CuboidSelection selection = new CuboidSelection(Bukkit.getWorld(this.location.getString("KOTHS." + koth + ".world")), this.getLocation(koth, "cornerA"), this.getLocation(koth, "cornerB"));
            if (selection.contains(event.getTo()) && !selection.contains(event.getFrom())) {
                player.sendMessage(ColorText.translate("&eEntering: &9&l" + koth + " KOTH"));
            }
            else {
                if (!selection.contains(event.getFrom()) || selection.contains(event.getTo())) {
                    continue;
                }
                player.sendMessage(ColorText.translate("&eLeaving: &9&l" + koth + " KOTH"));
            }
        }
    }
    
    public Location getLocation(final String koth, final String corner) {
        final LocationFile location = this.location;
        final World world = Bukkit.getWorld((String)LocationFile.getConfig().get("KOTHS." + koth + ".world"));
        final double x = location.getDouble("KOTHS." + koth + "." + corner + ".x");
        final double y = location.getDouble("KOTHS." + koth + "." + corner + ".y");
        final double z = location.getDouble("KOTHS." + koth + "." + corner + ".z");
        return new Location(world, x, y, z);
    }

}
