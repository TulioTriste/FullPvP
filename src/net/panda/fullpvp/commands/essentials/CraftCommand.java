package net.panda.fullpvp.commands.essentials;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.panda.fullpvp.utilities.ColorText;
import net.panda.fullpvp.utilities.Utils;

public class CraftCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("fullpvp.command.craft")) {
			sender.sendMessage(ColorText.translate(Utils.NO_PERMISSION));
			return true;
		}
		Player player = (Player) sender;
		player.openWorkbench(player.getLocation(), true);
		return true;
	}

}
