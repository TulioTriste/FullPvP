package net.panda.fullpvp.tournaments;

import org.bukkit.inventory.PlayerInventory;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffect;

import net.panda.fullpvp.FullPvP;
import net.panda.fullpvp.configuration.MessagesFile;
import net.panda.fullpvp.tournaments.runnable.TournamentRunnable;
import net.panda.fullpvp.utilities.ColorText;
import net.panda.fullpvp.utilities.ItemBuilder;
import net.panda.fullpvp.utilities.PlayerUtil;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Map;

public class TournamentManager {
	
    private final FullPvP plugin;
    public final Map<UUID, Integer> matches;
    private Tournament tournament;
    private final List<UUID> players;
    private boolean created;
    
    public TournamentManager() {
        this.plugin = FullPvP.getInstance();
        this.matches = new HashMap<UUID, Integer>();
        this.players = new ArrayList<UUID>();
        this.created = false;
    }
    
    public boolean isInTournament(final UUID uuid) {
        return this.players.contains(uuid);
    }
    
    public boolean isInTournament(final Player player) {
        return this.players.contains(player.getUniqueId());
    }
    
    public void kickPlayer(final UUID uuid) {
        this.players.remove(uuid);
    }
    
    public void createTournament(final CommandSender commandSender, final int size, final TournamentType type, final Player player) {
        final Tournament tournament = new Tournament(size, type, player);
        this.tournament = tournament;
        final TournamentRunnable runnable = new TournamentRunnable(tournament);
        MessagesFile messages = MessagesFile.getConfig();
        if (type == TournamentType.SUMO) {
            runnable.startSumo();
        }
        for(String msg : messages.getStringList("Tournament.Created-Event")) {
        	commandSender.sendMessage(ColorText.translate(msg
        			.replace("{max-players}", size + "")
        			.replace("{type}", type + "")));
        }
        this.created = true;
    }
    
	public void playerLeft(final Tournament tournament, final Player player, final boolean message) {
        if (message) {
            player.sendMessage(ColorText.translate(MessagesFile.getConfig().getString("Tournament.Player-Leave-Event")));
            tournament.rollbackInventory(player);
        }
        this.players.remove(player.getUniqueId());
        for (final PotionEffect effects : player.getActivePotionEffects()) {
            player.removePotionEffect(effects.getType());
        }
        if(plugin.getCombatTagListener().hasCooldown(player)) {
        	plugin.getCombatTagListener().removeCooldown(player);
        }
        tournament.removePlayer(player.getUniqueId());
        if (message) {
            tournament.broadcast(ColorText.translate(MessagesFile.getConfig().getString("Tournament.Leave-Event")
            		.replace("{player}", player.getName())
            		.replace("{players}", tournament.getPlayers().size() + "")
            		.replace("{size}", tournament.getSize() + "")));
        }
        if (player.isOnline()) {
            tournament.rollbackInventory(player);
            player.teleport(Bukkit.getWorld("World").getSpawnLocation());
        }
        if (this.players.size() == 1) {
            final Player winner = Bukkit.getPlayer((UUID)this.players.get(0));
            for (final Player online : Bukkit.getServer().getOnlinePlayers()) {
            	for(String msg : MessagesFile.getConfig().getStringList("Tournament.Winner-Event")) {
            		online.sendMessage(msg.replace("{winner}", winner.getName()));
            	}
                online.playSound(online.getLocation(), Sound.ENDERDRAGON_GROWL, 2.0f, 2.0f);
                this.plugin.getCombatTagListener().removeCooldown(winner);
                tournament.rollbackInventory(winner);
                for (final PotionEffect effects2 : winner.getActivePotionEffects()) {
                    winner.removePotionEffect(effects2.getType());
                }
            }
            this.plugin.getTournamentManager().setCreated(false);
            for (final Player online : Bukkit.getServer().getOnlinePlayers()) {
            	tournament.rollbackInventory(player);
                if (this.plugin.getTournamentManager().isInTournament(online.getUniqueId())) {
                    this.players.remove(online.getUniqueId());
                    online.teleport(Bukkit.getWorld("World").getSpawnLocation());
                    tournament.rollbackInventory(player);
                    for (final PotionEffect effects2 : online.getActivePotionEffects()) {
                        online.removePotionEffect(effects2.getType());
                    }
                }
            }
            
            ConfigurationSection config = FullPvP.getInstance().getConfig().getConfigurationSection("Host-Menu.items");
            for(int i = 1; i <= config.getKeys(false).size(); ++i) {
            	Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), config.getString("." + i + ".Winner-Command"));
            }
        }
        else if (this.players.size() == 0) {
            this.plugin.getTournamentManager().setCreated(false);
        }
        else if (this.players.size() > 1) {
            final TournamentRunnable runnable = new TournamentRunnable(tournament);
            if (this.plugin.getTournamentManager().getTournament().getType() == TournamentType.SUMO) {
                tournament.setCountdown(4);
                tournament.setCurrentRound(tournament.getCurrentRound() + 1);
                runnable.startSumo();
            }
        }
        PlayerUtil.startingSumo(player);
    }
    
    public void leaveTournament(final Player player) {
        if (!this.isInTournament(player.getUniqueId())) {
            return;
        }
        this.playerLeft(this.tournament, player, true);
    }
    
    private void playerJoined(final Tournament tournament, final Player player) {
        tournament.addPlayer(player.getUniqueId());
        this.players.add(player.getUniqueId());
        player.setFoodLevel(20);
        player.setHealth(20.0);
        for (final PotionEffect effects : player.getActivePotionEffects()) {
            player.removePotionEffect(effects.getType());
        }
            tournament.broadcast(ColorText.translate(MessagesFile.getConfig().getString("Tournament.Joined-Event")
            		.replace("{player}", player.getName())
            		.replace("{players", tournament.getPlayers().size() + "")
            		.replace("{size}", tournament.getSize() + "")));
    }
    
    public void joinTournament(final Player player) {
        final Tournament tournament = this.tournament;
        if (this.players.size() >= tournament.getSize()) {
            player.sendMessage(ColorText.translate(MessagesFile.getConfig().getString("Tournament.Event-Full")));
        }
        else {
            this.playerJoined(tournament, player);
        }
    }
    
    public void forceStart() {
        if (this.tournament.getType() == TournamentType.FFA) {
            for (final UUID players : this.players) {
                final Player online = Bukkit.getPlayer(players);
                this.tournament.teleport(online, "FFA.Spawn");
            }
            this.tournament.setTournamentState(TournamentState.FIGHTING);
            this.tournament.setProtection(10);
            for (final UUID players : this.players) {
                final Player online = Bukkit.getPlayer(players);
                final PlayerInventory inventory = online.getInventory();
                inventory.clear();
                inventory.setItem(0, new ItemBuilder(Material.ENCHANTED_BOOK).displayName(ColorText.translate("&6Default Kit")).lore(new String[] { ColorText.translate("&7Click derecho para equiparte el kit!") }).build());
                online.updateInventory();
                online.setGameMode(GameMode.SURVIVAL);
                online.setAllowFlight(false);
                online.setFlying(false);
            }
        }
        else if (this.tournament.getType() == TournamentType.SUMO) {
            for (final UUID players : this.players) {
                final Player online = Bukkit.getPlayer(players);
                this.tournament.teleport(online, "Sumo.Spawn");
                online.getInventory().clear();
                online.setGameMode(GameMode.SURVIVAL);
                online.setAllowFlight(false);
                online.setFlying(false);
            }
            this.tournament.setTournamentState(TournamentState.STARTING);
        }
    }
    
    public List<UUID> getPlayers() {
        return this.players;
    }
    
    public void setCreated(final boolean s) {
        this.created = s;
    }
    
    public boolean isCreated() {
        return this.created;
    }
    
    public Tournament getTournament() {
        return this.tournament;
    }
}
