package net.bfcode.fullpvp.utilities.user;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import net.bfcode.fullpvp.FullPvP;


public class ServerHandler {
    private final List<String> announcements;
    private final FullPvP plugin;
    public boolean useProtocolLib;
    private int clearlagdelay;
    private int announcementDelay;
    private long chatSlowedMillis;
    private long chatDisabledMillis;
    private int chatSlowedDelay;
    private String broadcastFormat;
    private FileConfiguration config;
    private boolean decreasedLagMode;
    private boolean end;
    private Location endExit;
    private boolean donorOnly;
    private int worldBorder;
    private int netherBorder;
    private int endBorder;
    
    public ServerHandler(final FullPvP plugin) {
        this.announcements = new ArrayList<String>();
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.reloadServerData();
    }
    
    public void setServerBorder(final World.Environment environment, final Integer integer) {
        if (environment.equals(World.Environment.NORMAL)) {
            this.worldBorder = integer;
        }
        else if (environment.equals(World.Environment.NETHER)) {
            this.netherBorder = integer;
        }
        else if (environment.equals(World.Environment.THE_END)) {
            this.endBorder = integer;
        }
    }
    
    private void reloadServerData() {
        this.plugin.reloadConfig();
        this.config = this.plugin.getConfig();
    }
    
    public void saveServerData() {
        this.plugin.saveConfig();
    }
    
    public void setChatSlowedMillis(final long ticks) {
        this.chatSlowedMillis = System.currentTimeMillis() + ticks;
    }
    
    public long getRemainingChatSlowedMillis() {
        return this.chatSlowedMillis - System.currentTimeMillis();
    }
    
    public boolean isChatDisabled() {
        return this.getRemainingChatDisabledMillis() > 0L;
    }
    
    public long getChatDisabledMillis() {
        return this.chatDisabledMillis;
    }
    
    public void setChatDisabledMillis(final long ticks) {
        final long millis = System.currentTimeMillis();
        this.chatDisabledMillis = millis + ticks;
    }
    
    public long getRemainingChatDisabledMillis() {
        return this.chatDisabledMillis - System.currentTimeMillis();
    }
    
    public int getChatSlowedDelay() {
        return this.chatSlowedDelay;
    }
    
    public List<String> getAnnouncements() {
        return this.announcements;
    }
    
    public boolean isUseProtocolLib() {
        return this.useProtocolLib;
    }
    
    public void setUseProtocolLib(final boolean useProtocolLib) {
        this.useProtocolLib = useProtocolLib;
    }
    
    public int getClearlagdelay() {
        return this.clearlagdelay;
    }
    
    public void setClearlagdelay(final int clearlagdelay) {
        this.clearlagdelay = clearlagdelay;
    }
    
    public int getAnnouncementDelay() {
        return this.announcementDelay;
    }
    
    public void setAnnouncementDelay(final int announcementDelay) {
        this.announcementDelay = announcementDelay;
    }
    
    public long getChatSlowedMillis() {
        return this.chatSlowedMillis;
    }
    
    public void setChatSlowedDelay(final int chatSlowedDelay) {
        this.chatSlowedDelay = chatSlowedDelay;
    }
    
    public String getBroadcastFormat() {
        return this.broadcastFormat;
    }
    
    public FileConfiguration getConfig() {
        return this.config;
    }
    
    public boolean isDecreasedLagMode() {
        return this.decreasedLagMode;
    }
    
    public void setDecreasedLagMode(final boolean decreasedLagMode) {
        this.decreasedLagMode = decreasedLagMode;
    }
    
    public boolean isEnd() {
        return this.end;
    }
    
    public void setEnd(final boolean end) {
        this.end = end;
    }
    
    public Location getEndExit() {
        return this.endExit;
    }
    
    public boolean isDonorOnly() {
        return this.donorOnly;
    }
    
    public void setDonorOnly(final boolean donorOnly) {
        this.donorOnly = donorOnly;
    }
    
    public int getWorldBorder() {
        return this.worldBorder;
    }
    
    public int getNetherBorder() {
        return this.netherBorder;
    }
    
    public int getEndBorder() {
        return this.endBorder;
    }

}
