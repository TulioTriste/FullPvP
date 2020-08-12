package net.panda.fullpvp.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.panda.fullpvp.listener.VanishListener;
import net.panda.fullpvp.utilities.ColorText;
import net.panda.fullpvp.utilities.Utils;

public class VanishCommand implements CommandExecutor  {

	public static ArrayList<Player> staff = new ArrayList<Player>();
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("vanish")) {
			if (!sender.hasPermission("fullpvp.command.vanish")) {
				sender.sendMessage(Utils.NO_PERMISSION);
				return true;
			}
			if (args.length < 1) {
				if (!(sender instanceof Player)) {
					sender.sendMessage(Utils.MUST_BE_PLAYER);
					return true;
				}
				Player p = (Player) sender;
				VanishListener.getInstance();
				if (VanishListener.isVanished(p.getPlayer())) {
					VanishListener.getInstance().setVanish(p, false);
					sender.sendMessage(ColorText.translate("&eYou have turned your vanish &coff&e."));
					return true;
				}
				VanishListener.getInstance().setVanish(p, true);
				sender.sendMessage(ColorText.translate("&eYou have turned your vanish &aon&e."));
				return true;
			}
			if (!sender.hasPermission("fullpvp.command.vanish.others")) {
				sender.sendMessage(Utils.NO_PERMISSION);
				return true;
			}
			Player t = Bukkit.getPlayer(args[0]);
			if (t == null) {
				sender.sendMessage(Utils.PLAYER_NOT_FOUND);
				return true;
			}
			VanishListener.getInstance();
			if (VanishListener.isVanished(t.getPlayer())) {
				VanishListener.getInstance().setVanish(t, false);
				sender.sendMessage(ColorText.translate("&7You have &cdisabled &7" + t.getName() + "'s &3&lVanish Mode"));
				return true;
			}
			VanishListener.getInstance().setVanish(t, true);
			sender.sendMessage(ColorText.translate("&7You have &aenabled &7" + t.getName() + "'s &3&lVanish Mode"));
			return true;
		}
		return false;
	}

}
