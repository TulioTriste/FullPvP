package net.bfcode.fullpvp.configuration;

import java.io.File;
import org.bukkit.plugin.Plugin;

import net.bfcode.fullpvp.FullPvP;

import org.bukkit.configuration.file.YamlConfiguration;

public class PointsFile extends YamlConfiguration {
    private static PointsFile config;
    private Plugin plugin;
    private File configFile;
    
    public static PointsFile getConfig() {
        if (PointsFile.config == null) {
            PointsFile.config = new PointsFile();
        }
        return PointsFile.config;
    }
    
    private Plugin main() {
        return (Plugin)FullPvP.getPlugin();
    }
    
    public PointsFile() {
        this.plugin = this.main();
        this.configFile = new File(this.plugin.getDataFolder(), "points.yml");
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
        this.plugin.saveResource("points.yml", false);
    }
}
