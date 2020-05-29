package net.bfcode.fullpvp.balance;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.google.common.collect.ImmutableList;

import net.bfcode.fullpvp.FullPvP;
import net.bfcode.fullpvp.utilities.BaseConstants;
import net.bfcode.fullpvp.utilities.BukkitUtils;
import net.bfcode.fullpvp.utilities.JavaUtil;
import net.bfcode.fullpvp.utilities.JavaUtils;

import org.bukkit.command.TabCompleter;
import org.bukkit.command.CommandExecutor;

public class EconomyCommand implements CommandExecutor, TabCompleter {
	
    private static final ImmutableList<String> COMPLETIONS;
    private static final ImmutableList<String> GIVE;
    private static final ImmutableList<String> TAKE;
    private final FullPvP plugin;
    
    public EconomyCommand(final FullPvP plugin) {
        this.plugin = plugin;
    }
    
    @SuppressWarnings({ "deprecation", "unlikely-arg-type" })
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        final boolean hasStaffPermission = sender.hasPermission(command.getPermission() + ".staff");
        OfflinePlayer target;
        if (args.length > 0 && hasStaffPermission) {
            target = BukkitUtils.offlinePlayerWithNameOrUUID(args[0]);
        }
        else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <playerName>");
                return true;
            }
            target = (OfflinePlayer)sender;
        }
        if (!target.hasPlayedBefore() && !target.isOnline()) {
            sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
            return true;
        }
        final UUID uuid = target.getUniqueId();
        final int balance = this.plugin.getEconomyManager().getBalance(uuid);
        if (args.length < 2 || !hasStaffPermission) {
            sender.sendMessage(ChatColor.YELLOW + (sender.equals(target) ? "Your balance" : ("Balance of " + target.getName())) + " is " + ChatColor.LIGHT_PURPLE + '$' + balance + ChatColor.YELLOW + '.');
            return true;
        }
        if (EconomyCommand.GIVE.contains(args[1].toLowerCase())) {
            if (args.length < 3) {
                sender.sendMessage(ChatColor.RED + "Usage: /" + label + ' ' + target.getName() + ' ' + args[1] + " <amount>");
                return true;
            }
            final Integer amount = JavaUtil.tryParseInt(args[2]);
            if (amount == null) {
                sender.sendMessage(ChatColor.RED + "'" + args[2] + "' is not a valid number.");
                return true;
            }
            final int newBalance = this.plugin.getEconomyManager().addBalance(uuid, amount);
            sender.sendMessage(new String[] { ChatColor.YELLOW + "Added " + ChatColor.LIGHT_PURPLE + '$' + JavaUtils.format((Number)amount) + ChatColor.YELLOW + " to balance of " + target.getName() + '.', 
            		ChatColor.YELLOW + "Balance of " + target.getName() + " is now " + ChatColor.LIGHT_PURPLE + '$' + newBalance + ChatColor.YELLOW + '.' });
            return true;
        }
        else if (EconomyCommand.TAKE.contains(args[1].toLowerCase())) {
            if (args.length < 3) {
                sender.sendMessage(ChatColor.RED + "Usage: /" + label + ' ' + target.getName() + ' ' + args[1] + " <amount>");
                return true;
            }
            final Integer amount = JavaUtil.tryParseInt(args[2]);
            if (amount == null) {
                sender.sendMessage(ChatColor.RED + "'" + args[2] + "' is not a valid number.");
                return true;
            }
            final int newBalance = this.plugin.getEconomyManager().subtractBalance(uuid, amount);
            sender.sendMessage(new String[] { ChatColor.YELLOW + "Taken " + ChatColor.LIGHT_PURPLE + '$' + JavaUtils.format((Number)amount) + ChatColor.YELLOW + " from balance of " + target.getName() + '.', 
            		ChatColor.YELLOW + "Balance of " + target.getName() + " is now " + ChatColor.LIGHT_PURPLE + '$' + newBalance + ChatColor.YELLOW + '.' });
            return true;
        }
        else {
            if (!args[1].equalsIgnoreCase("set")) {
            	sender.sendMessage(ChatColor.YELLOW + (sender.equals(target) ? "Your balance" : ("Balance of " + target.getName())) + " is " + ChatColor.LIGHT_PURPLE + '$' + balance + ChatColor.YELLOW + '.');
                return true;
            }
            if (args.length < 3) {
                sender.sendMessage(ChatColor.RED + "Usage: /" + label + ' ' + target.getName() + ' ' + args[1] + " <amount>");
                return true;
            }
            final Integer amount = JavaUtil.tryParseInt(args[2]);
            if (amount == null) {
                sender.sendMessage(ChatColor.RED + "'" + args[2] + "' is not a valid number.");
                return true;
            }
            final int newBalance = this.plugin.getEconomyManager().setBalance(uuid, amount);
            sender.sendMessage(ChatColor.YELLOW + "Set balance of " + target.getName() + " to " + ChatColor.LIGHT_PURPLE + '$' + JavaUtils.format((Number)newBalance) + ChatColor.YELLOW + '.');
            return true;
        }
    }
    
    public List<String> onTabComplete(final CommandSender commandSender, final Command command, final String s, final String[] args) {
        return (args.length == 2) ? BukkitUtils.getCompletions(args, (List<String>)EconomyCommand.COMPLETIONS) : Collections.emptyList();
    }
    
    static {
        TAKE = ImmutableList.of("take", "negate", "minus", "subtract");
        GIVE = ImmutableList.of("give", "add");
        COMPLETIONS = ImmutableList.of("add", "set", "take");
    }
}
