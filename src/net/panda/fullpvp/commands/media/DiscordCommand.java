package net.panda.fullpvp.commands.media;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.panda.fullpvp.configuration.MessagesFile;
import net.panda.fullpvp.utilities.ColorText;

import org.bukkit.command.CommandExecutor;

public class DiscordCommand implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command cmd, final String cmdLabel, final String[] args) {
    	MessagesFile messages = MessagesFile.getConfig();
    	for(String msg : messages.getStringList("Discord")) {
            sender.sendMessage(ColorText.translate(msg));
    	}
        return true;
    }
}
