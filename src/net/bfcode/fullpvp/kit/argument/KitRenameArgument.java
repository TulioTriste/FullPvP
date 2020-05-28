package net.bfcode.fullpvp.kit.argument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;

import net.bfcode.fullpvp.FullPvP;
import net.bfcode.fullpvp.kit.Kit;
import net.bfcode.fullpvp.kit.event.KitRenameEvent;
import net.bfcode.fullpvp.utilities.CommandArgument;

public class KitRenameArgument extends CommandArgument
{
    private final FullPvP plugin;
    
    public KitRenameArgument(final FullPvP plugin) {
        super("rename", "Renames a kit");
        this.plugin = plugin;
        this.permission = "command.kit.argument." + this.getName();
    }
    
    @Override
    public String getUsage(final String label) {
        return "/" + label + ' ' + this.getName() + " <kitName> <newKitName>";
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        Kit kit = this.plugin.getKitManager().getKit(args[2]);
        if (kit != null) {
            sender.sendMessage(ChatColor.RED + "There is already a kit named " + kit.getName() + '.');
            return true;
        }
        kit = this.plugin.getKitManager().getKit(args[1]);
        if (kit == null) {
            sender.sendMessage(ChatColor.RED + "There is not a kit named " + args[1] + '.');
            return true;
        }
        final KitRenameEvent event = new KitRenameEvent(kit, kit.getName(), args[2]);
        Bukkit.getPluginManager().callEvent((Event)event);
        if (event.isCancelled()) {
            return true;
        }
        if (event.getOldName().equals(event.getNewName())) {
            sender.sendMessage(ChatColor.RED + "This kit is already called " + event.getNewName() + '.');
            return true;
        }
        kit.setName(event.getNewName());
        sender.sendMessage(ChatColor.AQUA + "Renamed kit " + event.getOldName() + " to " + event.getNewName() + '.');
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
