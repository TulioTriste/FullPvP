package net.galanthus.fullpvp.commands.essentials;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;

import gnu.trove.map.TObjectLongMap;
import gnu.trove.map.hash.TObjectLongHashMap;
import net.galanthus.fullpvp.FullPvP;
import net.galanthus.fullpvp.commands.BaseCommand;
import net.galanthus.fullpvp.event.PlayerFreezeEvent;
import net.galanthus.fullpvp.utilities.BaseConstants;
import net.galanthus.fullpvp.utilities.BukkitUtils;
import net.galanthus.fullpvp.utilities.ColorText;
import net.galanthus.fullpvp.utilities.PlayerUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class FreezeCommand implements Listener, Runnable, CommandExecutor {
    private static final String FREEZE_BYPASS = "fullpvp.command.freeze.bypass";
    private final TObjectLongMap<UUID> frozenPlayers = new TObjectLongHashMap<>();
    private final Set<UUID> inventoryUnlock = new HashSet<>();
    private long defaultFreezeDuration;
    private static Set<UUID> frozen = new HashSet<>();

    public static boolean isFrozen(UUID uuid){
        return frozen.contains(uuid);
        
    }
    
    public FreezeCommand(FullPvP plugin) {
        this.defaultFreezeDuration = TimeUnit.MINUTES.toMillis(60);
        Bukkit.getServer().getPluginManager().registerEvents((Listener) this, (Plugin) plugin);
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this, 1, 1);
    }
    
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	if (!sender.hasPermission("fullpvp.command.freeze")) {
			sender.sendMessage(ColorText.translate("&cYou don't have permission to execute this command."));
			return true;
		}
        if (args.length < 1) {
            sender.sendMessage(ColorText.translate("&cUsage: /freeze <all|player>"));
        }
        else {
            Long freezeTicks = this.defaultFreezeDuration;
            long millis = System.currentTimeMillis();
            Player target = Bukkit.getServer().getPlayer(args[0]);
            if (target == null || !BaseCommand.canSee(sender, target)) {
                sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
                return true;
            }
            if (target.equals(sender) && target.hasPermission(FREEZE_BYPASS)) {
                sender.sendMessage(ChatColor.RED + "You cannot freeze yourself.");
                return true;
            }
            UUID targetUUID = target.getUniqueId();
            boolean shouldFreeze = this.getRemainingPlayerFrozenMillis(targetUUID) > 0;
            PlayerFreezeEvent playerFreezeEvent = new PlayerFreezeEvent(target, shouldFreeze);
            Bukkit.getServer().getPluginManager().callEvent(playerFreezeEvent);
            if (playerFreezeEvent.isCancelled()) {
                sender.sendMessage(ChatColor.RED + "Unable to freeze " + target.getName() + '.');
                return false;
            }
            if (shouldFreeze) {
                FreezeCommand.frozen.remove(target.getUniqueId());
                this.frozenPlayers.remove(targetUUID);
                inventoryUnlock.remove(targetUUID);
                target.sendMessage(ChatColor.GREEN + "You have been un-frozen.");
                target.updateInventory();
                PlayerUtil.allowMovement(target);
                Command.broadcastCommandMessage(sender, (ChatColor.YELLOW + target.getName() + " is no longer frozen"));
            } else {
                FreezeCommand.frozen.add(target.getUniqueId());
                this.frozenPlayers.put(targetUUID, millis + freezeTicks);
                String timeString = DurationFormatUtils.formatDurationWords(freezeTicks, true, true);
                PlayerUtil.denyMovement(target);
                target.sendMessage(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + BukkitUtils.STRAIGHT_LINE_DEFAULT.substring(0, 37));
                target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588"));
                target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f\u2588\u2588\u2588\u2588&4\u2588&f\u2588\u2588\u2588\u2588"));
                target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f\u2588\u2588\u2588&4\u2588&6\u2588&4\u2588&f\u2588\u2588\u2588     &eHas sido frozeado por un miembro del staff"));
                target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f\u2588\u2588&4\u2588&6\u2588&0\u2588&6\u2588&4\u2588&f\u2588\u2588        &eSi te desconectas seras &4&lBANEADO"));
                target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f\u2588&4\u2588&6\u2588&6\u2588&0\u2588&6\u2588&6\u2588&4\u2588&f\u2588   &ePor favor obedece las instrucciones del staff"));
                target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f\u2588&4\u2588&6\u2588&6\u2588&6\u2588&6\u2588&6\u2588&4\u2588&f\u2588"));
                target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4\u2588&6\u2588&6\u2588&6\u2588&0\u2588&6\u2588&6\u2588&6\u2588&4\u2588"));
                target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588"));
                target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588"));
                target.sendMessage(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + BukkitUtils.STRAIGHT_LINE_DEFAULT.substring(0, 37));
                Command.broadcastCommandMessage(sender, (ChatColor.YELLOW + target.getName() + " is now frozen for " + timeString));
            }
        }
        return true;
    }


    private int i = 0;

    public void run() {
        for(UUID uuid: frozen) {
            Player player = Bukkit.getPlayer(uuid);
            if(player != null) {
                if (i % (10 * 20) == 0) {
                    player.sendMessage(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + BukkitUtils.STRAIGHT_LINE_DEFAULT.substring(0, 37));
                    player.sendMessage(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + BukkitUtils.STRAIGHT_LINE_DEFAULT.substring(0, 37));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f\u2588\u2588\u2588\u2588&4\u2588&f\u2588\u2588\u2588\u2588"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f\u2588\u2588\u2588&4\u2588&6\u2588&4\u2588&f\u2588\u2588\u2588     &eHas sido frozeado por un miembro del staff"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f\u2588\u2588&4\u2588&6\u2588&0\u2588&6\u2588&4\u2588&f\u2588\u2588        &eSi te desconectas seras &4&lBANEADO"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f\u2588&4\u2588&6\u2588&6\u2588&0\u2588&6\u2588&6\u2588&4\u2588&f\u2588   &ePor favor obedece las instrucciones del staff"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f\u2588&4\u2588&6\u2588&6\u2588&6\u2588&6\u2588&6\u2588&4\u2588&f\u2588"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4\u2588&6\u2588&6\u2588&6\u2588&0\u2588&6\u2588&6\u2588&6\u2588&4\u2588"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588"));
                    player.sendMessage(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + BukkitUtils.STRAIGHT_LINE_DEFAULT.substring(0, 37));
                }
            }
        }
        i++;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? null : args.length == 2 && args[0].equalsIgnoreCase("lock") ? null : Collections.emptyList();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
			@SuppressWarnings("deprecation")
			Player attacker = BukkitUtils.getFinalAttacker(event, false);
            if (attacker == null) {
                return;
            }
            Player player = (Player) entity;
            if (!(this.getRemainingServerFrozenMillis() <= 0 && this.getRemainingPlayerFrozenMillis(player.getUniqueId()) <= 0 || player.hasPermission(FREEZE_BYPASS))) {
                if (!player.hasPermission(FREEZE_BYPASS)) {
                    attacker.sendMessage(ChatColor.RED + player.getName() + " is currently frozen, you may not attack.");
                    event.setCancelled(true);
                }
                return;
            }
            if (!(this.getRemainingServerFrozenMillis() <= 0 && this.getRemainingPlayerFrozenMillis(attacker.getUniqueId()) <= 0 || attacker.hasPermission(FREEZE_BYPASS))) {
                event.setCancelled(true);
                attacker.sendMessage(ChatColor.RED + "You may not attack players whilst frozen.");
            }
        }
    }
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onEntityDamage(EntityDamageEvent event) {
		Entity entity = event.getEntity();
		
		if(entity instanceof Player) {
			Player player = (Player) entity;
			if(!(this.getRemainingServerFrozenMillis() <= 0 && this.getRemainingPlayerFrozenMillis(player.getUniqueId()) <= 0 || player.hasPermission(FREEZE_BYPASS))) {
				event.setCancelled(true);
			}
		}
	}

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        if (from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ()) {
            return;
        }
        Player player = event.getPlayer();
        if (!(this.getRemainingServerFrozenMillis() <= 0 && this.getRemainingPlayerFrozenMillis(player.getUniqueId()) <= 0 || player.hasPermission(FREEZE_BYPASS))) {
            event.setTo(event.getFrom());
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL || event.getCause() == PlayerTeleportEvent.TeleportCause.COMMAND) {
            Location from = event.getFrom();
            Location to = event.getTo();
            if (from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ()) {
                return;
            }
            Player player = event.getPlayer();
            if (!(this.getRemainingServerFrozenMillis() <= 0 && this.getRemainingPlayerFrozenMillis(player.getUniqueId()) <= 0 || player.hasPermission(FREEZE_BYPASS))) {
                event.setTo(event.getFrom());
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (!(this.getRemainingServerFrozenMillis() <= 0 && this.getRemainingPlayerFrozenMillis(player.getUniqueId()) <= 0 || player.hasPermission(FREEZE_BYPASS))) {
            player.sendMessage(ChatColor.RED + "You may not use blocks whilst frozen.");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!(this.getRemainingServerFrozenMillis() <= 0 && this.getRemainingPlayerFrozenMillis(player.getUniqueId()) <= 0 || player.hasPermission(FREEZE_BYPASS))) {
            player.sendMessage(ChatColor.RED + "You may not use blocks whilst frozen.");
            event.setCancelled(true);
        }
    }

	@EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        BaseComponent[] components = new ComponentBuilder(player.getName()).color(BaseConstants.fromBukkit(ChatColor.YELLOW))
                .append(" has ")
                .append("QUIT").color(BaseConstants.fromBukkit(ChatColor.DARK_RED))
                .append(" while frozen ").color(BaseConstants.fromBukkit(ChatColor.YELLOW))
                .append("(BAN)").color(BaseConstants.fromBukkit(ChatColor.GRAY))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(
                        ChatColor.GRAY + "Click to ban " + ChatColor.WHITE + player.getName()
                )))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ban -s " + player.getName() + " Disconnected whilst frozen"))
                .create();
        if (frozen.contains(player.getUniqueId())) {
            for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                if (!online.hasPermission("command.freeze")) {
                    continue;
                }
                online.spigot().sendMessage(components);
                return;
            }
        }
    }

    public long getRemainingServerFrozenMillis() {
        return -1;
    }

    public long getRemainingPlayerFrozenMillis(UUID uuid) {
        long remaining = this.frozenPlayers.get(uuid);
        if (remaining == this.frozenPlayers.getNoEntryValue()) {
            return 0;
        }
        return remaining - System.currentTimeMillis();
    }
}
