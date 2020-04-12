package net.galanthus.fullpvp.koth.argument;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.selections.Selection;

import net.galanthus.fullpvp.FullPvP;
import net.galanthus.fullpvp.configuration.LocationFile;
import net.galanthus.fullpvp.utilities.ColorText;
import net.galanthus.fullpvp.utilities.CommandArgument;
import net.galanthus.fullpvp.utilities.Utils;

public class KothCreateArgument extends CommandArgument {
	LocationFile location;
	
    @SuppressWarnings("unused")
	private final FullPvP plugin;
    
    public KothCreateArgument(final FullPvP plugin) {
        super("create", "Defines a new event", new String[] { "make", "define" });
        this.plugin = plugin;
        this.permission = "fullpvp.command.koth.argument." + this.getName();
        this.location = LocationFile.getConfig();
    }
    
    public String getUsage(final String label) {
        return "/" + label + ' ' + this.getName() + " <kothName>";
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
    	if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.MUST_BE_PLAYER);
            return true;
        }
    	final Player player = (Player)sender;
        if (!player.hasPermission(this.permission)) {
            player.sendMessage(ChatColor.RED + "No Permission.");
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        final StringBuilder x = new StringBuilder();
        for (int i = 1; i < args.length; ++i) {
            x.append(String.valueOf(String.valueOf(args[i])) + " ");
        }
        final String koth = x.toString().trim();
        if (this.location.getConfigurationSection("KOTHS." + koth) != null) {
            player.sendMessage(ColorText.translate("&cThis KOTH is already created."));
            return true;
        }
        final Selection sel = Utils.getWorldEdit().getSelection(player);
        if (sel == null) {
            player.sendMessage(ColorText.translate("&cYou must make a WorldEdit selection."));
            return true;
        }
        LocationFile.getConfig().set("KOTHS." + koth + ".world", sel.getMaximumPoint().getWorld().getName());
        LocationFile.getConfig().set("KOTHS." + koth + ".cornerA.x", sel.getMaximumPoint().getX());
        LocationFile.getConfig().set("KOTHS." + koth + ".cornerA.y", sel.getMaximumPoint().getY());
        LocationFile.getConfig().set("KOTHS." + koth + ".cornerA.z", sel.getMaximumPoint().getZ());
        LocationFile.getConfig().set("KOTHS." + koth + ".cornerB.x", sel.getMinimumPoint().getX());
        LocationFile.getConfig().set("KOTHS." + koth + ".cornerB.y", sel.getMinimumPoint().getY());
        LocationFile.getConfig().set("KOTHS." + koth + ".cornerB.z", sel.getMinimumPoint().getZ());
        location.save();
        location.reload();
        player.sendMessage(ChatColor.BLUE.toString() +  ChatColor.BOLD + args[1] + " KOTH " + ChatColor.WHITE + "has been created.");
        return true;
    }
}
