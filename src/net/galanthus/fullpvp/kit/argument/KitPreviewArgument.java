package net.galanthus.fullpvp.kit.argument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;

import net.galanthus.fullpvp.FullPvP;
import net.galanthus.fullpvp.kit.Kit;
import net.galanthus.fullpvp.kit.KitListener;
import net.galanthus.fullpvp.utilities.CommandArgument;

public class KitPreviewArgument extends CommandArgument
{
    private final FullPvP plugin;
    
    public KitPreviewArgument(final FullPvP plugin) {
        super("preview", "Preview the items you will get in a kit");
        this.plugin = plugin;
        this.aliases = new String[] { "look", "check", "see" };
        this.permission = "command.kit.argument." + this.getName();
    }
    
    @EventHandler
    @Override
    public String getUsage(final String label) {
        return "/" + label + ' ' + this.getName() + " <kitName>";
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        final Kit kit = this.plugin.getKitManager().getKit(args[1]);
        if (kit == null) {
            sender.sendMessage(ChatColor.RED + "There is not a kit named " + args[1] + '.');
            return true;
        }
        final Inventory trackedInventory = kit.getPreview((Player)sender);
        KitListener.previewInventory.add(trackedInventory);
        ((Player)sender).openInventory(trackedInventory);
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
