package net.bfcode.fullpvp.tournaments.file;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import net.bfcode.fullpvp.FullPvP;

public class TournamentFile extends YamlConfiguration {

	private static TournamentFile config;
    private Plugin plugin;
    private File configFile;
    
    public static TournamentFile getConfig() {
        if (TournamentFile.config == null) {
            TournamentFile.config = new TournamentFile();
        }
        return TournamentFile.config;
    }
    
    private Plugin main() {
        return FullPvP.getPlugin();
    }
    
    public TournamentFile() {
        this.plugin = this.main();
        this.configFile = new File(this.plugin.getDataFolder(), "tournament.yml");
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
        this.plugin.saveResource("tournament.yml", false);
    }
}