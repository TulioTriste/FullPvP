package net.bfcode.fullpvp.commands.tournaments.arguments;

import org.apache.commons.lang.WordUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.bfcode.fullpvp.FullPvP;
import net.bfcode.fullpvp.tournaments.Tournament;
import net.bfcode.fullpvp.tournaments.TournamentState;
import net.bfcode.fullpvp.tournaments.TournamentType;
import net.bfcode.fullpvp.utilities.ColorText;
import net.bfcode.fullpvp.utilities.CommandArgument;

public class TournamentStatusArgument extends CommandArgument
{
    private FullPvP plugin;
    
    public TournamentStatusArgument() {
        super("status", "Status of a tournament");
        this.plugin = FullPvP.getPlugin();
        this.permission = "tournament.default";
    }
    
    public String getUsage(final String label) {
        return '/' + label + ' ' + this.getName();
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        final Tournament tournament = this.plugin.getTournamentManager().getTournament();
        if(tournament.getPlayers().size() == 0) {
        	sender.sendMessage(ColorText.translate("&cDo not have events running."));
        }
        if(tournament.getTournamentState() == TournamentState.FIGHTING) {
        	sender.sendMessage(ColorText.translate("&7&m----------------------------------------"));
            sender.sendMessage(ColorText.translate("&6Type&7: &f" + WordUtils.capitalizeFully(tournament.getType().name())));
            sender.sendMessage(ColorText.translate("&6Round&7: &f" + tournament.getCurrentRound()));
            if (tournament.getType() == TournamentType.SUMO) {
                sender.sendMessage(ColorText.translate("&6Current Fight:"));
                sender.sendMessage(ColorText.translate("   &2" + tournament.getFirstPlayer().getName() + " &avs &2" + tournament.getSecondPlayer().getName()));
            }
            sender.sendMessage(ColorText.translate("&6Next Round&7: &f" + (tournament.getCurrentRound() + 1)));
            sender.sendMessage(ColorText.translate("&6Players&7: &f" + tournament.getPlayers().size() + "&7/&f" + tournament.getSize()));
            sender.sendMessage(ColorText.translate("&6Hoster&7: &f" + tournament.getHoster().getName()));
        	sender.sendMessage(ColorText.translate("&7&m----------------------------------------"));
        }
        else if(tournament.getTournamentState() == TournamentState.STARTING) {
        	sender.sendMessage(ColorText.translate("&7&m----------------------------------------"));
        	sender.sendMessage(ColorText.translate("&6The event is Starting..."));
        	sender.sendMessage("");
        	sender.sendMessage(ColorText.translate("&6Event&7: &7(&e" + tournament.getType().toString() + "&7)"));
        	sender.sendMessage(ColorText.translate("&6Players&7: &7" + tournament.getPlayers().size() + "/" + tournament.getSize()));
        	sender.sendMessage(ColorText.translate("&7&m----------------------------------------"));
        }
        return true;
    }
}
