package net.panda.fullpvp.utilities.user;

import java.util.LinkedHashMap;
import java.util.Set;
import java.util.HashMap;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import org.bukkit.command.ConsoleCommandSender;
import com.google.common.base.Preconditions;

import net.panda.fullpvp.FullPvP;
import net.panda.fullpvp.utilities.Config;

import org.bukkit.command.CommandSender;

import java.util.UUID;
import java.util.Map;

import org.bukkit.plugin.java.JavaPlugin;

public class UserManager
{
    private final ConsoleUser console;
    private final JavaPlugin plugin;
    private Config userConfig;
    private Map<UUID, ServerParticipator> participators;
    
    public UserManager(final FullPvP plugin) {
        this.plugin = plugin;
        this.reloadParticipatorData();
        final ServerParticipator participator = this.participators.get(ConsoleUser.CONSOLE_UUID);
        if (participator != null) {
            this.console = (ConsoleUser)participator;
        }
        else {
            this.participators.put(ConsoleUser.CONSOLE_UUID, this.console = new ConsoleUser());
        }
    }
    
    public ConsoleUser getConsole() {
        return this.console;
    }
    
    public Map<UUID, ServerParticipator> getParticipators() {
        return this.participators;
    }
    
    public ServerParticipator getParticipator(final CommandSender sender) {
        Preconditions.checkNotNull(sender, "CommandSender cannot be null");
        if (sender instanceof ConsoleCommandSender) {
            return this.console;
        }
        if (sender instanceof Player) {
            return this.participators.get(((Player)sender).getUniqueId());
        }
        return null;
    }
    
    public ServerParticipator getParticipator(final UUID uuid) {
        Preconditions.checkNotNull(uuid, "Unique ID cannot be null");
        return this.participators.get(uuid);
    }
    
    public BaseUser getUser(final UUID uuid) {
        final ServerParticipator participator = this.getParticipator(uuid);
        BaseUser baseUser;
        if (participator != null && participator instanceof BaseUser) {
            baseUser = (BaseUser)participator;
        }
        else {
            this.participators.put(uuid, baseUser = new BaseUser(uuid));
        }
        return baseUser;
    }
    
    public void reloadParticipatorData() {
        this.userConfig = new Config(this.plugin, "participators");
        final Object object = this.userConfig.get("participators");
        if (object instanceof MemorySection) {
            final MemorySection section = (MemorySection)object;
            final Set<String> keys = (Set<String>)section.getKeys(false);
            this.participators = new HashMap<UUID, ServerParticipator>(keys.size());
            for (final String id : keys) {
                this.participators.put(UUID.fromString(id), (ServerParticipator)this.userConfig.get("participators." + id));
            }
        }
        else {
            this.participators = new HashMap<UUID, ServerParticipator>();
        }
    }
    
    public void saveParticipatorData() {
        final Map<String, ServerParticipator> saveMap = new LinkedHashMap<String, ServerParticipator>(this.participators.size());
        for (final Map.Entry<UUID, ServerParticipator> entry : this.participators.entrySet()) {
            saveMap.put(entry.getKey().toString(), entry.getValue());
        }
        this.userConfig.set("participators", saveMap);
        this.userConfig.save();
    }
}
