package net.panda.fullpvp.listener;

import java.util.List;
import java.util.Random;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import net.panda.fullpvp.FullPvP;
import net.panda.fullpvp.utilities.ColorText;
import net.panda.fullpvp.utilities.ItemMaker;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class HeadLootListener implements Listener
{
    public HeadLootListener(final FullPvP plugin) {
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
    }
    
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (player.getItemInHand().getType() == Material.SKULL_ITEM && player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ColorText.translate("&6Head Loot &7(Right Click)"))) {
                event.setCancelled(true);
                this.giveLoot(player);
                this.decrementItemInHand(player);
                player.updateInventory();
                return;
            }
            if (player.getItemInHand().getType() == Material.PAPER && player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ColorText.translate("&eMoney &7[$35K]")) && player.getItemInHand().getItemMeta().hasLore()) {
                this.decrementItemInHand(player);
                player.updateInventory();
                FullPvP.getInstance().getEconomyManager().setBalance(player.getUniqueId(), FullPvP.getInstance().getEconomyManager().getBalance(player.getUniqueId()) + 35000);
                player.sendMessage(ColorText.translate("&aYou have received 35K of money."));
                return;
            }
        }
    }
    
    private void giveLoot(final Player player) {
        final List<ItemStack> items = new ArrayList<ItemStack>();
        items.add(new ItemMaker(Material.DIAMOND_SWORD).enchant(Enchantment.DAMAGE_ALL, 5).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.FIRE_ASPECT, 2).create());
        items.add(new ItemMaker(Material.DIAMOND_HELMET).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).enchant(Enchantment.DURABILITY, 3).create());
        items.add(new ItemMaker(Material.DIAMOND_CHESTPLATE).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).enchant(Enchantment.DURABILITY, 3).create());
        items.add(new ItemMaker(Material.DIAMOND_LEGGINGS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).enchant(Enchantment.DURABILITY, 3).create());
        items.add(new ItemMaker(Material.DIAMOND_BOOTS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.PROTECTION_FALL, 4).create());
        items.add(new ItemMaker(Material.GOLDEN_APPLE, 20).data((short)1).create());
        items.add(new ItemMaker(Material.EMERALD_BLOCK, 48).create());
        items.add(new ItemMaker(Material.POTION, 12).data((short)8233).displayName("&cStrenght Potion &7[x12]").create());
        items.add(new ItemMaker(Material.POTION, 12).data((short)8226).displayName("&bSpeed Potion &7[x12]").create());
        items.add(new ItemMaker(Material.PAPER).displayName("&eMoney &7[$35K]").lore("&7Right-Click to receive the money.").create());
        final int random = new Random().nextInt(items.size());
        final ItemStack items2 = items.get(random);
        final ItemStack item = new ItemStack(items2);
        player.getInventory().addItem(new ItemStack[] { item });
        Bukkit.broadcastMessage(ColorText.translate("&9" + player.getName() + " &ehas opened a &6Head Loot&e."));
    }
    
    private void decrementItemInHand(final Player player) {
        final ItemStack itemStack = player.getItemInHand();
        if (itemStack.getAmount() <= 1) {
            player.setItemInHand(new ItemStack(Material.AIR, 1));
        }
        else {
            itemStack.setAmount(itemStack.getAmount() - 1);
        }
    }
}
