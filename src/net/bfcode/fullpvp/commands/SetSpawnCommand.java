package net.bfcode.fullpvp.commands;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import net.bfcode.fullpvp.FullPvP;
import net.bfcode.fullpvp.configuration.LocationFile;
import net.bfcode.fullpvp.utilities.ColorText;
import net.bfcode.fullpvp.utilities.Utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class SetSpawnCommand implements CommandExecutor {
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.MUST_BE_PLAYER);
            return true;
        }
        final Player player = (Player)sender;
        if (!player.hasPermission(Utils.PERMISSION + "setspawn")) {
            player.sendMessage(Utils.NO_PERMISSION);
            return true;
        }
        this.setSpawnLocation(player);
        player.sendMessage(ColorText.translate(FullPvP.getPlugin().getConfig().getString("Spawn.Set-Spawn")));
        player.playSound(player.getLocation(), Sound.EXPLODE, 1.0f, 1.0f);
        return true;
    }
    
    private void setSpawnLocation(final Player player) {
        final LocationFile location = LocationFile.getConfig();
        location.set("Locations.Spawn.X", (Object)player.getLocation().getBlockX());
        location.set("Locations.Spawn.Y", (Object)player.getLocation().getBlockY());
        location.set("Locations.Spawn.Z", (Object)player.getLocation().getBlockZ());
        location.set("Locations.Spawn.World", (Object)player.getWorld().getName());
        location.save();
        location.reload();
    }
}
