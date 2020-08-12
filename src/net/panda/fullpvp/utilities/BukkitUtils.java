package net.panda.fullpvp.utilities;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import gnu.trove.list.TCharList;
import gnu.trove.list.array.TCharArrayList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.v1_8_R3.MinecraftServer;

@Getter
@RequiredArgsConstructor
public class BukkitUtils {
	
	private static ImmutableMap<ChatColor, DyeColor> CHAT_DYE_COLOUR_MAP;
    
	private static ImmutableSet<PotionEffectType> DEBUFF_TYPES;
    
	private static String STRAIGHT_LINE_TEMPLATE;
    
	public static String STRAIGHT_LINE_DEFAULT;
    
	private static TCharList COLOUR_CHARACTER_LIST;

	static {
        STRAIGHT_LINE_TEMPLATE = ChatColor.STRIKETHROUGH.toString() + Strings.repeat("-", 256);
        STRAIGHT_LINE_DEFAULT = BukkitUtils.STRAIGHT_LINE_TEMPLATE.substring(0, 55);
        CHAT_DYE_COLOUR_MAP = ImmutableMap.<ChatColor, DyeColor> builder().put(ChatColor.AQUA, DyeColor.LIGHT_BLUE).put(ChatColor.BLACK, DyeColor.BLACK).put(ChatColor.BLUE, DyeColor.LIGHT_BLUE).put(ChatColor.DARK_AQUA, DyeColor.CYAN).put(ChatColor.DARK_BLUE, DyeColor.BLUE).put(ChatColor.DARK_GRAY, DyeColor.GRAY).put(ChatColor.DARK_GREEN, DyeColor.GREEN).put(ChatColor.DARK_PURPLE, DyeColor.PURPLE).put(ChatColor.DARK_RED, DyeColor.RED).put(ChatColor.GOLD, DyeColor.ORANGE).put(ChatColor.GRAY, DyeColor.SILVER).put(ChatColor.GREEN, DyeColor.LIME).put(ChatColor.LIGHT_PURPLE, DyeColor.MAGENTA).put(ChatColor.RED, DyeColor.RED).put(ChatColor.WHITE, DyeColor.WHITE).put(ChatColor.YELLOW, DyeColor.YELLOW).build();
        DEBUFF_TYPES = ImmutableSet.<PotionEffectType> builder().add(PotionEffectType.BLINDNESS).add(PotionEffectType.CONFUSION).add(PotionEffectType.HARM).add(PotionEffectType.HUNGER).add(PotionEffectType.POISON).add(PotionEffectType.SATURATION).add(PotionEffectType.SLOW).add(PotionEffectType.SLOW_DIGGING).add(PotionEffectType.WEAKNESS).add(PotionEffectType.WITHER).build();
        ChatColor[] values = ChatColor.values();
        COLOUR_CHARACTER_LIST = (TCharList) new TCharArrayList(values.length);
        for (ChatColor colour : values) {
            BukkitUtils.COLOUR_CHARACTER_LIST.add(colour.getChar());
        }
    }
	
    public static int countColoursUsed(String id, boolean ignoreDuplicates) {
        int count = 0;
        Set<ChatColor> found = new HashSet<ChatColor>();
        for (int i = 1; i < id.length(); ++i) {
            char current = id.charAt(i);
            if (BukkitUtils.COLOUR_CHARACTER_LIST.contains(current) && id.charAt(i - 1) == '&' && (ignoreDuplicates || found.add(ChatColor.getByChar(current)))) {
                ++count;
            }
        }
        return count;
    }

    public static List<String> getCompletions(String[] args, List<String> input) {
        return getCompletions(args, input, 80);
    }

	public static ArrayList<Location> getCircle(final Location center, final double radius, final int amount) {
        final World world = center.getWorld();
        final double increment = 6.283185307179586 / amount;
        final ArrayList<Location> locations = new ArrayList<Location>();
        for (int i = 0; i < amount; ++i) {
            final double angle = i * increment;
            final double x = center.getX() + radius * Math.cos(angle);
            final double z = center.getZ() + radius * Math.sin(angle);
            locations.add(new Location(world, x, center.getY(), z));
        }
        return locations;
    }

    public static List<String> getCompletions(String[] args, List<String> input, int limit) {
        Preconditions.checkNotNull( args);
        Preconditions.checkArgument(args.length != 0);
        String argument = args[args.length - 1];
        return input.stream().filter(string -> string.regionMatches(true, 0, argument, 0, argument.length())).limit(limit).collect(Collectors.toList());
    }

    public static String getDisplayName(CommandSender sender) {
        Preconditions.checkNotNull( sender);
        return (sender instanceof Player) ? ((Player) sender).getDisplayName() : sender.getName();
    }

    public static DyeColor toDyeColor(ChatColor colour) {
        return (DyeColor) BukkitUtils.CHAT_DYE_COLOUR_MAP.get( colour);
    }

    public static MetadataValue getMetaData(Metadatable metadatable, String input, Plugin plugin) {
        List<MetadataValue> values = (List<MetadataValue>)metadatable.getMetadata(input);
        for (MetadataValue value : values) {
            if (value.getOwningPlugin() == plugin) {
                return value;
            }
        }
        return null;
    }
    
    @Deprecated
	public static Player getFinalAttacker(EntityDamageEvent ede, boolean ignoreSelf) {
        Player attacker = null;
        if (ede instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) ede;
            Entity damager = event.getDamager();
            if (event.getDamager() instanceof Player) {
                attacker = (Player) damager;
            } else if (event.getDamager() instanceof Projectile) {
                Projectile projectile = (Projectile) damager;
                ProjectileSource shooter = projectile.getShooter();
                if (shooter instanceof Player) {
                    attacker = (Player) shooter;
                }
            }
            if (attacker != null && ignoreSelf && event.getEntity().equals(attacker)) {
                attacker = null;
            }
        }
        return attacker;
    }

    public static Player playerWithNameOrUUID(String string) {
        if (string == null) {
            return null;
        }
        return JavaUtils.isUUID(string) ? Bukkit.getPlayer(UUID.fromString(string)) : Bukkit.getPlayer(string);
    }

    @Deprecated
    public static OfflinePlayer offlinePlayerWithNameOrUUID(String string) {
        if (string == null) {
            return null;
        }
        return JavaUtils.isUUID(string) ? Bukkit.getOfflinePlayer(UUID.fromString(string)) : Bukkit.getOfflinePlayer(string);
    }

    public static boolean isWithinX(Location location, Location other, double distance) {
        return location.getWorld().equals(other.getWorld()) && Math.abs(other.getX() - location.getX()) <= distance && Math.abs(other.getZ() - location.getZ()) <= distance;
    }

    public static Location getHighestLocation(Location origin) {
        return getHighestLocation(origin, null);
    }
    
    public static String formatTime(long ms) {
        boolean hour = TimeUnit.MILLISECONDS.toHours(ms) > 0L;
        boolean minute = TimeUnit.MILLISECONDS.toMinutes(ms) > 0L;
        String format;
        if (hour) {
            format = "hh:mm:ss";
        }
        else {
            if (!minute) {
                long remainingMs = ms % 1000L;
                int remainingS = (int)(ms / 1000L);
                return new DecimalFormat("#0.0s").format(Double.valueOf(remainingS + "." + remainingMs));
            }
            format = "mm:ss";
        }
        return formatTime(format, ms);
    }
    
    public static String formatTime(String format, long time) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("EST"));
        return sdf.format(new Date(time));
    }

    public static Location getHighestLocation(Location origin, Location def) {
        Preconditions.checkNotNull( origin,  "The location cannot be null");
        Location cloned = origin.clone();
        World world = cloned.getWorld();
        int x = cloned.getBlockX();
        int y = world.getMaxHeight();
        int z = cloned.getBlockZ();
        while (y > origin.getBlockY()) {
            Block block = world.getBlockAt(x, --y, z);
            if (!block.isEmpty()) {
                Location next = block.getLocation();
                next.setPitch(origin.getPitch());
                next.setYaw(origin.getYaw());
                return next;
            }
        }
        return def;
    }

    public static boolean isDebuff(PotionEffectType type) {
        return BukkitUtils.DEBUFF_TYPES.contains( type);
    }

    public static boolean isDebuff(PotionEffect potionEffect) {
        return isDebuff(potionEffect.getType());
    }

    public static boolean isDebuff(ThrownPotion thrownPotion) {
        for (PotionEffect effect : thrownPotion.getEffects()) {
            if (isDebuff(effect)) {
                return true;
            }
        }
        return false;
    }
    
    public static ImmutableMap<ChatColor, DyeColor> getCHAT_DYE_COLOUR_MAP() {
		return CHAT_DYE_COLOUR_MAP;
	}
    
    public static TCharList getCOLOUR_CHARACTER_LIST() {
		return COLOUR_CHARACTER_LIST;
	}
    
    public static ImmutableSet<PotionEffectType> getDEBUFF_TYPES() {
		return DEBUFF_TYPES;
	}
    
    public static String getSTRAIGHT_LINE_DEFAULT() {
		return STRAIGHT_LINE_DEFAULT;
	}
    
    public static String getSTRAIGHT_LINE_TEMPLATE() {
		return STRAIGHT_LINE_TEMPLATE;
	}

    public static long getIdleTime(final Player player) {
        Preconditions.checkNotNull((Object)player);
        final long idleTime = ((CraftPlayer)player).getHandle().getChunkZ();
        return (idleTime > 0L) ? (MinecraftServer.az() - idleTime) : 0L;
    }
}
