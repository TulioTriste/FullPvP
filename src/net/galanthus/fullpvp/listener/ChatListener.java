package net.galanthus.fullpvp.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.clip.deluxetags.DeluxeTag;
import net.galanthus.fullpvp.FullPvP;
import net.galanthus.fullpvp.clans.ClanHandler;
import net.galanthus.fullpvp.utilities.ColorText;

public class ChatListener implements Listener {
	
    public ChatListener(final FullPvP plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
	
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onPlayerChat(final AsyncPlayerChatEvent event) {
		final Player player = event.getPlayer();
		String prefix = DeluxeTag.getPlayerDisplayTag(player);
		String rank = FullPvP.getPlugin().getChat().getPlayerPrefix(player);
		String name = player.getDisplayName() + "&7: &f";
		String message = player.hasPermission("fullpvp.chat.color") ? ColorText.translate(event.getMessage()) : event.getMessage();
		event.setCancelled(true);
		if (ClanHandler.hasClan(player) == true) {
			String clan = "&2[" + ClanHandler.getClan(player) + "] ";
			String chat = ColorText.translate(clan + prefix + rank + name) + (message);
            for (final Player recipient : event.getRecipients()) {
                recipient.sendMessage(chat);
            }
            Bukkit.getConsoleSender().sendMessage(chat);
		} else if (ClanHandler.hasClan(player) == false) {
			String chat = ColorText.translate(prefix + rank + name) + (message);
			for (final Player recipient : event.getRecipients()) {
	            recipient.sendMessage(chat);
	        } 
	        Bukkit.getConsoleSender().sendMessage(chat);
		}
	}
}
