package net.bfcode.fullpvp.listener.staffmode;

import org.bukkit.GameMode;
import org.bukkit.inventory.ItemStack;

public class StaffPlayerData {

	private ItemStack[] contents;
	private ItemStack[] armor;
	private GameMode gameMode;
	
	public StaffPlayerData() { }

	public ItemStack[] getContents() {
		return contents;
	}

	public void setContents(ItemStack[] contents) {
		this.contents = contents;
	}

	public ItemStack[] getArmor() {
		return armor;
	}

	public void setArmor(ItemStack[] armor) {
		this.armor = armor;
	}

	public GameMode getGameMode() {
		return gameMode;
	}

	public void setGameMode(GameMode gameMode) {
		this.gameMode = gameMode;
	}
}
