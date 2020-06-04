package net.bfcode.fullpvp.commands.essentials;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import com.google.common.collect.ImmutableSet;

import net.bfcode.fullpvp.utilities.BaseConstants;
import net.bfcode.fullpvp.utilities.BukkitUtils;
import net.bfcode.fullpvp.utilities.ColorText;
import net.bfcode.fullpvp.utilities.Utils;

public class FeedCommand implements CommandExecutor {    
    
	@Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
		if (!sender.hasPermission("fullpvp.command.feed")) {
			sender.sendMessage(ColorText.translate(Utils.NO_PERMISSION));
			return true;
		}
        Player onlyTarget = null;
        Collection<Player> targets;
        if (args.length > 0 && sender.hasPermission("fullpvp.command.feed.others")) {
            if (args[0].equalsIgnoreCase("all") && sender.hasPermission("fullpvp.command.feed.all")) {
                targets = ImmutableSet.copyOf(Bukkit.getOnlinePlayers());
            }
            else {
                if ((onlyTarget = BukkitUtils.playerWithNameOrUUID(args[0])) == null) {
                    sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
                    return true;
                }
                targets = ImmutableSet.of(onlyTarget);
            }
        }
        else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Utils.MUST_BE_PLAYER);
                return true;
            }
            targets = ImmutableSet.of((onlyTarget = (Player) sender));
        }
        if (onlyTarget != null && onlyTarget.getFoodLevel() == 20) {
            sender.sendMessage(ChatColor.RED + onlyTarget.getName() + " already has full hunger.");
            return true;
        }
        for (final Player target : targets) {
            target.removePotionEffect(PotionEffectType.HUNGER);
            target.setFoodLevel(20);
        }
        sender.sendMessage(ChatColor.YELLOW + "Feed " + ((onlyTarget == null) ? "all online players" : onlyTarget.getName()) + '.');
        return true;
    }
    
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        return (args.length == 1) ? null : Collections.emptyList();
    }
}
