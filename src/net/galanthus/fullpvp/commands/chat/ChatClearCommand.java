package net.galanthus.fullpvp.commands.chat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.galanthus.fullpvp.utilities.ColorText;
import net.md_5.bungee.api.ChatColor;

public class ChatClearCommand implements CommandExecutor {
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!sender.hasPermission("fullpvp.command.chatclear")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
            return true;
        } else {
        	for (int i = 0; i < 100; ++i) {
            	Bukkit.broadcastMessage("");
        	}
    	}
        Bukkit.broadcastMessage(ColorText.translate("&eChat has been cleared by &d" + sender.getName() + "&e."));
	return true;
	}
}
