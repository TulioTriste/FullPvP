package net.panda.fullpvp.utilities.user;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import com.google.common.collect.Sets;

import net.panda.fullpvp.utilities.GenericUtils;

public abstract class ServerParticipator implements ConfigurationSerializable {
    private final UUID uniqueId;
    private final Set<String> ignoring;
    private final Set<String> messageSpying;
    private UUID lastRepliedTo;
    private boolean messagesVisible;
    private long lastSpeakTimeMillis;
    private long lastReceivedMessageMillis;
    private long lastSentMessageMillis;
    
	public ServerParticipator(final UUID uniqueId) {
        this.ignoring = (Set<String>)Sets.newTreeSet((Comparator<String>)String.CASE_INSENSITIVE_ORDER);
        this.messageSpying = Sets.newHashSet();
        this.messagesVisible = true;
        this.uniqueId = uniqueId;
    }
    
	public ServerParticipator(final Map<String, Object> map) {
        this.ignoring = (Set<String>)Sets.newTreeSet((Comparator<String>)String.CASE_INSENSITIVE_ORDER);
        this.messageSpying = Sets.newHashSet();
        this.messagesVisible = true;
        this.uniqueId = UUID.fromString((String) map.get("uniqueID"));
        this.ignoring.addAll(GenericUtils.createList(map.get("ignoring"), String.class));
        this.messageSpying.addAll(GenericUtils.createList(map.get("messageSpying"), String.class));
        Object object = map.get("lastRepliedTo");
        if (object instanceof String) {
            this.lastRepliedTo = UUID.fromString((String)object);
        }
        if ((object = map.get("messagesVisible")) instanceof Boolean) {
            this.messagesVisible = (boolean)object;
        }
        if ((object = map.get("lastSpeakTimeMillis")) instanceof String) {
            this.lastSpeakTimeMillis = Long.parseLong((String)object);
        }
        if ((object = map.get("lastReceivedMessageMillis")) instanceof String) {
            this.lastReceivedMessageMillis = Long.parseLong((String)object);
        }
        if ((object = map.get("lastSentMessageMillis")) instanceof String) {
            this.lastSentMessageMillis = Long.parseLong((String)object);
        }
    }
    
    public Map<String, Object> serialize() {
        final LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("uniqueID", this.uniqueId.toString());
        map.put("ignoring", new ArrayList<String>(this.ignoring));
        map.put("messageSpying", new ArrayList<String>(this.messageSpying));
        if (this.lastRepliedTo != null) {
            map.put("lastRepliedTo", this.lastRepliedTo.toString());
        }
        map.put("messagesVisible", this.messagesVisible);
        map.put("lastSpeakTimeMillis", Long.toString(this.lastSpeakTimeMillis));
        map.put("lastReceivedMessageMillis", Long.toString(this.lastReceivedMessageMillis));
        map.put("lastSentMessageMillis", Long.toString(this.lastSentMessageMillis));
        return map;
    }
    
    public abstract String getName();
    
    public UUID getUniqueId() {
        return this.uniqueId;
    }
    
    public Set<String> getIgnoring() {
        return this.ignoring;
    }
    
    public Set<String> getMessageSpying() {
        return this.messageSpying;
    }
    
    public UUID getLastRepliedTo() {
        return this.lastRepliedTo;
    }
    
    public void setLastRepliedTo(final UUID lastRepliedTo) {
        this.lastRepliedTo = lastRepliedTo;
    }
    
    public Player getLastRepliedToPlayer() {
        return Bukkit.getPlayer(this.lastRepliedTo);
    }
    
    public boolean isMessagesVisible() {
        return this.messagesVisible;
    }
    
    public void setMessagesVisible(final boolean messagesVisible) {
        this.messagesVisible = messagesVisible;
    }
    
    public long getLastSpeakTimeRemaining() {
        if (this.lastSpeakTimeMillis > 0L) {
            return this.lastSpeakTimeMillis - System.currentTimeMillis();
        }
        return 0L;
    }
    
    public long getLastSpeakTimeMillis() {
        return this.lastSpeakTimeMillis;
    }
    
    public void setLastSpeakTimeMillis(final long lastSpeakTimeMillis) {
        this.lastSpeakTimeMillis = lastSpeakTimeMillis;
    }
    
    public long getLastReceivedMessageMillis() {
        return this.lastReceivedMessageMillis;
    }
    
    public void setLastReceivedMessageMillis(final long lastReceivedMessageMillis) {
        this.lastReceivedMessageMillis = lastReceivedMessageMillis;
    }
    
    public long getLastSentMessageMillis() {
        return this.lastSentMessageMillis;
    }
    
    public void setLastSentMessageMillis(final long lastSentMessageMillis) {
        this.lastSentMessageMillis = lastSentMessageMillis;
    }
}
