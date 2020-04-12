package net.galanthus.fullpvp.utilities;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Message {

	public static void sendMessage(String message) {
		for(Player online : Bukkit.getOnlinePlayers()) {
			online.sendMessage(ColorText.translate(message));
		}
	}
	
	public static void sendMessage(String message, String permission) {
		for(Player online : Bukkit.getOnlinePlayers()) {
			
			if(online.hasPermission(permission)) {
				online.sendMessage(message);
			}
		
		}
	}
	
	public static void sendMessageWithoutPlayer(Player player, String message) {
		for(Player online : Bukkit.getOnlinePlayers()) {
			if(online != player) {
				online.sendMessage(message);
			}
		}
	}
	
	public static void sendMessageWithoutPlayer(Player player, String message, String permission) {
		for(Player online : Bukkit.getOnlinePlayers()) {
			
			if(online != player && online.hasPermission(permission)) {
				online.sendMessage(message);
			}
		}
	}
}
