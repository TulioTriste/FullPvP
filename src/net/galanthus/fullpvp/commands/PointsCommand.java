package net.galanthus.fullpvp.commands;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.galanthus.fullpvp.configuration.PointsFile;
import net.galanthus.fullpvp.utilities.ColorText;

public class PointsCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		PointsFile points = PointsFile.getConfig();
		UUID UUID = player.getUniqueId();
		sender.sendMessage(ColorText.translate("&aYou have &2" + points.getInt("users." + UUID + ".points") + " RP."));
		return true;
	}

}
