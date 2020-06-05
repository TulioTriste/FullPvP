package net.bfcode.fullpvp.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.bfcode.fullpvp.FullPvP;
import net.bfcode.fullpvp.utilities.ColorText;
import net.bfcode.fullpvp.utilities.InventoryMaker;
import net.bfcode.fullpvp.utilities.ItemMaker;

public class RepairSignListener implements Listener {
	
	final String crystal_permission = "fullpvp.rank.crystal";
	final String esmerald_permission = "fullpvp.rank.esmerald";
	final String mysthic_permission = "fullpvp.rank.mysthic";
	final String legend_permission = "fullpvp.rank.legend";
	final String reab_permission = "fullpvp.rank.reab";
	
	final int repair_user = 1000;
	final int repair_crystal = 900;
	final int repair_esmerald = 800;
	final int repair_mysthic = 700;
	final int repair_legend = 600;
	final int repair_reab = 500;
    final int repair_all_user = 4400;
    final int repair_all_crystal = 4000;
    final int repair_all_esmerald = 3600;
    final int repair_all_mysthic = 3200;
    final int repair_all_legend = 2800;
    final int repair_all_reab = 2400;
	
    public RepairSignListener() {
        Bukkit.getPluginManager().registerEvents(this, FullPvP.getInstance());
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getState() instanceof Sign) {
            final Sign sign = (Sign)event.getClickedBlock().getState();
            if (sign.getLine(1).contains("Fix")) {
            	if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) {
                    event.setCancelled(true);
                    player.sendMessage(ColorText.translate("&cYou must have anything in your hand."));
                    return;
                }
                if (player.getItemInHand().getDurability() == 0) {
                    event.setCancelled(true);
                    player.sendMessage(ColorText.translate("&cItem is already full repaired."));
                    return;
                }
                if (player.getItemInHand().getType().isBlock() || player.getItemInHand().getType().isTransparent() || player.getItemInHand().getType().isFlammable() || player.getItemInHand().getType().isOccluding() || player.getItemInHand().getType().isRecord() || player.getItemInHand().getType() == Material.POTION || player.getItemInHand().getType() == Material.BUCKET || player.getItemInHand().getType() == Material.LAVA_BUCKET || player.getItemInHand().getType() == Material.MILK_BUCKET || player.getItemInHand().getType() == Material.WATER_BUCKET || player.getItemInHand().getType() == Material.BOOK || player.getItemInHand().getType() == Material.ENCHANTED_BOOK || player.getItemInHand().getType() == Material.WRITTEN_BOOK || player.getItemInHand().getType() == Material.BOOK_AND_QUILL) {
                    event.setCancelled(true);
                    player.sendMessage(ColorText.translate("&cYou may not repair this item."));
                    return;
                }
                event.setCancelled(true);
                ItemStack item = player.getInventory().getItemInHand().clone();
                final ItemStack item_hand = new ItemMaker(item).displayName
                		("&6Click to repair item.").lore
        				("&eUser&7: &f\u26C3 " + repair_user,
        				"&3&lCrystal Rank&7: &f\u26C3 " + repair_crystal,
        				"&a&lEsmerald Rank&7: &f\u26C3 " + repair_esmerald,
        				"&c&lMysthic Rank&7: &f\u26C3 " + repair_mysthic,
        				"&5&lLegend Rank&7: &f\u26C3 " + repair_legend,
        				"&6&lReab Rank&7: &f\u26C3 " + repair_reab).create();
                player.openInventory(new InventoryMaker(null, 3, "Fix")
                		.setItem(0, new ItemMaker(Material.STAINED_GLASS_PANE).data((short)7).create())
                		.setItem(1, new ItemMaker(Material.STAINED_GLASS_PANE).data((short)7).create())
                		.setItem(2, new ItemMaker(Material.STAINED_GLASS_PANE).data((short)7).create())
                		.setItem(3, new ItemMaker(Material.STAINED_GLASS_PANE).data((short)7).create())
                		.setItem(4, new ItemMaker(Material.STAINED_GLASS_PANE).data((short)7).create())
                		.setItem(5, new ItemMaker(Material.STAINED_GLASS_PANE).data((short)7).create())
                		.setItem(6, new ItemMaker(Material.STAINED_GLASS_PANE).data((short)7).create())
                		.setItem(7, new ItemMaker(Material.STAINED_GLASS_PANE).data((short)7).create())
                		.setItem(8, new ItemMaker(Material.STAINED_GLASS_PANE).data((short)7).create())
                		.setItem(9, new ItemMaker(Material.STAINED_GLASS_PANE).data((short)7).create())
                		.setItem(10, new ItemMaker(Material.STAINED_GLASS_PANE).data((short)7).create())
                		.setItem(11, item_hand)
                		.setItem(12, new ItemMaker(Material.STAINED_GLASS_PANE).data((short)7).create())
                		.setItem(13, new ItemMaker(Material.SKULL_ITEM).data((short)3)
                				.displayName("&6Your Information").lore("&7\u25CF &eMoney: &f$" + FullPvP.getInstance().getEconomyManager().getBalance(player.getUniqueId())).create())
                		.setItem(14, new ItemMaker(Material.STAINED_GLASS_PANE).data((short)7).create())
                		.setItem(15, new ItemMaker(Material.ANVIL).displayName
                				("&6Click to repair all items.").lore
                				("&eUser&7: &f\u26C3 " + repair_all_user,
                				"&3&lCrystal Rank&7: &f\u26C3 " + repair_all_crystal,
                				"&a&lEsmerald Rank&7: &f\u26C3 " + repair_all_esmerald,
                				"&c&lMysthic Rank&7: &f\u26C3 " + repair_all_mysthic,
                				"&5&lLegend Rank&7: &f\u26C3 " + repair_all_legend,
                				"&6&lReab Rank&7: &f\u26C3 " + repair_all_reab).create())
                		.setItem(16, new ItemMaker(Material.STAINED_GLASS_PANE).data((short)7).create())
                		.setItem(17, new ItemMaker(Material.STAINED_GLASS_PANE).data((short)7).create())
                		.setItem(18, new ItemMaker(Material.STAINED_GLASS_PANE).data((short)7).create())
                		.setItem(19, new ItemMaker(Material.STAINED_GLASS_PANE).data((short)7).create())
                		.setItem(20, new ItemMaker(Material.STAINED_GLASS_PANE).data((short)7).create())
                		.setItem(21, new ItemMaker(Material.STAINED_GLASS_PANE).data((short)7).create())
                		.setItem(22, new ItemMaker(Material.STAINED_GLASS_PANE).data((short)7).create())
                		.setItem(23, new ItemMaker(Material.STAINED_GLASS_PANE).data((short)7).create())
                		.setItem(24, new ItemMaker(Material.STAINED_GLASS_PANE).data((short)7).create())
                		.setItem(25, new ItemMaker(Material.STAINED_GLASS_PANE).data((short)7).create())
                		.setItem(26, new ItemMaker(Material.STAINED_GLASS_PANE).data((short)7).create()).create());
            }
        }
    }
    
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        final Player player = (Player)event.getWhoClicked();
        final int slot = event.getSlot();
        if (event.getInventory().getName().equals("Fix")) {
        	if(event.getClickedInventory() == null || event.getInventory() != event.getClickedInventory()) {
        		return;
        	}
            event.setCancelled(true);
            final int balance = FullPvP.getInstance().getEconomyManager().getBalance(player.getUniqueId());
            if (slot == 11) {
                if (player.hasPermission(crystal_permission)) {
                	if (balance < repair_crystal) {
                        player.closeInventory();
                        player.sendMessage(ColorText.translate("&cYou don't have enough money to repair this item."));
                        return;
                    }
                	FullPvP.getInstance().getEconomyManager().setBalance(player.getUniqueId(), balance - repair_crystal);
                    player.getItemInHand().setDurability((short)0);
                    player.closeInventory();
                    player.sendMessage(ColorText.translate("&aYour item has been repaired successfully."));
                    player.playSound(player.getLocation(), Sound.ANVIL_USE, 1.0f, 1.0f);
                    return;
                } else if (player.hasPermission(esmerald_permission)) {
                	if (balance < repair_esmerald) {
                        player.closeInventory();
                        player.sendMessage(ColorText.translate("&cYou don't have enough money to repair this item."));
                        return;
                    }
                	FullPvP.getInstance().getEconomyManager().setBalance(player.getUniqueId(), balance - repair_esmerald);
                    player.getItemInHand().setDurability((short)0);
                    player.closeInventory();
                    player.sendMessage(ColorText.translate("&aYour item has been repaired successfully."));
                    player.playSound(player.getLocation(), Sound.ANVIL_USE, 1.0f, 1.0f);
                    return;
                } else if (player.hasPermission(mysthic_permission)) {
                	if (balance < repair_mysthic) {
                        player.closeInventory();
                        player.sendMessage(ColorText.translate("&cYou don't have enough money to repair this item."));
                        return;
                    }
                	FullPvP.getInstance().getEconomyManager().setBalance(player.getUniqueId(), balance - repair_mysthic);
                    player.getItemInHand().setDurability((short)0);
                    player.closeInventory();
                    player.sendMessage(ColorText.translate("&aYour item has been repaired successfully."));
                    player.playSound(player.getLocation(), Sound.ANVIL_USE, 1.0f, 1.0f);
                    return;
                } else if (player.hasPermission(legend_permission)) {
                	if (balance < repair_legend) {
                        player.closeInventory();
                        player.sendMessage(ColorText.translate("&cYou don't have enough money to repair this item."));
                        return;
                    }
                	FullPvP.getInstance().getEconomyManager().setBalance(player.getUniqueId(), balance - repair_legend);
                    player.getItemInHand().setDurability((short)0);
                    player.closeInventory();
                    player.sendMessage(ColorText.translate("&aYour item has been repaired successfully."));
                    player.playSound(player.getLocation(), Sound.ANVIL_USE, 1.0f, 1.0f);
                    return;
                } else if (player.hasPermission(reab_permission)) {
                	if (balance < repair_reab) {
                        player.closeInventory();
                        player.sendMessage(ColorText.translate("&cYou don't have enough money to repair this item."));
                        return;
                    }
                	FullPvP.getInstance().getEconomyManager().setBalance(player.getUniqueId(), balance - repair_reab);
                    player.getItemInHand().setDurability((short)0);
                    player.closeInventory();
                    player.sendMessage(ColorText.translate("&aYour item has been repaired successfully."));
                    player.playSound(player.getLocation(), Sound.ANVIL_USE, 1.0f, 1.0f);
                    return;
                }  else {
                	if (balance < repair_user) {
                        player.closeInventory();
                        player.sendMessage(ColorText.translate("&cYou don't have enough money to repair this item."));
                        return;
                    }
                	FullPvP.getInstance().getEconomyManager().setBalance(player.getUniqueId(), balance - repair_user);
                    player.getItemInHand().setDurability((short)0);
                    player.closeInventory();
                    player.sendMessage(ColorText.translate("&aYour item has been repaired successfully."));
                    player.playSound(player.getLocation(), Sound.ANVIL_USE, 1.0f, 1.0f);
                    return;
                }
            }
            else if (slot == 15) {
            	if (player.hasPermission(crystal_permission)) {
            		if (balance < repair_all_crystal) {
                        player.closeInventory();
                        player.sendMessage(ColorText.translate("&cYou don't have enough money to repair all item."));
                        return;
                    }
            		repair(player);
                    ItemStack[] arrayOfItemStack;
                    int localItemStack1 = (arrayOfItemStack = player.getInventory().getArmorContents()).length;
                    for (int ix = 0; ix < localItemStack1; ix++) {
                      ItemStack i = arrayOfItemStack[ix];
                      repair(i);
                    }
            		FullPvP.getInstance().getEconomyManager().setBalance(player.getUniqueId(), balance - repair_all_crystal);
                    player.closeInventory();
                    player.sendMessage(ColorText.translate("&aAll item has been repaired successfully."));
                    player.playSound(player.getLocation(), Sound.ANVIL_USE, 1.0f, 1.0f);
                    return;
            	} else if (player.hasPermission(esmerald_permission)) {
            		if (balance < repair_all_esmerald) {
                        player.closeInventory();
                        player.sendMessage(ColorText.translate("&cYou don't have enough money to repair all item."));
                        return;
                    }
            		repair(player);
                    ItemStack[] arrayOfItemStack;
                    int localItemStack1 = (arrayOfItemStack = player.getInventory().getArmorContents()).length;
                    for (int ix = 0; ix < localItemStack1; ix++) {
                      ItemStack i = arrayOfItemStack[ix];
                      repair(i);
                    }
            		FullPvP.getInstance().getEconomyManager().setBalance(player.getUniqueId(), balance - repair_all_esmerald);
                    player.closeInventory();
                    player.sendMessage(ColorText.translate("&aAll item has been repaired successfully."));
                    player.playSound(player.getLocation(), Sound.ANVIL_USE, 1.0f, 1.0f);
                    return;
            	} else if (player.hasPermission(mysthic_permission)) {
            		if (balance < repair_all_mysthic) {
                        player.closeInventory();
                        player.sendMessage(ColorText.translate("&cYou don't have enough money to repair all item."));
                        return;
                    }
            		repair(player);
                    ItemStack[] arrayOfItemStack;
                    int localItemStack1 = (arrayOfItemStack = player.getInventory().getArmorContents()).length;
                    for (int ix = 0; ix < localItemStack1; ix++) {
                      ItemStack i = arrayOfItemStack[ix];
                      repair(i);
                    }
            		FullPvP.getInstance().getEconomyManager().setBalance(player.getUniqueId(), balance - repair_all_mysthic);
                    player.closeInventory();
                    player.sendMessage(ColorText.translate("&aAll item has been repaired successfully."));
                    player.playSound(player.getLocation(), Sound.ANVIL_USE, 1.0f, 1.0f);
                    return;
            	} else if (player.hasPermission(legend_permission)) {
            		if (balance < repair_all_legend) {
                        player.closeInventory();
                        player.sendMessage(ColorText.translate("&cYou don't have enough money to repair all item."));
                        return;
                    }
            		repair(player);
                    ItemStack[] arrayOfItemStack;
                    int localItemStack1 = (arrayOfItemStack = player.getInventory().getArmorContents()).length;
                    for (int ix = 0; ix < localItemStack1; ix++) {
                      ItemStack i = arrayOfItemStack[ix];
                      repair(i);
                    }
            		FullPvP.getInstance().getEconomyManager().setBalance(player.getUniqueId(), balance - repair_all_legend);
                    player.closeInventory();
                    player.sendMessage(ColorText.translate("&aAll item has been repaired successfully."));
                    player.playSound(player.getLocation(), Sound.ANVIL_USE, 1.0f, 1.0f);
                    return;
            	} else if (player.hasPermission(reab_permission)) {
            		if (balance < repair_all_reab) {
                        player.closeInventory();
                        player.sendMessage(ColorText.translate("&cYou don't have enough money to repair all item."));
                        return;
                    }
            		repair(player);
                    ItemStack[] arrayOfItemStack;
                    int localItemStack1 = (arrayOfItemStack = player.getInventory().getArmorContents()).length;
                    for (int ix = 0; ix < localItemStack1; ix++) {
                      ItemStack i = arrayOfItemStack[ix];
                      repair(i);
                    }
            		FullPvP.getInstance().getEconomyManager().setBalance(player.getUniqueId(), balance - repair_all_reab);
                    player.closeInventory();
                    player.sendMessage(ColorText.translate("&aAll item has been repaired successfully."));
                    player.playSound(player.getLocation(), Sound.ANVIL_USE, 1.0f, 1.0f);
                    return;
            	} 
            	else {
            		if (balance < repair_all_user) {
                        player.closeInventory();
                        player.sendMessage(ColorText.translate("&cYou don't have enough money to repair all item."));
                        return;
                    }
            		repair(player);
                    ItemStack[] arrayOfItemStack;
                    int localItemStack1 = (arrayOfItemStack = player.getInventory().getArmorContents()).length;
                    for (int ix = 0; ix < localItemStack1; ix++) {
                      ItemStack i = arrayOfItemStack[ix];
                      repair(i);
                    }
            		FullPvP.getInstance().getEconomyManager().setBalance(player.getUniqueId(), balance - repair_all_user);
                    player.closeInventory();
                    player.sendMessage(ColorText.translate("&aAll item has been repaired successfully."));
                    player.playSound(player.getLocation(), Sound.ANVIL_USE, 1.0f, 1.0f);
                    return;
            	}
            }
        }
    }
    
    private void repair(ItemStack i) {
	    try {
	      if (itemCheck(i)) {
	    	  i.setDurability((short)0);
	      }
	    } catch (Exception exception) {}
	}
	
	public void repair(Player player) {
		for (int i = 0; i <= 36; i++) {
		  try {
		    ItemStack item = player.getInventory().getItem(i);
		    if (itemCheck(item)) {
		    	player.getInventory().getItem(i).setDurability((short)0);
		    }
		  } catch (Exception exception) {}
		}
	}
	
	@SuppressWarnings("deprecation")
	private boolean itemCheck(ItemStack item) {
	    if (item.getType().getId() == 256 || item.getType().getId() == 257 || 
	      item.getType().getId() == 258 || item.getType().getId() == 259 || 
	      item.getType().getId() == 261 || item.getType().getId() == 267 || 
	      item.getType().getId() == 268 || item.getType().getId() == 269 || 
	      item.getType().getId() == 270 || item.getType().getId() == 271 || 
	      item.getType().getId() == 272 || item.getType().getId() == 273 || 
	      item.getType().getId() == 274 || item.getType().getId() == 275 || 
	      item.getType().getId() == 276 || item.getType().getId() == 277 || 
	      item.getType().getId() == 278 || item.getType().getId() == 279 || 
	      item.getType().getId() == 283 || item.getType().getId() == 284 || 
	      item.getType().getId() == 285 || item.getType().getId() == 286 || 
	      item.getType().getId() == 290 || item.getType().getId() == 291 || 
	      item.getType().getId() == 292 || item.getType().getId() == 293 || 
	      item.getType().getId() == 294 || item.getType().getId() == 298 || 
	      item.getType().getId() == 299 || item.getType().getId() == 300 || 
	      item.getType().getId() == 301 || item.getType().getId() == 302 || 
	      item.getType().getId() == 303 || item.getType().getId() == 304 || 
	      item.getType().getId() == 305 || item.getType().getId() == 306 || 
	      item.getType().getId() == 307 || item.getType().getId() == 308 || 
	      item.getType().getId() == 309 || item.getType().getId() == 310 || 
	      item.getType().getId() == 311 || item.getType().getId() == 312 || 
	      item.getType().getId() == 313 || item.getType().getId() == 314 || 
	      item.getType().getId() == 315 || item.getType().getId() == 316 || 
	      item.getType().getId() == 317 || item.getType().getId() == 346 || 
	      item.getType().getId() == 359) 
	    	return true;
	    return false;
	}
}
