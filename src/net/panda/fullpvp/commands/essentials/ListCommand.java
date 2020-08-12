package net.panda.fullpvp.commands.essentials;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.panda.fullpvp.configuration.MessagesFile;
import net.panda.fullpvp.utilities.ColorText;

public class ListCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player)sender;
		for(String msg : MessagesFile.getConfig().getStringList("list-command")) {
			player.sendMessage(ColorText.translate(msg).replace("{online}", Bukkit.getOnlinePlayers().size() + ""));
		}
		return true;
	}

}
