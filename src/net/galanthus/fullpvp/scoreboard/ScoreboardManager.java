package net.galanthus.fullpvp.scoreboard;

import org.bukkit.Statistic;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import net.galanthus.fullpvp.FullPvP;
import net.galanthus.fullpvp.clans.ClanHandler;
import net.galanthus.fullpvp.commands.StaffModeCommand;
import net.galanthus.fullpvp.commands.essentials.FreezeCommand;
import net.galanthus.fullpvp.configuration.PointsFile;
import net.galanthus.fullpvp.destroythecore.DTCHandler;
import net.galanthus.fullpvp.listener.VanishListener;
import net.galanthus.fullpvp.tournaments.Tournament;
import net.galanthus.fullpvp.tournaments.TournamentManager;
import net.galanthus.fullpvp.tournaments.TournamentState;
import net.galanthus.fullpvp.tournaments.TournamentType;
import net.galanthus.fullpvp.utilities.ColorText;
import net.galanthus.fullpvp.utilities.Utils;

import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.WeakHashMap;
import org.bukkit.event.Listener;

public class ScoreboardManager implements Listener {
    public WeakHashMap<Player, ScoreboardAPI> helper;
    private static String bars;
    private String scoreboardTitle;
    private FullPvP plugin;
    private Configuration config;
    
    public ScoreboardManager() {
        this.plugin = FullPvP.getPlugin();
        this.config = (Configuration)this.plugin.getConfig();
        this.helper = new WeakHashMap<Player, ScoreboardAPI>();
    }
    
    public ScoreboardAPI getScoreboardFor(final Player player) {
        return this.helper.get(player);
    }
    
    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        event.setQuitMessage((String)null);
        this.helper.remove(player);
    }
    
    @EventHandler
    public void onPlayerKick(final PlayerKickEvent event) {
        final Player player = event.getPlayer();
        this.helper.remove(player);
    }
    
	@EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        event.setJoinMessage((String)null);
        event.setJoinMessage((String)null);
        this.registerScoreboard(player);
        for (final Player online : Bukkit.getServer().getOnlinePlayers()) {
            this.resendTabList(online);
        }
    }
    
    public void unregister(final Scoreboard board, final String name) {
        final Team team = board.getTeam(name);
        if (team != null) {
            team.unregister();
        }
    }
    
    public Team getTeam(final Scoreboard board, final String name, final String prefix) {
        Team team = board.getTeam(name);
        if (team == null) {
            team = board.registerNewTeam(name);
        }
        team.setPrefix(prefix);
        return team;
    }
    
	public void registerScoreboard(final Player player) {
        final Scoreboard bukkitScoreboard = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
        player.setScoreboard(bukkitScoreboard);
        this.scoreboardTitle = ColorText.translate(this.config.getString("Scoreboard.Title").replace("|", "\u2503"));
        final ScoreboardAPI localScoreboardHelper = new ScoreboardAPI(bukkitScoreboard, this.scoreboardTitle);
        this.helper.put(player, localScoreboardHelper);
        this.helper.put(player, localScoreboardHelper);
        this.helper.put(player, localScoreboardHelper);
        this.resendTabList(player);
        for (final Player other : Bukkit.getServer().getOnlinePlayers()) {
            if (other != player && this.getScoreboardFor(other) != null) {
                final Scoreboard scoreboard = this.getScoreboardFor(other).getScoreBoard();
                final Team otherteam = this.getTeam(scoreboard, "other", ColorText.translate("&c"));
                otherteam.addEntry(player.getName());
            }
        }
    }
    
	public void resendTabList(final Player player) {
        if (this.getScoreboardFor(player) == null) {
            return;
        }
        final Scoreboard scoreboard = this.getScoreboardFor(player).getScoreBoard();
        this.unregister(scoreboard, "player");
        this.unregister(scoreboard, "other");
        final Team playerteam = this.getTeam(scoreboard, "player", ColorText.translate("&a"));
        final Team otherteam = this.getTeam(scoreboard, "other", ColorText.translate("&c"));
        for (final Player other : Bukkit.getServer().getOnlinePlayers()) {
            if (ClanHandler.areMember(other, player, ClanHandler.getClan(player))) {
                playerteam.addEntry(other.getName());
            }
            else {
                otherteam.addEntry(other.getName());
            }
        }
    }
    public void setupScoreboard() {
        new BukkitRunnable() {      	
			public void run() {
                for (final Player player : Bukkit.getServer().getOnlinePlayers()) {
                    if (ScoreboardManager.this.helper.containsKey(player)) {
	                    ScoreboardAPI lines = ScoreboardManager.this.getScoreboardFor(player);
	                    TournamentManager tournamentManager = FullPvP.getPlugin().getTournamentManager();
	                    PointsFile points = PointsFile.getConfig();
	                	UUID UUID = player.getUniqueId();
	                    String p = player.getName();
	                    String rank = ColorText.translate(FullPvP.getPlugin().getChat().getPlayerPrefix(player));
	                    lines.clear();
	                    lines.add(bars);
	                    if(StaffModeCommand.isMod(player)) {
	                        lines.add("&2&lStaffMode");
	                    	lines.add(" &f\u00BB &aVanished&7: " + (VanishListener.isVanished(player) ? "&a\u2714" : "&c\u2716"));
	                    	lines.add(" &f\u00BB &aPlayers&7: &f" + Bukkit.getOnlinePlayers().size());
	                    } else if (tournamentManager.isInTournament(player) && !FreezeCommand.isFrozen(player.getUniqueId())) {
                			Tournament tournament = FullPvP.getPlugin().getTournamentManager().getTournament();
                			int announceCountdown = tournament.getDesecrentAnn();
                			lines.add("&2&l" + tournament.getType().getName() + " Event");
                            if (tournament.getType() == TournamentType.SUMO) {
                            	lines.add(" &f\u00BB &aPlayers&7: &f" + tournament.getPlayers().size() + "/" + tournament.getSize());
                            	if (announceCountdown > 0) {
                            		lines.add(" &f\u00BB &aStarting&7: &f" + announceCountdown + "s");
                            	}
                            	if (tournament.getTournamentState() == TournamentState.WAITING) {
                            		lines.add(" &f\u00BB &aStatus&7: &fWaiting...");
                            	} else if (tournament.getTournamentState() == TournamentState.FIGHTING) {
                            		lines.add(" &f\u00BB &aStatus&7: &fFighting...");
                            		String first = tournament.getFirstPlayer().getDisplayName();
                                    String second = tournament.getSecondPlayer().getDisplayName();
                                    if (first.length() > 14) {
                                        first = first.substring(0, 14);
                                    }
                                    lines.add("");
                                    lines.add("&2&l" + first + " &aVS &2&l" + second);
                            	} else {
                            		lines.add(" &f\u00BB &aStatus&7: &fSelecting...");
                            	}
                            } else if (tournament.getType() == TournamentType.FFA || tournament.getType() == TournamentType.TNTTAG) {
                            	lines.add(" &f\u00BB &aPlayers&7: &f" + tournament.getPlayers().size() + "/" + tournament.getSize());
                            	if (announceCountdown > 0) {
                            		lines.add(" &f\u00BB &aStarting&7: &f" + announceCountdown + "s");
                            	}
                            	if (tournament.getTournamentState() == TournamentState.WAITING) {
                            		lines.add(" &f\u00BB &aStatus&7: &fWaiting...");		
                            	}
                            	else if (tournament.isActiveProtection()) {
	                            	lines.add(" &f\u00BB &aStatus&7: &fInvincibility...");
	                            }
                            	else {
                            		lines.add(" &f\u00BB &aStatus&7: &fFighting...");
                            	}
                            }
                		} else {
	                    	lines.add(" &aNombre&7: &f" + p);
	                    	lines.add(" &aRango&7: &f" + rank);
	                    	lines.add(" &aRP&7: &f" + points.getInt("users." + UUID + ".points"));
	                    	lines.add("");
		                    lines.add(" &aBajas&7: &f" + player.getStatistic(Statistic.PLAYER_KILLS));
		                    lines.add(" &aMuertes&7: &f" + player.getStatistic(Statistic.DEATHS));
		                    lines.add(" &aDinero&7: &2\u26C3 &f" + FullPvP.getPlugin().getEconomyManager().getBalance(player.getUniqueId()));
	                    }
                        for (final String dtc : DTCHandler.getDTCActiveList()) {
                            if (DTCHandler.isStarted(dtc)) {
                            	lines.add("&2&lDTC");
                                lines.add(" &f\u00BB &aPoints: &f" + DTCHandler.dtcFile.get("DTC." + dtc + ".PointsLeft"));
                                lines.add(" &f\u00BB &aCoords: &f" + DTCHandler.dtcFile.getInt("CurrentDTC." + dtc + ".X") + ", " + 
                                		DTCHandler.dtcFile.getInt("CurrentDTC." + dtc + ".Y") + ", " + 
                                		DTCHandler.dtcFile.getInt("CurrentDTC." + dtc + ".Z"));
                            }
                        }
                        if (FullPvP.getPlugin().getCombatTagListener().hasCooldown(player)) {
                            lines.add("&4&lCombat&7: &f" + Utils.formatLongMin(FullPvP.getPlugin().getCombatTagListener().getMillisecondsLeft(player)));
                        }
                        if (FullPvP.getPlugin().getEnderpearlListener().hasCooldown(player)) {
                            lines.add("&3&lEnderpearl&7: &f" + Utils.formatLongMin(FullPvP.getPlugin().getEnderpearlListener().getMillisecondsLeft(player)));
                        }
                        if (FullPvP.getPlugin().getSpawnHandler().getSpawnTasks().containsKey(player.getUniqueId())) {
                            lines.add("&a&lSpawn&7: &f" + Utils.formatLongMin(FullPvP.getPlugin().getSpawnHandler().getMillisecondsLeft(player)));
                        } else {
                            FullPvP.getPlugin().getSpawnHandler().applyWarmup(player);
                        }
                        lines.add("");
                        lines.add("&2galanthusmc.net");
                        lines.add(bars);
                        lines.update(player);
                    }
                }
            }
        }.runTaskTimer(FullPvP.getPlugin(), 2L, 2L);
    }
    
    static {
    	bars = "&7&m---------------------";
    }
}
