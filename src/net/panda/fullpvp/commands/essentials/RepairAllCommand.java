package net.panda.fullpvp.commands.essentials;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.panda.fullpvp.utilities.ColorText;
import net.panda.fullpvp.utilities.Utils;

public class RepairAllCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Utils.MUST_BE_PLAYER);
			return true;
		}
		Player player = (Player) sender;
		if (!player.hasPermission("fullpvp.command.repairall")) {
			player.sendMessage(Utils.NO_PERMISSION);
			return true;
		}
		repair(player);
        ItemStack[] arrayOfItemStack;
        int localItemStack1 = (arrayOfItemStack = player.getInventory().getArmorContents()).length;
        for (int ix = 0; ix < localItemStack1; ix++) {
          ItemStack i = arrayOfItemStack[ix];
          repair(i);
        }
        player.sendMessage(ColorText.translate("&aSuccessfully repaired all items."));
		return true;
	}
	
	private void repair(ItemStack i) {
	    try {
	      if (itemCheck(i)) {
	    	  i.setDurability((short)0);
	      }
	    } catch (Exception exception) {}
	}
	
	public void repair(Player player) {
		for (int i = 0; i <= 36; i++) {
		  try {
		    ItemStack item = player.getInventory().getItem(i);
		    if (itemCheck(item)) {
		    	player.getInventory().getItem(i).setDurability((short)0);
		    }
		  } catch (Exception exception) {}
		}
	}
	
	@SuppressWarnings("deprecation")
	private boolean itemCheck(ItemStack item) {
	    if (item.getType().getId() == 256 || item.getType().getId() == 257 || 
	      item.getType().getId() == 258 || item.getType().getId() == 259 || 
	      item.getType().getId() == 261 || item.getType().getId() == 267 || 
	      item.getType().getId() == 268 || item.getType().getId() == 269 || 
	      item.getType().getId() == 270 || item.getType().getId() == 271 || 
	      item.getType().getId() == 272 || item.getType().getId() == 273 || 
	      item.getType().getId() == 274 || item.getType().getId() == 275 || 
	      item.getType().getId() == 276 || item.getType().getId() == 277 || 
	      item.getType().getId() == 278 || item.getType().getId() == 279 || 
	      item.getType().getId() == 283 || item.getType().getId() == 284 || 
	      item.getType().getId() == 285 || item.getType().getId() == 286 || 
	      item.getType().getId() == 290 || item.getType().getId() == 291 || 
	      item.getType().getId() == 292 || item.getType().getId() == 293 || 
	      item.getType().getId() == 294 || item.getType().getId() == 298 || 
	      item.getType().getId() == 299 || item.getType().getId() == 300 || 
	      item.getType().getId() == 301 || item.getType().getId() == 302 || 
	      item.getType().getId() == 303 || item.getType().getId() == 304 || 
	      item.getType().getId() == 305 || item.getType().getId() == 306 || 
	      item.getType().getId() == 307 || item.getType().getId() == 308 || 
	      item.getType().getId() == 309 || item.getType().getId() == 310 || 
	      item.getType().getId() == 311 || item.getType().getId() == 312 || 
	      item.getType().getId() == 313 || item.getType().getId() == 314 || 
	      item.getType().getId() == 315 || item.getType().getId() == 316 || 
	      item.getType().getId() == 317 || item.getType().getId() == 346 || 
	      item.getType().getId() == 359) 
	    	return true;
	    return false;
	}
}
