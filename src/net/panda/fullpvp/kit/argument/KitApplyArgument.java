package net.panda.fullpvp.kit.argument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.panda.fullpvp.FullPvP;
import net.panda.fullpvp.kit.Kit;
import net.panda.fullpvp.utilities.CommandArgument;

public class KitApplyArgument extends CommandArgument
{
    private final FullPvP plugin;
    
    public KitApplyArgument(final FullPvP plugin) {
        super("apply", "Applies a kit to player");
        this.plugin = plugin;
        this.permission = "fullpvp.command.kit.argument." + this.getName();
    }
    
    @Override
    public String getUsage(final String label) {
        return "/" + label + ' ' + this.getName() + " <kitName> <playerName>";
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
        final Player target = Bukkit.getPlayer(args[2]);
        if (target == null || (sender instanceof Player && !((Player)sender).canSee(target))) {
            sender.sendMessage(ChatColor.RED + "Player '" + ChatColor.GRAY + args[2] + ChatColor.RED + "' not found.");
            return true;
        }
        if (kit.applyTo(target, true, true)) {
            sender.sendMessage(ChatColor.GRAY + "Applied kit '" + kit.getDisplayName() + "' to '" + target.getName() + "'.");
            return true;
        }
        sender.sendMessage(ChatColor.RED + "Failed to apply kit " + kit.getDisplayName() + " to " + target.getName() + '.');
        return true;
    }
    
    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length == 2) {
            final List<Kit> kits = this.plugin.getKitManager().getKits();
            final ArrayList<String> results = new ArrayList<String>(kits.size());
            for (final Kit kit : kits) {
                results.add(kit.getName());
            }
            return results;
        }
        if (args.length == 3) {
            return null;
        }
        return Collections.emptyList();
    }
}
