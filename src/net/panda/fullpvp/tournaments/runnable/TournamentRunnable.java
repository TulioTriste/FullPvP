package net.panda.fullpvp.tournaments.runnable;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.panda.fullpvp.FullPvP;
import net.panda.fullpvp.configuration.MessagesFile;
import net.panda.fullpvp.tournaments.Tournament;
import net.panda.fullpvp.tournaments.TournamentState;
import net.panda.fullpvp.utilities.ColorText;
import net.panda.fullpvp.utilities.PlayerUtil;

import org.bukkit.entity.Player;
import java.util.UUID;

public class TournamentRunnable {

    private final FullPvP plugin;
    private Tournament tournament;

    public TournamentRunnable(final Tournament tournament) {
        this.plugin = FullPvP.getInstance();
        this.tournament = tournament;
    }

	public Player getPlayerByUuid(final UUID uuid) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online.getUniqueId().equals(uuid)) {
                return online;
            }
        }
        throw new IllegalArgumentException();
    }

    public void startSumo() {
        new BukkitRunnable() {
            public void run() {
                if (!TournamentRunnable.this.plugin.getTournamentManager().isCreated()) {
                    this.cancel();
                    return;
                }
                if (TournamentRunnable.this.plugin.getTournamentManager().getPlayers().isEmpty()) {
                    this.cancel();
                    return;
                }
                if (TournamentRunnable.this.tournament.getTournamentState() == TournamentState.STARTING || TournamentRunnable.this.tournament.getTournamentState() == TournamentState.FIGHTING) {
                    final int countdown = TournamentRunnable.this.tournament.decrementCountdown();
                    TournamentRunnable.this.searchPlayers();
                    final Player first = TournamentRunnable.this.tournament.getFirstPlayer();
                    final Player second = TournamentRunnable.this.tournament.getSecondPlayer(); 
                    if(countdown == 5) {
                        TournamentRunnable.this.tournament.teleport(TournamentRunnable.this.tournament.getFirstPlayer(), "Sumo.First");
                        TournamentRunnable.this.tournament.teleport(TournamentRunnable.this.tournament.getSecondPlayer(), "Sumo.Second");
                        PlayerUtil.startingSumo(first);
                        PlayerUtil.startingSumo(second);
                    } else if(countdown == 0) {
                        for (final UUID players : TournamentRunnable.this.tournament.getPlayers()) {
                            try {
                                final Player player = TournamentRunnable.this.getPlayerByUuid(players);
                                player.sendMessage(ColorText.translate(MessagesFile.getConfig().getString("Tournament-messages.Sumo.Starting-Fight").replace("{first}", first.getName()).replace("{second}", second.getName())));
                            } catch (IllegalArgumentException ex) {
                            }
                        }
                        PlayerUtil.fightingSumo(first);;
                        PlayerUtil.fightingSumo(second);
                        TournamentRunnable.this.tournament.setTournamentState(TournamentState.FIGHTING);
                    }
                    else if ((countdown % 5 == 0 || countdown < 5) && countdown > 0) {
                        TournamentRunnable.this.tournament.broadcastWithSound(ColorText.translate(MessagesFile.getConfig().getString("Tournament-messages.Sumo.Starting-Round").replace("{countdown}", countdown + "")), Sound.CLICK);
                        if (countdown == 1) {
                            first.getInventory().clear();
                            second.getInventory().clear();
                        }
                    } else if (countdown < 0) {
                        this.cancel();
                    }
                }
            }
        }.runTaskTimer(this.plugin, 20L, 20L);
    }

    public void runAnnounce() {
        new BukkitRunnable() {
			public void run() {
                if (!TournamentRunnable.this.plugin.getTournamentManager().isCreated()) {
                    this.cancel();
                    return;
                }
                if (TournamentRunnable.this.plugin.getTournamentManager().getPlayers().isEmpty()) {
                    this.cancel();
                    return;
                }
                if (TournamentRunnable.this.tournament != null && TournamentRunnable.this.tournament.getTournamentState() == TournamentState.WAITING) {
                    final int countdown = TournamentRunnable.this.tournament.decrementAnnounce();
                    if (countdown == 0) {
                        Bukkit.broadcastMessage(ColorText.translate("&2&l" + TournamentRunnable.this.tournament.getType().getName() + " &fhas started with " + TournamentRunnable.this.tournament.getPlayers().size() + " players."));
                    }
                    else if ((countdown % 10 == 0 || countdown < 5) && countdown > 0) {
                    	Tournament tournament = FullPvP.getInstance().getTournamentManager().getTournament();
                    	Player player = TournamentRunnable.this.tournament.getHoster();
                    	String name = FullPvP.getInstance().getChat().getPlayerPrefix(player) + player.getDisplayName();
                    	TextComponent mensaje = new TextComponent();
                    	mensaje.setText(ColorText.translate("&2&l" + TournamentRunnable.this.tournament.getType().getName() + " &fhosted by &r" + name + " "+ "&fstarting in " + countdown + " second" + ((countdown == 1) ? "" : "s") + " &7(" + "&a" + tournament.getPlayers().size() + "&7/&a" + tournament.getSize() + "&7)" + " &a!Click to join�"));
                    	mensaje.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tour join"));
                    	for (final Player online : Bukkit.getServer().getOnlinePlayers()) {
                    		online.sendMessage(mensaje);
                    	}
                    }
                    else if (countdown < 0) {
                        if (TournamentRunnable.this.tournament.getPlayers().size() < 2) {
                            TournamentRunnable.this.plugin.getTournamentManager().setCreated(false);
                            for (final Player online2 : Bukkit.getServer().getOnlinePlayers()) {
                                if (TournamentRunnable.this.plugin.getTournamentManager().isInTournament(online2.getUniqueId())) {
                                    TournamentRunnable.this.tournament.rollbackInventory(online2);
                                    TournamentRunnable.this.plugin.getTournamentManager().kickPlayer(online2.getUniqueId());
                                    online2.sendMessage(ColorText.translate("&c&lYou were kicked from the tournament for&7: &fThe event need more players"));
                                    online2.teleport(Bukkit.getWorld("World").getSpawnLocation());
                                }
                            }
                        }
                        else {
                            TournamentRunnable.this.plugin.getTournamentManager().forceStart();
                        }
                        this.cancel();
                    }
                }
            }
        }.runTaskTimer(this.plugin, 20L, 20L);
    }

    public void searchPlayers() {
        final List<Player> players = new ArrayList<Player>();
        if (!players.isEmpty()) {
            players.clear();
        }
        for (final Player online : Bukkit.getServer().getOnlinePlayers()) {
            if (this.plugin.getTournamentManager().isInTournament(online.getUniqueId())) {
                players.add(online);
            }
        }
        Collections.shuffle(players);
        if (players.size() > 1) {
            this.tournament.setFirstPlayer(players.get(0));
            this.tournament.setSecondPlayer(players.get(1));
        }
    }
}
