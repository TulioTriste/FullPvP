package net.bfcode.fullpvp.listener.staffmode;

import java.util.ArrayList;
import java.util.UUID;

public enum OptionType {

	DAMAGE("Damage", new ArrayList<UUID>()),
	PLACE("Place", new ArrayList<UUID>()),
	BREAK("Break", new ArrayList<UUID>()),
	PICKUP("Pickup", new ArrayList<UUID>()),
	INTERACT("Interact", new ArrayList<UUID>()),
	CHEST("Chest", new ArrayList<UUID>());
	
	private String name;
	private ArrayList<UUID> players;
	
	private OptionType(String name, ArrayList<UUID> players) {
		this.name = name;
		this.players = players;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<UUID> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<UUID> players) {
		this.players = players;
	}
}
