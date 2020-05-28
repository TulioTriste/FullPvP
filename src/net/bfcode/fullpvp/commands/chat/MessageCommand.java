package net.bfcode.fullpvp.commands.chat;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import net.bfcode.fullpvp.FullPvP;
import net.bfcode.fullpvp.commands.BaseCommand;
import net.bfcode.fullpvp.event.PlayerMessageEvent;
import net.bfcode.fullpvp.utilities.BaseConstants;
import net.bfcode.fullpvp.utilities.BukkitUtils;

public class MessageCommand extends BaseCommand {
	
	public MessageCommand(FullPvP plugin) {
        super("message", "Sends a message to a recipient(s).");
        this.setAliases(new String[] { "msg", "m", "whisper", "w", "tell" });
        this.setUsage("/(command) <playerName> <message>");
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command is only executable for players.");
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        Player player = (Player)sender;
        Player target = BukkitUtils.playerWithNameOrUUID(args[0]);
        if (target == null || !BaseCommand.canSee(sender, target)) {
            sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
            return true;
        }
        String message = StringUtils.join((Object[])args, ' ', 1, args.length);
        Set<Player> recipients = Collections.singleton(target);
        PlayerMessageEvent playerMessageEvent = new PlayerMessageEvent(player, recipients, message, false, null);
        Bukkit.getPluginManager().callEvent((Event)playerMessageEvent);
        if (!playerMessageEvent.isCancelled()) {
            playerMessageEvent.send();
        }
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}
