package net.galanthus.fullpvp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.galanthus.fullpvp.handler.GiveawayHandler;
import net.galanthus.fullpvp.utilities.Utils;

import org.bukkit.command.CommandExecutor;

public class GiveawayCommand implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!sender.hasPermission(Utils.PERMISSION + "giveaway")) {
            sender.sendMessage(Utils.NO_PERMISSION);
            return true;
        }
        GiveawayHandler.setGiveawayEvent(sender, !GiveawayHandler.isStarted());
        return true;
    }
}
