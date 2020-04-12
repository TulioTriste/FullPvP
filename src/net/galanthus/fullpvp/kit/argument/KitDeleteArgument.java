package net.galanthus.fullpvp.kit.argument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;

import net.galanthus.fullpvp.FullPvP;
import net.galanthus.fullpvp.kit.Kit;
import net.galanthus.fullpvp.kit.event.KitRemoveEvent;
import net.galanthus.fullpvp.utilities.CommandArgument;

public class KitDeleteArgument extends CommandArgument
{
    private final FullPvP plugin;
    
    public KitDeleteArgument(final FullPvP plugin) {
        super("delete", "Deletes a kit");
        this.plugin = plugin;
        this.aliases = new String[] { "del", "remove" };
        this.permission = "command.kit.argument." + this.getName();
    }
    
    @Override
    public String getUsage(final String label) {
        return "/" + label + ' ' + this.getName() + " <kitName>";
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        final Kit kit = this.plugin.getKitManager().getKit(args[1]);
        if (kit == null) {
            sender.sendMessage(ChatColor.RED + "There is not a kit named " + args[1] + '.');
            return true;
        }
        final KitRemoveEvent event = new KitRemoveEvent(kit);
        Bukkit.getPluginManager().callEvent((Event)event);
        if (event.isCancelled()) {
            return true;
        }
        this.plugin.getKitManager().removeKit(kit);
        sender.sendMessage(ChatColor.GRAY + "Removed kit '" + args[1] + "'.");
        return true;
    }
    
    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length != 2) {
            return Collections.emptyList();
        }
        final List<Kit> kits = this.plugin.getKitManager().getKits();
        final ArrayList<String> results = new ArrayList<String>(kits.size());
        for (final Kit kit : kits) {
            results.add(kit.getName());
        }
        return results;
    }
}
