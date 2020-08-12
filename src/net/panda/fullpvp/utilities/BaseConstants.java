package net.panda.fullpvp.utilities;

import org.bukkit.ChatColor;

public final class BaseConstants
{
    public static final String PLAYER_WITH_NAME_OR_UUID_NOT_FOUND;
    
    public static net.md_5.bungee.api.ChatColor fromBukkit(ChatColor chatColor){
        return net.md_5.bungee.api.ChatColor.values()[chatColor.ordinal()];
    }
    
    static {
        PLAYER_WITH_NAME_OR_UUID_NOT_FOUND = ChatColor.RED + "Player '" + ChatColor.WHITE + "%1$s" + ChatColor.RED + "' not found.";
    }
}
