package net.galanthus.fullpvp.commands;

import java.util.Arrays;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.galanthus.fullpvp.utilities.ArgumentExecutor;
import net.galanthus.fullpvp.utilities.BaseConstants;
import net.galanthus.fullpvp.utilities.BukkitUtils;

public abstract class BaseCommand extends ArgumentExecutor {
    private static final Pattern USAGE_REPLACER_PATTERN;
    private final String name;
    private final String description;
    private String[] aliases;
    private String usage;
    
    public BaseCommand(final String name, final String description) {
        super(name);
        this.name = name;
        this.description = description;
    }
    
    public static boolean checkNull(final CommandSender sender, final String player) {
        final Player target = BukkitUtils.playerWithNameOrUUID(player);
        if (target == null || !canSee(sender, target)) {
            sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, player));
            return true;
        }
        return false;
    }
    
    public static boolean canSee(final CommandSender sender, final Player target) {
        return target != null && (!(sender instanceof Player) || ((Player)sender).canSee(target));
    }
    
    public final String getPermission() {
        return "fullpvp.command." + this.name;
    }
    
    public boolean isPlayerOnlyCommand() {
        return false;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public String getUsage() {
        if (this.usage == null) {
            this.usage = "";
        }
        return ChatColor.RED + "Usage: " + BaseCommand.USAGE_REPLACER_PATTERN.matcher(this.usage).replaceAll(this.name);
    }
    
    public void setUsage(final String usage) {
        this.usage = usage;
    }
    
    public String getUsage(final String label) {
        return ChatColor.RED + "" + BaseCommand.USAGE_REPLACER_PATTERN.matcher(this.usage).replaceAll(label);
    }
    
    public String[] getAliases() {
        if (this.aliases == null) {
            this.aliases = ArrayUtils.EMPTY_STRING_ARRAY;
        }
        return Arrays.copyOf(this.aliases, this.aliases.length);
    }
    
    protected void setAliases(final String[] aliases) {
        this.aliases = aliases;
    }
    
    static {
        USAGE_REPLACER_PATTERN = Pattern.compile("(command)", 16);
    }
}
