package net.bfcode.fullpvp.utilities;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;

public class ColorText
{
    public static String translate(final String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    
    public static String[] translateList(final List<String> list) {
        final List<String> newList = new ArrayList<String>();
        for (final String string : list) {
            newList.add(ChatColor.translateAlternateColorCodes('&', string));
        }
        return newList.toArray(new String[0]);
    }
    
    public static List<String> translate(final List<String> message) {
        for (int i = 0; i < message.size(); ++i) {
            message.set(i, translate(message.get(i)));
        }
        return message;
    }
}
