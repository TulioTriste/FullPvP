package net.bfcode.fullpvp.handler;

import net.bfcode.fullpvp.FullPvP;

public class Handler
{
    private FullPvP plugin;
    
    public Handler(final FullPvP plugin) {
        this.plugin = plugin;
    }
    
    public void enable() {
    }
    
    public void disable() {
    }
    
    public FullPvP getInstance() {
        return this.plugin;
    }
}
