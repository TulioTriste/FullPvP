package net.panda.fullpvp.commands.essentials;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;
import net.panda.fullpvp.configuration.MessagesFile;
import net.panda.fullpvp.utilities.ColorText;
import net.panda.fullpvp.utilities.Utils;

public class BroadcastCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		MessagesFile messages = MessagesFile.getConfig();
		if(!sender.hasPermission("fullpvp.command.broadcast")) {
			sender.sendMessage(Utils.NO_PERMISSION);
			return true;
		}
		if(args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " <message>");
			return true;
		}
		String r = "";
		if (args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				r = r + args[i] + " ";
			}
			Bukkit.broadcastMessage(ColorText.translate(messages.getString("Broadcast-Prefix") + "&r" + r));
		}
		return true;
	}

}
