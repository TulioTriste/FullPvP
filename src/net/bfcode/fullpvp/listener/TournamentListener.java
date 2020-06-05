package net.bfcode.fullpvp.listener;

import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.bfcode.fullpvp.FullPvP;
import net.bfcode.fullpvp.kit.event.KitApplyEvent;
import net.bfcode.fullpvp.tournaments.Tournament;
import net.bfcode.fullpvp.tournaments.TournamentState;
import net.bfcode.fullpvp.tournaments.TournamentType;
import net.bfcode.fullpvp.tournaments.file.TournamentFile;
import net.bfcode.fullpvp.utilities.ColorText;
import net.bfcode.fullpvp.utilities.ItemMaker;
import net.bfcode.fullpvp.utilities.LocationUtils;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.Listener;

public class TournamentListener implements Listener {
	
    private FullPvP plugin;
    
    public TournamentListener(final FullPvP plugin) {
    	Bukkit.getPluginManager().registerEvents(this, plugin);
        this.plugin = FullPvP.getInstance();
    }
    
    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent event) {
        final Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE && player.hasPermission("hcf.utils.staff")) {
            return;
        }
        if (this.plugin.getTournamentManager().getTournament() != null && this.plugin.getTournamentManager().isInTournament(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage(ColorText.translate("&cNo tienes permisos para romper en el evento."));
        }
    }
    
    @EventHandler
    public void onKitApply(final KitApplyEvent event) {
        if (this.plugin.getTournamentManager().isInTournament(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onCommand(final PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();
        if (this.plugin.getTournamentManager().isInTournament(player) && (event.getMessage().equalsIgnoreCase("/staff") || event.getMessage().equalsIgnoreCase("/mod") || event.getMessage().equalsIgnoreCase("/v") || event.getMessage().equalsIgnoreCase("/vanish") || event.getMessage().equalsIgnoreCase("/hcf:staff") || event.getMessage().equalsIgnoreCase("/hcf:vanish") || event.getMessage().equalsIgnoreCase("/hcf:mod") || event.getMessage().equalsIgnoreCase("/hcf:v") || event.getMessage().equalsIgnoreCase("/f") || event.getMessage().equalsIgnoreCase("hcf:/spawn") || event.getMessage().equalsIgnoreCase("/spawn") || event.getMessage().equalsIgnoreCase("/enderchest") || event.getMessage().equalsIgnoreCase("/ec") || event.getMessage().equalsIgnoreCase("/echest") || event.getMessage().equalsIgnoreCase("/chest") || event.getMessage().equalsIgnoreCase("/pv") || event.getMessage().equalsIgnoreCase("/playervault") || event.getMessage().equalsIgnoreCase("/faction") || event.getMessage().equalsIgnoreCase("/f") || event.getMessage().equalsIgnoreCase("/fac") || event.getMessage().equalsIgnoreCase("/f home") || event.getMessage().equalsIgnoreCase("/fac home") || event.getMessage().equalsIgnoreCase("/faction home") || event.getMessage().equalsIgnoreCase("/kit") || event.getMessage().equalsIgnoreCase("/gkit") || event.getMessage().equalsIgnoreCase("/kits") || event.getMessage().equalsIgnoreCase("/reclaim") || event.getMessage().equalsIgnoreCase("/more") || event.getMessage().equalsIgnoreCase("/feed") || event.getMessage().equalsIgnoreCase("/heal") || event.getMessage().equalsIgnoreCase("/rename") || event.getMessage().equalsIgnoreCase("/reclaim") || event.getMessage().equalsIgnoreCase("/fix") || event.getMessage().equalsIgnoreCase("/repair") || event.getMessage().contains("/ec") || event.getMessage().contains("/enderchest") || event.getMessage().equalsIgnoreCase("/fixall"))) {
            event.setCancelled(true);
            player.sendMessage(ColorText.translate("&cNo puedes usar este comando en el evento."));
        }
    }
    
    @EventHandler
    public void onBlockBreak(final BlockBreakEvent event) {
        final Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE && player.hasPermission("hcf.utils.staff")) {
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
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (player.getItemInHand().getType().equals(Material.NETHER_STAR) && 
            		player.getItemInHand().getItemMeta().hasDisplayName() && 
            		player.getItemInHand().getItemMeta().hasLore() && 
            		this.plugin.getTournamentManager().getTournament() != null && 
            		this.plugin.getTournamentManager().getTournament().getTournamentState() != 
            		TournamentState.FIGHTING && this.plugin.getTournamentManager().isInTournament(player.getUniqueId())) {
                player.performCommand("tournament leave");
            }
            Tournament tournament = this.plugin.getTournamentManager().getTournament();
            if (tournament.getType() == TournamentType.FFA && player.getItemInHand().getType().equals(Material.ENCHANTED_BOOK) && player.getItemInHand().getItemMeta().hasDisplayName() && player.getItemInHand().getItemMeta().hasLore() && this.plugin.getTournamentManager().getTournament() != null && this.plugin.getTournamentManager().isInTournament(player.getUniqueId())) {
                final PlayerInventory inventory = player.getInventory();
                inventory.clear();
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 0));
                inventory.setHelmet(new ItemMaker(Material.DIAMOND_HELMET).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).enchant(Enchantment.DURABILITY, 3).create());
                inventory.setChestplate(new ItemMaker(Material.DIAMOND_CHESTPLATE).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).enchant(Enchantment.DURABILITY, 3).create());
                inventory.setLeggings(new ItemMaker(Material.DIAMOND_LEGGINGS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).enchant(Enchantment.DURABILITY, 3).create());
                inventory.setBoots(new ItemMaker(Material.DIAMOND_BOOTS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.PROTECTION_FALL, 4).create());
                inventory.setItem(0, new ItemMaker(Material.DIAMOND_SWORD).enchant(Enchantment.DAMAGE_ALL, 1).enchant(Enchantment.DURABILITY, 3).create());
                inventory.setItem(1, new ItemMaker(Material.ENDER_PEARL, 16).create());
                inventory.setItem(8, new ItemMaker(Material.BAKED_POTATO, 64).create());
                for (int i = 0; i < 17; ++i) {
                    inventory.addItem(new ItemStack[] { new ItemStack(Material.POTION, 1, (short)16421) });
                }
                player.updateInventory();
            }
            if (player.getGameMode() == GameMode.CREATIVE) {
                return;
            }
            if (player.getItemInHand().getType().equals(Material.ENDER_PEARL) && this.plugin.getTournamentManager().getTournament().getTournamentState() == TournamentState.FIGHTING && this.plugin.getTournamentManager().getTournament().isActiveProtection()) {
                event.setUseItemInHand(Event.Result.DENY);
                player.sendMessage(ColorText.translate("&cEnderpearls desabilitadas en el periodo de gracia!"));
            }
        }
    }
    
    @EventHandler
    public void onItemDrop(final PlayerDropItemEvent event) {
        final Player player = event.getPlayer();
        if (this.plugin.getTournamentManager().isInTournament(player.getUniqueId())) {
            event.setCancelled(true);
            player.updateInventory();
            if (this.plugin.getTournamentManager().getTournament().getTournamentState() == TournamentState.FIGHTING) {
                if (player.getItemInHand().getType().equals(Material.ENCHANTED_BOOK) && player.getItemInHand().getItemMeta().hasDisplayName() && player.getItemInHand().getItemMeta().hasLore()) {
                    player.updateInventory();
                    return;
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
        final Player player = event.getPlayer();
        if (this.plugin.getTournamentManager().getTournament() != null && this.plugin.getTournamentManager().isInTournament(player.getUniqueId())) {
            if (this.plugin.getTournamentManager().getTournament().getType() == TournamentType.SUMO && player.getLocation().getBlockY() <= LocationUtils.getLocation(TournamentFile.getConfig().getString("Locations.Sumo.First")).getBlockY() - 2 && !player.isDead()) {
                player.setHealth(0.0);
            }
        }
    }
    
    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent event) {
        final Player player = event.getEntity();
        if (this.plugin.getTournamentManager().getTournament() != null && this.plugin.getTournamentManager().getTournament().getTournamentState() == TournamentState.FIGHTING && this.plugin.getTournamentManager().isInTournament(player.getUniqueId())) {
            new BukkitRunnable() {
                public void run() {
                    TournamentListener.this.plugin.getTournamentManager().playerLeft(TournamentListener.this.plugin.getTournamentManager().getTournament(), player, false);
                }
            }.runTaskLater(this.plugin, 20L);
        }
        final Tournament tournament = this.plugin.getTournamentManager().getTournament();
        if(player.getKiller() == null) {
        	Player first = tournament.getFirstPlayer();
        	Player second = tournament.getSecondPlayer();
        	if(FullPvP.getInstance().getTournamentManager().isInTournament(first)) {
        		tournament.teleport(first, "Sumo.Spawn");
        	} else if(FullPvP.getInstance().getTournamentManager().isInTournament(second)) {
        		tournament.teleport(second, "Sumo.Spawn");
        	}
        }
        else if(player.getKiller() instanceof Player && plugin.getTournamentManager().isInTournament(player.getKiller())) {
            tournament.teleport(player.getKiller(), "Sumo.Spawn");	
        }
        if (this.plugin.getTournamentManager().isInTournament(player) && (tournament.getType() == TournamentType.FFA)) {
            event.getDrops().clear();
        }
    }
    
    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        if (this.plugin.getTournamentManager().isInTournament(player.getUniqueId())) {
            this.plugin.getTournamentManager().playerLeft(this.plugin.getTournamentManager().getTournament(), player, false);
        }
    }
    
    @EventHandler
    public void onPlayerKick(final PlayerKickEvent event) {
        final Player player = event.getPlayer();
        if (this.plugin.getTournamentManager().isInTournament(player.getUniqueId())) {
            this.plugin.getTournamentManager().playerLeft(this.plugin.getTournamentManager().getTournament(), player, false);
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
    public void onInventoryClick(InventoryClickEvent event) {
    	Player player = (Player) event.getWhoClicked();
    	if(!this.plugin.getTournamentManager().isInTournament(player)) {
    		return;
    	}
    	if (event.getCurrentItem() == null || event.getSlotType() == null || event.getCurrentItem().getType() == Material.AIR) {
			return;
		}
    	if(event.getCurrentItem().getType().equals(Material.DIAMOND_BOOTS) || event.getCurrentItem().getType().equals(Material.DIAMOND_LEGGINGS) || event.getCurrentItem().getType().equals(Material.DIAMOND_CHESTPLATE) || event.getCurrentItem().getType().equals(Material.DIAMOND_HELMET)) {
    		player.sendMessage(ColorText.translate("&eNo puedes mover estos items en t� inventario."));
    		event.setCancelled(true);
    	}
    }
    
    @EventHandler
    public void onFoodChange(final FoodLevelChangeEvent event) {
        final Player player = (Player)event.getEntity();
        if (this.plugin.getTournamentManager().isInTournament(player)) {
            if (this.plugin.getTournamentManager().getTournament().getType() == TournamentType.SUMO) {
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
