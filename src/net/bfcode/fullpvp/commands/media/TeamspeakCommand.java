package net.bfcode.fullpvp.commands.media;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.bfcode.fullpvp.FullPvP;
import net.bfcode.fullpvp.utilities.ColorText;

import org.bukkit.command.CommandExecutor;

public class TeamspeakCommand implements CommandExecutor {
    public boolean onCommand(final CommandSender sender, final Command cmd, final String cmdLabel, final String[] args) {
    	sender.sendMessage(ColorText.translate(FullPvP.getPlugin().getConfig().getString("Teamspeak")));
        return true;
    }
}
