package net.panda.fullpvp.commands.tournaments.arguments;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.panda.fullpvp.FullPvP;
import net.panda.fullpvp.commands.StaffModeCommand;
import net.panda.fullpvp.listener.VanishListener;
import net.panda.fullpvp.tournaments.Tournament;
import net.panda.fullpvp.tournaments.TournamentType;
import net.panda.fullpvp.tournaments.runnable.TournamentRunnable;
import net.panda.fullpvp.utilities.ColorText;
import net.panda.fullpvp.utilities.CommandArgument;
import net.panda.fullpvp.utilities.Cooldowns;
import net.panda.fullpvp.utilities.Ints;
import net.panda.fullpvp.utilities.Utils;

public class TournamentCreateArgument extends CommandArgument {
	
    private FullPvP plugin;
    @SuppressWarnings("unused")
	private String sender;
    
    public TournamentCreateArgument() {
        super("create", "Create a tournament");
        this.plugin = FullPvP.getInstance();
        this.permission = "fullpvp.command.tournament.argument.create";
    }
    
    public String getUsage(final String label) {
        return '/' + label + ' ' + this.getName() + " <maxplayers> <sumo/ffa>";
    }
    
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        final Player player = (Player)sender;
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.MUST_BE_PLAYER);
            return true;
        }
        if (Cooldowns.isOnCooldown("TOURNAMENT_COOLDOWN", player) && !player.isOp() && !player.hasPermission("tournament.cooldown.bypass")) {
            player.sendMessage(ColorText.translate("&cYou cannot do this for another &l" + (Cooldowns.getCooldownForPlayerInt("TOURNAMENT_COOLDOWN", player) / 60) + " &cminutes."));
            return true;
        }
        if(StaffModeCommand.isMod(player) || VanishListener.isVanished(player)) {
        	player.sendMessage(ColorText.translate("&cYou do not have permission to execute this command in staffmode/vanish mode"));
        	return true;
        }
        if (args.length < 3) {
            player.sendMessage(ColorText.translate("&cUsage: " + this.getUsage(label)));
        }
        else {
            if (this.plugin.getCombatTagListener().hasCooldown(player)) {
                player.sendMessage(ColorText.translate("&cYou may not host while you are in spawn-tag."));
                return true;
            }
            final Integer size = Ints.tryParse(args[1]);
            if (size == null || size < 1) {
                player.sendMessage(ColorText.translate("&cInvalid size."));
                return true;
            }
            if (size == null || size > 100) {
                player.sendMessage(ColorText.translate("&cMaximun size is 100."));
                return true;
            }
            if (this.plugin.getTournamentManager().isCreated()) {
                player.sendMessage(ColorText.translate("&cThis tournament is already created."));
                return true;
            }
            try {
                this.sender = player.getDisplayName();
                final TournamentType type = TournamentType.valueOf(args[2].toUpperCase());
                this.plugin.getTournamentManager().createTournament(player, size, type, player);
                player.performCommand("tournament join");
                for (final Player online : Bukkit.getServer().getOnlinePlayers()) {
                	String name = FullPvP.getInstance().getChat().getPlayerPrefix(player) + player.getDisplayName();
                    Tournament tournament = FullPvP.getInstance().getTournamentManager().getTournament();
                    TextComponent mensaje = new TextComponent();
                    mensaje.setText(ColorText.translate("&2&l" + type.getName() + " &fhosted by &r" + name + " &7(" + "&a" + tournament.getPlayers().size() + "&7/&a" + tournament.getSize() + "&7)" + " &a!Click to join¡"));
                    mensaje.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tour join"));
                    online.sendMessage(mensaje.getText());
                }
                new TournamentRunnable(this.plugin.getTournamentManager().getTournament()).runAnnounce();
                Cooldowns.addCooldown("TOURNAMENT_COOLDOWN", player, 1800);
            }
            catch (Exception e) {
                player.sendMessage(ColorText.translate("&cTournamentType not found."));
            }
        }
        return true;
    }
}
