package net.panda.fullpvp.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerFreezeEvent extends PlayerEvent implements Cancellable
{
    private static final HandlerList handlers;
    private final boolean frozen;
    private boolean cancelled;
    
    public PlayerFreezeEvent(final Player player, final boolean frozen) {
        super(player);
        this.frozen = frozen;
    }
    
    public static HandlerList getHandlerList() {
        return PlayerFreezeEvent.handlers;
    }
    
    public boolean isFrozen() {
        return this.frozen;
    }
    
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    public HandlerList getHandlers() {
        return PlayerFreezeEvent.handlers;
    }
    
    static {
        handlers = new HandlerList();
    }
}
