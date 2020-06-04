package net.bfcode.fullpvp.commands.media;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.bfcode.fullpvp.configuration.MessagesFile;
import net.bfcode.fullpvp.utilities.ColorText;

import org.bukkit.command.CommandExecutor;

public class WebsiteCommand implements CommandExecutor {
    public boolean onCommand(final CommandSender sender, final Command cmd, final String cmdLabel, final String[] args) {
    	MessagesFile messages = MessagesFile.getConfig();
    	for(String msg : messages.getStringList("Website")) {
            sender.sendMessage(ColorText.translate(msg));
    	}
        return true;
    }
}
