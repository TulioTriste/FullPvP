package net.bfcode.fullpvp.listener;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import net.bfcode.fullpvp.FullPvP;
import net.bfcode.fullpvp.tournaments.Tournament;
import net.bfcode.fullpvp.tournaments.TournamentState;
import net.bfcode.fullpvp.tournaments.TournamentType;
import net.bfcode.fullpvp.tournaments.file.TournamentFile;
import net.bfcode.fullpvp.utilities.ColorText;
import net.bfcode.fullpvp.utilities.ItemMaker;
import net.bfcode.fullpvp.utilities.LocationUtils;
import net.bfcode.fullpvp.utilities.Messager;

public class TournamentListener implements Listener {

	private FullPvP plugin;
    
    public TournamentListener(FullPvP plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent event) {
        final Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE && player.hasPermission("fullpvp.utils.place")) {
            return;
        }
        if (this.plugin.getTournamentManager().getTournament() != null && this.plugin.getTournamentManager().isInTournament(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage(ColorText.translate("&cyou do not have permission to break in the event zone."));
        }
    }
    
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
    	Player player = event.getPlayer();
    	if(this.plugin.getTournamentManager().isInTournament(player)) {
    		if(event.getMessage().equalsIgnoreCase("/staff") 
    				|| event.getMessage().equalsIgnoreCase("/mod") 
    				|| event.getMessage().equalsIgnoreCase("/v") 
    				|| event.getMessage().equalsIgnoreCase("/vanish")
    				|| event.getMessage().equalsIgnoreCase("/fullpvp:staff") 
    				|| event.getMessage().equalsIgnoreCase("/fullpvp:vanish")
    				|| event.getMessage().equalsIgnoreCase("/fullpvp:mod") 
    				|| event.getMessage().equalsIgnoreCase("/fullpvp:v")
    				|| event.getMessage().equalsIgnoreCase("/f") 
    				|| event.getMessage().equalsIgnoreCase("fullpvp:/spawn")
    				|| event.getMessage().equalsIgnoreCase("/spawn")
    				|| event.getMessage().equalsIgnoreCase("/enderchest")
    				|| event.getMessage().equalsIgnoreCase("/ec")
    				|| event.getMessage().equalsIgnoreCase("/echest")
    				|| event.getMessage().equalsIgnoreCase("/pv")
    				|| event.getMessage().equalsIgnoreCase("/playervault")
    				|| event.getMessage().equalsIgnoreCase("/faction")
    				|| event.getMessage().equalsIgnoreCase("/f")
    				|| event.getMessage().equalsIgnoreCase("/fac")
    				|| event.getMessage().equalsIgnoreCase("/kit")
    				|| event.getMessage().equalsIgnoreCase("/gkit")
    				|| event.getMessage().equalsIgnoreCase("/kits")
    				|| event.getMessage().equalsIgnoreCase("/reclaim")
    				|| event.getMessage().equalsIgnoreCase("/more")
    				|| event.getMessage().equalsIgnoreCase("/feed")
    				|| event.getMessage().equalsIgnoreCase("/heal")
    				|| event.getMessage().equalsIgnoreCase("/rename")
    				|| event.getMessage().equalsIgnoreCase("/reclaim")
    				|| event.getMessage().equalsIgnoreCase("/fix")
    				|| event.getMessage().equalsIgnoreCase("/repair")
    				|| event.getMessage().equalsIgnoreCase("/fixall")) {
    			event.setCancelled(true);
    			Messager.player(player, "&cYou cannot use that commands in tournaments!");
    		}
    	}
    }
    
    @EventHandler
    public void onBlockBreak(final BlockBreakEvent event) {
        final Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE && player.hasPermission("fullpvp.utils.break")) {
            return;
        }
        if (this.plugin.getTournamentManager().getTournament() != null && this.plugin.getTournamentManager().isInTournament(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (!this.plugin.getTournamentManager().isInTournament(player.getUniqueId())) {
            return;
        }
        final Tournament tournament = FullPvP.getPlugin().getTournamentManager().getTournament();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (player.getItemInHand().getType().equals(Material.NETHER_STAR) && player.getItemInHand().getItemMeta().hasDisplayName() && player.getItemInHand().getItemMeta().hasLore() && this.plugin.getTournamentManager().getTournament() != null && this.plugin.getTournamentManager().getTournament().getTournamentState() != TournamentState.FIGHTING && this.plugin.getTournamentManager().isInTournament(player.getUniqueId())) {
                player.performCommand("tournament leave");
                tournament.rollbackInventory(player);
                player.updateInventory();
                return;
                
            }
            if(tournament.getType() == TournamentType.FFA) {
            	if (player.getItemInHand().getType().equals(Material.ENCHANTED_BOOK) && player.getItemInHand().getItemMeta().hasDisplayName() && player.getItemInHand().getItemMeta().hasLore() && this.plugin.getTournamentManager().getTournament() != null && this.plugin.getTournamentManager().isInTournament(player.getUniqueId())) {
                	final PlayerInventory inventory = player.getInventory();
                	inventory.clear();
                	inventory.setHelmet(new ItemMaker(Material.DIAMOND_HELMET).create());
                	inventory.setChestplate(new ItemMaker(Material.DIAMOND_CHESTPLATE).create());
                	inventory.setLeggings(new ItemMaker(Material.DIAMOND_LEGGINGS).create());
                	inventory.setBoots(new ItemMaker(Material.DIAMOND_BOOTS).create());
                	inventory.setItem(0, new ItemMaker(Material.DIAMOND_SWORD).create());
                	inventory.setItem(1, new ItemMaker(Material.GOLDEN_APPLE, 8).create());
                	player.updateInventory();
            	}
            }
            if(tournament.getType() == TournamentType.TNTTAG) {
            	if (player.getItemInHand().getType().equals(Material.ENCHANTED_BOOK) && player.getItemInHand().getItemMeta().hasDisplayName() && player.getItemInHand().getItemMeta().hasLore() && this.plugin.getTournamentManager().getTournament() != null && this.plugin.getTournamentManager().isInTournament(player.getUniqueId())) {
                	final PlayerInventory inventory = player.getInventory();
                	inventory.clear();
                	final int protection = 1;
                	final int sharpness = 1;
                	player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 1));
                	inventory.setHelmet(new ItemMaker(Material.DIAMOND_HELMET).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, protection).enchant(Enchantment.DURABILITY, 3).create());
                	inventory.setChestplate(new ItemMaker(Material.DIAMOND_CHESTPLATE).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, protection).enchant(Enchantment.DURABILITY, 3).create());
                	inventory.setLeggings(new ItemMaker(Material.DIAMOND_LEGGINGS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, protection).enchant(Enchantment.DURABILITY, 3).create());
                	inventory.setBoots(new ItemMaker(Material.DIAMOND_BOOTS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, protection).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.PROTECTION_FALL, 4).create());
                	inventory.setItem(0, new ItemMaker(Material.DIAMOND_SWORD).enchant(Enchantment.DAMAGE_ALL, sharpness).enchant(Enchantment.DURABILITY, 3).create());
                	inventory.setItem(1, new ItemMaker(Material.ENDER_PEARL, 16).create());
                	inventory.setItem(8, new ItemMaker(Material.BAKED_POTATO, 64).create());
                	for (int i = 0; i < 8; ++i) {
                    	inventory.addItem(new ItemStack(Material.POTION, 1, (short) 16421));
                	}
                	player.updateInventory();
            	}
            }
            if (player.getGameMode() == GameMode.CREATIVE) {
                return;
            }
            if (player.getItemInHand().getType().equals(Material.ENDER_PEARL) && this.plugin.getTournamentManager().getTournament().getTournamentState() == TournamentState.FIGHTING && this.plugin.getTournamentManager().getTournament().isActiveProtection()) {
                event.setUseItemInHand(Event.Result.DENY);
                player.sendMessage(ColorText.translate("&c&lEnderpearls &care disabled in grace period."));
            }
        }
    }
    
    @EventHandler
    public void onItemDrop(final PlayerDropItemEvent event) {
        final Player player = event.getPlayer();
        if (this.plugin.getTournamentManager().isInTournament(player.getUniqueId())) {
            event.setCancelled(true);
            if (this.plugin.getTournamentManager().getTournament().getTournamentState() == TournamentState.FIGHTING) {
                if (player.getItemInHand().getType().equals(Material.ENCHANTED_BOOK) && player.getItemInHand().getItemMeta().hasDisplayName() && player.getItemInHand().getItemMeta().hasLore()) {
                	player.updateInventory();
                    return;
                }
                if(player.getItemInHand().getType().equals(Material.DIAMOND_SPADE)) {
                	
                }
                player.updateInventory();
            }
        }
    }
    
    @EventHandler
    public void onEntityDamage(final EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            final Player player = (Player)event.getEntity();
            final Player damager = (Player)event.getDamager();
            if (this.plugin.getTournamentManager().getTournament() != null && this.plugin.getTournamentManager().getTournament().getTournamentState() == TournamentState.FIGHTING && this.plugin.getTournamentManager().getTournament().getType() == TournamentType.SUMO && this.plugin.getTournamentManager().isInTournament(player.getUniqueId()) && this.plugin.getTournamentManager().isInTournament(damager.getUniqueId())) {
                event.setDamage(0.0);
            }
        }
    }
    
    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent event) {
        final Location from = event.getFrom();
        final Location to = event.getTo();
        if (from.getBlockX() == to.getBlockX() && from.getBlockY() == to.getBlockY() && from.getBlockZ() == to.getBlockZ()) {
            return;
        }
        final Tournament tournament = FullPvP.getPlugin().getTournamentManager().getTournament();
        final Player player = event.getPlayer();
        if (this.plugin.getTournamentManager().getTournament() != null && this.plugin.getTournamentManager().getTournament().getTournamentState() == TournamentState.FIGHTING && this.plugin.getTournamentManager().isInTournament(player.getUniqueId())) {
            if (player.getLocation().getBlockY() <= 50 && tournament.getSize() == 1) {
                if (!player.isDead()) {
                    new BukkitRunnable() {
                        public void run() {
                            TournamentListener.this.plugin.getTournamentManager().playerLeft(TournamentListener.this.plugin.getTournamentManager().getTournament(), player, false);
                            tournament.rollbackInventory(player);
                        }
                    }.runTaskLater(this.plugin, 20L);
                }
            }
            else if (this.plugin.getTournamentManager().getTournament().getType() == TournamentType.SUMO && player.getLocation().getBlockY() <= LocationUtils.getLocation(TournamentFile.getConfig().getString("Locations.Sumo.First")).getBlockY() - 2 && !player.isDead()) {
                new BukkitRunnable() {
                    public void run() {
                        TournamentListener.this.plugin.getTournamentManager().playerLeft(TournamentListener.this.plugin.getTournamentManager().getTournament(), player, false);
                        tournament.rollbackInventory(player);
                    }
                }.runTaskLater(this.plugin, 20L);
            }
        }
    }
    
    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent event) {
        final Player player = event.getEntity();
        final Tournament tournament = this.plugin.getTournamentManager().getTournament();
        if(this.plugin.getTournamentManager().isInTournament(player)) {
        	if(tournament.getType() == TournamentType.FFA || tournament.getType() == TournamentType.TNTTAG) {
        		event.getDrops().clear();
        		tournament.rollbackInventory(player);
                player.updateInventory();
        	}
        	this.plugin.getTournamentManager().playerLeft(this.plugin.getTournamentManager().getTournament(), player, false);
        }
    }
    
    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
    	final Tournament tournament = this.plugin.getTournamentManager().getTournament();
        final Player player = event.getPlayer();
        if (this.plugin.getTournamentManager().isInTournament(player.getUniqueId())) {
            this.plugin.getTournamentManager().playerLeft(this.plugin.getTournamentManager().getTournament(), player, false);
            tournament.rollbackInventory(player);
            player.updateInventory();
        }
    }
    
    @EventHandler
    public void onPlayerKick(final PlayerKickEvent event) {
    	final Tournament tournament = this.plugin.getTournamentManager().getTournament();
        final Player player = event.getPlayer();
        if (this.plugin.getTournamentManager().isInTournament(player.getUniqueId())) {
            this.plugin.getTournamentManager().playerLeft(this.plugin.getTournamentManager().getTournament(), player, false);
            tournament.rollbackInventory(player);
            player.updateInventory();
        }
    }
    
    @EventHandler
    public void onEntityDamage(final EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            final Player player = (Player)event.getEntity();
            final Tournament tournament = this.plugin.getTournamentManager().getTournament();
            if (tournament != null && this.plugin.getTournamentManager().isInTournament(player)) {
                if (tournament.getTournamentState() == TournamentState.WAITING) {
                    event.setCancelled(true);
                }
                else if (tournament.getTournamentState() == TournamentState.STARTING) {
                    event.setCancelled(true);
                }
                else if (tournament.getTournamentState() == TournamentState.FIGHTING && tournament.isActiveProtection()) {
                    event.setCancelled(true);
                }
                else if (tournament.getTournamentState() == TournamentState.FIGHTING && tournament.getType() == TournamentType.SUMO) {
                    if (tournament.getFirstPlayer() == player || tournament.getSecondPlayer() == player) {
                        return;
                    }
                    event.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler
    public void onFoodChange(final FoodLevelChangeEvent event) {
        final Player player = (Player)event.getEntity();
        if (this.plugin.getTournamentManager().isInTournament(player)) {
            if (this.plugin.getTournamentManager().getTournament().getType() == TournamentType.SUMO) {
                event.setCancelled(true);
            }
            else if(this.plugin.getTournamentManager().getTournament().getType() == TournamentType.FFA) {
            	event.setCancelled(true);
            }
            else if (this.plugin.getTournamentManager().getTournament().getTournamentState() != TournamentState.FIGHTING) {
                event.setCancelled(true);
            }
            if (event.isCancelled()) {
                player.setFoodLevel(20);
            }
        }
    }
}
