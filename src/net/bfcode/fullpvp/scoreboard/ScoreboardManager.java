package net.bfcode.fullpvp.scoreboard;

import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;

import net.bfcode.fullpvp.FullPvP;
import net.bfcode.fullpvp.clans.ClanHandler;
import net.bfcode.fullpvp.commands.StaffModeCommand;
import net.bfcode.fullpvp.commands.essentials.FreezeCommand;
import net.bfcode.fullpvp.configuration.LocationFile;
import net.bfcode.fullpvp.configuration.ScoreBoardFile;
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
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.entity.Player;

import java.util.WeakHashMap;
import org.bukkit.event.Listener;

public class ScoreboardManager implements Listener {
    public WeakHashMap<Player, ScoreboardAPI> helper;
    private String scoreboardTitle;
    private FullPvP plugin;
    
    public ScoreboardManager() {
        this.plugin = FullPvP.getInstance();
        this.helper = new WeakHashMap<Player, ScoreboardAPI>();
    }
    
    public ScoreboardAPI getScoreboardFor(final Player player) {
        return this.helper.get(player);
    }
    
    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        event.setQuitMessage(null);
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
        event.setJoinMessage(null);
        event.setJoinMessage(null);
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
        this.scoreboardTitle = ColorText.translate(ScoreBoardFile.getConfig().getString("Scoreboard.Title").replace("|", "\u2503"));
        final ScoreboardAPI localScoreboardHelper = new ScoreboardAPI(bukkitScoreboard, this.scoreboardTitle);
        this.helper.put(player, localScoreboardHelper);
        this.helper.put(player, localScoreboardHelper);
        this.helper.put(player, localScoreboardHelper);
        this.resendTabList(player);
        for (final Player other : Bukkit.getServer().getOnlinePlayers()) {
            if (other != player && this.getScoreboardFor(other) != null) {
                final Scoreboard scoreboard = this.getScoreboardFor(other).getScoreBoard();
                final Team otherteam = this.getTeam(scoreboard, "other", ColorText.translate(FullPvP.getInstance().getChat().getPlayerSuffix(player) + ChatColor.valueOf(plugin.getConfig().getString("NameTags.enemy"))));
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
        final Team playerteam = this.getTeam(scoreboard, "player", ColorText.translate(FullPvP.getInstance().getChat().getPlayerSuffix(player) + ChatColor.valueOf(plugin.getConfig().getString("NameTags.members"))));
        final Team otherteam = this.getTeam(scoreboard, "other", ColorText.translate(FullPvP.getInstance().getChat().getPlayerSuffix(player) + ChatColor.valueOf(plugin.getConfig().getString("NameTags.enemy"))));
        final Team staffteam = this.getTeam(scoreboard, "staffmode", ColorText.translate(FullPvP.getInstance().getChat().getPlayerSuffix(player) + ChatColor.valueOf(plugin.getConfig().getString("NameTags.staff"))));
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
	                    ScoreboardAPI lines = ScoreboardManager.this.getScoreboardFor(player);
	                    TournamentManager tournamentManager = FullPvP.getInstance().getTournamentManager();
	                    ScoreBoardFile config = ScoreBoardFile.getConfig();
	                    LocationFile location = LocationFile.getConfig();
	                    lines.clear();
	                    for(String string : config.getStringList("Scoreboard.Lines")) {
	                    	
	                    	if(string.contains("{combat}")) {
	                    		
	                    		if(FullPvP.getInstance().getCombatTagListener().hasCooldown(player)) {
	                    			lines.add(string.replace("{combat}", Utils.formatLongMin(FullPvP.getInstance().getCombatTagListener().getMillisecondsLeft(player))));
	                    		}
	                    		
	                    		continue;
	                    	}
	                    	
	                    	if(string.contains("{enderpearl}")) {
	                    		
	                    		if(FullPvP.getInstance().getEnderpearlListener().hasCooldown(player)) {
	                    			lines.add(string.replace("{enderpearl}", Utils.formatLongMin(FullPvP.getInstance().getEnderpearlListener().getMillisecondsLeft(player))));
	                    		}
	                    		
	                    		continue;
	                    	}
	                    	
	                    	if(string.contains("{spawn}")) {
	                    		
	                    		if(FullPvP.getInstance().getSpawnHandler().isActive(player)) {
	                    			lines.add(string.replace("{spawn}", Utils.formatLongMin(FullPvP.getInstance().getSpawnHandler().getMillisecondsLeft(player))));
	                    		}
	                    		
	                    		continue;
	                    	}
	                    	
	                    	if(string.contains("{staffmode}")) {
	                    		
	                    		if(StaffModeCommand.isMod(player)) {
	                                for(String stafflines : config.getStringList("Scoreboard.Variables.StaffMode")) {
	                                	stafflines = stafflines.replace("{status}", (VanishListener.isVanished(player) ? "&a\u2714" : "&c\u2716"))
	                                	.replace("{players}", Bukkit.getOnlinePlayers().size() + "");
	                                	lines.add(stafflines);
	                                }
	                    			
	                    		}
	                    		
	                    		continue;
	                    	}
	                    	
	                    	if(string.contains("{freeze}")) {
	                    		
	                    		if(FreezeCommand.isFrozen(player.getUniqueId())) {
	                    			for(String freezelines : config.getStringList("Scoreboard.Variables.Freeze")) {
	                    				lines.add(freezelines);
	                    			}
	                    		}
	                    		
	                    		continue;
	                    	}
	                    	
	                    	if(string.contains("{dtc}")) {
	                    		
	                    		for(String dtc : DTCHandler.getDTCActiveList()) {
	                    			if(DTCHandler.isStarted(dtc) && !StaffModeCommand.isMod(player)) {
	                    				for(String dtclines : config.getStringList("Scoreboard.Variables.DestroyTheCore")) {
	                    					dtclines = dtclines.replace("{points-left}", DTCHandler.dtcFile.get("DTC." + dtc + ".PointsLeft") + "")
	                    					.replace("{x}", DTCHandler.dtcFile.getInt("CurrentDTC." + dtc + ".X") + "")
	                    					.replace("{y}", DTCHandler.dtcFile.getInt("CurrentDTC." + dtc + ".Y") + "")
	                    					.replace("{z}", DTCHandler.dtcFile.getInt("CurrentDTC." + dtc + ".Z") + "");
	                    					lines.add(dtclines);
	                    				}
	                    			}
	                    		}
	                    		
	                    		continue;
	                    	}
	                    	
	                    	if(string.contains("{claims}")) {
	                    		
			                    for (final String claim : location.getConfigurationSection("Claims").getKeys(false)) {
			                        final CuboidSelection selection = new CuboidSelection(Bukkit.getWorld(location.getString("Claims." + claim + ".world")), ScoreboardManager.this.getLocation(claim, "cornerA"), ScoreboardManager.this.getLocation(claim, "cornerB"));
			                        if(StaffModeCommand.isMod(player)) {
			                        	continue;
			                        }
			                        if(FreezeCommand.isFrozen(player.getUniqueId())) {
			                        	continue;
			                        }
			                        if(selection.contains(player.getLocation())) {
			                    		for(String noPvP : config.getStringList("Scoreboard.Variables.Claims.noPvP")) {
			                    			noPvP = noPvP.replace("{balance}", FullPvP.getInstance().getEconomyManager().getBalance(player.getUniqueId()) + "")
			                    			.replace("{kills}", player.getStatistic(Statistic.PLAYER_KILLS) + "")
			                    			.replace("{deaths}", player.getStatistic(Statistic.DEATHS) + "");
			                    			if(noPvP.contains("{clan-info}")) {
			                    				if(ClanHandler.hasClan(player)) {
			                    					String clan = ClanHandler.getClan(player);
			                    					for(String clanlines : config.getStringList("Scoreboard.Variables.Claims.Clan-info")) {
			                    						clanlines = clanlines.replace("{name}", ClanHandler.getClan(player))
			                    						.replace("{members-online}", ClanHandler.getClanMembers(clan) + "")
			                    						.replace("{max-members}", ClanHandler.getMinClanSize(clan) + "");
			                    						lines.add(clanlines);
			                    					}
			                    				}
			                    				continue;
			                    			}
			                    			lines.add(noPvP);
			                    		}
			                		} else {
			                			for(String nozone : config.getStringList("Scoreboard.Variables.Claims.noZone")) {
			                				nozone = nozone.replace("{balance}", FullPvP.getInstance().getEconomyManager().getBalance(player.getUniqueId()) + "")
					                    			.replace("{kills}", player.getStatistic(Statistic.PLAYER_KILLS) + "")
					                    			.replace("{deaths}", player.getStatistic(Statistic.DEATHS) + "");
			                    			if(nozone.contains("{clan-info}")) {
			                    				if(ClanHandler.hasClan(player)) {
			                    					String clan = ClanHandler.getClan(player);
			                    					for(String clanlines : config.getStringList("Scoreboard.Variables.Claims.Clan-info")) {
			                    						clanlines = clanlines.replace("{name}", ClanHandler.getClan(player))
			                    						.replace("{members-online}", ClanHandler.getClanMembers(clan) + "")
			                    						.replace("{max-members}", ClanHandler.getMinClanSize(clan) + "");
			                    						lines.add(clanlines);
			                    					}
			                    				}
			                    				continue;
			                    			}
			                    			lines.add(nozone);
			                			}
			                    	}
			                    }
	                    		continue;
	                    	}
	                    	
	                    	if(string.contains("{tournament}")) {
			                    if (tournamentManager.isInTournament(player) && !StaffModeCommand.isMod(player)) {
		                			Tournament tournament = FullPvP.getInstance().getTournamentManager().getTournament();
		                			int announceCountdown = tournament.getDesecrentAnn();
		                			lines.add(config.getString("Scoreboard.Variables.Tournament.Title")
		                					.replace("{event}", tournament.getType().getName()));
		                            if (tournament.getType() == TournamentType.SUMO) {
		                            	for(String sumolines : config.getStringList("Scoreboard.Variables.Tournament.Sumo.Lines")) {
		                            		sumolines = sumolines.replace("{players}", tournament.getPlayers().size() + "")
		                            				.replace("{max-players}", tournament.getSize() + "");
		                            		if(sumolines.contains("{countdown}")) {
				                            	if (announceCountdown > 0) {
				                            		lines.add(sumolines.replace("{countdown}", announceCountdown + ""));
				                            	}
		                            			continue;
		                            		}
		                            		lines.add(sumolines);
		                            	}
		                            	if (tournament.getTournamentState() == TournamentState.WAITING) {
		                            		for(String waitingsumolines : config.getStringList("Scoreboard.Variables.Tournament.Sumo.Waiting")) {
		                            			lines.add(waitingsumolines);
		                            		}
		                            	} else if (tournament.getTournamentState() == TournamentState.FIGHTING) {
		                            		for(String fightingsumolines : config.getStringList("Scoreboard.Variables.Tournament.Sumo.Fighting")) {
			                            		String first = tournament.getFirstPlayer().getDisplayName();
			                                    String second = tournament.getSecondPlayer().getDisplayName();
			                                    if (first.length() > 14) {
			                                        first = first.substring(0, 14);
			                                    }
		                            			fightingsumolines = fightingsumolines.replace("{first}", first)
		                            					.replace("{second}", second);
		                            			lines.add(fightingsumolines);
		                            		}
		                            	} else {
		                            		for(String selectingsumolines : config.getStringList("Scoreboard.Variables.Tournament.Sumo.Selecting")) {
		                            			lines.add(selectingsumolines);
		                            		}
		                            	}
		                            } else if (tournament.getType() == TournamentType.FFA) {
		                            	for(String ffalines : config.getStringList("Scoreboard.Variables.Tournament.FFA.Lines")) {
		                            		ffalines = ffalines.replace("{players}", tournament.getPlayers().size() + "")
		                            				.replace("{max-players}", tournament.getSize() + "");
		                            		if(ffalines.contains("{countdown}")) {
				                            	if (announceCountdown > 0) {
				                            		lines.add(ffalines.replace("{countdown}", announceCountdown + ""));
				                            	}
		                            			continue;
		                            		}
		                            		lines.add(ffalines);
		                            	}
		                            	if (tournament.getTournamentState() == TournamentState.WAITING) {
		                            		for(String waitingffalines : config.getStringList("Scoreboard.Variables.Tournament.FFA.Waiting")) {
		                            			lines.add(waitingffalines);
		                            		}
		                            	}
		                            	else if (tournament.isActiveProtection()) {
		                            		for(String invicibilityffalines : config.getStringList("Scoreboard.Variables.Tournament.FFA.Invicibility")) {
		                            			invicibilityffalines = invicibilityffalines.replace("{time}", tournament.getProtection() + "");
		                            			lines.add(invicibilityffalines);
		                            		}
			                            }
		                            	else {
		                            		for(String fightingffalines : config.getStringList("Scoreboard.Variables.Tournament.FFA.Fighting")) {
		                            			lines.add(fightingffalines);
		                            		}
		                            	}
		                            }
		                		}
			                    continue;
	                    	}
	                    	
		                    lines.add(ColorText.translate(string));
	                    	
	                    }
                        lines.update(player);
                    }
                }
            }
        }.runTaskTimer(FullPvP.getInstance(), 2L, 2L);
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
}
