package net.panda.fullpvp.commands.tournaments.arguments;

import org.apache.commons.lang.WordUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.panda.fullpvp.FullPvP;
import net.panda.fullpvp.tournaments.Tournament;
import net.panda.fullpvp.tournaments.TournamentState;
import net.panda.fullpvp.tournaments.TournamentType;
import net.panda.fullpvp.utilities.ColorText;
import net.panda.fullpvp.utilities.CommandArgument;

public class TournamentStatusArgument extends CommandArgument
{
    private FullPvP plugin;
    
    public TournamentStatusArgument() {
        super("status", "Status of a tournament");
        this.plugin = FullPvP.getInstance();
        this.permission = "fullpvp.command.tournament.argument.status";
    }
    
    public String getUsage(final String label) {
        return '/' + label + ' ' + this.getName();
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        final Tournament tournament = this.plugin.getTournamentManager().getTournament();
        if(!plugin.getTournamentManager().isCreated()) {
        	sender.sendMessage(ColorText.translate("&cDo not have events running."));
        }
        if(tournament.getTournamentState() == TournamentState.FIGHTING) {
        	sender.sendMessage(ColorText.translate("&7&m----------------------------------------"));
            sender.sendMessage(ColorText.translate("&6Type&7: &f" + WordUtils.capitalizeFully(tournament.getType().name())));
            if (tournament.getType() == TournamentType.SUMO) {
                sender.sendMessage(ColorText.translate("&6Round&7: &f" + tournament.getCurrentRound()));
                sender.sendMessage(ColorText.translate("&6Current Fight:"));
                sender.sendMessage(ColorText.translate("   &2" + tournament.getFirstPlayer().getName() + " &avs &2" + tournament.getSecondPlayer().getName()));
            }
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
