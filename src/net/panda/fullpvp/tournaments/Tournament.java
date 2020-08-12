package net.panda.fullpvp.tournaments;

import org.bukkit.Sound;
import org.bukkit.Material;
import java.util.HashSet;
import org.bukkit.inventory.ItemStack;

import net.panda.fullpvp.FullPvP;
import net.panda.fullpvp.tournaments.file.TournamentFile;
import net.panda.fullpvp.utilities.ColorText;
import net.panda.fullpvp.utilities.ItemBuilder;
import net.panda.fullpvp.utilities.LocationUtils;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.UUID;
import java.util.Set;

public class Tournament {
	
    private FullPvP plugin;
    private Set<UUID> players;
    private HashMap<Player, ItemStack[]> playerArmor;
    private HashMap<Player, ItemStack[]> playerInventory;
    public Set<UUID> matches;
    private int id;
    private int size;
    private TournamentState tournamentState;
    private int currentRound;
    private int countdown;
    private int pregame;
    private int announceCountdown;
    private TournamentType type;
    private Player hoster;
    private TournamentFile file;
    private Player firstPlayer;
    private Player secondPlayer;
    private long protection;
    
    public Tournament(final int size, final TournamentType type, final Player player) {
        this.plugin = FullPvP.getInstance();
        this.players = new HashSet<UUID>();
        this.playerArmor = new HashMap<Player, ItemStack[]>();
        this.playerInventory = new HashMap<Player, ItemStack[]>();
        this.matches = new HashSet<UUID>();
        this.tournamentState = TournamentState.WAITING;
        this.currentRound = 1;
        this.file = TournamentFile.getConfig();
        this.size = size;
        this.type = type;
        this.hoster = player;
        this.countdown = 11;
        this.pregame = 6;
        this.announceCountdown = 60;
    }
    
    public boolean isActiveProtection() {
        return this.getProtection() / 1000.0 > 0.0;
    }
    
    public long getProtection() {
        return this.protection - System.currentTimeMillis();
    }
    
    public void setProtection(final int time) {
        this.protection = System.currentTimeMillis() + (time + 1) * 1000L;
    }
    
    public void teleport(final Player player, final String location) {
        player.teleport(LocationUtils.getLocation(this.file.getString("Locations." + location)));
    }
    
    public void addPlayer(final UUID uuid) {
        this.players.add(uuid);
    }
    
    public void removePlayer(final UUID uuid) {
        this.players.remove(uuid);
    }
    
    public void saveInventory(final Player player) {
        this.playerArmor.put(player, player.getInventory().getArmorContents());
        this.playerInventory.put(player, player.getInventory().getContents());
        player.getInventory().clear();
        player.getInventory().setArmorContents((ItemStack[])null);
    }
    
    public void rollbackInventory(final Player player) {
        player.getInventory().setArmorContents((ItemStack[])this.playerArmor.get(player));
        player.getInventory().setContents((ItemStack[])this.playerInventory.get(player));
    }
    
    public void giveItemWait(final Player player) {
        player.getInventory().setItem(8, new ItemBuilder(Material.NETHER_STAR)
        		.displayName(ColorText.translate("&cLeave Event"))
        		.lore(new String[] { ColorText.translate("&7Righ Click to leave!") }).build());
    }
    
    public void broadcast(final String message) {
        for (final UUID uuid : this.players) {
            final Player player = this.plugin.getServer().getPlayer(uuid);
            player.sendMessage(message);
        }
    }
    
    public void broadcastWithSound(final String message, final Sound sound) {
        for (final UUID uuid : this.players) {
            final Player player = this.plugin.getServer().getPlayer(uuid);
            player.sendMessage(message);
            player.playSound(player.getLocation(), sound, 10.0f, 1.0f);
        }
    }
    
    public void setCountdown(final int countdown) {
        this.countdown = countdown;
    }
    
    public int decrementCountdown() {
        return --this.countdown;
    }
    
    public int decrementAnnounce() {
        return --this.announceCountdown;
    }
    
    public int getDesecrentAnn() {
        return this.announceCountdown;
    }
    
    public int getCooldown() {
        return this.countdown;
    }
    
    public void setPregame(final int pregame) {
        this.pregame = pregame;
    }
    
    public int decrementPregame() {
        return --this.pregame;
    }
    
    public int getPregame() {
        return this.pregame;
    }
    
    public void setTournamentState(final TournamentState tournamentState) {
        this.tournamentState = tournamentState;
    }
    
    public TournamentState getTournamentState() {
        return this.tournamentState;
    }
    
    public void setCurrentRound(final int currentRound) {
        this.currentRound = currentRound;
    }
    
    public int getCurrentRound() {
        return this.currentRound;
    }
    
    public int getId() {
        return this.id;
    }
    
    public Set<UUID> getPlayers() {
        return this.players;
    }
    
    public int getSize() {
        return this.size;
    }
    
    public TournamentType getType() {
        return this.type;
    }
    
    public Player getHoster() {
        return this.hoster;
    }
    
    public void setFirstPlayer(final Player firstPlayer) {
        this.firstPlayer = firstPlayer;
    }
    
    public Player getFirstPlayer() {
        return this.firstPlayer;
    }
    
    public void setSecondPlayer(final Player secondPlayer) {
        this.secondPlayer = secondPlayer;
    }
    
    public Player getSecondPlayer() {
        return this.secondPlayer;
    }
}
