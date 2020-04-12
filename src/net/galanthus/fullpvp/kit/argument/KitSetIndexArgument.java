package net.galanthus.fullpvp.kit.argument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.galanthus.fullpvp.FullPvP;
import net.galanthus.fullpvp.kit.Kit;
import net.galanthus.fullpvp.utilities.CommandArgument;
import net.galanthus.fullpvp.utilities.Ints;

public class KitSetIndexArgument extends CommandArgument {
	
    private final FullPvP plugin;
    
    public KitSetIndexArgument(final FullPvP plugin) {
        super("setindex", "Sets the position of a kit for the GUI");
        this.plugin = plugin;
        this.aliases = new String[] { "setorder", "setindex", "setpos", "setposition" };
        this.permission = "command.kit.argument." + this.getName();
    }
    
    @Override
    public String getUsage(final String label) {
        return "/" + label + ' ' + this.getName() + " <kitName> <index[0 = minimum]>";
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        final Kit kit = this.plugin.getKitManager().getKit(args[1]);
        if (kit == null) {
            sender.sendMessage(ChatColor.RED + "Kit '" + args[1] + "' not found.");
            return true;
        }
        Integer newIndex = Ints.tryParse(args[2]);
        if (newIndex == null) {
            sender.sendMessage(ChatColor.RED + "'" + args[2] + "' is not a number.");
            return true;
        }
        if (newIndex < 1) {
            sender.sendMessage(ChatColor.RED + "The kit index cannot be less than " + 1 + '.');
            return true;
        }
        final List<Kit> kits = this.plugin.getKitManager().getKits();
        final int totalKitAmount = kits.size() + 1;
        if (newIndex > totalKitAmount) {
            sender.sendMessage(ChatColor.RED + "The kit index must be a maximum of " + totalKitAmount + '.');
            return true;
        }
        final int previousIndex = kits.indexOf(kit) + 1;
        if (newIndex == previousIndex) {
            sender.sendMessage(ChatColor.RED + "Index of kit " + kit.getDisplayName() + " is already " + newIndex + '.');
            return true;
        }
        kits.remove(kit);
        --newIndex;
        kits.add(newIndex, kit);
        sender.sendMessage(ChatColor.AQUA + "Set the index of kit " + kit.getDisplayName() + " from " + previousIndex + " to " + newIndex + '.');
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
