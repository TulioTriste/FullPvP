package net.galanthus.fullpvp.commands.essentials;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import com.google.common.collect.ImmutableSet;

import net.galanthus.fullpvp.commands.BaseCommand;
import net.galanthus.fullpvp.utilities.BaseConstants;
import net.galanthus.fullpvp.utilities.BukkitUtils;
import net.galanthus.fullpvp.utilities.ColorText;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class HealCommand extends BaseCommand {
    private static final Set<PotionEffectType> HEALING_REMOVEABLE_POTION_EFFECTS;
    
    public HealCommand() {
        super("heal", "Heals a player.");
        this.setUsage("/(command) <playerName>");
    }
    
	@Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        Player onlyTarget = null;
        Collection<Player> targets;
        if (!sender.hasPermission("fullpvp.command.heal")) {
			sender.sendMessage(ColorText.translate("&cYou don't have permission to execute this command."));
			return true;
		}
        if (args.length > 0 && sender.hasPermission(command.getPermission() + ".others")) {
            if (args[0].equalsIgnoreCase("all") && sender.hasPermission(command.getPermission() + ".all")) {
                targets = (Collection<Player>)ImmutableSet.copyOf(Bukkit.getOnlinePlayers());
            }
            else {
                if ((onlyTarget = BukkitUtils.playerWithNameOrUUID(args[0])) == null || !BaseCommand.canSee(sender, onlyTarget)) {
                    sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
                    return true;
                }
                targets = (Collection<Player>)ImmutableSet.of(onlyTarget);
            }
        }
        else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(this.getUsage(label));
                return true;
            }
            targets = (Collection<Player>)ImmutableSet.of((onlyTarget = (Player)sender));
        }
        final double maxHealth;
        if (onlyTarget != null && (maxHealth = ((Damageable)onlyTarget).getHealth()) == ((Damageable)onlyTarget).getMaxHealth()) {
            sender.sendMessage(ChatColor.RED + onlyTarget.getName() + " already has full health (" + maxHealth + ").");
            return true;
        }
        for (final Player target : targets) {
            target.setHealth(((Damageable)target).getMaxHealth());
            for (final PotionEffectType type : HealCommand.HEALING_REMOVEABLE_POTION_EFFECTS) {
                target.removePotionEffect(type);
            }
            target.setFireTicks(0);
        }
        Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Healed " + ((onlyTarget == null) ? "all online players" : onlyTarget.getName()) + '.');
        return true;
    }
    
    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        return (args.length == 1) ? null : Collections.emptyList();
    }
    
    static {
        HEALING_REMOVEABLE_POTION_EFFECTS = (Set)ImmutableSet.of((Object)PotionEffectType.SLOW, (Object)PotionEffectType.SLOW_DIGGING, (Object)PotionEffectType.POISON, (Object)PotionEffectType.WEAKNESS);
    }
}
