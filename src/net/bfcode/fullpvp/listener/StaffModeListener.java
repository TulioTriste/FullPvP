package net.bfcode.fullpvp.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import net.bfcode.fullpvp.utilities.ColorText;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.bfcode.fullpvp.FullPvP;
import net.bfcode.fullpvp.commands.StaffModeCommand;
import net.bfcode.fullpvp.staffmode.StaffInventory;

public class StaffModeListener implements Listener {
	private final Map<UUID, Location> fakeChestLocationMap = new HashMap<UUID, Location>();

	public StaffModeListener(final FullPvP plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	  public static void handleFakeChest(Player player, Chest chest, boolean open)
	  {
	    Inventory chestInventory = chest.getInventory();
	    if ((chestInventory instanceof DoubleChestInventory)) {
	      chest = (Chest)((DoubleChestInventory)chestInventory).getHolder().getLeftSide();
	    }
	    player.playSound(chest.getLocation(), open ? Sound.CHEST_OPEN : Sound.CHEST_CLOSE, 1.0F, 1.0F);
	  }
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		if (StaffModeCommand.modMode.contains(event.getPlayer())) {
			StaffModeCommand.leaveMod(event.getPlayer());
		}
	}

	@EventHandler
	public void onGamemodeChange(PlayerMoveEvent event) {
		if (event.getPlayer().hasPermission("command.staffmode")) {
			if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
				if (StaffModeCommand.modMode.contains(event.getPlayer())) {
					event.getPlayer().setGameMode(GameMode.CREATIVE);
					event.getPlayer().sendMessage(ColorText.translate("&cYou may not switch gamemodes whilst in Staff Mode."));
				}
			}
		}
	}
	
	
	@EventHandler
	public void oJoin(PlayerJoinEvent event) {
		if (event.getPlayer().hasPermission("fullpvp.command.staffmode")) {
			StaffModeCommand.enterMod(event.getPlayer());
			if (event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
				event.getPlayer().setGameMode(GameMode.CREATIVE);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamage(EntityDamageEvent e) {
		if (e.isCancelled())
			return;
		Entity entity = e.getEntity();

		if ((entity instanceof Player)) {
			Player p = (Player) entity;
			if ((StaffModeCommand.modMode.contains(p)))
				e.setCancelled(true);
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if ((!(e.getEntity() instanceof Player))) {
			return;
		}
		Player p = (Player) e.getEntity();
		if (StaffModeCommand.modMode.contains(p)) {
			e.setCancelled(true);
		}
	}

	@SuppressWarnings("unlikely-arg-type")
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		if (StaffModeCommand.modMode.contains(event.getPlayer().getName())) {
			event.getPlayer().getInventory().clear();
			event.getPlayer().setGameMode(GameMode.SURVIVAL);
			StaffModeCommand.modMode.remove(event.getPlayer().getName());
		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if ((StaffModeCommand.modMode.contains(p))) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onBreak(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if ((StaffModeCommand.modMode.contains(p))) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if ((StaffModeCommand.modMode.contains(p))) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlace(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if ((StaffModeCommand.modMode.contains(p))) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPick(PlayerPickupItemEvent e) {
		if (StaffModeCommand.modMode.contains(e.getPlayer())) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onRecord(PlayerInteractEvent e) {
		List<Player> onlinePlayers = new ArrayList<>();
		for(Player player : Bukkit.getOnlinePlayers()) {
			onlinePlayers.add(player);
		}
		Player p = e.getPlayer();
		if ((StaffModeCommand.modMode.contains(p)) && (p.getItemInHand().getType() == Material.WATCH)
				&& (e.getAction().toString().contains("RIGHT"))) {
			Random rand = new Random();
			Player random = onlinePlayers.get(rand.nextInt(onlinePlayers.size()));
			if (Bukkit.getOnlinePlayers().size() == 1) {
				p.sendMessage(ChatColor.RED + "There are not enough players to use this.");
			}
			if (Bukkit.getOnlinePlayers().size()> 1) {
				if (p != random) {
					p.teleport(random);
					p.sendMessage(ChatColor.YELLOW + "You were teleported randomly to " + ChatColor.GOLD
							+ random.getName() + ChatColor.YELLOW + ".");
				}
				if (p == random) {
					p.sendMessage(ChatColor.RED + "Oops, it just randomly picked up you, please try again.");
				}

			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onVanish(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if ((e.getAction() == Action.RIGHT_CLICK_BLOCK) || (e.getAction() == Action.LEFT_CLICK_AIR)
				|| (e.getAction() == Action.LEFT_CLICK_BLOCK)) {
			return;
		}
		if ((StaffModeCommand.modMode.contains(p)) && (p.getItemInHand().getItemMeta().getDisplayName().equals(ColorText.translate("&6Vanish: &aEnabled"))) &&
				(e.getAction().toString().contains("RIGHT"))) {
			VanishListener.getInstance().setVanish(p, false);
			p.sendMessage(ChatColor.YELLOW.toString() + "You have toggled your vanish " + ChatColor.RED + "off" + ChatColor.YELLOW + ".");
			ItemStack carpet = new ItemStack(351, 1, (short) 8);
			ItemMeta carpetMeta = carpet.getItemMeta();
			carpetMeta.setDisplayName(ColorText.translate("&6Vanish: &cDisable"));
			carpet.setItemMeta(carpetMeta);

			p.getInventory().setItemInHand(carpet);

		} else if ((StaffModeCommand.modMode.contains(p))
				&& (p.getItemInHand().getItemMeta().getDisplayName().equals(ColorText.translate("&6Vanish: &cDisable")))
				&& (e.getAction().toString().contains("RIGHT"))) {
			VanishListener.getInstance().setVanish(p, true);
			p.sendMessage(ChatColor.YELLOW.toString() + "You have toggled your vanish " + ChatColor.GREEN + "on" + ChatColor.YELLOW + ".");
			ItemStack carpet = new ItemStack(351, 1, (short) 10);
			ItemMeta carpetMeta = carpet.getItemMeta();
			carpetMeta.setDisplayName(ColorText.translate("&6Vanish: &aEnabled"));
			carpet.setItemMeta(carpetMeta);

			p.getInventory().setItemInHand(carpet);
		}
	}
	
	@EventHandler
	public void onTop(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if(StaffModeCommand.isMod(player)) {
			if(event.getAction() == Action.RIGHT_CLICK_BLOCK && player.getItemInHand().getType() == Material.GHAST_TEAR || event.getAction() == Action.RIGHT_CLICK_AIR && player.getItemInHand().getType() == Material.GHAST_TEAR) {
				Bukkit.dispatchCommand(player, "top");
			}
		}
	}

	@EventHandler
	public void onCLick(InventoryClickEvent e) {
		if (e.getInventory().getTitle().startsWith(ChatColor.translateAlternateColorCodes('&', "&eInspecting: "))) {
			e.setCancelled(true);
		}
		
	}
	@EventHandler
	public void rightClick2(PlayerInteractEntityEvent e) {
		if (!(e.getRightClicked() instanceof Player)) {
			return;
		}
		Player staff = e.getPlayer();
		Player p = (Player) e.getRightClicked();
		if ((StaffModeCommand.modMode.contains(staff)) && (staff.getGameMode() == GameMode.CREATIVE)
				&& ((p instanceof Player)) && ((staff instanceof Player))
				&& (staff.getItemInHand().getType() == Material.BOOK)) {
			StaffInventory.inspector(staff, p);
		}

	}
	
	  @EventHandler
	  public void freezeClick(PlayerInteractEntityEvent e)
	  {
	    if (!(e.getRightClicked() instanceof Player)) {
	      return;
	    }
	    Player staff = e.getPlayer();
	    Player p = (Player)e.getRightClicked();
	    if ((StaffModeCommand.modMode.contains(staff)) && (staff.getGameMode() == GameMode.CREATIVE) && 
	      ((p instanceof Player)) && ((staff instanceof Player)) && 
	      (staff.getItemInHand().getType() == Material.PACKED_ICE)) {
	      staff.chat("/freeze " + p.getName());
	    }
	  }
	  

	@SuppressWarnings("unlikely-arg-type")
	@EventHandler
	public void onTag(EntityDamageByEntityEvent e) {
		if ((!(e.getEntity() instanceof Player)) || (!(e.getDamager() instanceof Player))) {
			return;
		}
		Player staff = (Player) e.getDamager();
		if (StaffModeCommand.modMode.contains(staff.getName())) {
			e.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
	  public void onPlayerInteract(PlayerInteractEvent event)
	  {
	    Player player = event.getPlayer();
	    UUID uuid = player.getUniqueId();
	    switch (event.getAction())
	    {
	    case LEFT_CLICK_BLOCK: 
	      if ((StaffModeCommand.modMode.contains(player))) {
	        event.setCancelled(true);
	      }
	      break;
	    case RIGHT_CLICK_BLOCK: 
	      Block block = event.getClickedBlock();
	      BlockState state = block.getState();
	      if (((state instanceof Chest)) && ((StaffModeCommand.modMode.contains(player))))
	      {
	        Chest chest = (Chest)state;
	        Location chestLocation = chest.getLocation();
	        InventoryType type = chest.getInventory().getType();
	        if ((type == InventoryType.CHEST) && (this.fakeChestLocationMap.putIfAbsent(uuid, chestLocation) == null))
	        {
	          ItemStack[] contents = chest.getInventory().getContents();
	          Inventory fakeInventory = Bukkit.createInventory(null, contents.length, ChatColor.YELLOW + "[Silent] " + type.getDefaultTitle());
	          fakeInventory.setContents(contents);
	          event.setCancelled(true);
	          player.openInventory(fakeInventory);
	          handleFakeChest(player, chest, true);
	          this.fakeChestLocationMap.put(uuid, chestLocation);
	        }
	      }
	      break;
		default:
			break;
	    }
	  }
	  
	  @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
	  public void onInventoryClose(InventoryCloseEvent event)
	  {
	    Player player = (Player)event.getPlayer();
	    Location chestLocation = (Location)this.fakeChestLocationMap.remove(player.getUniqueId());
	    BlockState blockState;
	    if ((chestLocation != null) && (((blockState = chestLocation.getBlock().getState()) instanceof Chest))) {
	      handleFakeChest(player, (Chest)blockState, false);
	    }
	  }
	  
	  @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
	  public void onInventoryClick(InventoryClickEvent event)
	  {
	    HumanEntity humanEntity = event.getWhoClicked();
	    Player player;
	    ItemStack stack;
	    if (((humanEntity instanceof Player)) && (this.fakeChestLocationMap.containsKey((player = (Player)humanEntity).getUniqueId())) && ((stack = event.getCurrentItem()) != null) && (stack.getType() != Material.AIR) && (!player.hasPermission("vanish.chestinteract")))
	    {
	      event.setCancelled(true);
	      player.sendMessage(ChatColor.RED + "You cannot interact with this.");
	    }
	  }
	}