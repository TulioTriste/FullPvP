package net.bfcode.fullpvp.commands;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import net.bfcode.fullpvp.FullPvP;
import net.bfcode.fullpvp.configuration.MessagesFile;
import net.bfcode.fullpvp.utilities.ColorText;
import net.bfcode.fullpvp.utilities.Utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class StatsCommand implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.MUST_BE_PLAYER);
            return true;
        }
        final Player player = (Player)sender;
        if (args.length < 1) {
            this.getPlayerStats(player, player);
        }
        else {
            final Player target = Bukkit.getPlayer(args[0]);
            if (!Utils.isOnline((CommandSender)player, target)) {
                Utils.PLAYER_NOT_FOUND((CommandSender)player, args[0]);
                return true;
            }
            Bukkit.getScheduler().runTaskAsynchronously(FullPvP.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    StatsCommand.this.getPlayerStats(player, target);
                }
            });
        }
        return true;
    }
    
    private void getPlayerStats(final Player player, final Player target) {
    	MessagesFile message = MessagesFile.getConfig();
    	for(String msg : message.getStringList("stats-command")) {
    		player.sendMessage(ColorText.translate(msg
    				.replace("{player}", target.getName())
    				.replace("{kills}", target.getStatistic(Statistic.PLAYER_KILLS) + "")
    				.replace("{deaths}", target.getStatistic(Statistic.DEATHS) + "")
    				.replace("{balance}", FullPvP.getPlugin().getEconomyManager().getBalance(target.getUniqueId()) + "")));
    	}
    }
}
