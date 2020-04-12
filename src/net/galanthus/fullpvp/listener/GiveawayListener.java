package net.galanthus.fullpvp.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

import net.galanthus.fullpvp.FullPvP;
import net.galanthus.fullpvp.handler.GiveawayHandler;
import net.galanthus.fullpvp.utilities.ColorText;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class GiveawayListener implements Listener {
	
    public GiveawayListener(final FullPvP plugin) {
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
    }
    
    @EventHandler
    public void onAsyncChat(final AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        if (event.isCancelled()) {
            return;
        }
        if (event.getMessage().startsWith(String.valueOf(FullPvP.getPlugin().getConfig().getInt("Giveaway.Number"))) && GiveawayHandler.isStarted()) {
            GiveawayHandler.setGiveawayEvent(false);
            Bukkit.broadcastMessage(ColorText.translate("&a" + player.getName() + " won the giveaway!"));
            Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getConsoleSender(), "key give " + player.getName() + " TierOne 2");
        }
    }
}
