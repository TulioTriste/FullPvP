package net.panda.fullpvp;

import java.io.File;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.milkbowl.vault.chat.Chat;
import net.panda.fullpvp.balance.EconomyCommand;
import net.panda.fullpvp.balance.EconomyManager;
import net.panda.fullpvp.balance.FlatFileEconomyManager;
import net.panda.fullpvp.balance.PayCommand;
import net.panda.fullpvp.claim.ClaimCommand;
import net.panda.fullpvp.claim.ClaimListener;
import net.panda.fullpvp.clans.ClanCommand;
import net.panda.fullpvp.clans.ClanHandler;
import net.panda.fullpvp.clans.ClanListener;
import net.panda.fullpvp.commands.HeadLootCommand;
import net.panda.fullpvp.commands.HostCommand;
import net.panda.fullpvp.commands.RefundCommand;
import net.panda.fullpvp.commands.SetSpawnCommand;
import net.panda.fullpvp.commands.SpawnCommand;
import net.panda.fullpvp.commands.StaffModeCommand;
import net.panda.fullpvp.commands.StatsCommand;
import net.panda.fullpvp.commands.StatsManagerCommand;
import net.panda.fullpvp.commands.VanishCommand;
import net.panda.fullpvp.commands.chat.ChatClearCommand;
import net.panda.fullpvp.commands.chat.MessageCommand;
import net.panda.fullpvp.commands.chat.ReplyCommand;
import net.panda.fullpvp.commands.essentials.BroadcastCommand;
import net.panda.fullpvp.commands.essentials.CraftCommand;
import net.panda.fullpvp.commands.essentials.EnchantCommand;
import net.panda.fullpvp.commands.essentials.FeedCommand;
import net.panda.fullpvp.commands.essentials.FlyCommand;
import net.panda.fullpvp.commands.essentials.FreezeCommand;
import net.panda.fullpvp.commands.essentials.GamemodeCommand;
import net.panda.fullpvp.commands.essentials.HealCommand;
import net.panda.fullpvp.commands.essentials.InvseeCommand;
import net.panda.fullpvp.commands.essentials.ListCommand;
import net.panda.fullpvp.commands.essentials.MoreCommand;
import net.panda.fullpvp.commands.essentials.RenameCommand;
import net.panda.fullpvp.commands.essentials.RepairAllCommand;
import net.panda.fullpvp.commands.essentials.RepairCommand;
import net.panda.fullpvp.commands.essentials.TopCommand;
import net.panda.fullpvp.commands.essentials.VaultCommand;
import net.panda.fullpvp.commands.media.DiscordCommand;
import net.panda.fullpvp.commands.media.StoreCommand;
import net.panda.fullpvp.commands.media.TeamspeakCommand;
import net.panda.fullpvp.commands.media.TwitterCommand;
import net.panda.fullpvp.commands.media.WebsiteCommand;
import net.panda.fullpvp.commands.tournaments.TournamentExecutor;
import net.panda.fullpvp.commands.warps.WarpsCommand;
import net.panda.fullpvp.destroythecore.DTCCommand;
import net.panda.fullpvp.destroythecore.DTCListener;
import net.panda.fullpvp.handler.PlayTimeManager;
import net.panda.fullpvp.handler.SpawnHandler;
import net.panda.fullpvp.kit.FlatFileKitManager;
import net.panda.fullpvp.kit.Kit;
import net.panda.fullpvp.kit.KitExecutor;
import net.panda.fullpvp.kit.KitListener;
import net.panda.fullpvp.kit.KitManager;
import net.panda.fullpvp.listener.ArrowListener;
import net.panda.fullpvp.listener.ChatListener;
import net.panda.fullpvp.listener.ChestListener;
import net.panda.fullpvp.listener.ColouredSignListener;
import net.panda.fullpvp.listener.CombatTagListener;
import net.panda.fullpvp.listener.DeathListener;
import net.panda.fullpvp.listener.EnderpearlListener;
import net.panda.fullpvp.listener.FreezeListener;
import net.panda.fullpvp.listener.HeadLootListener;
import net.panda.fullpvp.listener.PlayerListener;
import net.panda.fullpvp.listener.PotionShopListener;
import net.panda.fullpvp.listener.SellShopListener;
import net.panda.fullpvp.listener.StaffModeListener;
import net.panda.fullpvp.listener.TournamentListener;
import net.panda.fullpvp.listener.VanishListener;
import net.panda.fullpvp.listener.WorldListener;
import net.panda.fullpvp.scoreboard.ScoreboardManager;
import net.panda.fullpvp.tournaments.TournamentManager;
import net.panda.fullpvp.tournaments.file.TournamentFile;
import net.panda.fullpvp.tournaments.runnable.TournamentRunnable;
import net.panda.fullpvp.utilities.Cooldowns;
import net.panda.fullpvp.utilities.SignHandler;
import net.panda.fullpvp.utilities.itemdb.ItemDb;
import net.panda.fullpvp.utilities.itemdb.SimpleItemDb;
import net.panda.fullpvp.utilities.user.ServerHandler;
import net.panda.fullpvp.utilities.user.ServerParticipator;
import net.panda.fullpvp.utilities.user.UserManager;

@Setter
@Getter
public class FullPvP extends JavaPlugin implements Listener {

    @Getter
    private static FullPvP instance;
    private EconomyManager economyManager;
    private ScoreboardManager scoreboardHandler;
    private CombatTagListener combatTagListener;
    private EnderpearlListener enderpearlListener;
    private SpawnHandler spawnHandler;
    private ClanHandler clanHandler;
    private FreezeListener freezeListener;
    private TournamentManager tournamentManager;
    private TournamentFile tournamentFile;
    private TournamentRunnable tournamentRunnable;
    private KitManager kitManager;
    private KitExecutor kitExecutor;
    private ItemDb itemDb;
    private ServerHandler serverHandler;
    private UserManager userManager;
    private PlayTimeManager playTimeManager;
    private SignHandler signHandler;
    private Chat chat;

    public FullPvP() {
        spawnHandler = new SpawnHandler(this);
    }
    
    private boolean loadVault() {
    	RegisteredServiceProvider<Chat> provider = getServer().getServicesManager().getRegistration(Chat.class);
    	if(provider != null) {
    		chat = provider.getProvider();
    	}
    	return (chat != null);
    }
    
    public Chat getChat() {
		return chat;
	}
    
    public void onEnable() {
        load();
        loadVault();
        ConfigurationSerialization.registerClass(Kit.class);
        ConfigurationSerialization.registerClass(ServerParticipator.class);
    }
    
    public void onDisable() {
        unregisterHandlers();
        economyManager.saveEconomyData();
        serverHandler.saveServerData();
        signHandler.cancelTasks(null);
        kitManager.saveKitData();
    }
    
    private void load() {
        FullPvP.instance = this;
        loadVault();
        registerConfiguration();
        registerCommands();
        registerListeners();
        registerScoreboard();
        registerHandlers();
        System.out.println("");
        System.out.println("FullPvP Core");
        System.out.println("Version: 1.0.0");
        System.out.println("Author: TulioTriste & Risas");
        System.out.println("");
        Cooldowns.createCooldown("TOURNAMENT_COOLDOWN");
    }
    
    private void registerCommands() {
    	getCommand("fly").setExecutor(new FlyCommand());
    	getCommand("repairall").setExecutor(new RepairAllCommand());
    	getCommand("repair").setExecutor(new RepairCommand());
    	getCommand("refund").setExecutor(new RefundCommand());
    	getCommand("top").setExecutor(new TopCommand());
    	getCommand("warp").setExecutor(new WarpsCommand());
    	getCommand("reply").setExecutor(new ReplyCommand(this));
    	getCommand("message").setExecutor(new MessageCommand());
    	getCommand("enderchest").setExecutor(new VaultCommand());
    	getCommand("gamemode").setExecutor(new GamemodeCommand());
    	getCommand("invsee").setExecutor(new InvseeCommand(this));
    	getCommand("enchant").setExecutor(new EnchantCommand());
    	getCommand("heal").setExecutor(new HealCommand());
    	getCommand("feed").setExecutor(new FeedCommand());
    	getCommand("more").setExecutor(new MoreCommand());
    	getCommand("rename").setExecutor(new RenameCommand());
    	getCommand("craft").setExecutor(new CraftCommand());
    	getCommand("broadcast").setExecutor(new BroadcastCommand());
        getCommand("setspawn").setExecutor(new SetSpawnCommand());
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("economy").setExecutor(new EconomyCommand(this));
        getCommand("pay").setExecutor(new PayCommand(this));
        getCommand("stats").setExecutor(new StatsCommand());
        getCommand("statsmanager").setExecutor(new StatsManagerCommand());
        getCommand("claim").setExecutor(new ClaimCommand());
        getCommand("headloot").setExecutor(new HeadLootCommand());
        getCommand("destroythecore").setExecutor(new DTCCommand());
        getCommand("clan").setExecutor(new ClanCommand());
        getCommand("list").setExecutor(new ListCommand());
        getCommand("discord").setExecutor(new DiscordCommand());
        getCommand("store").setExecutor(new StoreCommand());
        getCommand("teamspeak").setExecutor(new TeamspeakCommand());
        getCommand("twitter").setExecutor(new TwitterCommand());
        getCommand("website").setExecutor(new WebsiteCommand());
        getCommand("staffmode").setExecutor(new StaffModeCommand());
        getCommand("vanish").setExecutor(new VanishCommand());
        getCommand("chatclear").setExecutor(new ChatClearCommand());
        getCommand("freeze").setExecutor(new FreezeCommand(this));
        getCommand("tournament").setExecutor(new TournamentExecutor());
        getCommand("host").setExecutor(new HostCommand());
        kitExecutor = new KitExecutor(this);
        getCommand("kit").setExecutor(kitExecutor);
    }
    
    private void registerListeners() {
        new HostCommand();
        new ClaimListener(this);
        new ChestListener(this);
//        new SellShopListener(this);
        new PotionShopListener(this);
        new HeadLootListener(this);
        new PlayerListener(this);
        new DTCListener(this);
        new ClanListener(this);
        new ArrowListener(this);
        new DeathListener(this);
        new ChatListener(this);
        new ColouredSignListener(this);
        new VanishListener();
        new StaffModeListener(this);
        new TournamentListener(this);
        new WorldListener(this);
        new KitListener(this);
        (combatTagListener = new CombatTagListener(this)).init();
        (enderpearlListener = new EnderpearlListener(this)).init();
        economyManager = new FlatFileEconomyManager(this);
		tournamentManager = new TournamentManager();
    }
    
    public void directories() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        final File localFile1 = new File(getDataFolder(), "playerdata");
        if (!localFile1.exists()) {
            localFile1.mkdir();
        }
    }
    
	private void registerScoreboard() {
        scoreboardHandler = new ScoreboardManager();
        Bukkit.getPluginManager().registerEvents(scoreboardHandler, this);
        scoreboardHandler.setupScoreboard();
        for (final Player online : Bukkit.getServer().getOnlinePlayers()) {
            scoreboardHandler.registerScoreboard(online);
            scoreboardHandler.resendTabList(online);
        }
        new BukkitRunnable() {
			public void run() {
                for (final Player online : Bukkit.getServer().getOnlinePlayers()) {
                    FullPvP.this.scoreboardHandler.resendTabList(online);
                }
            }
        }.runTaskTimer(this, 20L, 20L);
    }
    
    private void registerHandlers() {
        spawnHandler.enable();
        serverHandler = new ServerHandler(this);
        itemDb = new SimpleItemDb(this);
        signHandler = new SignHandler(this);
        kitManager = new FlatFileKitManager(this);
        userManager = new UserManager(this);
        userManager.saveParticipatorData();
    }
    
    private void unregisterHandlers() {
        spawnHandler.disable();
    }

    private FileConfigurationOptions getFileConfiguration() {
        return getConfig().options();
    }
    
    private void registerConfiguration() {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            final File configuration = new File(getDataFolder(), "config.yml");
            if (configuration.exists()) {
                System.out.println("Config.yml found, loading...");
            }
            else {
                getFileConfiguration().copyDefaults(true);
                saveConfig();
                System.out.println("Config.yml not found, creating...");
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
