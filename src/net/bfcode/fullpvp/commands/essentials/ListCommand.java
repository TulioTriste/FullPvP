package net.bfcode.fullpvp.commands.essentials;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.bfcode.fullpvp.utilities.ColorText;

public class ListCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player)sender;
		player.sendMessage("");
		player.sendMessage(ColorText.translate("&aActualmente hay &2&l" + Bukkit.getOnlinePlayers().size() + " &ajugadores conectados!"));
		player.sendMessage("");
		return true;
	}

}
