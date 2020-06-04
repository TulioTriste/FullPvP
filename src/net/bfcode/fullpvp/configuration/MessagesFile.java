package net.bfcode.fullpvp.configuration;

import java.io.File;
import org.bukkit.plugin.Plugin;

import net.bfcode.fullpvp.FullPvP;

import org.bukkit.configuration.file.YamlConfiguration;

public class MessagesFile extends YamlConfiguration {
    private static MessagesFile config;
    private Plugin plugin;
    private File configFile;
    
    public static MessagesFile getConfig() {
        if (MessagesFile.config == null) {
            MessagesFile.config = new MessagesFile();
        }
        return MessagesFile.config;
    }
    
    private Plugin main() {
        return (Plugin)FullPvP.getPlugin();
    }
    
    public MessagesFile() {
        this.plugin = this.main();
        this.configFile = new File(this.plugin.getDataFolder(), "messages.yml");
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
        this.plugin.saveResource("messages.yml", false);
    }
}
