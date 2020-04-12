package net.galanthus.fullpvp.commands.essentials;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.galanthus.fullpvp.utilities.ColorText;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class VaultCommand implements CommandExecutor {
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
    	if (!sender.hasPermission("fullpvp.command.enderchest")) {
			sender.sendMessage(ColorText.translate("&cYou don't have permission to execute this command."));
			return true;
		}
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
            return false;
        }
        Player target;
        final Player player = target = (Player)sender;
        if (player.hasPermission("fullpvp.command.enderchest.others") && args.length != 0) {
            target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(ChatColor.RED + "Player not found or isn't online.");
                return true;
            }
        }
        player.openInventory(target.getEnderChest());
        player.sendMessage(ColorText.translate("&eOpened " + (player.equals(target) ? "your" : (target.getName() + "'s")) + " enderchest."));
        return true;
    }
}
