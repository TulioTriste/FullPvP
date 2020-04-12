package net.galanthus.fullpvp.kit;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.galanthus.fullpvp.FullPvP;
import net.galanthus.fullpvp.kit.argument.KitApplyArgument;
import net.galanthus.fullpvp.kit.argument.KitCreateArgument;
import net.galanthus.fullpvp.kit.argument.KitDeleteArgument;
import net.galanthus.fullpvp.kit.argument.KitDisableArgument;
import net.galanthus.fullpvp.kit.argument.KitGuiArgument;
import net.galanthus.fullpvp.kit.argument.KitListArgument;
import net.galanthus.fullpvp.kit.argument.KitPreviewArgument;
import net.galanthus.fullpvp.kit.argument.KitRenameArgument;
import net.galanthus.fullpvp.kit.argument.KitSetDelayArgument;
import net.galanthus.fullpvp.kit.argument.KitSetDescriptionArgument;
import net.galanthus.fullpvp.kit.argument.KitSetImageArgument;
import net.galanthus.fullpvp.kit.argument.KitSetIndexArgument;
import net.galanthus.fullpvp.kit.argument.KitSetItemsArgument;
import net.galanthus.fullpvp.utilities.ArgumentExecutor;
import net.galanthus.fullpvp.utilities.BukkitUtils;
import net.galanthus.fullpvp.utilities.CommandArgument;
import net.galanthus.fullpvp.utilities.CommandWrapper;

public class KitExecutor extends ArgumentExecutor
{
    private final FullPvP plugin;
    
    public KitExecutor(final FullPvP plugin) {
        super("kit");
        this.plugin = plugin;
        this.addArgument(new KitApplyArgument(plugin));
        this.addArgument(new KitCreateArgument(plugin));
        this.addArgument(new KitDeleteArgument(plugin));
        this.addArgument(new KitSetDescriptionArgument(plugin));
        this.addArgument(new KitDisableArgument(plugin));
        this.addArgument(new KitGuiArgument(plugin));
        this.addArgument(new KitListArgument(plugin));
        this.addArgument(new KitPreviewArgument(plugin));
        this.addArgument(new KitRenameArgument(plugin));
        this.addArgument(new KitSetDelayArgument(plugin));
        this.addArgument(new KitSetImageArgument(plugin));
        this.addArgument(new KitSetIndexArgument(plugin));
        this.addArgument(new KitSetItemsArgument(plugin));
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length < 1) {
            CommandWrapper.printUsage(sender, label, this.arguments);
            sender.sendMessage(ChatColor.GREEN + "/" + label + " <kitName> " + ChatColor.GRAY + "- Applies a kit.");
            return true;
        }
        final CommandArgument argument = this.getArgument(args[0]);
        final String permission = ((argument == null) ? null : argument.getPermission());
        if (argument != null && (permission == null || sender.hasPermission(permission))) {
            argument.onCommand(sender, command, label, args);
            return true;
        }
        final Kit kit = this.plugin.getKitManager().getKit(args[0]);
        final String kitPermission;
        if (sender instanceof Player && kit != null && ((kitPermission = kit.getPermissionNode()) == null || sender.hasPermission(kitPermission))) {
            final Player player = (Player)sender;
            kit.applyTo(player, false, true);
            return true;
        }
        sender.sendMessage(ChatColor.RED + "Kit or command " + args[0] + " not found.");
        return true;
    }
    
    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length != 1) {
            return super.onTabComplete(sender, command, label, args);
        }
        List<String> previous = super.onTabComplete(sender, command, label, args);
        final ArrayList<String> kitNames = new ArrayList<String>();
        for (final Kit kit : this.plugin.getKitManager().getKits()) {
            final String permission = kit.getPermissionNode();
            if (permission != null && !sender.hasPermission(permission)) {
                continue;
            }
            kitNames.add(kit.getName());
        }
        if (previous == null || previous.isEmpty()) {
            previous = kitNames;
        }
        else {
            previous = new ArrayList<String>(previous);
            previous.addAll(0, kitNames);
        }
        return BukkitUtils.getCompletions(args, previous);
    }
}
