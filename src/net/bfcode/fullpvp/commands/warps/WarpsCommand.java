package net.bfcode.fullpvp.commands.warps;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.bfcode.fullpvp.FullPvP;
import net.bfcode.fullpvp.configuration.LocationFile;
import net.bfcode.fullpvp.utilities.ColorText;
import net.bfcode.fullpvp.utilities.Utils;

public class WarpsCommand implements CommandExecutor {
	
	LocationFile location = LocationFile.getConfig();
	
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.MUST_BE_PLAYER);
            return true;
        }
        final Player player = (Player)sender;
        if (args.length < 1) {
            this.getUsage(player);
        }
        else if (args[0].equalsIgnoreCase("create")) {
        	if (!player.hasPermission("fullpvp.command.warp.argument.create")) {
        		player.sendMessage(Utils.NO_PERMISSION);
        		return true;
        	}
            if (args.length < 2) {
                player.sendMessage(ColorText.translate("&cUsage: /warp create <warpName>"));
                return true;
            }
            StringBuilder x = new StringBuilder();
            for (int i = 1; i < args.length; ++i) {
                x.append(String.valueOf(String.valueOf(args[i].toLowerCase())) + " ");
            }
            String warp = x.toString().trim();
            if (this.location.getConfigurationSection("Warps." + warp) != null) {
                player.sendMessage(ColorText.translate("&cThis Warp is already created."));
                return true;
            }
            location.set("Warps." + warp + ".X", (Object)player.getLocation().getBlockX());
            location.set("Warps." + warp + ".Y", (Object)player.getLocation().getBlockY());
            location.set("Warps." + warp + ".Z", (Object)player.getLocation().getBlockZ());
            location.set("Warps." + warp + ".world", (Object)player.getWorld().getName());
            location.save();
            location.reload();
            player.sendMessage(ColorText.translate("&aWarp " + args[1].toLowerCase() + " has been successfully created."));
            return true;
        }
        else if (args[0].equalsIgnoreCase("delete")) {
        	if (!player.hasPermission("fullpvp.command.warp.argument.delete")) {
        		player.sendMessage(Utils.NO_PERMISSION);
        		return true;
        	}
        	if (args.length < 2) {
            	player.sendMessage(ColorText.translate("&cUsage: /warp delete <warpName>"));
                return true;
            }
        	String warp = args[1].toLowerCase();
        	location.set("Warps." + warp, null);
        	location.save();
        	location.reload();
        	player.sendMessage(ColorText.translate("&cWarp " + args[1].toLowerCase() + " has been successfully deleted."));
        	return true;
        }
        else if (args[0].equalsIgnoreCase("list")) {
        	sender.sendMessage(ColorText.translate("&6&lWarps List: &f" + this.getWarpsList().toString().replace("[", "").replace("]", "")));
        	return true;
        }
        else if (args[0].equalsIgnoreCase("tp")) {
        	if (args.length < 2) {
                player.sendMessage(ColorText.translate("&cUsage: /warp tp <warpName>"));
                return true;
            }
        	if (FullPvP.getPlugin().getCombatTagListener().hasCooldown(player)) {
                player.sendMessage(ColorText.translate("&cYou can't teleport to this warp if you have &4&lCombat Tag&c."));
                return true;
            }
        	String warp = args[1].toLowerCase();
            int x = location.getInt("Warps." + warp + ".X");
            int y = location.getInt("Warps." + warp + ".Y");
            int z = location.getInt("Warps." + warp + ".Z");
            String world = location.getString("Warps." + warp + ".world");
            Location locations = new Location(Bukkit.getWorld(world), (double)x, (double)y, (double)z);
            player.teleport(locations);
            player.sendMessage(ColorText.translate("&eSuccessfully teleported to warp &6" + warp + "&e!"));
        	return true;
        } else {
            player.sendMessage(ColorText.translate("&cWarp sub-command '" + args[0] + "' not found."));
            return true;
        }
        return true;
    }
    
    private void getUsage(final CommandSender sender) {
        sender.sendMessage(ColorText.translate("&8&m------------------------------"));
        sender.sendMessage(ColorText.translate("&6&lWarp Commands"));
        sender.sendMessage(ColorText.translate(""));
        if (sender.hasPermission("fullpvp.command.warp.bypass")) {
        	sender.sendMessage(ColorText.translate("&e/warp create <warpName> &7- &fCreate a warp location."));
            sender.sendMessage(ColorText.translate("&e/warp delete <warpName> &7- &fDelete a warp location."));
        }
        sender.sendMessage(ColorText.translate("&e/warp list &7- &fShow all warps."));
        sender.sendMessage(ColorText.translate("&e/warp tp <warpName> &7- &fTeleport to this warp."));
        sender.sendMessage(ColorText.translate("&8&m------------------------------"));
    }
    
    private Set<String> getWarpsList() {
        return (Set<String>)this.location.getConfigurationSection("Warps").getKeys(false);
    }
}
