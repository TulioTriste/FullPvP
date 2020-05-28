package net.bfcode.fullpvp.utilities;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationUtils{
	public static String getString(final Location loc){
		final StringBuilder builder = new StringBuilder();
		if(loc == null){
			return "unset";
		}
		builder.append(loc.getX()).append("|");
		builder.append(loc.getY()).append("|");
		builder.append(loc.getZ()).append("|");
		builder.append(loc.getWorld().getName()).append("|");
		builder.append(loc.getYaw()).append("|");
		builder.append(loc.getPitch());
		return builder.toString();
	}

	public static Location getLocation(final String s){
		if(s == null || s.equals("unset") || s.equals("")){
			return null;
		}
		final String[] data = s.split("\\|");
		final double x = Double.parseDouble(data[0]);
		final double y = Double.parseDouble(data[1]);
		final double z = Double.parseDouble(data[2]);
		final World world = Bukkit.getWorld(data[3]);
		final Float yaw = Float.parseFloat(data[4]);
		final Float pitch = Float.parseFloat(data[5]);
		return new Location(world, x, y, z, yaw, pitch);
	}

	public static boolean isSameLocation(final Location loc1, final Location loc2){
		return loc1 != null && loc2 != null && loc1.equals(loc2);
	}


}
