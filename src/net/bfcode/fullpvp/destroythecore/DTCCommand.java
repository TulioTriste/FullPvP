package net.bfcode.fullpvp.destroythecore;

import org.bukkit.Location;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import net.bfcode.fullpvp.FullPvP;
import net.bfcode.fullpvp.configuration.MessagesFile;
import net.bfcode.fullpvp.utilities.ColorText;
import net.bfcode.fullpvp.utilities.Ints;
import net.bfcode.fullpvp.utilities.Utils;

import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class DTCCommand implements CommandExecutor {
	
	@Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.MUST_BE_PLAYER);
            return true;
        }
        Player player = (Player)sender;
        MessagesFile message = MessagesFile.getConfig();
        if (!player.hasPermission("fullpvp.command.destroythecore")) {
            player.sendMessage(Utils.NO_PERMISSION);
            return true;
        }
        if (args.length < 1) {
            this.getUsage(player, label);
        }
        else if (args[0].equalsIgnoreCase("wand")) {
            player.getInventory().addItem(new ItemStack[] { DTCHandler.getDTCWand() });
            player.sendMessage(ColorText.translate("&eYou have received the &aDTC Wand&e."));
        }
        else if (args[0].equalsIgnoreCase("create")) {
            if (args.length < 3) {
                this.getUsage(player, label);
                return true;
            }
            final String DTC = args[1];
            if (DTC.length() > 10) {
                player.sendMessage(ColorText.translate("&cMax name is 10 length."));
                return true;
            }
            if (!DTCHandler.isSetSelection()) {
                player.sendMessage(ColorText.translate("&cFirst, you must select the block."));
                return true;
            }
            if (DTCHandler.isAlreadyCreated(DTC)) {
                player.sendMessage(ColorText.translate("&cDTC '" + DTC + " is already created."));
                return true;
            }
            final Integer point = Ints.tryParse(args[2]);
            if (point == null) {
                player.sendMessage(ColorText.translate("&c'" + args[2] + "' is not a valid number."));
                return true;
            }
            if (point <= 0) {
                player.sendMessage(ColorText.translate("&cThe Point must be positive."));
                return true;
            }
            DTCHandler.createDTC(DTC, point);
            player.sendMessage(ColorText.translate("&eYou have created &a" + DTC + " DTC Event &esuccessfully."));
        }
        else if (args[0].equalsIgnoreCase("delete")) {
            if (args.length < 2) {
                this.getUsage(player, label);
                return true;
            }
            final String DTC = args[1];
            if (!DTCHandler.isAlreadyCreated(DTC)) {
                player.sendMessage(ColorText.translate("&cDTC '" + DTC + "' not created."));
                return true;
            }
            DTCHandler.deleteDTC(DTC);
            player.sendMessage(ColorText.translate("&eYou have deleted &a" + DTC + " DTC Event &esucessfully."));
        }
        else if (args[0].equalsIgnoreCase("list")) {
            player.sendMessage(ColorText.translate("&eDTC Event List: &f" + DTCHandler.getDTCList().toString().replace("[", "").replace("]", "")));
        }
        else if (args[0].equalsIgnoreCase("start")) {
            if (args.length < 2) {
                this.getUsage(player, label);
                return true;
            }
            final String DTC = args[1];
            if (!DTCHandler.isAlreadyCreated(DTC)) {
                player.sendMessage(ColorText.translate("&cDTC '" + DTC + "' not created."));
                return true;
            }
            if (DTCHandler.isStarted(DTC)) {
                player.sendMessage(ColorText.translate("&cDTC Event '" + DTC + "' is already started."));
                return true;
            }
            DTCHandler.setDTCEvent(DTC, true);
            Bukkit.broadcastMessage(ColorText.translate(message.getString("Destroy-The-Core.Started")
            		.replace("{player}", player.getName())
            		.replace("{dtc}", DTC)));
        }
        else if (args[0].equalsIgnoreCase("stop")) {
            if (args.length < 2) {
                this.getUsage(player, label);
                return true;
            }
            final String DTC = args[1];
            if (!DTCHandler.isAlreadyCreated(DTC)) {
                player.sendMessage(ColorText.translate("&cDTC '" + DTC + "' not created."));
                return true;
            }
            if (!DTCHandler.isStarted(DTC)) {
                player.sendMessage(ColorText.translate("&cDTC Event '" + DTC + "' is already stopped."));
                return true;
            }
            DTCHandler.setDTCEvent(DTC, false);
            Bukkit.broadcastMessage(ColorText.translate(message.getString("Destroy-The-Core.Stopped")
            		.replace("{player}", player.getName())
            		.replace("{dtc}", DTC)));
        }
        else if (args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("teleport")) {
            if (args.length < 2) {
                this.getUsage(player, label);
                return true;
            }
            final String DTC = args[1];
            if (!DTCHandler.isAlreadyCreated(DTC)) {
                player.sendMessage(ColorText.translate("&cDTC '" + DTC + "' not created."));
                return true;
            }
            final int x = DTCHandler.getDTCLocX(DTC);
            final int y = DTCHandler.getDTCLocY(DTC);
            final int z = DTCHandler.getDTCLocZ(DTC);
            final String world = player.getWorld().getName();
            final Location location = new Location(Bukkit.getWorld(world), (double)x, (double)y, (double)z);
            player.teleport(location);
            player.sendMessage(ColorText.translate("&eYou has been teleported to DTC: &f" + DTC));
        }
        else {
            player.sendMessage(ColorText.translate("&cDTC sub-command '" + args[0] + "' not found."));
        }
        return true;
    }
    
    private void getUsage(CommandSender sender, String label) {
        sender.sendMessage(ColorText.translate("&7&m------------------------------"));
        sender.sendMessage(ColorText.translate("&6&lDTC Commands"));
        sender.sendMessage("");
        sender.sendMessage(ColorText.translate("  &e/" + label + " list &7- &fList of all DTC Event."));
        sender.sendMessage(ColorText.translate("  &e/" + label + " wand &7- &fReceive a wand to create a DTC Event."));
        sender.sendMessage(ColorText.translate("  &e/" + label + " create <name> <points> &7- &fCreate a DTC Event."));
        sender.sendMessage(ColorText.translate("  &e/" + label + " delete <name> &7- &fDelete a DTC Event."));
        sender.sendMessage(ColorText.translate("  &e/" + label + " start <name> &7- &fStart a DTC Event."));
        sender.sendMessage(ColorText.translate("  &e/" + label + " stop <name> &7- &fStop a DTC Event."));
        sender.sendMessage(ColorText.translate("  &e/" + label + " teleport <name> &7- &fTeleport to a DTC Event"));
        sender.sendMessage(ColorText.translate("&7&m------------------------------"));
    }
}
