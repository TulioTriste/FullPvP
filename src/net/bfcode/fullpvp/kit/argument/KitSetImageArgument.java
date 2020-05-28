package net.bfcode.fullpvp.kit.argument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.bfcode.fullpvp.FullPvP;
import net.bfcode.fullpvp.kit.Kit;
import net.bfcode.fullpvp.utilities.CommandArgument;

public class KitSetImageArgument extends CommandArgument
{
    private final FullPvP plugin;
    
    public KitSetImageArgument(final FullPvP plugin) {
        super("setimage", "Sets the image of kit in GUI to held item");
        this.plugin = plugin;
        this.aliases = new String[] { "setitem", "setpic", "setpicture" };
        this.permission = "command.kit.argument." + this.getName();
    }
    
    @Override
    public String getUsage(final String label) {
        return "/" + label + ' ' + this.getName() + " <kitName>";
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This argument is only executable by players.");
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        final Player player = (Player)sender;
        final ItemStack stack = player.getItemInHand();
        if (stack == null || stack.getType() == Material.AIR) {
            player.sendMessage(ChatColor.RED + "You are not holding anything.");
            return true;
        }
        final Kit kit = this.plugin.getKitManager().getKit(args[1]);
        if (kit == null) {
            sender.sendMessage(ChatColor.RED + "There is not a kit named " + args[1] + '.');
            return true;
        }
        kit.setImage(stack.clone());
        sender.sendMessage(ChatColor.AQUA + "Set image of kit " + ChatColor.YELLOW + kit.getDisplayName() + ChatColor.AQUA + " to " + ChatColor.YELLOW + this.plugin.getItemDb().getName(stack) + ChatColor.AQUA + '.');
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
