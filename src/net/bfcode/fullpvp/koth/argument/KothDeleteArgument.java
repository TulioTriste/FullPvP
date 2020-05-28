package net.bfcode.fullpvp.koth.argument;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.bfcode.fullpvp.FullPvP;
import net.bfcode.fullpvp.configuration.LocationFile;
import net.bfcode.fullpvp.utilities.ColorText;
import net.bfcode.fullpvp.utilities.CommandArgument;
import net.bfcode.fullpvp.utilities.Utils;

public class KothDeleteArgument extends CommandArgument implements CommandExecutor {

	LocationFile location;
	private static String koth;
	
    @SuppressWarnings("unused")
	private final FullPvP plugin;
    
    public KothDeleteArgument(final FullPvP plugin) {
        super("delete", "Delete a KOTH", new String[] { "remove" });
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
        koth = args[1];
        this.setKothLocation(player);
        player.sendMessage(ColorText.translate("&aKOTH location has been successfully set."));
        player.playSound(player.getLocation(), Sound.EXPLODE, 1.0f, 1.0f);
        return true;
    }
        
    private void setKothLocation(final Player player) {
    	
        final LocationFile location = LocationFile.getConfig();
        location.set("KOTHS." + koth + ".X", (Object)player.getLocation().getBlockX());
        location.set("KOTHS." + koth + ".Y", (Object)player.getLocation().getBlockY());
        location.set("KOTHS." + koth + ".Z", (Object)player.getLocation().getBlockZ());
        location.save();
        location.reload();
    }
}
