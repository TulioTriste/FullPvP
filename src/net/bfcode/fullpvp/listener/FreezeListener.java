package net.bfcode.fullpvp.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.bfcode.fullpvp.FullPvP;
import net.bfcode.fullpvp.utilities.BukkitUtils;
import net.bfcode.fullpvp.utilities.ColorText;
import net.bfcode.fullpvp.utilities.Message;

public class FreezeListener implements Listener {

	private ArrayList<UUID> freezedPlayers;
	private HashMap<UUID, FreezeTask> freezeTasks;
	private boolean frozen;
	
	public FreezeListener(FullPvP plugin) {
		this.freezedPlayers = new ArrayList<UUID>();
		this.freezeTasks = new HashMap<UUID, FreezeTask>();
		this.frozen = false;
	}
	
	public void enable() {
		FullPvP.getInstance().getServer().getPluginManager().registerEvents(this, FullPvP.getInstance());
	}
	
	public void disable() {
		this.freezedPlayers.clear();
		this.freezeTasks.clear();
	}
	
	public boolean isServerFrozen() {
		return this.frozen;
	}
	
	public void freezeServer(CommandSender sender) {
		this.frozen = true;
		
		Message.sendMessage(ColorText.translate("&cServer has been frozen by &f" + sender.getName() + "&c."));
	}
	
	public void unfreezeServer(CommandSender sender) {
		this.frozen = false;
		
		Message.sendMessage(ColorText.translate("&cServer has been unfrozen by &f" + sender.getName() + "&c."));
	}
	
	public boolean isFrozen(Player player) {
		return this.freezedPlayers.contains(player.getUniqueId());
	}
	
	public boolean isBypassing(Player player) {
		return player.hasPermission("fullpvp.command.freeze.bypass");
	}
	
	public void addFreeze(Player player, Player target) {
		this.freezedPlayers.add(target.getUniqueId());
		FreezeTask task = new FreezeTask(target);
		task.runTaskTimer(FullPvP.getInstance(), 100L, 100L);
		this.freezeTasks.put(target.getUniqueId(), task);
		
		Message.sendMessage(ColorText.translate("&f" + target.getName() + " &ehas been freezed by &f" + player.getName() + "&e."), "fullpvp.command.freeze");
	}
	
	public void addPortalFreeze(Player target) {
		FreezeTask task = new FreezeTask(target);
		task.runTaskTimer(FullPvP.getInstance(), 100L, 100L);
		this.freezeTasks.put(target.getUniqueId(), task);
	}
	
	public void removePortalFreeze(Player target) {
		if(this.freezeTasks.containsKey(target.getUniqueId())) {
			this.freezeTasks.get(target.getUniqueId()).cancel();
			this.freezeTasks.remove(target.getUniqueId());
		}
	}
	
	public void setFrozen(Player player) {
		this.freezedPlayers.add(player.getUniqueId());
		FreezeTask task = new FreezeTask(player);
		task.runTaskTimer(FullPvP.getInstance(), 100L, 100L);
		this.freezeTasks.put(player.getUniqueId(), task);
	}
	
	public void removeFreeze(Player player, Player target) {
		if(this.freezeTasks.containsKey(target.getUniqueId())) {
			this.freezeTasks.get(target.getUniqueId()).cancel();
			this.freezeTasks.remove(target.getUniqueId());
		}
		if(this.freezedPlayers.contains(target.getUniqueId())) {
			this.freezedPlayers.remove(target.getUniqueId());
		}
		Message.sendMessage(ColorText.translate("&f" + target.getName() + " &ehas been unfrozen by &f" + player.getName() + "&e."), "fullpvp.command.freeze");
		target.sendMessage(ColorText.translate("&eYou have been unfrozen by &a" + player.getName() + "&e."));
	}

	public ArrayList<UUID> getFreezedPlayers() {
		return freezedPlayers;
	}
	
	@EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
    	if (event.getEntity() instanceof Player) {
    		if(event.getDamager() instanceof Player) {
    			Player victim = (Player)event.getEntity();
    			Player damager = (Player)event.getDamager();
    			
    			if(this.isServerFrozen()) {
    				event.setCancelled(true);
    				return;
    			}
    			
    			if(this.freezedPlayers.contains(damager.getUniqueId())) {
    				event.setCancelled(true);
    				damager.sendMessage(ColorText.translate("&cYou can not damage other players while frozen!"));
    			} else if(this.freezedPlayers.contains(victim.getUniqueId())) {
    				event.setCancelled(true);
    				damager.sendMessage(ColorText.translate("&c&l" + victim.getName() + " &cis frozen. You can not damage him!"));
    			}
    		} else if(event.getDamager() instanceof Projectile) {
				Projectile projectile = (Projectile) event.getDamager();
				if (projectile.getShooter() instanceof Player) {
					Player shooter = (Player) projectile.getShooter();
					if (shooter != event.getEntity()) {
						Player player = (Player) event.getEntity();
						
						if(this.isServerFrozen()) {
		    				event.setCancelled(true);
		    				return;
		    			}
		    			
		    			if(this.freezedPlayers.contains(shooter.getUniqueId())) {
		    				event.setCancelled(true);
		    				shooter.sendMessage(ColorText.translate("&cYou can not damage other players while frozen!"));
		    			} else if(this.freezedPlayers.contains(player.getUniqueId())) {
		    				event.setCancelled(true);
		    				shooter.sendMessage(ColorText.translate("&c&l" + player.getName() + " &cis frozen. You can not damage him!"));
		    			}
					}
				}
			}
    	}
    }
	
    public void onCommand(Player player, Player target, final CommandSender sender, final Command command, final String label, final String[] args) {
		this.freezedPlayers.add(target.getUniqueId());
		FreezeTask task = new FreezeTask(target);
		task.runTaskTimer(FullPvP.getInstance(), 100L, 100L);
		this.freezeTasks.put(target.getUniqueId(), task);
        if (target.hasPermission("fullpvp.command.freeze.bypass")) {
            sender.sendMessage(ColorText.translate("&cYou may not freeze to " + target.getName() + "."));
            return;
        }
	}
    
	@EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
    	Player player = event.getPlayer();
    	Action action = event.getAction();
    	
    	if(player.getGameMode().equals(GameMode.CREATIVE)) return;
    	
    	if(event.hasItem() && (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) && event.getItem().getType().equals(Material.ENDER_PEARL)) {
    		if(this.freezedPlayers.contains(player.getUniqueId())) {
    			event.setUseItemInHand(Event.Result.DENY);
    		}
    	}
    }
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		Entity entity = event.getEntity();
		
		if(entity instanceof Player) {
			Player player = (Player) entity;
			if((this.freezedPlayers.contains(player.getUniqueId()) || (this.isServerFrozen() && !this.isBypassing(player)))) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
    	Player player = event.getPlayer();
    	if(this.getFreezedPlayers().contains(player.getUniqueId())) {
    		for(String command : FullPvP.getInstance().getConfig().getStringList("freeze-disabled-commands")) {
    			if(event.getMessage().toLowerCase().startsWith("/" + command.toLowerCase())) {
    				event.setCancelled(true);
    				player.sendMessage(ColorText.translate("&cYou cannot use this command while frozen."));
    			}
    		}
    	}
    }

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Location from = event.getFrom();
    	Location to = event.getTo();
    	
    	if((this.freezedPlayers.contains(player.getUniqueId()) || (this.isServerFrozen() && !this.isBypassing(player)))) {
    		
    		if(from.getX() != to.getX() || from.getZ() != to.getZ()) {
    			player.teleport(from);
    		}
    	}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if((this.freezedPlayers.contains(player.getUniqueId()) || (this.isServerFrozen() && !this.isBypassing(player)))) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if((this.freezedPlayers.contains(player.getUniqueId()) || (this.isServerFrozen() && !this.isBypassing(player)))) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		HumanEntity humanEntity = event.getWhoClicked();
		if(humanEntity instanceof Player) {
			Player player = (Player) humanEntity;
			if((this.freezedPlayers.contains(player.getUniqueId()) || (this.isServerFrozen() && !this.isBypassing(player)))) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onPlayerItemDrop(PlayerDropItemEvent event) {
		Player player = event.getPlayer();	
		if((this.freezedPlayers.contains(player.getUniqueId()) || (this.isServerFrozen() && !this.isBypassing(player)))) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();
		if((this.freezedPlayers.contains(player.getUniqueId()) || (this.isServerFrozen() && !this.isBypassing(player)))) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if(this.freezeTasks.containsKey(player.getUniqueId())) {
			this.freezeTasks.get(player.getUniqueId()).cancel();
			this.freezeTasks.remove(player.getUniqueId());
			if (!player.hasPermission("fullpvp.command.freeze")) {
				return;
			}
			Message.sendMessage(" ");
			Message.sendMessage(" ");
			Message.sendMessage(" ");
			Message.sendMessage(ColorText.translate("&4&l" + player.getName() + " &chas left while frozen."));
			Message.sendMessage(" ");
			Message.sendMessage(" ");
			Message.sendMessage(" ");
		}
	}
	
	public class FreezeTask extends BukkitRunnable {
		
		private Player player;
		
		public FreezeTask(Player player) {
			this.player = player;
		}

		@Override
		public void run() {
            player.sendMessage(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + BukkitUtils.STRAIGHT_LINE_DEFAULT.substring(0, 37));
            player.sendMessage(ChatColor.YELLOW + " Has sido frozeado por un staff");
            player.sendMessage(ChatColor.YELLOW + "   Si te desconectas seras " + ChatColor.DARK_RED + ChatColor.BOLD + "BANEADO");
            player.sendMessage(ChatColor.YELLOW + "     Por favor obedece las instrucciones del staff");
            player.sendMessage(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + BukkitUtils.STRAIGHT_LINE_DEFAULT.substring(0, 37));
		}
	}
	
	public class FreezeTask2 extends BukkitRunnable {
		
		private Player player;
		
		public FreezeTask2(Player player) {
			this.player = player;
		}

		@Override
		public void run() {
            player.sendMessage(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + BukkitUtils.STRAIGHT_LINE_DEFAULT.substring(0, 37));
            player.sendMessage(ChatColor.YELLOW + " Click to accept use hacks");
            player.sendMessage(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + BukkitUtils.STRAIGHT_LINE_DEFAULT.substring(0, 37));
		}
	}
}
