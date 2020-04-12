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

public class PotionShopListener implements Listener {
    public PotionShopListener(final FullPvP plugin) {
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
    }
    
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getState() instanceof Sign) {
            final Sign sign = (Sign)event.getClickedBlock().getState();
            if (sign.getLine(1).contains("Potion Shop")) {
                final ItemStack PANEL = new ItemMaker(Material.STAINED_GLASS_PANE).data((short)7).displayName("").create();
                player.openInventory(new InventoryMaker(null, 2, "Potion Shop")
                		.addItem(new ItemMaker(Material.POTION).data((short)16421).displayName("&dInstant Health Potion").lore("", "&7Level: &fII", "&7Cost: &f$150").create())
                		.addItem(new ItemMaker(Material.POTION).data((short)8258).displayName("&eSpeed Potion").lore("", "&7Level: &fI", "&7Duration: &f8 Minutes", "&7Cost: &f$200").create())
                		.addItem(new ItemMaker(Material.POTION).displayName("&eSpeed Potion").data((short)8226).lore("", "&7Level: &fII", "&7Duration: &f1:30 Minutes", "&7Cost: &f$450").create())
                		.addItem(new ItemMaker(Material.POTION).data((short)8257).displayName("&dRegeneration Potion").lore("", "&7Level: &fI", "&7Duration: &f2 Minutes", "&7Cost: &f$150").create())
                		.addItem(new ItemMaker(Material.POTION).data((short)8259).displayName("&6FireResistance Potion").lore("", "&7Level: &fI", "&7Duration: &f8 Minutes", "&7Cost: &f$200").create())
                		.addItem(new ItemMaker(Material.POTION).data((short)8265).displayName("&cStrenght Potion").lore("", "&7Level: &fI", "&7Duration: &f8 Minutes", "&7Cost: &f$200").create())
                		.addItem(new ItemMaker(Material.POTION).data((short)8233).displayName("&cStrenght Potion").lore("", "&7Level: &fII", "&7Duration: &f1:30 Minutes", "&7Cost: &f$450").create())
                		.setItem(7, PANEL)
                		.setItem(8, PANEL)
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
        final String message = ColorText.translate("&cYou don't have enough money to buy this.");
        final String inventoryFull = ColorText.translate("&cYour Inventory is Full");
        if (event.getInventory().getName().equals("Potion Shop")) {
        	if(event.getClickedInventory() == null || event.getInventory() != event.getClickedInventory()) {
        		return;
        	}
            event.setCancelled(true);
            final int balance = FullPvP.getPlugin().getEconomyManager().getBalance(player.getUniqueId());
            if (slot == 35) {
                player.closeInventory();
            }
            else if (slot == 0) {
                if (balance < 150) {
                    player.sendMessage(message);
                    return;
                }
                if (player.getInventory().firstEmpty() < 0) {
                    player.closeInventory();
                    player.sendMessage(ColorText.translate(inventoryFull));
                    return;
                }
                FullPvP.getPlugin().getEconomyManager().setBalance(player.getUniqueId(), balance - 150);
                player.getInventory().addItem(new ItemStack[] { new ItemMaker(Material.POTION).data((short)16421).create() });
                player.sendMessage(ColorText.translate("&eYou have purchased an &dInstant Health Potion&e."));
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
            }
            else if (slot == 1) {
                if (balance < 200) {
                    player.sendMessage(message);
                    return;
                }
                if (player.getInventory().firstEmpty() < 0) {
                    player.closeInventory();
                    player.sendMessage(ColorText.translate(inventoryFull));
                    return;
                }
                FullPvP.getPlugin().getEconomyManager().setBalance(player.getUniqueId(), balance - 200);
                player.getInventory().addItem(new ItemStack[] { new ItemMaker(Material.POTION).data((short)8258).create() });
                player.sendMessage(ColorText.translate("&eYou have purchased an &bSpeed Potion&e."));
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
            }
            else if (slot == 2) {
                if (balance < 450) {
                    player.sendMessage(message);
                    return;
                }
                if (player.getInventory().firstEmpty() < 0) {
                    player.closeInventory();
                    player.sendMessage(ColorText.translate(inventoryFull));
                    return;
                }
                FullPvP.getPlugin().getEconomyManager().setBalance(player.getUniqueId(), balance - 450);
                player.getInventory().addItem(new ItemStack[] { new ItemMaker(Material.POTION).data((short)8226).create() });
                player.sendMessage(ColorText.translate("&eYou have purchased an &bSpeed Potion&e."));
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
            }
            else if (slot == 3) {
                if (balance < 150) {
                    player.sendMessage(message);
                    return;
                }
                if (player.getInventory().firstEmpty() < 0) {
                    player.closeInventory();
                    player.sendMessage(ColorText.translate(inventoryFull));
                    return;
                }
                FullPvP.getPlugin().getEconomyManager().setBalance(player.getUniqueId(), balance - 150);
                player.getInventory().addItem(new ItemStack[] { new ItemMaker(Material.POTION).data((short)8257).create() });
                player.sendMessage(ColorText.translate("&eYou have purchased an &dRegeneration Potion&e."));
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
            }
            else if (slot == 4) {
                if (balance < 200) {
                    player.sendMessage(message);
                    return;
                }
                if (player.getInventory().firstEmpty() < 0) {
                    player.closeInventory();
                    player.sendMessage(ColorText.translate(inventoryFull));
                    return;
                }
                FullPvP.getPlugin().getEconomyManager().setBalance(player.getUniqueId(), balance - 200);
                player.getInventory().addItem(new ItemStack[] { new ItemMaker(Material.POTION).data((short)8259).create() });
                player.sendMessage(ColorText.translate("&eYou have purchased an &6FireResistance Potion&e."));
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
            }
            else if (slot == 5) {
                if (balance < 200) {
                    player.sendMessage(message);
                    return;
                }
                FullPvP.getPlugin().getEconomyManager().setBalance(player.getUniqueId(), balance - 200);
                player.getInventory().addItem(new ItemStack[] { new ItemMaker(Material.POTION).data((short)8265).create() });
                player.sendMessage(ColorText.translate("&eYou have purchased an &cStrenght Potion&e."));
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
            }
            else if (slot == 6) {
                if (balance < 450) {
                    player.sendMessage(message);
                    return;
                }
                if (player.getInventory().firstEmpty() < 0) {
                    player.closeInventory();
                    player.sendMessage(ColorText.translate(inventoryFull));
                    return;
                }
                FullPvP.getPlugin().getEconomyManager().setBalance(player.getUniqueId(), balance - 450);
                player.getInventory().addItem(new ItemStack[] { new ItemMaker(Material.POTION).data((short)8233).create() });
                player.sendMessage(ColorText.translate("&eYou have purchased an &cStrenght Potion&e."));
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
            }
        }
    }
}
