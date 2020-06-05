package net.bfcode.fullpvp.utilities;

import java.util.stream.Collectors;
import com.google.common.base.Preconditions;
import java.util.List;
import org.bukkit.Bukkit;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import net.bfcode.fullpvp.configuration.MessagesFile;

import org.bukkit.ChatColor;
import java.text.DecimalFormat;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;

public class Utils {

    public static String NO_PERMISSION;
    public static String MUST_BE_PLAYER;
    public static String PLAYER_NOT_FOUND;
    
    public static boolean isOnline(final CommandSender sender, final Player player) {
        return player != null && (!(sender instanceof Player) || ((Player)sender).canSee(player));
    }
    
    public static String formatLongMin(final long time) {
        final long totalSecs = time / 1000L;
        return String.format("%02d:%02d", totalSecs / 60L, totalSecs % 60L);
    }
    
    public static String formatIntMin(final int time) {
        return String.format("%02d:%02d", time / 60, time % 60);
    }
    
    public static DecimalFormat getDecimalFormat() {
        return new DecimalFormat("0.0");
    }
    
    public static DecimalFormat getNormalFormat() {
        return new DecimalFormat("0");
    }
    
    public static String format(final double tps) {
        return ((tps > 18.0) ? ChatColor.GREEN : ((tps > 16.0) ? ChatColor.YELLOW : ChatColor.RED)).toString() + ((tps > 20.0) ? "*" : "") + Math.min(Math.round(tps * 100.0) / 100.0, 20.0);
    }
    
    public static WorldEditPlugin getWorldEdit() {
        return (WorldEditPlugin)Bukkit.getPluginManager().getPlugin("WorldEdit");
    }
    
    public static List<String> getCompletions(final String[] args, final List<String> input) {
        return getCompletions(args, input, 80);
    }
    
    @SuppressWarnings({ "unused", "null" })
	public static List<String> getCompletions(final String[] args, final List<String> input, final int limit) {
        Preconditions.checkNotNull((Object)args);
        Preconditions.checkArgument(args.length != 0);
        final String argument = args[args.length - 1];
        final String s = null;
        return input.stream().filter(string -> string.regionMatches(true, 0, s, 0, s.length())).limit(limit).collect(Collectors.toList());
    }
    
    static {
    	MessagesFile messages = MessagesFile.getConfig();
        Utils.NO_PERMISSION = ColorText.translate(messages.getString("No-Permission"));
        Utils.MUST_BE_PLAYER = ColorText.translate("&cYou must be player to execute this command.");
        Utils.PLAYER_NOT_FOUND = ColorText.translate("&cPlayer not found.");
    }
}
