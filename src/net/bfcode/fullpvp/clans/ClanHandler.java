package net.bfcode.fullpvp.clans;

import java.util.Set;
import org.bukkit.ChatColor;
import java.util.Iterator;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import org.bukkit.entity.Player;

import net.bfcode.fullpvp.utilities.ColorText;

public class ClanHandler {
	
    public static ClanFile clans;
    
	@SuppressWarnings("rawtypes")
	public static void createClan(final Player player, final String clan) {
        ClanHandler.clans.set("Clans." + clan + ".Leader", player.getName());
        ClanHandler.clans.set("Clans." + clan + ".Description", "None");
        final List<String> list = new ArrayList<String>();
        list.add(player.getName());
        ClanHandler.clans.set("Clans." + clan + ".Members", list);
        ClanHandler.clans.set("Clans." + clan + ".Invite", new ArrayList());
        final Date now = new Date();
        final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        ClanHandler.clans.set("Clans." + clan + ".Date", format.format(now));
        ClanHandler.clans.set("Clans." + clan + ".TotalKills", 0L);
        ClanHandler.clans.set("Clans." + clan + ".Balance", 0L);
        ClanHandler.clans.set("Clans." + clan + ".MinClanSize", 8L);
        saveClans();
        player.sendMessage(ColorText.translate("&aClan successfully created."));
    }
    
	public static void setMinClanSize(final String clan, final int minclansize) {
    	ClanHandler.clans.set("Clans." + clan + ".MinClanSize", minclansize);
    	saveClans();
    }
	
	public static int getMinClanSize(final String clan) {
    	return ClanHandler.clans.getInt("Clans." + clan + ".MinClanSize");
    }
	
    public static void disbandClan(final String clan) {
        ClanHandler.clans.set("Clans." + clan, null);
        saveClans();
    }
    
    public static void setTotalKills(final String clan, final int totalKills) {
    	ClanHandler.clans.set("Clans." + clan + ".TotalKills", totalKills);
    	saveClans();
    }
    
    public static int getTotalKills(final String clan) {
    	return ClanHandler.clans.getInt("Clans." + clan + ".TotalKills");
    }
    
    public static void addBalance(final String clan, final int money) {
    	ClanHandler.clans.set("Clans." + clan + ".Balance", money);
    	saveClans();
    }
    
    public static int getBalance(final String clan) {
		return ClanHandler.clans.getInt("Clans." + clan + ".Balance");
    }
    
    public static void disbandClan(final String clan, final Player player) {
        ClanHandler.clans.set("Clans." + clan, null);
        saveClans();
        player.sendMessage(ColorText.translate("&cYour clan has been disbanded."));
    }
    
    public static void printClanInformation(final Player player, final String clan) {
    	player.sendMessage(ColorText.translate(""));
        player.sendMessage(ColorText.translate("&2&lInformación del Clan"));
        player.sendMessage(ColorText.translate(" &aNombre: &f" + clan));
        player.sendMessage(ColorText.translate(" &aLider: &f" + getClanLeader(clan)));
        player.sendMessage(ColorText.translate(" &aDinero: &f$" + getBalance(clan) + " &7\u2503 &aTotal Kills: &f" + getTotalKills(clan)));
        player.sendMessage(ColorText.translate(" &aDescripción: &f" + getDescription(clan)));
        player.sendMessage(ColorText.translate(" &aMiembros: &c(" + getClanMembers(clan) + "/" + getMinClanSize(clan) + ")" + "&a " + getMembers(clan)));
        player.sendMessage(ColorText.translate(""));
    }
    
    public static boolean isAlreadyInvited(final String clan, final Player player) {
        return ClanHandler.clans.getStringList("Clans." + clan + ".Invite").contains(player.getName());
    }
    
	public static void sendMessage(final String message, final String clan) {
        for (final Player online : Bukkit.getOnlinePlayers()) {
            if (isMember(online, clan)) {
                online.sendMessage(ColorText.translate(message));
            }
        }
    }
    
    public static void removeInvite(final String clan, final Player player) {
        final List<String> invite = new ArrayList<String>();
        invite.remove(player.getName());
        ClanHandler.clans.set("Clans." + clan + ".Invite", invite);
        saveClans();
    }
    
    public static boolean areMember(final Player player, final Player target, final String clan) {
        return isMember(player, clan) && isMember(target, clan);
    }
    
    public static boolean isMember(final Player player, final String clan) {
        return ClanHandler.clans.getStringList("Clans." + clan + ".Members").contains(player.getName());
    }
    
    public static void addInvite(final String clan, final Player player) {
        final List<String> invite = new ArrayList<String>();
        invite.add(player.getName());
        ClanHandler.clans.set("Clans." + clan + ".Invite", invite);
        saveClans();
    }
    
    public static String getInvites(final String clan) {
        if (alreadyCreated(clan)) {
            final List<String> list = ClanHandler.clans.getStringList("Clans." + clan + ".Invite");
            if (list.size() != 0) {
                Collections.sort(list);
                ClanHandler.clans.set("Clans." + clan + ".Invite", list);
                saveClans();
            }
            return list.toString().replace("[", "").replace("]", "");
        }
        return null;
    }
    
    public static void setDescription(final String clan, final String description) {
        ClanHandler.clans.set("Clans." + clan + ".Description", description);
        saveClans();
    }
    
    public static String getDescription(final String clan) {
        return ClanHandler.clans.getString("Clans." + clan + ".Description");
    }
    
    @SuppressWarnings("rawtypes")
	public static String getClan(final Player player) {
        final Iterator localIterator1 = listClans().iterator();
        while (localIterator1.hasNext()) {
            final String clan = (String) localIterator1.next();
            if (ClanHandler.clans.getString("Clans." + clan + ".Leader").equalsIgnoreCase(player.getName())) {
                return clan;
            }
            if (ClanHandler.clans.getStringList("Clans." + clan + ".Members").contains(player.getName())) {
                return clan;
            }
            localIterator1.hasNext();
        }
        return null;
    }
    
    public static String getClanLeader(final String clan) {
        return ClanHandler.clans.getString("Clans." + clan + ".Leader");
    }
    
    public static void removeMember(final String clan, final Player player) {
        final List<String> list = ClanHandler.clans.getStringList("Clans." + clan + ".Members");
        list.remove(player.getName());
        ClanHandler.clans.set("Clans." + clan + ".Members", list);
        saveClans();
    }
    
    public static void addMember(final String clan, final Player player) {
        final List<String> list = ClanHandler.clans.getStringList("Clans." + clan + ".Members");
        list.add(player.getName());
        ClanHandler.clans.set("Clans." + clan + ".Members", list);
        final List<String> list2 = ClanHandler.clans.getStringList("Clans." + clan + ".Invite");
        list2.remove(player.getName());
        ClanHandler.clans.set("Clans." + clan + ".Invite", list2);
        saveClans();
    }
    
    public static String getMembers(final String clan) {
        if (alreadyCreated(clan)) {
            final List<String> list = ClanHandler.clans.getStringList("Clans." + clan + ".Members");
            if (list.size() != 0) {
                Collections.sort(list);
                ClanHandler.clans.set("Clans." + clan + ".Members", list);
                saveClans();
            }
            return list.toString().replace("[", "").replace("]", "");
        }
        return null;
    }
    
    public static int getClanMembers(final String clan) {
        final List<String> list = ClanHandler.clans.getStringList("Clans." + clan + ".Members");
        return list.size();
    }
    
    public static boolean hasClan(final Player player) {
        return isLeader(player) || isMember(player);
    }
    
    public static boolean alreadyCreated(final String clan) {
        return ClanHandler.clans.contains("Clans." + clan);
    }
    
    @SuppressWarnings("unused")
	private static boolean existClan(final String clan) {
        return getRightClan(ChatColor.stripColor(clan)) != null;
    }
    
    private static String getRightClan(final String clan) {
        for (final String clan2 : listClans()) {
            if (clan2.equalsIgnoreCase(clan)) {
                return clan2;
            }
        }
        return null;
    }
    
    public static boolean isLeader(final Player player) {
        for (final String clan : listClans()) {
            if (ClanHandler.clans.getString("Clans." + clan + ".Leader").contains(player.getName())) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isLeader(final Player player, final String clan) {
        return ClanHandler.clans.getString("Clans." + clan + ".Leader").contains(player.getName());
    }
    
    private static boolean isMember(final Player player) {
        for (final String clan : listClans()) {
            if (ClanHandler.clans.getStringList("Clans." + clan + ".Members").contains(player.getName())) {
                return true;
            }
        }
        return false;
    }
    
    private static Set<String> listClans() {
        return (Set<String>)ClanHandler.clans.getConfigurationSection("Clans").getKeys(false);
    }
    
    private static void saveClans() {
        ClanHandler.clans.save();
        ClanHandler.clans.reload();
    }
    
    static {
        ClanHandler.clans = ClanFile.getConfig();
    }
}
