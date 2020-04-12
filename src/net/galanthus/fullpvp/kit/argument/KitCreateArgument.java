package net.galanthus.fullpvp.kit.argument;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import net.galanthus.fullpvp.FullPvP;
import net.galanthus.fullpvp.kit.Kit;
import net.galanthus.fullpvp.kit.event.KitCreateEvent;
import net.galanthus.fullpvp.utilities.CommandArgument;
import net.galanthus.fullpvp.utilities.JavaUtils;

public class KitCreateArgument extends CommandArgument
{
    private final FullPvP plugin;
    
    public KitCreateArgument(final FullPvP plugin) {
        super("create", "Creates a kit");
        this.plugin = plugin;
        this.permission = "command.kit.argument." + this.getName();
    }
    
    @Override
    public String getUsage(final String label) {
        return "/" + label + ' ' + this.getName() + " <kitName> [kitDescription]";
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players may create kits.");
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        if (!JavaUtils.isAlphanumeric(args[1])) {
            sender.sendMessage(ChatColor.GRAY + "Kit names may only be alphanumeric.");
            return true;
        }
        Kit kit = this.plugin.getKitManager().getKit(args[1]);
        if (kit != null) {
            sender.sendMessage(ChatColor.RED + "There is already a kit named " + args[1] + '.');
            return true;
        }
        final Player player = (Player)sender;
        kit = new Kit(args[1], (args.length >= 3) ? args[2] : null, player.getInventory(), player.getActivePotionEffects());
        final KitCreateEvent event = new KitCreateEvent(kit);
        Bukkit.getPluginManager().callEvent((Event)event);
        if (event.isCancelled()) {
            return true;
        }
        this.plugin.getKitManager().createKit(kit);
        sender.sendMessage(ChatColor.GRAY + "Created kit '" + kit.getDisplayName() + "'.");
        return true;
    }
}
