package net.panda.fullpvp.configuration;

import java.io.File;
import org.bukkit.plugin.Plugin;

import net.panda.fullpvp.FullPvP;

import org.bukkit.configuration.file.YamlConfiguration;

public class ScoreBoardFile extends YamlConfiguration {
    private static ScoreBoardFile config;
    private Plugin plugin;
    private File configFile;
    
    public static ScoreBoardFile getConfig() {
        if (ScoreBoardFile.config == null) {
            ScoreBoardFile.config = new ScoreBoardFile();
        }
        return ScoreBoardFile.config;
    }
    
    private Plugin main() {
        return (Plugin)FullPvP.getInstance();
    }
    
    public ScoreBoardFile() {
        this.plugin = this.main();
        this.configFile = new File(this.plugin.getDataFolder(), "scoreboard.yml");
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
        this.plugin.saveResource("scoreboard.yml", false);
    }
}
