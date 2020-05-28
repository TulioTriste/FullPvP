package net.bfcode.fullpvp.commands.media;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.bfcode.fullpvp.FullPvP;
import net.bfcode.fullpvp.utilities.ColorText;

import org.bukkit.command.CommandExecutor;

public class StoreCommand implements CommandExecutor {
    public boolean onCommand(final CommandSender sender, final Command cmd, final String cmdLabel, final String[] args) {
    	sender.sendMessage(ColorText.translate(FullPvP.getPlugin().getConfig().getString("Store")));
        return true;
    }
}
