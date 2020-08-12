package net.panda.fullpvp.commands.essentials;

import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.panda.fullpvp.utilities.BaseConstants;
import net.panda.fullpvp.utilities.BukkitUtils;
import net.panda.fullpvp.utilities.Utils;

public class FlyCommand implements CommandExecutor {
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        Player target;
        if (!sender.hasPermission("fullpvp.command.fly")) {
			sender.sendMessage(Utils.NO_PERMISSION);
			return true;
		}
        if (args.length > 0 && sender.hasPermission("fullpvp.command.fly.others")) {
            target = BukkitUtils.playerWithNameOrUUID(args[0]);
        }
        else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Utils.MUST_BE_PLAYER);
                return true;
            }
            target = (Player)sender;
        }
        if (target == null) {
            sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
            return true;
        }
        final boolean newFlight = !target.getAllowFlight();
        target.setAllowFlight(newFlight);
        if (newFlight) {
            target.setFlying(true);
        }
        Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Flight mode of " + target.getName() + " set to " + newFlight + '.');
        return true;
    }
    
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        return (args.length == 1) ? null : Collections.emptyList();
    }
}
