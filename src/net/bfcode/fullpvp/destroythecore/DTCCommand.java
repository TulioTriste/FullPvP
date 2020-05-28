package net.bfcode.fullpvp.destroythecore;

import org.bukkit.Location;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import net.bfcode.fullpvp.FullPvP;
import net.bfcode.fullpvp.utilities.ColorText;
import net.bfcode.fullpvp.utilities.Ints;
import net.bfcode.fullpvp.utilities.Utils;

import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class DTCCommand implements CommandExecutor {
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.MUST_BE_PLAYER);
            return true;
        }
        final Player player = (Player)sender;
        if (!player.hasPermission(Utils.PERMISSION + "destroythecore")) {
            player.sendMessage(Utils.NO_PERMISSION);
            return true;
        }
        if (args.length < 1) {
            this.getUsage((CommandSender)player);
        }
        else if (args[0].equalsIgnoreCase("wand")) {
            player.getInventory().addItem(new ItemStack[] { DTCHandler.getDTCWand() });
            player.sendMessage(ColorText.translate("&eYou have received the &aDTC Wand&e."));
        }
        else if (args[0].equalsIgnoreCase("create")) {
            if (args.length < 3) {
                this.getUsage((CommandSender)player);
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
                this.getUsage((CommandSender)player);
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
                this.getUsage((CommandSender)player);
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
            Bukkit.broadcastMessage(ColorText.translate(FullPvP.getPlugin().getConfig().getString("DestroyTheCore.Started").replace("%playername%", player.getName()).replace("%destroythecore%", DTC)));
        }
        else if (args[0].equalsIgnoreCase("stop")) {
            if (args.length < 2) {
                this.getUsage((CommandSender)player);
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
            Bukkit.broadcastMessage(ColorText.translate(FullPvP.getPlugin().getConfig().getString("DestroyTheCore.Stopped").replace("%playername%", player.getName()).replace("%destroythecore%", DTC)));
        }
        else if (args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("teleport")) {
            if (args.length < 2) {
                this.getUsage((CommandSender)player);
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
    
    private void getUsage(final CommandSender sender) {
        sender.sendMessage(ColorText.translate("&7&m------------------------------"));
        sender.sendMessage(ColorText.translate("&6DTC Command: &7(Page 1 of 1)"));
        sender.sendMessage("");
        sender.sendMessage(ColorText.translate("&e/destroythecore <list>"));
        sender.sendMessage(ColorText.translate("&7List of all DTC Event"));
        sender.sendMessage("");
        sender.sendMessage(ColorText.translate("&e/destroythecore <wand>"));
        sender.sendMessage(ColorText.translate("&7Receive a wand to create a DTC Event"));
        sender.sendMessage("");
        sender.sendMessage(ColorText.translate("&e/destroythecore <create> <dtcName> <amount>"));
        sender.sendMessage(ColorText.translate("&7Create a DTC Event"));
        sender.sendMessage("");
        sender.sendMessage(ColorText.translate("&e/destroythecore <delete> <dtcName>"));
        sender.sendMessage(ColorText.translate("&7Delete a DTC Event"));
        sender.sendMessage("");
        sender.sendMessage(ColorText.translate("&e/destroythecore <start> <dtcName>"));
        sender.sendMessage(ColorText.translate("&7Start a DTC Event"));
        sender.sendMessage("");
        sender.sendMessage(ColorText.translate("&e/destroythecore <stop> <dtcName>"));
        sender.sendMessage(ColorText.translate("&7Stop a DTC Event"));
        sender.sendMessage("");
        sender.sendMessage(ColorText.translate("&e/destroythecore <teleport> <dtcName>"));
        sender.sendMessage(ColorText.translate("&7Teleport to a DTC Event"));
        sender.sendMessage(ColorText.translate("&7&m------------------------------"));
    }
}
