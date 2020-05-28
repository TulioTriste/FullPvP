package net.bfcode.fullpvp.kit.argument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.bfcode.fullpvp.FullPvP;
import net.bfcode.fullpvp.kit.Kit;
import net.bfcode.fullpvp.utilities.BukkitUtils;
import net.bfcode.fullpvp.utilities.CommandArgument;

public class KitListArgument extends CommandArgument
{
    private final FullPvP plugin;
    
    public KitListArgument(final FullPvP plugin) {
        super("list", "Lists all current kits");
        this.plugin = plugin;
        this.permission = "command.kit.argument." + this.getName();
    }
    
    @Override
    public String getUsage(final String label) {
        return "/" + label + ' ' + this.getName();
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        final List<Kit> kits = this.plugin.getKitManager().getKits();
        if (kits.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "No kits have been defined.");
            return true;
        }
        final ArrayList<String> kitNames = new ArrayList<String>();
        for (final Kit kit : kits) {
            final String permission = kit.getPermissionNode();
            if (permission != null && !sender.hasPermission(permission)) {
                continue;
            }
            kitNames.add(kit.getDisplayName());
        }
        final String kitList = StringUtils.join(kitNames, ChatColor.GRAY + ", " + ChatColor.YELLOW);
        sender.sendMessage(ChatColor.DARK_GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        sender.sendMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "Kits " + ChatColor.GREEN + "[" + kitNames.size() + '/' + kits.size() + "]");
        sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        sender.sendMessage(ChatColor.GRAY + "[" + ChatColor.YELLOW + kitList + ChatColor.GRAY + ']');
        sender.sendMessage(ChatColor.DARK_GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        return true;
    }
    
    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        return Collections.emptyList();
    }
}
