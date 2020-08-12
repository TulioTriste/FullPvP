package net.panda.fullpvp.utilities.user;

import java.util.LinkedHashMap;
import java.util.Map;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class NameHistory implements ConfigurationSerializable
{
    private final String name;
    private final long millis;
    
    public NameHistory(final Map<String, Object> map) {
        this.name = (String) map.get("name");
        this.millis = Long.parseLong((String) map.get("millis"));
    }
    
    public NameHistory(final String name, final long millis) {
        this.name = name;
        this.millis = millis;
    }
    
    public Map<String, Object> serialize() {
        final LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("name", this.name);
        map.put("millis", Long.toString(this.millis));
        return map;
    }
    
    public String getName() {
        return this.name;
    }
    
    public long getMillis() {
        return this.millis;
    }
}
