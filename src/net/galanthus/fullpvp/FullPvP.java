package net.galanthus.fullpvp;

import java.io.File;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.galanthus.fullpvp.balance.EconomyCommand;
import net.galanthus.fullpvp.balance.EconomyManager;
import net.galanthus.fullpvp.balance.FlatFileEconomyManager;
import net.galanthus.fullpvp.balance.PayCommand;
import net.galanthus.fullpvp.claim.ClaimCommand;
import net.galanthus.fullpvp.claim.ClaimListener;
import net.galanthus.fullpvp.clans.ClanCommand;
import net.galanthus.fullpvp.clans.ClanHandler;
import net.galanthus.fullpvp.clans.ClanListener;
import net.galanthus.fullpvp.commands.GiveawayCommand;
import net.galanthus.fullpvp.commands.GunCommand;
import net.galanthus.fullpvp.commands.HeadLootCommand;
import net.galanthus.fullpvp.commands.PointsCommand;
import net.galanthus.fullpvp.commands.RefundCommand;
import net.galanthus.fullpvp.commands.SetSpawnCommand;
import net.galanthus.fullpvp.commands.SpawnCommand;
import net.galanthus.fullpvp.commands.StaffModeCommand;
import net.galanthus.fullpvp.commands.StatsCommand;
import net.galanthus.fullpvp.commands.StatsManagerCommand;
import net.galanthus.fullpvp.commands.VanishCommand;
import net.galanthus.fullpvp.commands.chat.ChatClearCommand;
import net.galanthus.fullpvp.commands.chat.MessageCommand;
import net.galanthus.fullpvp.commands.chat.ReplyCommand;
import net.galanthus.fullpvp.commands.essentials.BroadcastCommand;
import net.galanthus.fullpvp.commands.essentials.CraftCommand;
import net.galanthus.fullpvp.commands.essentials.EnchantCommand;
import net.galanthus.fullpvp.commands.essentials.FeedCommand;
import net.galanthus.fullpvp.commands.essentials.FlyCommand;
import net.galanthus.fullpvp.commands.essentials.FreezeCommand;
import net.galanthus.fullpvp.commands.essentials.GamemodeCommand;
import net.galanthus.fullpvp.commands.essentials.HealCommand;
import net.galanthus.fullpvp.commands.essentials.InvseeCommand;
import net.galanthus.fullpvp.commands.essentials.ListCommand;
import net.galanthus.fullpvp.commands.essentials.MoreCommand;
import net.galanthus.fullpvp.commands.essentials.RenameCommand;
import net.galanthus.fullpvp.commands.essentials.RepairAllCommand;
import net.galanthus.fullpvp.commands.essentials.RepairCommand;
import net.galanthus.fullpvp.commands.essentials.TopCommand;
import net.galanthus.fullpvp.commands.essentials.VaultCommand;
import net.galanthus.fullpvp.commands.media.DiscordCommand;
import net.galanthus.fullpvp.commands.media.StoreCommand;
import net.galanthus.fullpvp.commands.media.TeamspeakCommand;
import net.galanthus.fullpvp.commands.media.TwitterCommand;
import net.galanthus.fullpvp.commands.media.WebsiteCommand;
import net.galanthus.fullpvp.commands.tournaments.TournamentExecutor;
import net.galanthus.fullpvp.commands.tournaments.arguments.TournamentHostArgument;
import net.galanthus.fullpvp.commands.warps.WarpsCommand;
import net.galanthus.fullpvp.destroythecore.DTCCommand;
import net.galanthus.fullpvp.destroythecore.DTCListener;
import net.galanthus.fullpvp.handler.PlayTimeManager;
import net.galanthus.fullpvp.handler.SpawnHandler;
import net.galanthus.fullpvp.kit.FlatFileKitManager;
import net.galanthus.fullpvp.kit.Kit;
import net.galanthus.fullpvp.kit.KitExecutor;
import net.galanthus.fullpvp.kit.KitListener;
import net.galanthus.fullpvp.kit.KitManager;
import net.galanthus.fullpvp.koth.KothExecutor;
import net.galanthus.fullpvp.koth.KothListener;
import net.galanthus.fullpvp.listener.ArrowListener;
import net.galanthus.fullpvp.listener.ChatListener;
import net.galanthus.fullpvp.listener.ChestListener;
import net.galanthus.fullpvp.listener.ColouredSignListener;
import net.galanthus.fullpvp.listener.CombatTagListener;
import net.galanthus.fullpvp.listener.DeathListener;
import net.galanthus.fullpvp.listener.EnderpearlListener;
import net.galanthus.fullpvp.listener.FreezeListener;
import net.galanthus.fullpvp.listener.GiveawayListener;
import net.galanthus.fullpvp.listener.GunListener;
import net.galanthus.fullpvp.listener.HeadLootListener;
import net.galanthus.fullpvp.listener.PlayerListener;
import net.galanthus.fullpvp.listener.PointsListener;
import net.galanthus.fullpvp.listener.PotionShopListener;
import net.galanthus.fullpvp.listener.RepairSignListener;
import net.galanthus.fullpvp.listener.SellShopListener;
import net.galanthus.fullpvp.listener.StaffModeListener;
import net.galanthus.fullpvp.listener.TournamentListener;
import net.galanthus.fullpvp.listener.VanishListener;
import net.galanthus.fullpvp.listener.WorldListener;
import net.galanthus.fullpvp.scoreboard.ScoreboardManager;
import net.galanthus.fullpvp.tournaments.TournamentManager;
import net.galanthus.fullpvp.tournaments.file.TournamentFile;
import net.galanthus.fullpvp.tournaments.runnable.TournamentRunnable;
import net.galanthus.fullpvp.utilities.Cooldowns;
import net.galanthus.fullpvp.utilities.SignHandler;
import net.galanthus.fullpvp.utilities.itemdb.ItemDb;
import net.galanthus.fullpvp.utilities.itemdb.SimpleItemDb;
import net.galanthus.fullpvp.utilities.user.ServerHandler;
import net.galanthus.fullpvp.utilities.user.ServerParticipator;
import net.galanthus.fullpvp.utilities.user.UserManager;
import net.milkbowl.vault.chat.Chat;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class FullPvP extends JavaPlugin implements Listener {
	
    public static FullPvP instance;
    private static FullPvP plugin;
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
        registerConfiguration();
        FullPvP.plugin = this;
        System.out.println("");
        System.out.println("FullPvP Core - GalanthusMC");
        System.out.println("Version: 1.0.0");
        System.out.println("Author: JavaPinq & Risas");
        System.out.println("");
        registerCommands();
        registerListeners();
        registerScoreboard();
        registerHandlers();
        loadVault();
        Cooldowns.createCooldown("TOURNAMENT_COOLDOWN");
    }
    
    private void registerCommands() {
    	getCommand("fly").setExecutor(new FlyCommand());
    	getCommand("repairall").setExecutor(new RepairAllCommand());
    	getCommand("repair").setExecutor(new RepairCommand());
    	getCommand("refund").setExecutor(new RefundCommand());
    	getCommand("points").setExecutor(new PointsCommand());
    	getCommand("top").setExecutor(new TopCommand());
    	getCommand("warp").setExecutor(new WarpsCommand());
    	getCommand("reply").setExecutor(new ReplyCommand(this));
    	getCommand("message").setExecutor(new MessageCommand(this));
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
        getCommand("gun").setExecutor(new GunCommand());
        getCommand("headloot").setExecutor(new HeadLootCommand());
        getCommand("destroythecore").setExecutor(new DTCCommand());
        getCommand("clan").setExecutor(new ClanCommand());
        getCommand("giveaway").setExecutor(new GiveawayCommand());
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
        getCommand("koth").setExecutor(new KothExecutor(this));
        kitExecutor = new KitExecutor(this);
        getCommand("kit").setExecutor(kitExecutor);
    }
    
    private void registerListeners() {
        final PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new TournamentHostArgument(), this);
        manager.registerEvents(new PointsListener(), this);
    	new RepairSignListener(this);
        new ClaimListener(this);
        new GunListener(this);
        new ChestListener(this);
        new SellShopListener(this);
        new PotionShopListener(this);
        new HeadLootListener(this);
        new PlayerListener(this);
        new DTCListener(this);
        new ClanListener(this);
        new GiveawayListener(this);
        new ArrowListener(this);
        new DeathListener(this);
        new ChatListener(this);
        new ColouredSignListener(this);
        new VanishListener();
        new StaffModeListener(this);
        new TournamentListener(this);
        new KothListener(this);
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
    
    public SpawnHandler getSpawnHandler() {
    	return spawnHandler; 
    }
    
    public EnderpearlListener getEnderpearlListener() {
    	return enderpearlListener; 
    }
    
    public CombatTagListener getCombatTagListener() {
    	return combatTagListener; 
    }
    
    public static FullPvP getPlugin() {
    	return FullPvP.plugin; 
    }
    
    public static FullPvP getInstance() {
    	return FullPvP.instance; 
    }
    
    public ClanHandler getClanHandler() {
    	return clanHandler; 
    }

    public TournamentManager getTournamentManager() {
    	return tournamentManager; 
    }
    
    public TournamentFile getTournamentFile() {
    	return tournamentFile; 
    }
    
    public TournamentRunnable getTournamentRunnable() {
    	return tournamentRunnable; 
    }
    
    public EconomyManager getEconomyManager() {
    	return economyManager; 
    }
    
    public FreezeListener getFreezeListener() {
    	return freezeListener; 
    }
    
    public KitManager getKitManager() {
    	return kitManager; 
    }
    
    public ServerHandler getServerHandler() {
    	return serverHandler; 
    }
    
    public UserManager getUserManager() {
    	return userManager; 
    }
    
    public ItemDb getItemDb() {
    	return itemDb; 
    }
    
    public PlayTimeManager getPlayTimeManager() {
    	return playTimeManager; 
    }
    
    public SignHandler getSignHandler() {
    	return signHandler; 
    }
}
