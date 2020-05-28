package net.bfcode.fullpvp.handler;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import net.bfcode.fullpvp.FullPvP;
import net.bfcode.fullpvp.utilities.ColorText;

public class GiveawayHandler {
    private static boolean started;
    
    public static boolean isStarted() {
        return GiveawayHandler.started;
    }
    
    public static void setGiveawayEvent(final boolean b) {
        if (b) {
            GiveawayHandler.started = true;
            final int number = (int)(Math.random() * 100.0 + 1.0);
            FullPvP.getPlugin().getConfig().set("Giveaway.Number", number);
            FullPvP.getPlugin().saveConfig();
            FullPvP.getPlugin().reloadConfig();
        }
        else {
            GiveawayHandler.started = false;
        }
    }
    
    public static void setGiveawayEvent(final CommandSender sender, final boolean b) {
        if (b) {
            GiveawayHandler.started = true;
            final int number = (int)(Math.random() * 100.0 + 1.0);
            FullPvP.getPlugin().getConfig().set("Giveaway.Number", number);
            FullPvP.getPlugin().saveConfig();
            FullPvP.getPlugin().reloadConfig();
            Bukkit.broadcastMessage(ColorText.translate("&eGiveaway has been started by &d" + sender.getName() + "&e."));
            sender.sendMessage(ColorText.translate("&cThe number is: " + number));
        }
        else {
            GiveawayHandler.started = false;
            Bukkit.broadcastMessage(ColorText.translate("&eGiveaway has been stopped by &d" + sender.getName() + "&e."));
        }
    }
}
