package net.bfcode.fullpvp.claim;

import java.util.Set;
import com.sk89q.worldedit.bukkit.selections.Selection;

import net.bfcode.fullpvp.configuration.LocationFile;
import net.bfcode.fullpvp.utilities.ColorText;
import net.bfcode.fullpvp.utilities.Utils;

import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class ClaimCommand implements CommandExecutor {

    private LocationFile locationFile = LocationFile.getConfig();

    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.MUST_BE_PLAYER);
            return true;
        }
        final Player player = (Player)sender;
        if (!player.hasPermission("fullpvp.command.claim")) {
            player.sendMessage(Utils.NO_PERMISSION);
            return true;
        }
        if (args.length < 1) {
            this.getUsage(sender, label);
        }
        else if (args[0].equalsIgnoreCase("nopvp")) {
            if (args.length < 2) {
                this.getUsage(sender, label);
                return true;
            }
            final StringBuilder x = new StringBuilder();
            for (int i = 1; i < args.length; ++i) {
                x.append(String.valueOf(String.valueOf(args[i])) + " ");
            }
            final String town = x.toString().trim();
            if (this.locationFile.getConfigurationSection("Claims." + town) != null) {
                player.sendMessage(ColorText.translate("&cThis claim is already created!"));
                return true;
            }
            final Selection sel = Utils.getWorldEdit().getSelection(player);
            if (sel == null) {
                player.sendMessage(ColorText.translate("&cYou must make a WorldEdit selection."));
                return true;
            }
            player.performCommand("/expand vert");
            this.locationFile.set("Claims." + town + ".world", sel.getMaximumPoint().getWorld().getName());
            this.locationFile.set("Claims." + town + ".pvp", false);
            this.locationFile.set("Claims." + town + ".cornerA.x", sel.getMaximumPoint().getX() + 1);
            this.locationFile.set("Claims." + town + ".cornerA.y", sel.getMaximumPoint().getY());
            this.locationFile.set("Claims." + town + ".cornerA.z", sel.getMaximumPoint().getZ() + 1);
            this.locationFile.set("Claims." + town + ".cornerB.x", sel.getMinimumPoint().getX());
            this.locationFile.set("Claims." + town + ".cornerB.y", sel.getMinimumPoint().getY());
            this.locationFile.set("Claims." + town + ".cornerB.z", sel.getMinimumPoint().getZ());
            this.locationFile.save();
            this.locationFile.reload();
            player.sendMessage(ColorText.translate("&eYou have created a claim &f" + town + " &e(&aNo PvP&e)"));
        }
        else if (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("remove")) {
            if (args.length < 2) {
                player.sendMessage(ColorText.translate("&cUsage: /" + label + " delete <name>"));
                return true;
            }
            String town = args[1];
            if (this.locationFile.getConfigurationSection("Claims." + town) == null) {
                player.sendMessage(ColorText.translate("&cThis claim is already deleted."));
                return true;
            }
            this.locationFile.set("Claims." + town, null);
            this.locationFile.save();
            this.locationFile.reload();
            player.sendMessage(ColorText.translate("&6You've successfully delete " + town + "."));
            return true;
        }
        else if (args[0].equalsIgnoreCase("list")) {
            player.sendMessage(ColorText.translate("&eList of all Claims: &f" + this.getClaimList().toString().replace("[", "").replace("]", "")));
        }
        else {
            player.sendMessage(ColorText.translate("&cClaim sub-command '" + args[0] + "' not found."));
        }
        return true;
    }
    
    private Set<String> getClaimList() {
        return (Set<String>)this.locationFile.getConfigurationSection("Claims").getKeys(false);
    }
    
    private void getUsage(CommandSender sender, String label) {
        sender.sendMessage(ColorText.translate("&7&m------------------------------"));
        sender.sendMessage(ColorText.translate("&6&lClaim Commands"));
        sender.sendMessage("");
        sender.sendMessage(ColorText.translate("&e/" + label + " pvp <name> &7- &fCreate a SafeZone Claim."));
        sender.sendMessage(ColorText.translate("&e/" + label + " nopvp <name> &7- &fCreate a PvP Claim."));
        sender.sendMessage(ColorText.translate("&e/" + label + " delete <name> &7- &fDelete a Claim.."));
        sender.sendMessage(ColorText.translate("&e/" + label + " list &7- &fList of all Claims."));
        sender.sendMessage(ColorText.translate("&7&m------------------------------"));
    }
}
