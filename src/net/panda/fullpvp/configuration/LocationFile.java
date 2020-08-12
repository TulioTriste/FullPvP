package net.panda.fullpvp.configuration;

import java.io.File;
import org.bukkit.plugin.Plugin;

import net.panda.fullpvp.FullPvP;

import org.bukkit.configuration.file.YamlConfiguration;

public class LocationFile extends YamlConfiguration {
    private static LocationFile config;
    private Plugin plugin;
    private File configFile;
    
    public static LocationFile getConfig() {
        if (LocationFile.config == null) {
            LocationFile.config = new LocationFile();
        }
        return LocationFile.config;
    }
    
    private Plugin main() {
        return (Plugin)FullPvP.getInstance();
    }
    
    public LocationFile() {
        this.plugin = this.main();
        this.configFile = new File(this.plugin.getDataFolder(), "locations.yml");
        this.saveDefault();
        this.reload();
    }
    
    public void reload() {
        try {
            super.load(this.configFile);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void save() {
        try {
            super.save(this.configFile);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void saveDefault() {
        this.plugin.saveResource("locations.yml", false);
    }
}
