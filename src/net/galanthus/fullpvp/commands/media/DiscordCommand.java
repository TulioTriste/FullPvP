package net.galanthus.fullpvp.commands.media;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.galanthus.fullpvp.FullPvP;
import net.galanthus.fullpvp.utilities.ColorText;

import org.bukkit.command.CommandExecutor;

public class DiscordCommand implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command cmd, final String cmdLabel, final String[] args) {
        sender.sendMessage(ColorText.translate(FullPvP.getPlugin().getConfig().getString("Discord")));
        return true;
    }
}
