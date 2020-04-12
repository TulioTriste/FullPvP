package net.galanthus.fullpvp.koth.argument;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.galanthus.fullpvp.FullPvP;
import net.galanthus.fullpvp.utilities.CommandArgument;

public class KothCancelArgument extends CommandArgument {
	
    @SuppressWarnings("unused")
	private final FullPvP plugin;
    
    public KothCancelArgument(final FullPvP plugin) {
        super("cancel", "Cancels a running event", new String[] { "stop", "end" });
        this.plugin = plugin;
        this.permission = "fullpvp.command.event.argument." + this.getName();
    }
    
    public String getUsage(final String label) {
        return "/" + label + ' ' + this.getName();
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + sender.getName() + ChatColor.WHITE + " has cancelled the active KOTH.");
        return true;
    }
}
