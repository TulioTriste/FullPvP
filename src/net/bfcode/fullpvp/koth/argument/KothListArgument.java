package net.bfcode.fullpvp.koth.argument;

import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.bfcode.fullpvp.FullPvP;
import net.bfcode.fullpvp.configuration.LocationFile;
import net.bfcode.fullpvp.utilities.ColorText;
import net.bfcode.fullpvp.utilities.CommandArgument;

public class KothListArgument extends CommandArgument {
	LocationFile location;
	
    @SuppressWarnings("unused")
	private final FullPvP plugin;
    
    public KothListArgument(final FullPvP plugin) {
        super("list", "Check the uptime of an event");
        this.plugin = plugin;
        this.permission = "hcf.command.event.argument." + this.getName();
        this.location = LocationFile.getConfig();
    }
    
    public String getUsage(final String label) {
        return "/" + label + ' ' + this.getName();
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        sender.sendMessage(ColorText.translate("&6&lKOTH List: &f" + this.getKothList().toString().replace("[", "").replace("]", "")));
        return true;
    }
    
    private Set<String> getKothList() {
        return (Set<String>)this.location.getConfigurationSection("KOTHS").getKeys(false);
    }
}
