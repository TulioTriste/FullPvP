package net.galanthus.fullpvp.claim;

import java.util.Set;
import com.sk89q.worldedit.bukkit.selections.Selection;

import net.galanthus.fullpvp.configuration.LocationFile;
import net.galanthus.fullpvp.utilities.ColorText;
import net.galanthus.fullpvp.utilities.Utils;

import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class ClaimCommand implements CommandExecutor {
    LocationFile location;
    
    public ClaimCommand() {
        this.location = LocationFile.getConfig();
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.MUST_BE_PLAYER);
            return true;
        }
        final Player player = (Player)sender;
        if (!player.hasPermission(Utils.PERMISSION + "claim")) {
            player.sendMessage(Utils.NO_PERMISSION);
            return true;
        }
        if (args.length < 1) {
            this.getUsage(player);
        }
        else if (args[0].equalsIgnoreCase("pvp")) {
            if (args.length < 2) {
                this.getUsage(sender);
                return true;
            }
            final StringBuilder x = new StringBuilder();
            for (int i = 1; i < args.length; ++i) {
                x.append(String.valueOf(String.valueOf(args[i])) + " ");
            }
            final String town = x.toString().trim();
            if (this.location.getConfigurationSection("Claims." + town) != null) {
                player.sendMessage(ColorText.translate("&cThis claim is already created!"));
                return true;
            }
            final Selection sel = Utils.getWorldEdit().getSelection(player);
            if (sel == null) {
                player.sendMessage(ColorText.translate("&cYou must make a WorldEdit selection."));
                return true;
            }
            this.location.set("Claims." + town + ".world", sel.getMaximumPoint().getWorld().getName());
            this.location.set("Claims." + town + ".pvp", true);
            this.location.set("Claims." + town + ".cornerA.x", sel.getMaximumPoint().getX());
            final LocationFile location = this.location;
            LocationFile.getConfig().set("Claims." + town + ".cornerA.y", sel.getMaximumPoint().getY());
            LocationFile.getConfig().set("Claims." + town + ".cornerA.z", sel.getMaximumPoint().getZ());
            LocationFile.getConfig().set("Claims." + town + ".cornerB.x", sel.getMinimumPoint().getX());
            LocationFile.getConfig().set("Claims." + town + ".cornerB.y", sel.getMinimumPoint().getY());
            LocationFile.getConfig().set("Claims." + town + ".cornerB.z", sel.getMinimumPoint().getZ());
            location.save();
            location.reload();
            player.sendMessage(ColorText.translate("&eYou have created a claim &f" + town + " &e(&cPvP&e)"));
        }
        else if (args[0].equalsIgnoreCase("nopvp")) {
            if (args.length < 2) {
                this.getUsage(sender);
                return true;
            }
            final StringBuilder x = new StringBuilder();
            for (int i = 1; i < args.length; ++i) {
                x.append(String.valueOf(String.valueOf(args[i])) + " ");
            }
            final String town = x.toString().trim();
            if (this.location.getConfigurationSection("Claims." + town) != null) {
                player.sendMessage(ColorText.translate("&cThis claim is already created!"));
                return true;
            }
            final Selection sel = Utils.getWorldEdit().getSelection(player);
            if (sel == null) {
                player.sendMessage(ColorText.translate("&cYou must make a WorldEdit selection."));
                return true;
            }
            location.set("Claims." + town + ".world", sel.getMaximumPoint().getWorld().getName());
            this.location.set("Claims." + town + ".pvp", false);
            this.location.set("Claims." + town + ".cornerA.x", sel.getMaximumPoint().getX());
            LocationFile.getConfig().set("Claims." + town + ".cornerA.y", sel.getMaximumPoint().getY());
            LocationFile.getConfig().set("Claims." + town + ".cornerA.z", sel.getMaximumPoint().getZ());
            LocationFile.getConfig().set("Claims." + town + ".cornerB.x", sel.getMinimumPoint().getX());
            LocationFile.getConfig().set("Claims." + town + ".cornerB.y", sel.getMinimumPoint().getY());
            LocationFile.getConfig().set("Claims." + town + ".cornerB.z", sel.getMinimumPoint().getZ());
            location.save();
            location.reload();
            player.sendMessage(ColorText.translate("&eYou have created a claim &f" + town + " &e(&aNo PvP&e)"));
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
        return (Set<String>)this.location.getConfigurationSection("Claims").getKeys(false);
    }
    
    private void getUsage(final CommandSender sender) {
        sender.sendMessage(ColorText.translate("&7&m------------------------------"));
        sender.sendMessage(ColorText.translate("&6Claim Commands"));
        sender.sendMessage("");
        sender.sendMessage(ColorText.translate("&e/claim pvp <claimName> &7- &fCreate a SafeZone Claim."));
        sender.sendMessage(ColorText.translate("&e/claim nopvp <claimName> &7- &fCreate a PvP Claim."));
        sender.sendMessage(ColorText.translate("&e/claim list &7- &fList of all Claims."));
        sender.sendMessage(ColorText.translate("&7&m------------------------------"));
    }
}
