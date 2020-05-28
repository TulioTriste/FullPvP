package net.bfcode.fullpvp.commands.tournaments.arguments;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.bfcode.fullpvp.FullPvP;
import net.bfcode.fullpvp.tournaments.file.TournamentFile;
import net.bfcode.fullpvp.utilities.ColorText;
import net.bfcode.fullpvp.utilities.CommandArgument;
import net.bfcode.fullpvp.utilities.LocationUtils;

public class TournamentSetArgument extends CommandArgument {
    private TournamentFile file; 
    
    public TournamentSetArgument() {
        super("set", "Set locations of the tournament");
        this.plugin = FullPvP.getPlugin();
        this.permission = "tournament.admin";
        this.file = TournamentFile.getConfig();
    }
    
    public String getUsage(final String label) {
        return '/' + label + ' ' + this.getName() + " <spawn|sumo|ffa>";
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ColorText.translate("&cYou must be player to execute this command."));
            return true;
        }
        final Player player = (Player)sender;
        if (args.length < 2) {
            player.sendMessage(ColorText.translate("&cUsage: " + this.getUsage(label)));
        }
        else if (args[1].equalsIgnoreCase("spawn")) {
            this.file.set("Locations.Spawn", LocationUtils.getString(player.getLocation()));
            this.file.save();
            this.file.reload();
            player.sendMessage(ColorText.translate("&6&lTournament &8* &aSpawn location saved."));
        }
        else if (args[1].equalsIgnoreCase("sumo")) {
            if (args.length < 3) {
                player.sendMessage(ColorText.translate("&cUsage: /" + label + ' ' + this.getName() + " sumo <spawn|first|second>"));
                return true;
            }
            if (args[2].equalsIgnoreCase("spawn")) {
                this.file.set("Locations.Sumo.Spawn", LocationUtils.getString(player.getLocation()));
                this.file.save();
                this.file.reload();
                player.sendMessage(ColorText.translate("&6&lTournament &8* &aSumo Spawn location saved."));
            }
            else if (args[2].equalsIgnoreCase("first")) {
                this.file.set("Locations.Sumo.First", LocationUtils.getString(player.getLocation()));
                this.file.save();
                this.file.reload();
                player.sendMessage(ColorText.translate("&6&lTournament &8* &aSumo First location saved."));
            }
            else if (args[2].equalsIgnoreCase("second")) {
                this.file.set("Locations.Sumo.Second", LocationUtils.getString(player.getLocation()));
                this.file.save();
                this.file.reload();
                player.sendMessage(ColorText.translate("&6&lTournament &8* &aSumo Second location saved."));
            }
            else {
                player.sendMessage(ColorText.translate("&cTournament sub-command '" + args[2] + "' not found."));
            }
        }
        else if (args[1].equalsIgnoreCase("ffa")) {
            if (args.length < 3) {
                player.sendMessage(ColorText.translate("&cUsage: /" + label + ' ' + this.getName() + " ffa <spawn>"));
                return true;
            }
            if (args[2].equalsIgnoreCase("spawn")) {
                this.file.set("Locations.FFA.Spawn", LocationUtils.getString(player.getLocation()));
                this.file.save();
                this.file.reload();
                player.sendMessage(ColorText.translate("&6&lTournament &8* &aFFA Spawn location saved."));
            }
            else {
                player.sendMessage(ColorText.translate("&cTournament sub-command '" + args[2] + "' not found."));
            }
        } else {
            player.sendMessage(ColorText.translate("&cTournament sub-command '" + args[1] + "' not found."));
        }
        return true;
    }
}
