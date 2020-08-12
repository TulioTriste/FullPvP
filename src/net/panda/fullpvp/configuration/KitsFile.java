package net.panda.fullpvp.configuration;

import java.io.File;
import org.bukkit.plugin.Plugin;

import net.panda.fullpvp.FullPvP;

import org.bukkit.configuration.file.YamlConfiguration;

public class KitsFile extends YamlConfiguration
{
    private static KitsFile config;
    private Plugin plugin;
    private File configFile;
    
    public static KitsFile getConfig() {
        if (KitsFile.config == null) {
            KitsFile.config = new KitsFile();
        }
        return KitsFile.config;
    }
    
    private Plugin main() {
        return FullPvP.getInstance();
    }
    
    public KitsFile() {
        this.plugin = this.main();
        this.configFile = new File(this.plugin.getDataFolder(), "kits.yml");
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
        this.plugin.saveResource("kits.yml", false);
    }
}
