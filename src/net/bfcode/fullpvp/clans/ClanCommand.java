package net.bfcode.fullpvp.clans;

import org.bukkit.Sound;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.bfcode.fullpvp.utilities.ColorText;
import net.bfcode.fullpvp.utilities.Utils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class ClanCommand implements CommandExecutor {
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.MUST_BE_PLAYER);
            return true;
        }
        final Player player = (Player)sender;
        if (args.length < 1) {
            this.getUsage(player);
        }
        else if (args[0].equalsIgnoreCase("create")) {
            if (args.length < 2) {
                player.sendMessage(ColorText.translate("&cUsage: /clan create <name>"));
                return true;
            }
            final String clan = args[1].toUpperCase();
            if (clan.length() < 2) {
                player.sendMessage(ColorText.translate("&cThe min clan name characters is 2."));
                return true;
            }
            if (clan.length() > 5) {
                player.sendMessage(ColorText.translate("&cThe max clan name characters is 5."));
                return true;
            }
            if (clan.contains("*") || clan.contains(",") || clan.contains("[") || clan.contains("]") || clan.contains("-") || clan.contains(".") || clan.contains("&")) {
                player.sendMessage(ColorText.translate("&cYou can't use that symbols."));
                return true;
            }
            if (ClanHandler.hasClan(player)) {
                player.sendMessage(ColorText.translate("&cYou already in a clan."));
                return true;
            }
            if (ClanHandler.alreadyCreated(clan)) {
                player.sendMessage(ColorText.translate("&cClan '" + args[1] + "' is already created."));
                return true;
            }
            ClanHandler.createClan(player, clan);
        }
        else if (args[0].equalsIgnoreCase("delete")) {
            if (!ClanHandler.hasClan(player)) {
                player.sendMessage(ColorText.translate("&cYou are not in a clan."));
                return true;
            }
            if (!ClanHandler.isLeader(player, ClanHandler.getClan(player))) {
                player.sendMessage(ColorText.translate("&cYou must be leader to delete this clan."));
                return true;
            }
            ClanHandler.disbandClan(ClanHandler.getClan(player), player);
        }
        else if (args[0].equalsIgnoreCase("forcedelete")) {
            if (args.length < 2) {
            	player.sendMessage(ColorText.translate("&cUsage: /clan forcedisband <clanName>"));
                return true;
            }
            final String clan = args[1];
            if (!ClanHandler.alreadyCreated(clan)) {
                player.sendMessage(ColorText.translate("&cClan not found."));
                return true;
            }
            ClanHandler.disbandClan(clan, player);
        }
        else if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("who") || args[0].equalsIgnoreCase("show")) {
            if (args.length < 2) {
                if (!ClanHandler.hasClan(player)) {
                    player.sendMessage(ColorText.translate("&cYou are not in a clan."));
                    return true;
                }
                ClanHandler.printClanInformation(player, ClanHandler.getClan(player));
                return true;
            }
            else {
                final String clan = args[1];
                if (!ClanHandler.alreadyCreated(clan)) {
                    player.sendMessage(ColorText.translate("&cClan '" + clan + "' not found."));
                    return true;
                }
                ClanHandler.printClanInformation(player, clan);
            }
        }
        else if (args[0].equalsIgnoreCase("setdescription") || args[0].equalsIgnoreCase("setdec")) {
            if (args.length < 2) {
            	player.sendMessage(ColorText.translate("&cUsage: /clan setdescription <description>"));
                return true;
            }
            if (!ClanHandler.hasClan(player)) {
                player.sendMessage(ColorText.translate("&cYou are not in a clan."));
                return true;
            }
            if (ClanHandler.hasClan(player) && !ClanHandler.isLeader(player)) {
                player.sendMessage(ColorText.translate("&cYou must be Lider to edit the description."));
                return true;
            }
            String description = "";
            for (int desc = 1; desc < args.length; ++desc) {
                description = description + args[desc] + " ";
            }
            ClanHandler.setDescription(ClanHandler.getClan(player), description);
            player.sendMessage(ColorText.translate("&eSuccessfully updated the description of your clan."));
        }
        else if (args[0].equalsIgnoreCase("invite")) {
            if (args.length < 2) {
            	player.sendMessage(ColorText.translate("&cUsage: /clan invite <player>"));
                return true;
            }
            final Player target = Bukkit.getPlayer(args[1]);
            if (!Utils.isOnline(player, target)) {
                Utils.PLAYER_NOT_FOUND(player, args[1]);
                return true;
            }
            if (!ClanHandler.hasClan(player)) {
                player.sendMessage(ColorText.translate("&cYou are not in a clan."));
                return true;
            }
            if (ClanHandler.hasClan(player) && !ClanHandler.isLeader(player)) {
                player.sendMessage(ColorText.translate("&cYou must be Lider to invite players."));
                return true;
            }
            if (ClanHandler.hasClan(target)) {
                player.sendMessage(ColorText.translate("&c" + target.getName() + " already have a clan."));
                return true;
            }
            if (ClanHandler.isAlreadyInvited(ClanHandler.getClan(player), target)) {
                player.sendMessage(ColorText.translate("&c" + target.getName() + " is already invited to your clan."));
                return true;
            }
            if(ClanHandler.getClanMembers(ClanHandler.getClan(player)) == ClanHandler.getMinClanSize(ClanHandler.getClan(player))) {
            	player.sendMessage(ColorText.translate("&cIt has reached the limit of members in your clan."));
            	return true;
            }
            ClanHandler.addInvite(ClanHandler.getClan(player), target);
            ClanHandler.sendMessage("&a" + target.getName() + " &ewas invited to your clan.", ClanHandler.getClan(player));
            TextComponent mensaje = new TextComponent();
            mensaje.setText(ColorText.translate("&eYou has been invited to '&a" + ClanHandler.getClan(player) + "&e' type &f'/clan join " + ClanHandler.getClan(player) + "' or &7(Click here)"));
            mensaje.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan join " + ClanHandler.getClan(player)));
            target.sendMessage(mensaje);
        }
        else if (args[0].equalsIgnoreCase("deinvite")) {
            if (args.length < 2) {
            	player.sendMessage(ColorText.translate("&cUsage: /clan deinvite <player>"));
                return true;
            }
            final Player target = Bukkit.getPlayer(args[1]);
            if (!Utils.isOnline(player, target)) {
                Utils.PLAYER_NOT_FOUND(player, args[1]);
                return true;
            }
            if (!ClanHandler.hasClan(player)) {
                player.sendMessage(ColorText.translate("&cYou are not in a clan."));
                return true;
            }
            if (ClanHandler.hasClan(player) && !ClanHandler.isLeader(player)) {
                player.sendMessage(ColorText.translate("&cYou must be Lider to deinvite players."));
                return true;
            }
            if (!ClanHandler.isAlreadyInvited(ClanHandler.getClan(player), target)) {
                player.sendMessage(ColorText.translate("&c" + target.getName() + " is not invited to your clan."));
                return true;
            }
            ClanHandler.removeInvite(ClanHandler.getClan(player), target);
            player.sendMessage(ColorText.translate("&eYou have revoked the invite of " + target.getName() + '.'));
        }
        else if (args[0].equalsIgnoreCase("join")) {
            if (args.length < 2) {
            	player.sendMessage(ColorText.translate("&cUsage: /clan join <clanName>"));
                return true;
            }
            final String clan = args[1];
            if (ClanHandler.hasClan(player)) {
                player.sendMessage(ColorText.translate("&cYou already in a clan."));
                return true;
            }
            if (!ClanHandler.alreadyCreated(clan)) {
                player.sendMessage(ColorText.translate("&cClan '" + clan + "' not found."));
                return true;
            }
            if (!ClanHandler.isAlreadyInvited(clan, player)) {
                player.sendMessage(ColorText.translate("&cYou need a invite to join to this clan."));
                return true;
            }
            ClanHandler.addMember(clan, player);
            player.sendMessage(ColorText.translate("&eYou have joined to &a" + clan + " &eClan."));
            ClanHandler.sendMessage("&a" + player.getName() + " &ehas joined to your clan.", ClanHandler.getClan(player));
        }
        else if (args[0].equalsIgnoreCase("forcejoin")) {
            if (!player.isOp()) {
                player.sendMessage(Utils.NO_PERMISSION);
                return true;
            }
            if (args.length < 2) {
            	player.sendMessage(ColorText.translate("&cUsage: /clan forcejoin <clanName>"));
                return true;
            }
            final String clan = args[1];
            if (!ClanHandler.alreadyCreated(clan)) {
                player.sendMessage(ColorText.translate("&cClan '" + clan + "' not found."));
                return true;
            }
            ClanHandler.addMember(clan, player);
            player.sendMessage(ColorText.translate("&eSuccessfully joined to " + clan + '.'));
        }
        else if (args[0].equalsIgnoreCase("leave")) {
            if (!ClanHandler.hasClan(player)) {
                player.sendMessage(ColorText.translate("&cYou are not in a clan."));
                return true;
            }
            if (ClanHandler.hasClan(player) && ClanHandler.isLeader(player)) {
            	player.sendMessage(ColorText.translate("&cYou can't leave your clan, because you are the leader."));
            	return true;
            }
            ClanHandler.removeMember(ClanHandler.getClan(player), player);
            player.sendMessage(ColorText.translate("&eYou leave of that clan."));
        }
        else if (args[0].equalsIgnoreCase("kick")) {
            if (args.length < 2) {
            	player.sendMessage(ColorText.translate("&cUsage: /clan kick <player>"));
                return true;
            }
            final Player target = Bukkit.getPlayer(args[1]);
            if (!Utils.isOnline(player, target)) {
                Utils.PLAYER_NOT_FOUND(player, args[1]);
                return true;
            }
            if (!ClanHandler.hasClan(player)) {
                player.sendMessage(ColorText.translate("&cYou are not in a clan."));
                return true;
            }
            if (ClanHandler.hasClan(player) && !ClanHandler.isLeader(player)) {
                player.sendMessage(ColorText.translate("&cYou must be leader to kick players from the clan."));
                return true;
            }
            if (!ClanHandler.isMember(target, ClanHandler.getClan(player))) {
                player.sendMessage(ColorText.translate("&c" + target.getName() + " is not in your clan."));
                return true;
            }
            ClanHandler.removeMember(ClanHandler.getClan(player), target);
            player.sendMessage(ColorText.translate("&eYou kicked to " + target.getName() + " from your clan."));
            target.sendMessage(ColorText.translate("&eYou were kicked from " + ClanHandler.getClan(player) + " Clan."));
            target.playSound(target.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
        }
        else if (args[0].equalsIgnoreCase("msg") || args[0].equalsIgnoreCase("message")) {
            if (!ClanHandler.hasClan(player)) {
                player.sendMessage(ColorText.translate("&cYou are not in a clan."));
                return true;
            }
            if (args.length < 2) {
            	player.sendMessage(ColorText.translate("&cUsage: /clan msg <message>"));
                return true;
            }
            String message2 = "";
            for (int i = 1; i < args.length; ++i) {
                message2 = message2 + args[i] + " ";
            }
            ClanHandler.sendMessage("&7(&a" + ClanHandler.getClan(player) + "&7) &9" + player.getName() + "&7: &f" + message2, ClanHandler.getClan(player));
        }
        else if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("h")) {
            if (args.length < 2) {
                this.getUsage(player);
                return true;
            }
        }
        else if(args[0].equalsIgnoreCase("reload")) {
        	if(args.length < 1) {
        		player.sendMessage(ColorText.translate("&cUsage: /clan reload"));
        		return true;
        	}
        	if(!player.hasPermission("clans.command.reload")) {
        		player.sendMessage(ColorText.translate(Utils.NO_PERMISSION));
        		return true;
        	}
        	player.sendMessage(ColorText.translate("&aThe clans.yml has been successfully reloaded."));
        	ClanFile.getConfig().reload();
        	return true;
        }
        else {
            player.sendMessage(ColorText.translate("&cClan sub-command '" + args[0] + "' not found."));
        }
        return true;
    }
    
    private void getUsage(final CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(ColorText.translate("&2[Clans] &7- &a/clan create <name>"));
        sender.sendMessage(ColorText.translate("&2[Clans] &7- &a/clan delete"));
        sender.sendMessage(ColorText.translate("&2[Clans] &7- &a/clan info"));
        sender.sendMessage(ColorText.translate("&2[Clans] &7- &a/clan join <clan>"));
        sender.sendMessage(ColorText.translate("&2[Clans] &7- &a/clan leave"));
        sender.sendMessage(ColorText.translate("&2[Clans] &7- &a/clan invite <player>"));
        sender.sendMessage(ColorText.translate("&2[Clans] &7- &a/clan deinvite <player>"));
        sender.sendMessage(ColorText.translate("&2[Clans] &7- &a/clan kick <player>"));
        sender.sendMessage(ColorText.translate("&2[Clans] &7- &a/clan msg <message>"));
        sender.sendMessage(ColorText.translate("&2[Clans] &7- &a/clan setdescription <description>"));
        if (sender.isOp()) {
            sender.sendMessage(ColorText.translate("&2[Clans] &7- &a/clan forcejoin <clan>"));
            sender.sendMessage(ColorText.translate("&2[Clans] &7- &a/clan forcedelete <clan>"));
            sender.sendMessage(ColorText.translate("&2[Clans] &7- &a/clan reload"));
        }
        sender.sendMessage("");
    }
}