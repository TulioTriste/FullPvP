package net.galanthus.fullpvp.commands.tournaments.arguments;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.galanthus.fullpvp.FullPvP;
import net.galanthus.fullpvp.tournaments.Tournament;
import net.galanthus.fullpvp.utilities.ColorText;
import net.galanthus.fullpvp.utilities.CommandArgument;

public class TournamentCancelArgument extends CommandArgument {
	
    private FullPvP plugin;
    
    public TournamentCancelArgument() {
        super("cancel", "Cancel the current tournament");
        this.plugin = FullPvP.getPlugin();
        this.permission = "tournament.default";
    }
    
    public String getUsage(final String label) {
        return '/' + label + ' ' + this.getName();
    }
    
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ColorText.translate("&cYou must be player to execute this command."));
            return true;
        }
        final Player player = (Player)sender;
        if (args.length < 1) {
            player.sendMessage(ColorText.translate("&cUsage: " + this.getUsage(label)));
        }
        else {
            if (!this.plugin.getTournamentManager().isCreated()) {
                player.sendMessage(ColorText.translate("&cThis tournament doesn't exist."));
                return true;
            }
            if (this.plugin.getTournamentManager().getTournament().getHoster() != player) {
                player.sendMessage(ColorText.translate("&cYou must be host to cancel the tournament."));
                return true;
            }
            final Tournament tournament = this.plugin.getTournamentManager().getTournament();
            if (tournament != null) {
                this.plugin.getTournamentManager().setCreated(false);
                for (final Player online : Bukkit.getServer().getOnlinePlayers()) {
                    if (this.plugin.getTournamentManager().isInTournament(online.getUniqueId())) {
                        tournament.rollbackInventory(online);
                        this.plugin.getTournamentManager().kickPlayer(online.getUniqueId());
                        online.sendMessage(ColorText.translate("&c&lYou were kicked from the tournament for&7: &fCancelled"));
                        online.teleport(Bukkit.getWorld("Mapa").getSpawnLocation());
                    }
                }
            }
            else {
                player.sendMessage(ColorText.translate("&cThis tournament doesn't exist."));
            }
        }
        return true;
    }
}
