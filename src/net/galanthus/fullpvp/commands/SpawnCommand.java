package net.galanthus.fullpvp.commands;

import org.bukkit.Location;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.galanthus.fullpvp.FullPvP;
import net.galanthus.fullpvp.configuration.LocationFile;
import net.galanthus.fullpvp.utilities.ColorText;
import net.galanthus.fullpvp.utilities.Utils;

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
        if (!player.hasPermission(Utils.PERMISSION + "spawn")) {
            player.sendMessage(Utils.NO_PERMISSION);
            return true;
        }
        if (FullPvP.getPlugin().getSpawnHandler().getSpawnTasks().containsKey(player.getUniqueId())) {
            player.sendMessage(ColorText.translate(FullPvP.getPlugin().getConfig().getString("Spawn.AlreadyTeleporting")));
            return true;
        }
        if (FullPvP.getPlugin().getCombatTagListener().hasCooldown(player)) {
            player.sendMessage(ColorText.translate("&cYou can't teleport to spawn if you have &4&lCombat Tag&c."));
            return true;
        }
        if (player.hasPermission("rank.staff")) {
        	LocationFile locations = LocationFile.getConfig();
            int x = locations.getInt("Locations.Spawn.X");
            int y = locations.getInt("Locations.Spawn.Y");
            int z = locations.getInt("Locations.Spawn.Z");
            String world = locations.getString("Locations.Spawn.World");
            Location location = new Location(Bukkit.getWorld(world), (double)x, (double)y, (double)z);
            player.teleport(location);
            FullPvP.getPlugin().getSpawnHandler().removeSpawn(player);
        	return true;
        } else {
        	FullPvP.getPlugin().getSpawnHandler().createSpawn(player);
            player.sendMessage(ColorText.translate(FullPvP.getPlugin().getConfig().getString("Spawn.Teleporting")).replace("%time%", "" + FullPvP.getPlugin().getConfig().getInt("Spawn.Time")));
            return true;
        }
    }
}
