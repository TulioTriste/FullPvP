package net.galanthus.fullpvp.commands;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import net.galanthus.fullpvp.FullPvP;
import net.galanthus.fullpvp.utilities.ColorText;
import net.galanthus.fullpvp.utilities.Utils;

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
        player.sendMessage(ColorText.translate("&7&m--------------------------------"));
        player.sendMessage(ColorText.translate("&6" + target.getName() + " &eStatistic"));
        player.sendMessage("");
        player.sendMessage(ColorText.translate("&8 &eKills&7: &f" + target.getStatistic(Statistic.PLAYER_KILLS)));
        player.sendMessage(ColorText.translate("&8 &eDeaths&7: &f" + target.getStatistic(Statistic.DEATHS)));
        player.sendMessage(ColorText.translate("&8 &eMoney&7: &2$" + FullPvP.getPlugin().getEconomyManager().getBalance(target.getUniqueId())));
        player.sendMessage(ColorText.translate("&7&m--------------------------------"));
    }
}
