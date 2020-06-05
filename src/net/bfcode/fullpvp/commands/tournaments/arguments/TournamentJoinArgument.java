package net.bfcode.fullpvp.commands.tournaments.arguments;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.bfcode.fullpvp.FullPvP;
import net.bfcode.fullpvp.commands.StaffModeCommand;
import net.bfcode.fullpvp.listener.VanishListener;
import net.bfcode.fullpvp.tournaments.Tournament;
import net.bfcode.fullpvp.tournaments.TournamentState;
import net.bfcode.fullpvp.utilities.ColorText;
import net.bfcode.fullpvp.utilities.CommandArgument;
import net.bfcode.fullpvp.utilities.Utils;

public class TournamentJoinArgument extends CommandArgument
{
    private FullPvP plugin;
    
    public TournamentJoinArgument() {
        super("join", "Join to a tournament");
        this.plugin = FullPvP.getInstance();
        this.permission = "fullpvp.command.tournament.argument.join";
    }
    
    public String getUsage(final String label) {
        return '/' + label + ' ' + this.getName();
    }
    
	@SuppressWarnings("unused")
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.MUST_BE_PLAYER);
            return true;
        }
        final Player player = (Player)sender;
        final Tournament tournament = this.plugin.getTournamentManager().getTournament();
        
        if(tournament == null) {
        	return true;
        }

        final int countdown = this.plugin.getTournamentManager().getTournament().getCooldown();
        if(StaffModeCommand.isMod(player) == true || VanishListener.isVanished(player) ==  true) {
        	player.sendMessage(ColorText.translate("&cYou cannot join with mod mode or vanish!"));
        } else {
            if (tournament != null) {
                if (this.plugin.getCombatTagListener().hasCooldown(player)) {
                    player.sendMessage(ColorText.translate("&cYou may not join while you are in spawn-tag."));
                }
                else if (this.plugin.getTournamentManager().isInTournament(player.getUniqueId())) {
                    player.sendMessage(ColorText.translate("&cYou are already in the tournament."));
                }
                else if(tournament.getPlayers().size() == tournament.getSize()) {
                	player.sendMessage(ColorText.translate("&cThe tournament is full!"));
                }
                else if(countdown == 0) {
                	player.sendMessage(ColorText.translate("&cThis tournament is already started!"));
                }
                else if (tournament.getTournamentState() == TournamentState.FIGHTING) {
                    player.sendMessage(ColorText.translate("&cThis tournament is already started!"));
                }
                else {
                    this.plugin.getTournamentManager().joinTournament(player);
                    tournament.saveInventory(player);
                    if (player.getGameMode() != GameMode.SURVIVAL) {
                        player.setGameMode(GameMode.SURVIVAL);
                    }
                    if (player.isFlying()) {
                        player.setAllowFlight(false);
                        player.setFlying(false);
                    }
                    tournament.teleport(player, "Spawn");
                    tournament.giveItemWait(player);
                }
            } else {
                player.sendMessage(ColorText.translate("&cThis tournament doesn't exist."));
                return true;
            }
        }
        return true;
    }
}
