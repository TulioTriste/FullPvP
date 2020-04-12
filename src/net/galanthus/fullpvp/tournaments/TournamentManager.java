package net.galanthus.fullpvp.tournaments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

import net.galanthus.fullpvp.FullPvP;
import net.galanthus.fullpvp.tournaments.runnable.TournamentRunnable;
import net.galanthus.fullpvp.utilities.ColorText;
import net.galanthus.fullpvp.utilities.ItemBuilder;

public class TournamentManager {
	
	private final FullPvP plugin;
    public final Map<UUID, Integer> matches;
    private Tournament tournament;
    private final List<UUID> players;
    private boolean created;
    
    public TournamentManager() {
        this.plugin = FullPvP.getPlugin();
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
        if (type == TournamentType.SUMO) {
            runnable.startSumo();
        }
        commandSender.sendMessage(ColorText.translate("&aSuccessfully created tournament, size " + size + " with type " + type + '.'));
        this.created = true;
    }
    
	public void playerLeft(final Tournament tournament, final Player player, final boolean message) {
        if (message) {
            player.sendMessage(ColorText.translate("&cYou left the tournament."));
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
            tournament.broadcast(ColorText.translate("&a" + player.getDisplayName() + " &chas left the tournament. &7(" + tournament.getPlayers().size() + '/' + tournament.getSize() + ')'));
        }
        if (player.isOnline()) {
            tournament.rollbackInventory(player);
            player.teleport(Bukkit.getWorld("Mapa").getSpawnLocation());
        }
        if (this.players.size() == 1) {
            final Player winner = Bukkit.getPlayer((UUID)this.players.get(0));
            for (final Player online : Bukkit.getServer().getOnlinePlayers()) {
                online.sendMessage(new String[2]);
                online.sendMessage(ColorText.translate("&2[Winner] &f" + winner.getDisplayName()));
                online.sendMessage(ColorText.translate("&2[Winner] &f" + winner.getDisplayName()));
                online.sendMessage(ColorText.translate("&2[Winner] &f" + winner.getDisplayName()));
                online.sendMessage(new String[2]);
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
                    online.teleport(Bukkit.getWorld("Mapa").getSpawnLocation());
                    tournament.rollbackInventory(player);
                    for (final PotionEffect effects2 : online.getActivePotionEffects()) {
                        online.removePotionEffect(effects2.getType());
                    }
                }
            }
            if (this.getTournament().getType() == TournamentType.SUMO) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "cr givekey " + winner.getName() + " Event 2");
            }
            else if (this.getTournament().getType() == TournamentType.FFA) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "cr givekey " + winner.getName() + " Event 2");
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
        tournament.broadcast(ColorText.translate("&a" + player.getDisplayName() + " &ahas joined the tournament. &7(" + tournament.getPlayers().size() + '/' + tournament.getSize() + ')'));
    }
    
    public void joinTournament(final Player player) {
        final Tournament tournament = this.tournament;
        if (this.players.size() >= tournament.getSize()) {
            player.sendMessage(ColorText.translate("&cThis tournament is already full!"));
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
                inventory.setItem(0, new ItemBuilder(Material.ENCHANTED_BOOK).displayName(ColorText.translate("&6Default Kit")).lore(ColorText.translate("&7Right click to equip default kit")).build());
                online.updateInventory();
                online.setGameMode(GameMode.SURVIVAL);
                online.setAllowFlight(false);
                online.setFlying(false);
            }
        }
        else if (this.tournament.getType() == TournamentType.TNTTAG) {
            for (final UUID players : this.players) {
                final Player online = Bukkit.getPlayer(players);
                this.tournament.teleport(online, "TNTTag.Spawn");
            }
            this.tournament.setTournamentState(TournamentState.FIGHTING);
            this.tournament.setProtection(10);
            for (final UUID players : this.players) {
                final Player online = Bukkit.getPlayer(players);
                final PlayerInventory inventory = online.getInventory();
                inventory.clear();
                inventory.setItem(0, new ItemBuilder(Material.ENCHANTED_BOOK).displayName("&6Default Kit").lore("&7Right click to equip default kit").build());
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