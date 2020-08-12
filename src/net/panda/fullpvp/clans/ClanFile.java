package net.panda.fullpvp.clans;

import java.io.File;
import org.bukkit.plugin.Plugin;

import net.panda.fullpvp.FullPvP;

import org.bukkit.configuration.file.YamlConfiguration;

public class ClanFile extends YamlConfiguration
{
    private static ClanFile config;
    private Plugin plugin;
    private File configFile;
    
    public static ClanFile getConfig() {
        if (ClanFile.config == null) {
            ClanFile.config = new ClanFile();
        }
        return ClanFile.config;
    }
    
    private Plugin main() {
        return (Plugin)FullPvP.getInstance();
    }
    
    public ClanFile() {
        this.plugin = this.main();
        this.configFile = new File(this.plugin.getDataFolder(), "clans.yml");
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
        this.plugin.saveResource("clans.yml", false);
    }
}
