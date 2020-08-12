package net.panda.fullpvp.commands;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import net.panda.fullpvp.FullPvP;
import net.panda.fullpvp.configuration.LocationFile;
import net.panda.fullpvp.utilities.ColorText;
import net.panda.fullpvp.utilities.Utils;

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
        if (!player.hasPermission("fullpvp.command.setspawn")) {
            player.sendMessage(Utils.NO_PERMISSION);
            return true;
        }
        this.setSpawnLocation(player);
        player.sendMessage(ColorText.translate(FullPvP.getInstance().getConfig().getString("Spawn.Set-Spawn")));
        player.playSound(player.getLocation(), Sound.EXPLODE, 1.0f, 1.0f);
        return true;
    }
    
    private void setSpawnLocation(final Player player) {
        final LocationFile location = LocationFile.getConfig();
        location.set("Locations.Spawn.X", player.getLocation().getBlockX());
        location.set("Locations.Spawn.Y", player.getLocation().getBlockY());
        location.set("Locations.Spawn.Z", player.getLocation().getBlockZ());
        location.set("Locations.Spawn.World", player.getWorld().getName());
        location.save();
        location.reload();
    }
}
