package net.bfcode.fullpvp.kit;

import java.util.ArrayList;
import java.util.UUID;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.meta.ItemMeta;

import net.bfcode.fullpvp.FullPvP;
import net.bfcode.fullpvp.commands.StaffModeCommand;
import net.bfcode.fullpvp.kit.event.KitApplyEvent;
import net.bfcode.fullpvp.utilities.user.BaseUser;

import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import java.util.Objects;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import java.util.List;
import org.bukkit.event.Listener;

public class KitListener implements Listener {
    public static List<Inventory> previewInventory;
    private final FullPvP plugin;
    
    public KitListener(final FullPvP plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent e) {
        if (KitListener.previewInventory.contains(e.getInventory())) {
            KitListener.previewInventory.remove(e.getInventory());
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onInventoryClick(final InventoryClickEvent event) {
        if (KitListener.previewInventory.contains(event.getInventory())) {
            event.setCancelled(true);
            return;
        }
        final Inventory inventory = event.getInventory();
        if (inventory == null) {
            return;
        }
        final String title = inventory.getTitle();
        final HumanEntity humanEntity = event.getWhoClicked();
        if (title.contains("Kit Selector") && humanEntity instanceof Player) {
            event.setCancelled(true);
            if (!Objects.equals(event.getView().getTopInventory(), event.getClickedInventory())) {
                return;
            }
            final ItemStack stack = event.getCurrentItem();
            if (stack == null || !stack.hasItemMeta()) {
                return;
            }
            final ItemMeta meta = stack.getItemMeta();
            if (!meta.hasDisplayName()) {
                return;
            }
            final Player player = (Player)humanEntity;
            final String name = ChatColor.stripColor(stack.getItemMeta().getDisplayName());
            final Kit kit = this.plugin.getKitManager().getKit(name);
            if (kit == null) {
                return;
            }
            kit.applyTo(player, false, true);
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onKitApply(final KitApplyEvent event) {
        if (event.isForce()) {
            return;
        }
        final Player player = event.getPlayer();
        final Kit kit = event.getKit();
        if (!player.isOp() && !kit.isEnabled()) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "The " + kit.getDisplayName() + " kit is currently disabled.");
            return;
        }
        final String kitPermission = kit.getPermissionNode();
        if (kitPermission != null && !player.hasPermission(kitPermission)) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You do not have permission to use this kit.");
            return;
        }
        final UUID uuid = player.getUniqueId();
        final long minPlaytimeMillis = kit.getMinPlaytimeMillis();
        if (minPlaytimeMillis > 0L && this.plugin.getPlayTimeManager().getTotalPlayTime(uuid) < minPlaytimeMillis) {
            player.sendMessage(ChatColor.RED + "You need at least " + kit.getMinPlaytimeWords() + " minimum playtime to use kit " + kit.getDisplayName() + '.');
            event.setCancelled(true);
            return;
        }
        final BaseUser baseUser = this.plugin.getUserManager().getUser(uuid);
        final long remaining = baseUser.getRemainingKitCooldown(kit);
        if (remaining > 0L) {
            player.sendMessage(ChatColor.RED + "You cannot use the " + kit.getDisplayName() + " kit for " + DurationFormatUtils.formatDurationWords(remaining, true, true) + '.');
            event.setCancelled(true);
            return;
        }
        final int curUses = baseUser.getKitUses(kit);
        final int maxUses;
        if (curUses >= (maxUses = kit.getMaximumUses()) && maxUses != Integer.MAX_VALUE) {
            player.sendMessage(ChatColor.RED + "You have already used this kit " + curUses + '/' + maxUses + " times.");
            event.setCancelled(true);
        }
        if (StaffModeCommand.isMod(player)) {
            player.sendMessage(ChatColor.RED + "You cannot apply kits while in staff mode.");
            event.setCancelled(true);
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onKitApplyMonitor(final KitApplyEvent event) {
        if (!event.isForce()) {
            final Kit kit = event.getKit();
            final BaseUser baseUser = this.plugin.getUserManager().getUser(event.getPlayer().getUniqueId());
            baseUser.incrementKitUses(kit);
            baseUser.updateKitCooldown(kit);
        }
    }
    
    static {
        KitListener.previewInventory = new ArrayList<Inventory>();
    }
}
