package net.bfcode.fullpvp.commands;

import org.bukkit.Location;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.bfcode.fullpvp.FullPvP;
import net.bfcode.fullpvp.configuration.LocationFile;
import net.bfcode.fullpvp.utilities.ColorText;
import net.bfcode.fullpvp.utilities.Utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class SpawnCommand implements CommandExecutor {
	
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.MUST_BE_PLAYER);
            return true;
        }
        final Player player = (Player)sender;
        if (!player.hasPermission("fullpvp.command.spawn")) {
            player.sendMessage(Utils.NO_PERMISSION);
            return true;
        }
        if (FullPvP.getInstance().getSpawnHandler().getSpawnTasks().containsKey(player.getUniqueId())) {
            player.sendMessage(ColorText.translate(FullPvP.getInstance().getConfig().getString("Spawn.Already-Teleporting")));
            return true;
        }
        if (FullPvP.getInstance().getCombatTagListener().hasCooldown(player)) {
            player.sendMessage(ColorText.translate(FullPvP.getInstance().getConfig().getString("Spawn.Combat-Tag")));
            return true;
        }
        if (player.hasPermission("fullpvp.command.spawn.staff")) {
        	LocationFile locations = LocationFile.getConfig();
            int x = locations.getInt("Locations.Spawn.X");
            int y = locations.getInt("Locations.Spawn.Y");
            int z = locations.getInt("Locations.Spawn.Z");
            String world = locations.getString("Locations.Spawn.World");
            Location location = new Location(Bukkit.getWorld(world), (double)x, (double)y, (double)z);
            player.teleport(location);
            FullPvP.getInstance().getSpawnHandler().removeSpawn(player);
        	return true;
        } else {
        	FullPvP.getInstance().getSpawnHandler().createSpawn(player);
            player.sendMessage(ColorText.translate(FullPvP.getInstance().getConfig().getString("Spawn.Teleporting"))
            		.replace("{time}", String.valueOf(FullPvP.getInstance().getConfig().getInt("Spawn.Time"))));
            return true;
        }
    }
}
