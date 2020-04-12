package net.galanthus.fullpvp.listener;

import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import net.galanthus.fullpvp.FullPvP;
import net.galanthus.fullpvp.utilities.ColorText;
import net.galanthus.fullpvp.utilities.InventoryMaker;
import net.galanthus.fullpvp.utilities.ItemMaker;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;


public class SellShopListener implements Listener {
	
	private int user = 500;
	private int crystal = 1000;
	private int esmerald = 1500;
	private int mysthic = 2000;
	private int legend = 2500;
	private int reab = 3000;
	
    public SellShopListener(final FullPvP plugin) {
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getState() instanceof Sign) {
            final Sign sign = (Sign)event.getClickedBlock().getState();
            if (sign.getLine(1).contains("Sell Shop")) {
            	final ItemStack PANEL = new ItemMaker(Material.STAINED_GLASS_PANE).data((short)7).displayName("").create();
                player.openInventory(new InventoryMaker(null, 2, "Sell Shop")
                		.setItem(0, new ItemMaker(Material.IRON_INGOT, 6).displayName("&7Iron Ingot").lore("","&eConvert: &fx4 Gold Ingot").create())
                		.setItem(1, new ItemMaker(Material.GOLD_INGOT, 4).displayName("&6Gold Ingot").lore("","&eConvert: &fx2 Diamond").create())
                		.setItem(2, new ItemMaker(Material.DIAMOND, 2).displayName("&bDiamond").lore("","&eConvert: &fx1 Emerald").create())
                		.setItem(3, new ItemMaker(Material.EMERALD_BLOCK, 4).displayName("&aEmerald Block").lore("","&eConvert: &f\u26C3 " + user).create())
                		.setItem(4, new ItemMaker(Material.EMERALD_BLOCK, 4).displayName("&aEmerald Block").lore("","&eConvert: &f\u26C3 " + crystal, "&fOnly for &3&lCrystal Rank","&fPurchase at &areabpvp.buycraft.net").create())
                		.setItem(5, new ItemMaker(Material.EMERALD_BLOCK, 4).displayName("&aEmerald Block").lore("","&eConvert: &f\u26C3 " + esmerald, "&fOnly for &a&lEsmerald Rank","&fPurchase at &areabpvp.buycraft.net").create())
                		.setItem(6, new ItemMaker(Material.EMERALD_BLOCK, 4).displayName("&aEmerald Block").lore("","&eConvert: &f\u26C3 " + mysthic, "&fOnly for &c&lMysthic Rank","&fPurchase at &areabpvp.buycraft.net").create())
                		.setItem(7, new ItemMaker(Material.EMERALD_BLOCK, 4).displayName("&aEmerald Block").lore("","&eConvert: &f\u26C3 " + legend, "&fOnly for &5&lLegend Rank","&fPurchase at &areabpvp.buycraft.net").create())
                		.setItem(8, new ItemMaker(Material.EMERALD_BLOCK, 4).displayName("&aEmerald Block").lore("","&eConvert: &f\u26C3 " + reab, "&fOnly for &6&lReab Rank","&fPurchase at &areabpvp.buycraft.net").create())
                		.setItem(9, PANEL)
                		.setItem(10, PANEL)
                		.setItem(11, PANEL)
                		.setItem(12, PANEL)
                		.setItem(13, new ItemMaker(Material.SKULL_ITEM).data((short)3)
                				.displayName("&6Your Information").lore("&7\u25CF &eMoney: &f$" + FullPvP.getPlugin().getEconomyManager().getBalance(player.getUniqueId())).create())
                		.setItem(14, PANEL)
                		.setItem(15, PANEL)
                		.setItem(16, PANEL)
                		.setItem(17, PANEL).create());
            }
        }
    }
    
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        final Player player = (Player)event.getWhoClicked();
        final int slot = event.getSlot();
        final String inventoryFull = ColorText.translate("&cYour inventory is full.");
        if (event.getInventory().getName().equals("Sell Shop")) {
        	if(event.getClickedInventory() == null || event.getInventory() != event.getClickedInventory()) {
        		return;
        	}
            event.setCancelled(true);
            final int balance = FullPvP.getPlugin().getEconomyManager().getBalance(player.getUniqueId());
            if (slot == 35) {
                player.closeInventory();
            }
            else if (slot == 0) {
            	ItemStack[] inv = player.getInventory().getContents();
            	if (player.getInventory().firstEmpty() < 0) {
                    player.closeInventory();
                    player.sendMessage(ColorText.translate(inventoryFull));
                    return;
                }
            	for (int i = 0; i < inv.length; i++) {
            		if (inv[i] != null) {
            			ItemStack item = new ItemStack(Material.IRON_INGOT, 6);
            			if (inv[i].getType() == Material.IRON_INGOT && inv[i].getAmount() >= item.getAmount()) {
            				ItemStack MATERIAL = new ItemMaker(Material.IRON_INGOT, 6).create();
            				ItemStack CONVERT = new ItemMaker(Material.GOLD_INGOT, 4).create();
            				player.getInventory().removeItem(new ItemStack[] { MATERIAL } );
                            player.getInventory().addItem(new ItemStack[] { CONVERT });
                            player.sendMessage(ColorText.translate("&eYou've converted x6 Iron Ingot to x4 Gold Ingot."));
                            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
            				return;
            			}
            		} else {
            			player.sendMessage(ColorText.translate("&cYou don't have x6 Iron Ingot."));
            			return;
            		}
            	}
            }
            else if (slot == 1) {
            	ItemStack[] inv = player.getInventory().getContents();
            	if (player.getInventory().firstEmpty() < 0) {
                    player.closeInventory();
                    player.sendMessage(ColorText.translate(inventoryFull));
                    return;
                }
            	for (int i = 0; i < inv.length; i++) {
            		if (inv[i] != null) {
            			ItemStack item = new ItemStack(Material.GOLD_INGOT, 4);
            			if (inv[i].getType() == Material.GOLD_INGOT && inv[i].getAmount() >= item.getAmount()) {
            				ItemStack MATERIAL = new ItemMaker(Material.GOLD_INGOT, 4).create();
            				ItemStack CONVERT = new ItemMaker(Material.DIAMOND, 2).create();
            				player.getInventory().removeItem(new ItemStack[] { MATERIAL } );
                            player.getInventory().addItem(new ItemStack[] { CONVERT });
                            player.sendMessage(ColorText.translate("&eYou've converted x4 Gold Ingot to x2 Diamond."));
                            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
            				return;
            			}
            		} else {
            			player.sendMessage(ColorText.translate("&cYou don't have x4 Gold Ingot."));
            			return;
            		}
            	}
            }
            else if (slot == 2) {
            	ItemStack[] inv = player.getInventory().getContents();
            	if (player.getInventory().firstEmpty() < 0) {
                    player.closeInventory();
                    player.sendMessage(ColorText.translate(inventoryFull));
                    return;
                }
            	for (int i = 0; i < inv.length; i++) {
            		if (inv[i] != null) {
            			ItemStack item = new ItemStack(Material.DIAMOND, 2);
            			if (inv[i].getType() == Material.DIAMOND && inv[i].getAmount() >= item.getAmount()) {
            				ItemStack MATERIAL = new ItemMaker(Material.DIAMOND, 2).create();
            				ItemStack CONVERT = new ItemMaker(Material.EMERALD, 1).create();
            				player.getInventory().removeItem(new ItemStack[] { MATERIAL } );
                            player.getInventory().addItem(new ItemStack[] { CONVERT });
                            player.sendMessage(ColorText.translate("&eYou've converted x2 Diamond to x1 Emerald."));
                            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
            				return;
            			}
            		} else {
            			player.sendMessage(ColorText.translate("&cYou don't have x2 Diamond."));
            			return;
            		}
            	}
            }
            else if (slot == 3) {
            	ItemStack[] inv = player.getInventory().getContents();
            	if (player.getInventory().firstEmpty() < 0) {
                    player.closeInventory();
                    player.sendMessage(ColorText.translate(inventoryFull));
                    return;
                }
            	for (int i = 0; i < inv.length; i++) {
            		if (inv[i] != null) {
            			ItemStack item = new ItemStack(Material.EMERALD_BLOCK, 4);
            			if (inv[i].getType() == Material.EMERALD_BLOCK && inv[i].getAmount() >= item.getAmount()) {
            				ItemStack MATERIAL = new ItemMaker(Material.EMERALD_BLOCK, 4).create();
            				player.getInventory().removeItem(new ItemStack[] { MATERIAL } );
            				FullPvP.getPlugin().getEconomyManager().setBalance(player.getUniqueId(), balance + user);
            				player.sendMessage(ColorText.translate("&eYou've received &2$" + user + " &emoney."));
                            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
            				return;	
            			}
            		} else {
            			player.sendMessage(ColorText.translate("&cYou don't have x8 Emerald Block."));
            			return;
            		}
            	}
            }
            else if (slot == 4) {
            	ItemStack[] inv = player.getInventory().getContents();
            	if (player.getInventory().firstEmpty() < 0) {
                    player.closeInventory();
                    player.sendMessage(ColorText.translate(inventoryFull));
                    return;
                }
            	for (int i = 0; i < inv.length; i++) {
            		if (player.hasPermission("fullpvp.rank.crystal")) {
		        		if (inv[i] != null) {
		        			ItemStack item = new ItemStack(Material.EMERALD_BLOCK, 4);
		        			if (inv[i].getType() == Material.EMERALD_BLOCK && inv[i].getAmount() >= item.getAmount()) {
		        				ItemStack MATERIAL = new ItemMaker(Material.EMERALD_BLOCK, 4).create();
		        				player.getInventory().removeItem(new ItemStack[] { MATERIAL } );
		        				FullPvP.getPlugin().getEconomyManager().setBalance(player.getUniqueId(), balance + crystal);
		        				player.sendMessage(ColorText.translate("&eYou've received &2$" + crystal + " &emoney."));
		                        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
		        				return;	
		        			}
		        		} else {
		        			player.sendMessage(ColorText.translate("&cYou don't have x8 Emerald Block."));
		        			return;
            			}
            		} else {
            			player.sendMessage(ColorText.translate("&cYou don't have Crystal Rank."));
            			player.sendMessage(ColorText.translate("&ePurchase at &areabpvp.buycraft.net"));
            			return;
            		}
            	}
            }
            else if (slot == 5) {
            	ItemStack[] inv = player.getInventory().getContents();
            	if (player.getInventory().firstEmpty() < 0) {
                    player.closeInventory();
                    player.sendMessage(ColorText.translate(inventoryFull));
                    return;
                }
            	for (int i = 0; i < inv.length; i++) {
            		if (player.hasPermission("fullpvp.rank.esmerald")) {
		        		if (inv[i] != null) {
		        			ItemStack item = new ItemStack(Material.EMERALD_BLOCK, 4);
		        			if (inv[i].getType() == Material.EMERALD_BLOCK && inv[i].getAmount() >= item.getAmount()) {
		        				ItemStack MATERIAL = new ItemMaker(Material.EMERALD_BLOCK, 4).create();
		        				player.getInventory().removeItem(new ItemStack[] { MATERIAL } );
		        				FullPvP.getPlugin().getEconomyManager().setBalance(player.getUniqueId(), balance + esmerald);
		        				player.sendMessage(ColorText.translate("&eYou've received &2$" + esmerald + " &emoney."));
		                        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
		        				return;	
		        			}
		        		} else {
		        			player.sendMessage(ColorText.translate("&cYou don't have x8 Emerald Block."));
		        			return;
            			}
            		} else {
            			player.sendMessage(ColorText.translate("&cYou don't have Esmerald Rank."));
            			player.sendMessage(ColorText.translate("&ePurchase at &areabpvp.buycraft.net"));
            			return;
            		}
            	}
            }
            else if (slot == 6) {
            	ItemStack[] inv = player.getInventory().getContents();
            	if (player.getInventory().firstEmpty() < 0) {
                    player.closeInventory();
                    player.sendMessage(ColorText.translate(inventoryFull));
                    return;
                }
            	for (int i = 0; i < inv.length; i++) {
            		if (player.hasPermission("fullpvp.rank.mysthic")) {
		        		if (inv[i] != null) {
		        			ItemStack item = new ItemStack(Material.EMERALD_BLOCK, 4);
		        			if (inv[i].getType() == Material.EMERALD_BLOCK && inv[i].getAmount() >= item.getAmount()) {
		        				ItemStack MATERIAL = new ItemMaker(Material.EMERALD_BLOCK, 4).create();
		        				player.getInventory().removeItem(new ItemStack[] { MATERIAL } );
		        				FullPvP.getPlugin().getEconomyManager().setBalance(player.getUniqueId(), balance + mysthic);
		        				player.sendMessage(ColorText.translate("&eYou've received &2$" + mysthic + " &emoney."));
		                        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
		        				return;	
		        			}
		        		} else {
		        			player.sendMessage(ColorText.translate("&cYou don't have x8 Emerald Block."));
		        			return;
            			}
            		} else {
            			player.sendMessage(ColorText.translate("&cYou don't have Mysthic Rank."));
            			player.sendMessage(ColorText.translate("&ePurchase at &areabpvp.buycraft.net"));
            			return;
            		}
            	}
            }
            else if (slot == 7) {
            	ItemStack[] inv = player.getInventory().getContents();
            	if (player.getInventory().firstEmpty() < 0) {
                    player.closeInventory();
                    player.sendMessage(ColorText.translate(inventoryFull));
                    return;
                }
            	for (int i = 0; i < inv.length; i++) {
            		if (player.hasPermission("fullpvp.rank.legend")) {
		        		if (inv[i] != null) {
		        			ItemStack item = new ItemStack(Material.EMERALD_BLOCK, 4);
		        			if (inv[i].getType() == Material.EMERALD_BLOCK && inv[i].getAmount() >= item.getAmount()) {
		        				ItemStack MATERIAL = new ItemMaker(Material.EMERALD_BLOCK, 4).create();
		        				player.getInventory().removeItem(new ItemStack[] { MATERIAL } );
		        				FullPvP.getPlugin().getEconomyManager().setBalance(player.getUniqueId(), balance + legend);
		        				player.sendMessage(ColorText.translate("&eYou've received &2$" + legend + " &emoney."));
		                        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
		        				return;	
		        			}
		        		} else {
		        			player.sendMessage(ColorText.translate("&cYou don't have x8 Emerald Block."));
		        			return;
            			}
            		} else {
            			player.sendMessage(ColorText.translate("&cYou don't have Legend Rank."));
            			player.sendMessage(ColorText.translate("&ePurchase at &areabpvp.buycraft.net"));
            			return;
            		}
            	}
            }
            else if (slot == 8) {
            	ItemStack[] inv = player.getInventory().getContents();
            	if (player.getInventory().firstEmpty() < 0) {
                    player.closeInventory();
                    player.sendMessage(ColorText.translate(inventoryFull));
                    return;
                }
            	for (int i = 0; i < inv.length; i++) {
            		if (player.hasPermission("fullpvp.rank.reab")) {
		        		if (inv[i] != null) {
		        			ItemStack item = new ItemStack(Material.EMERALD_BLOCK, 4);
		        			if (inv[i].getType() == Material.EMERALD_BLOCK && inv[i].getAmount() >= item.getAmount()) {
		        				ItemStack MATERIAL = new ItemMaker(Material.EMERALD_BLOCK, 4).create();
		        				player.getInventory().removeItem(new ItemStack[] { MATERIAL } );
		        				FullPvP.getPlugin().getEconomyManager().setBalance(player.getUniqueId(), balance + reab);
		        				player.sendMessage(ColorText.translate("&eYou've received &2$" + reab + " &emoney."));
		                        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
		        				return;	
		        			}
		        		} else {
		        			player.sendMessage(ColorText.translate("&cYou don't have x8 Emerald Block."));
		        			return;
            			}
            		} else {
            			player.sendMessage(ColorText.translate("&cYou don't have Reab Rank."));
            			player.sendMessage(ColorText.translate("&ePurchase at &areabpvp.buycraft.net"));
            			return;
            		}
            	}
            }
        }
    }
}
