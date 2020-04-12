package net.galanthus.fullpvp.kit.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.galanthus.fullpvp.kit.Kit;

public class KitCreateEvent extends Event implements Cancellable
{
    private static final HandlerList handlers;
    private final Kit kit;
    private boolean cancelled;
    
    public KitCreateEvent(final Kit kit) {
        this.cancelled = false;
        this.kit = kit;
    }
    
    public static HandlerList getHandlerList() {
        return KitCreateEvent.handlers;
    }
    
    public Kit getKit() {
        return this.kit;
    }
    
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    public HandlerList getHandlers() {
        return KitCreateEvent.handlers;
    }
    
    static {
        handlers = new HandlerList();
    }
}
