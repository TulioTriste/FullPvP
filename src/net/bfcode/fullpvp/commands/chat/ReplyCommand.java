package net.bfcode.fullpvp.commands.chat;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.google.common.collect.Sets;

import net.bfcode.fullpvp.FullPvP;
import net.bfcode.fullpvp.event.PlayerMessageEvent;
import net.bfcode.fullpvp.utilities.user.BaseUser;

public class ReplyCommand implements CommandExecutor {
	
    private static long VANISH_REPLY_TIMEOUT;
    private FullPvP plugin;
    
    public ReplyCommand(FullPvP plugin) {
        this.plugin = plugin;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command is only executable for players.");
            return true;
        }
        Player player = (Player)sender;
        UUID uuid = player.getUniqueId();
        BaseUser baseUser = this.plugin.getUserManager().getUser(uuid);
        UUID lastReplied = baseUser.getLastRepliedTo();
        Player target = ((lastReplied == null) ? null : Bukkit.getPlayer(lastReplied));
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <message>");
            if (lastReplied != null) {
                sender.sendMessage(ChatColor.RED + "You are in a conversation with " + target.getName() + '.');
            }
            return true;
        }
        long millis = System.currentTimeMillis();
        if (target == null || millis - baseUser.getLastReceivedMessageMillis() > ReplyCommand.VANISH_REPLY_TIMEOUT) {
            sender.sendMessage(ChatColor.RED + "There is no player to reply to.");
            return true;
        }
        String message = StringUtils.join((Object[])args, ' ');
		HashSet recipients = Sets.newHashSet((Object[])new Player[] { target });
        PlayerMessageEvent playerMessageEvent = new PlayerMessageEvent(player, recipients, message, false, lastReplied);
        Bukkit.getPluginManager().callEvent((Event)playerMessageEvent);
        if (!playerMessageEvent.isCancelled()) {
            playerMessageEvent.send();
        }
        return true;
    }
    
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
    
    static {
        VANISH_REPLY_TIMEOUT = TimeUnit.SECONDS.toMillis(45L);
    }
}
