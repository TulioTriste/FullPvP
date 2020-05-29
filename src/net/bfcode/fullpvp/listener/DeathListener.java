package net.bfcode.fullpvp.listener;

import org.apache.commons.lang.WordUtils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;
import java.util.Objects;
import org.bukkit.ChatColor;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;

import net.bfcode.fullpvp.FullPvP;
import net.minecraft.server.v1_8_R3.EntityLiving;

import org.bukkit.Bukkit;

import java.util.regex.Pattern;
import org.bukkit.event.Listener;

public class DeathListener implements Listener {
	
    private static Pattern UNDERSCORE_PATTERN;
    private static Pattern LEFT_BRACKET_PATTERN;
    private static Pattern RIGHT_BRACKET_LAST_OCCURRENCE_PATTERN;
    
    public DeathListener(final FullPvP plugin) {
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerDeath(final PlayerDeathEvent event) {
        String message = event.getDeathMessage();
        if (message != null && !message.isEmpty()) {
            final Entity entity = (Entity)event.getEntity();
            final Entity killer = (Entity)this.getKiller(event);
            message = DeathListener.LEFT_BRACKET_PATTERN.matcher(message).replaceFirst(ChatColor.WHITE + "");
            message = DeathListener.RIGHT_BRACKET_LAST_OCCURRENCE_PATTERN.matcher(message).replaceFirst(ChatColor.WHITE + "$1.");
            if (entity != null) {
                message = message.replaceFirst(this.getEntityName(entity), ChatColor.RED + this.getFormattedName(entity) + ChatColor.YELLOW);
            }
            if (killer != null && !Objects.equals(killer, entity)) {
                message = message.replaceFirst(this.getEntityName(killer), ChatColor.RED + this.getFormattedName(killer) + ChatColor.YELLOW);
            }
            event.setDeathMessage(this.getDeathMessage(event.getEntity(), (Entity)this.getKiller(event), EntityDamageEvent.DamageCause.CUSTOM));
        }
    }
    
    private String getDeathMessage(final Player player, final Entity killer, final EntityDamageEvent.DamageCause cause) {
        String input = "";
        if (killer instanceof Player) {
            final ItemStack item = ((Player)killer).getItemInHand();
            if (item != null && item.getType() == Material.BOW) {
                input = ChatColor.RED + this.getFormattedName((Entity)player) + ChatColor.YELLOW + " was shot by " + ChatColor.RED + this.getFormattedName(killer);
                input = input + ChatColor.YELLOW + " from " + ChatColor.BLUE + (int)player.getLocation().distance(killer.getLocation()) + ChatColor.BLUE + " blocks" + ChatColor.YELLOW + ".";
            }
            else {
                input = ChatColor.RED + this.getFormattedName((Entity)player) + ChatColor.YELLOW + " was slain by " + ChatColor.RED + this.getFormattedName(killer);
            }
        }
        else if (cause == EntityDamageEvent.DamageCause.FALL) {
            input = ChatColor.RED + this.getFormattedName((Entity)player) + ChatColor.YELLOW + " fell from a high place.";
        }
        else if (cause == EntityDamageEvent.DamageCause.FIRE) {
            input = ChatColor.RED + this.getFormattedName((Entity)player) + ChatColor.YELLOW + " died to fire.";
        }
        else if (cause == EntityDamageEvent.DamageCause.LIGHTNING) {
            input = ChatColor.RED + this.getFormattedName((Entity)player) + ChatColor.YELLOW + " died to lightning.";
        }
        else if (cause == EntityDamageEvent.DamageCause.WITHER) {
            input = ChatColor.RED + this.getFormattedName((Entity)player) + ChatColor.YELLOW + " withered away.";
        }
        else if (cause == EntityDamageEvent.DamageCause.DROWNING) {
            input = ChatColor.RED + this.getFormattedName((Entity)player) + ChatColor.YELLOW + " drowned.";
        }
        else if (cause == EntityDamageEvent.DamageCause.FALLING_BLOCK) {
            input = ChatColor.RED + this.getFormattedName((Entity)player) + ChatColor.YELLOW + " died to a falling block.";
        }
        else if (cause == EntityDamageEvent.DamageCause.MAGIC) {
            input = ChatColor.RED + this.getFormattedName((Entity)player) + ChatColor.YELLOW + " died to magic.";
        }
        else if (cause == EntityDamageEvent.DamageCause.VOID) {
            input = ChatColor.RED + this.getFormattedName((Entity)player) + ChatColor.YELLOW + " fell into the void.";
        }
        else if (cause == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
            input = ChatColor.RED + this.getFormattedName((Entity)player) + ChatColor.YELLOW + " died to an explosion.";
        }
        else if (cause == EntityDamageEvent.DamageCause.LAVA) {
            input = ChatColor.RED + this.getFormattedName((Entity)player) + ChatColor.YELLOW + " burnt to a crisp.";
        }
        else if (cause == EntityDamageEvent.DamageCause.STARVATION) {
            input = ChatColor.RED + this.getFormattedName((Entity)player) + ChatColor.YELLOW + " starved to death.";
        }
        else {
            input = ChatColor.RED + this.getFormattedName((Entity)player) + ChatColor.YELLOW + " died.";
        }
        return input;
    }
    
    private CraftEntity getKiller(final PlayerDeathEvent event) {
        final EntityLiving lastAttacker = ((CraftPlayer)event.getEntity()).getHandle().lastDamager;
        return (lastAttacker != null) ? lastAttacker.getBukkitEntity() : null;
    }
    
    private String getEntityName(final Entity entity) {
        return ((CraftEntity)entity).getHandle().getScoreboardDisplayName().c();
    }
    
    private String getFormattedName(final Entity entity) {
        Objects.requireNonNull(entity, "Entity cannot be null");
        if (entity instanceof Player) {
            final Player player = (Player)entity;
            return player.getName() + ChatColor.GRAY + '[' + ChatColor.WHITE + player.getStatistic(Statistic.PLAYER_KILLS) + ChatColor.GRAY + ']';
        }
        return DeathListener.UNDERSCORE_PATTERN.matcher(WordUtils.capitalizeFully(entity.getType().name())).replaceAll(" ");
    }
    
    static {
        DeathListener.UNDERSCORE_PATTERN = Pattern.compile("_", 16);
        DeathListener.LEFT_BRACKET_PATTERN = Pattern.compile("\\[");
        DeathListener.RIGHT_BRACKET_LAST_OCCURRENCE_PATTERN = Pattern.compile("(?s)(.*)\\]");
    }
}