package net.galanthus.fullpvp.kit.argument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.galanthus.fullpvp.FullPvP;
import net.galanthus.fullpvp.kit.Kit;
import net.galanthus.fullpvp.utilities.CommandArgument;
import net.galanthus.fullpvp.utilities.JavaUtils;

public class KitSetDelayArgument extends CommandArgument
{
    private final FullPvP plugin;
    
    public KitSetDelayArgument(final FullPvP plugin) {
        super("setdelay", "Sets the delay time of a kit");
        this.plugin = plugin;
        this.aliases = new String[] { "delay", "setcooldown", "cooldown" };
        this.permission = "command.kit.argument." + this.getName();
    }
    
    @Override
    public String getUsage(final String label) {
        return "/" + label + ' ' + this.getName() + " <kitName> <delay>";
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
        final long duration = JavaUtils.parse(args[2]);
        if (duration == -1L) {
            sender.sendMessage(ChatColor.RED + "Invalid duration, use the correct format: 10m 1s");
            return true;
        }
        kit.setDelayMillis(duration);
        sender.sendMessage(ChatColor.YELLOW + "Set delay of kit " + kit.getName() + " to " + DurationFormatUtils.formatDurationWords(duration, true, true) + '.');
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
