package net.panda.fullpvp.utilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class CommandWrapper implements CommandExecutor, TabCompleter {
    private Collection<CommandArgument> arguments;
    
    public CommandWrapper(Collection<CommandArgument> arguments) {
        this.arguments = arguments;
    }
    
    public static void printUsage(CommandSender sender, String label, Collection<CommandArgument> arguments) {
        sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        sender.sendMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + WordUtils.capitalizeFully(label) + " Help");
        Integer amount = 0;
        for (CommandArgument argument : arguments) {
            String permission = argument.getPermission();
            if (permission != null && !sender.hasPermission(permission)) {
                continue;
            }
            sender.sendMessage(ChatColor.YELLOW + argument.getUsage(label) + ChatColor.GRAY + " - " + ChatColor.WHITE + argument.getDescription());
            ++amount;
        }
        if (amount > 0) {
            sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        }
    }
    
    public static CommandArgument matchArgument(String id, CommandSender sender, Collection<CommandArgument> arguments) {
        for (CommandArgument argument : arguments) {
            String permission = argument.getPermission();
            if (permission == null || sender.hasPermission(permission)) {
                if (!argument.getName().equalsIgnoreCase(id) && !Arrays.asList(argument.getAliases()).contains(id)) {
                    continue;
                }
                return argument;
            }
        }
        return null;
    }
    
    public static List<String> getAccessibleArgumentNames(CommandSender sender, Collection<CommandArgument> arguments) {
        ArrayList<String> results = new ArrayList<String>();
        for (CommandArgument argument : arguments) {
            String permission = argument.getPermission();
            if (permission != null && !sender.hasPermission(permission)) {
                continue;
            }
            results.add(argument.getName());
        }
        return results;
    }
    
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            printUsage(sender, label, this.arguments);
            return true;
        }
        CommandArgument argument = matchArgument(args[0], sender, this.arguments);
        if (argument == null) {
            printUsage(sender, label, this.arguments);
            return true;
        }
        return argument.onCommand(sender, command, label, args);
    }
    
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return Collections.emptyList();
        }
        List<String> results;
        if (args.length == 1) {
            results = getAccessibleArgumentNames(sender, this.arguments);
        }
        else {
            CommandArgument argument = matchArgument(args[0], sender, this.arguments);
            if (argument == null) {
                return Collections.emptyList();
            }
            results = argument.onTabComplete(sender, command, label, args);
            if (results == null) {
                return null;
            }
        }
        return BukkitUtils.getCompletions(args, results);
    }
    
    @SuppressWarnings("serial")
	public static class ArgumentComparator implements Comparator<CommandArgument>, Serializable {
    	
        @Override
        public int compare(CommandArgument primaryArgument, CommandArgument secondaryArgument) {
            return secondaryArgument.getName().compareTo(primaryArgument.getName());
        }
    }
}
