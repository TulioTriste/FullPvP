package net.bfcode.fullpvp.commands.essentials;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.bfcode.fullpvp.utilities.BaseConstants;
import net.bfcode.fullpvp.utilities.BukkitUtils;
import net.bfcode.fullpvp.utilities.ColorText;
import net.bfcode.fullpvp.utilities.Utils;

public class GamemodeCommand implements CommandExecutor {
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
    	if (!sender.hasPermission("fullpvp.command.gamemode")) {
            sender.sendMessage(ColorText.translate("&cYou don't have permission to execute this command."));
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <modeName> [playerName]");
            return true;
        }
        final GameMode mode = this.getGameModeByName(args[0]);
        if (mode == null) {
            sender.sendMessage(ChatColor.RED + "Gamemode '" + args[0] + "' not found.");
            return true;
        }
        Player target;
        if (args.length > 1) {
            target = (sender.hasPermission("fullpvp.command.gamemode.others") ? BukkitUtils.playerWithNameOrUUID(args[1]) : null);
        }
        else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Utils.MUST_BE_PLAYER);
                return true;
            }
            target = (Player)sender;
        }
        if (target == null) {
            sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[1]));
            return true;
        }
        if (target.getGameMode() == mode) {
            sender.sendMessage(ChatColor.RED + "Gamemode of " + target.getName() + " is already " + mode.name() + '.');
            return true;
        }
        target.setGameMode(mode);
        Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Set gamemode of " + target.getName() + " to " + ChatColor.YELLOW + mode.name().toString().toLowerCase() + ChatColor.YELLOW + '.');
        return true;
    }
    
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length != 1) {
            return Collections.emptyList();
        }
        final GameMode[] gameModes = GameMode.values();
        final ArrayList<String> results = new ArrayList<String>(gameModes.length);
        for (final GameMode mode : gameModes) {
            results.add(mode.name());
        }
        return BukkitUtils.getCompletions(args, results);
    }
    
    private GameMode getGameModeByName(String id) {
        if ((id = id.toLowerCase(Locale.ENGLISH)).equalsIgnoreCase("gmc") || id.contains("creat") || id.equalsIgnoreCase("1") || id.equalsIgnoreCase("c")) {
            return GameMode.CREATIVE;
        }
        if (id.equalsIgnoreCase("gms") || id.contains("survi") || id.equalsIgnoreCase("0") || id.equalsIgnoreCase("s")) {
            return GameMode.SURVIVAL;
        }
        if (id.equalsIgnoreCase("gma") || id.contains("advent") || id.equalsIgnoreCase("2") || id.equalsIgnoreCase("a")) {
            return GameMode.ADVENTURE;
        }
        if (id.equalsIgnoreCase("gmt") || id.contains("toggle") || id.contains("cycle") || id.equalsIgnoreCase("t")) {
            return null;
        }
        return null;
    }
}
