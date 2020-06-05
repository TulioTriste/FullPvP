package net.bfcode.fullpvp.commands.tournaments.arguments;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.bfcode.fullpvp.FullPvP;
import net.bfcode.fullpvp.tournaments.Tournament;
import net.bfcode.fullpvp.utilities.ColorText;
import net.bfcode.fullpvp.utilities.CommandArgument;
import net.bfcode.fullpvp.utilities.Utils;

public class TournamentLeaveArgument extends CommandArgument
{
    private FullPvP plugin;
    
    public TournamentLeaveArgument() {
        super("leave", "Leave from the tournament");
        this.plugin = FullPvP.getInstance();
        this.permission = "fullpvp.command.tournament.argument.leave";
    }
    
    public String getUsage(final String label) {
        return '/' + label + ' ' + this.getName();
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.NO_PERMISSION);
            return true;
        }
        final Player player = (Player)sender;
        final Tournament tournament = this.plugin.getTournamentManager().getTournament();
        if (tournament != null) {
            if (!this.plugin.getTournamentManager().isInTournament(player.getUniqueId())) {
                player.sendMessage(ColorText.translate("&cYou are not playing in the tournament."));
            }
            else if (tournament.getHoster().getName().equalsIgnoreCase(player.getName())) {
                player.sendMessage(ColorText.translate("&cYou cannot leave from your tournament!"));
            }else{
            	this.plugin.getTournamentManager().leaveTournament(player);
            }
        }
        else {
            player.sendMessage(ColorText.translate("&cThis tournament doesn't exist."));
        }
        return true;
    }
}
