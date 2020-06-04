package net.bfcode.fullpvp.commands.chat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.bfcode.fullpvp.configuration.MessagesFile;
import net.bfcode.fullpvp.utilities.ColorText;
import net.bfcode.fullpvp.utilities.Utils;

public class ChatClearCommand implements CommandExecutor {
	
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
		MessagesFile messages = MessagesFile.getConfig();
        if (!sender.hasPermission("fullpvp.command.chatclear")) {
            sender.sendMessage(Utils.NO_PERMISSION);
            return true;
        } else {
        	for (int i = 0; i < 150; ++i) {
            	Bukkit.broadcastMessage("");
        	}
    	}
        Bukkit.broadcastMessage(ColorText.translate(messages.getString("ChatClear-message").replace("{player}", sender.getName())));
	return true;
	}
}
