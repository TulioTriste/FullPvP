package net.bfcode.fullpvp.kit.argument;

import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.bfcode.fullpvp.FullPvP;
import net.bfcode.fullpvp.kit.Kit;
import net.bfcode.fullpvp.utilities.CommandArgument;

public class KitGuiArgument extends CommandArgument
{
    private final FullPvP plugin;
    
    public KitGuiArgument(final FullPvP plugin) {
        super("gui", "Opens the kit gui");
        this.plugin = plugin;
        this.aliases = new String[] { "menu" };
        this.permission = "command.kit.argument." + this.getName();
    }
    
    @Override
    public String getUsage(final String label) {
        return "/" + label + ' ' + this.getName();
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players may open kit GUI's.");
            return true;
        }
        final List<Kit> kits = this.plugin.getKitManager().getKits();
        if (kits.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "No kits have been defined.");
            return true;
        }
        final Player player = (Player)sender;
        player.openInventory(this.plugin.getKitManager().getGui(player));
        return true;
    }
    
    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        return Collections.emptyList();
    }
}
