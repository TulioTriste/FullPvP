package net.bfcode.fullpvp.scoreboard;

import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;

import net.bfcode.fullpvp.FullPvP;
import net.bfcode.fullpvp.clans.ClanHandler;
import net.bfcode.fullpvp.commands.StaffModeCommand;
import net.bfcode.fullpvp.commands.essentials.FreezeCommand;
import net.bfcode.fullpvp.configuration.LocationFile;
import net.bfcode.fullpvp.configuration.PointsFile;
import net.bfcode.fullpvp.destroythecore.DTCHandler;
import net.bfcode.fullpvp.listener.VanishListener;
import net.bfcode.fullpvp.tournaments.Tournament;
import net.bfcode.fullpvp.tournaments.TournamentManager;
import net.bfcode.fullpvp.tournaments.TournamentState;
import net.bfcode.fullpvp.tournaments.TournamentType;
import net.bfcode.fullpvp.utilities.ColorText;
import net.bfcode.fullpvp.utilities.Utils;

import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
                final Team otherteam = this.getTeam(scoreboard, "other", ColorText.translate(FullPvP.getPlugin().getChat().getPlayerSuffix(player) + "&e"));
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
        this.unregister(scoreboard, "staff");
        final Team playerteam = this.getTeam(scoreboard, "player", ColorText.translate(FullPvP.getPlugin().getChat().getPlayerSuffix(player) + "&a"));
        final Team otherteam = this.getTeam(scoreboard, "other", ColorText.translate(FullPvP.getPlugin().getChat().getPlayerSuffix(player) + "&e"));
        final Team staffteam = this.getTeam(scoreboard, "staffmode", ColorText.translate(FullPvP.getPlugin().getChat().getPlayerSuffix(player) + "&b"));
        for (final Player other : Bukkit.getServer().getOnlinePlayers()) {
            if (ClanHandler.areMember(other, player, ClanHandler.getClan(player)) && !StaffModeCommand.isMod(player)) {
                playerteam.addEntry(other.getName());
            }
            else if(!ClanHandler.areMember(other, player, ClanHandler.getClan(player)) && !StaffModeCommand.isMod(player)) {
                otherteam.addEntry(other.getName());
            }
            else if(StaffModeCommand.isMod(player)) {
            	staffteam.addEntry(other.getName());
            }
        }
    }
    public void setupScoreboard() {
        new BukkitRunnable() {      	
			public void run() {
                for (final Player player : Bukkit.getServer().getOnlinePlayers()) {
                    if (ScoreboardManager.this.helper.containsKey(player)) {
                        LocationFile location = LocationFile.getConfig();
	                    ScoreboardAPI lines = ScoreboardManager.this.getScoreboardFor(player);
	                    TournamentManager tournamentManager = FullPvP.getPlugin().getTournamentManager();
	                    PointsFile points = PointsFile.getConfig();
	                	UUID UUID = player.getUniqueId();
	                	String uuidd = player.getUniqueId().toString();
	                    String p = player.getName();
	                    lines.clear();
	                    lines.add(bars);
		                    if(StaffModeCommand.isMod(player)) {
		                        lines.add("&2&lStaffMode");
		                    	lines.add(" &f\u00BB &dVanished&7: " + (VanishListener.isVanished(player) ? "&a\u2714" : "&c\u2716"));
		                    	lines.add(" &f\u00BB &dPlayers&7: &f" + Bukkit.getOnlinePlayers().size());
		                    } else if (tournamentManager.isInTournament(player) && !FreezeCommand.isFrozen(player.getUniqueId())) {
	                			Tournament tournament = FullPvP.getPlugin().getTournamentManager().getTournament();
	                			int announceCountdown = tournament.getDesecrentAnn();
	                			lines.add("&5&l" + tournament.getType().getName() + " Event");
	                            if (tournament.getType() == TournamentType.SUMO) {
	                            	lines.add(" &f\u00BB &dPlayers&7: &f" + tournament.getPlayers().size() + "/" + tournament.getSize());
	                            	if (announceCountdown > 0) {
	                            		lines.add(" &f\u00BB &dStarting&7: &f" + announceCountdown + "s");
	                            	}
	                            	if (tournament.getTournamentState() == TournamentState.WAITING) {
	                            		lines.add(" &f\u00BB &dStatus&7: &fWaiting...");
	                            	} else if (tournament.getTournamentState() == TournamentState.FIGHTING) {
	                            		lines.add(" &f\u00BB &dStatus&7: &fFighting...");
	                            		String first = tournament.getFirstPlayer().getDisplayName();
	                                    String second = tournament.getSecondPlayer().getDisplayName();
	                                    if (first.length() > 14) {
	                                        first = first.substring(0, 14);
	                                    }
	                                    lines.add("");
	                                    lines.add("&5&l" + first + " &dVS &5&l" + second);
	                            	} else {
	                            		lines.add(" &f\u00BB &dStatus&7: &fSelecting...");
	                            	}
	                            } else if (tournament.getType() == TournamentType.FFA || tournament.getType() == TournamentType.TNTTAG) {
	                            	lines.add(" &f\u00BB &dPlayers&7: &f" + tournament.getPlayers().size() + "/" + tournament.getSize());
	                            	if (announceCountdown > 0) {
	                            		lines.add(" &f\u00BB &dStarting&7: &f" + announceCountdown + "s");
	                            	}
	                            	if (tournament.getTournamentState() == TournamentState.WAITING) {
	                            		lines.add(" &f\u00BB &dStatus&7: &fWaiting...");		
	                            	}
	                            	else if (tournament.isActiveProtection()) {
		                            	lines.add(" &f\u00BB &dStatus&7: &fInvincibility...");
		                            }
	                            	else {
	                            		lines.add(" &f\u00BB &dStatus&7: &fFighting...");
	                            	}
	                            }
	                		} 
		                    for (final String claim : location.getConfigurationSection("Claims").getKeys(false)) {
		                        final CuboidSelection selection = new CuboidSelection(Bukkit.getWorld(location.getString("Claims." + claim + ".world")), ScoreboardManager.this.getLocation(claim, "cornerA"), ScoreboardManager.this.getLocation(claim, "cornerB"));
		                        final boolean isPvP = location.getBoolean("Claims." + claim + ".pvp");
		                        final String clan = FullPvP.getPlugin().getClanHandler().getClan(player);
			                    if(selection.contains(player.getLocation()) && !StaffModeCommand.isMod(player)) {
			                    	if(!isPvP) {
			                			lines.add("&dDinero &7\u00BB &f$" + FullPvP.getPlugin().getEconomyManager().getBalance(UUID));
			                			if(FullPvP.getPlugin().getClanHandler().hasClan(player)) {
				                			lines.add("&dClan &7\u00BB &9" + FullPvP.getPlugin().getClanHandler().getClan(player));
				                			lines.add("&dOnline &7\u00BB &f" + FullPvP.getPlugin().getClanHandler().getClanMembers(clan) + "/" + FullPvP.getPlugin().getClanHandler().getMembers(clan));
				                			lines.add("");
			                			}
			                			lines.add("");
			                    	}
			                		if(isPvP) {
			                			lines.add("&dKills &7\u00BB &f" + player.getStatistic(Statistic.PLAYER_KILLS));
			                			lines.add("&dDeaths &7\u00BB &f" + player.getStatistic(Statistic.DEATHS));
			                		}
		                		}
	                    }
                        for (final String dtc : DTCHandler.getDTCActiveList()) {
                            if (DTCHandler.isStarted(dtc)) {
                            	lines.add("&2&lDTC");
                                lines.add(" &f\u00BB &dPoints: &f" + DTCHandler.dtcFile.get("DTC." + dtc + ".PointsLeft"));
                                lines.add(" &f\u00BB &dCoords: &f" + DTCHandler.dtcFile.getInt("CurrentDTC." + dtc + ".X") + ", " + 
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
                        lines.add("&5faltryxpvp.us");
                        lines.add(bars);
                        lines.update(player);
                    }
                }
            }
        }.runTaskTimer(FullPvP.getPlugin(), 2L, 2L);
    }
    
    public Location getLocation(final String town, final String corner) {
        LocationFile location = LocationFile.getConfig();
        final LocationFile location1 = location;
        final World world = Bukkit.getWorld((String)LocationFile.getConfig().get("Claims." + town + ".world"));
        final double x = location1.getDouble("Claims." + town + "." + corner + ".x");
        final double y = location1.getDouble("Claims." + town + "." + corner + ".y");
        final double z = location1.getDouble("Claims." + town + "." + corner + ".z");
        return new Location(world, x, y, z);
    }
    
    static {
    	bars = "&7&m---------------------";
    }
}
