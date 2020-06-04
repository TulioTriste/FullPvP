package net.bfcode.fullpvp.commands.essentials;

import java.util.Objects;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import net.bfcode.fullpvp.utilities.BukkitUtils;
import net.bfcode.fullpvp.utilities.ColorText;
import net.bfcode.fullpvp.utilities.Utils;

public class TopCommand implements CommandExecutor {
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.MUST_BE_PLAYER);
            return true;
        }
        if (!sender.hasPermission("fullpvp.command.top")) {
			sender.sendMessage(Utils.NO_PERMISSION);
			return true;
		}
        final Player player = (Player)sender;
        final Location origin = player.getLocation().clone();
        final Location highestLocation = BukkitUtils.getHighestLocation(origin.clone());
        if (highestLocation != null && !Objects.equals(highestLocation, origin)) {
            final Block originBlock = origin.getBlock();
            if ((highestLocation.getBlockY() - originBlock.getY() != 1 || originBlock.getType() != Material.WATER) && originBlock.getType() != Material.STATIONARY_WATER) {
                player.teleport(highestLocation.add(0.0, 1.0, 0.0), PlayerTeleportEvent.TeleportCause.COMMAND);
                Command.broadcastCommandMessage(sender, ChatColor.GOLD + "Teleported to highest location.");
                return true;
            }
        }
        sender.sendMessage(ChatColor.RED + "No highest location found.");
        return true;
    }
}
