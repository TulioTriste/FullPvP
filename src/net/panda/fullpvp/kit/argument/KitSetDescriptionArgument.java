package net.panda.fullpvp.kit.argument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.panda.fullpvp.FullPvP;
import net.panda.fullpvp.kit.Kit;
import net.panda.fullpvp.utilities.CommandArgument;

public class KitSetDescriptionArgument extends CommandArgument
{
    private final FullPvP plugin;
    
    public KitSetDescriptionArgument(final FullPvP plugin) {
        super("setdescription", "Sets the description of a kit");
        this.plugin = plugin;
        this.aliases = new String[] { "setdesc" };
        this.permission = "command.kit.argument." + this.getName();
    }
    
    @Override
    public String getUsage(final String label) {
        return "/" + label + ' ' + this.getName() + " <kitName> <none|description>";
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        final Kit kit = this.plugin.getKitManager().getKit(args[1]);
        if (kit == null) {
            sender.sendMessage(ChatColor.RED + "There is not a kit named " + args[1] + '.');
            return true;
        }
        if (args[2].equalsIgnoreCase("none") || args[2].equalsIgnoreCase("null")) {
            kit.setDescription(null);
            sender.sendMessage(ChatColor.YELLOW + "Removed description of kit " + kit.getDisplayName() + '.');
            return true;
        }
        final String description = ChatColor.translateAlternateColorCodes('&', StringUtils.join((Object[])args, ' ', 2, args.length));
        kit.setDescription(description);
        sender.sendMessage(ChatColor.YELLOW + "Set description of kit " + kit.getDisplayName() + " to " + description + '.');
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
