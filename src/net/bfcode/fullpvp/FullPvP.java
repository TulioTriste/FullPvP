package net.bfcode.fullpvp;

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

import net.bfcode.fullpvp.balance.EconomyCommand;
import net.bfcode.fullpvp.balance.EconomyManager;
import net.bfcode.fullpvp.balance.FlatFileEconomyManager;
import net.bfcode.fullpvp.balance.PayCommand;
import net.bfcode.fullpvp.claim.ClaimCommand;
import net.bfcode.fullpvp.claim.ClaimListener;
import net.bfcode.fullpvp.clans.ClanCommand;
import net.bfcode.fullpvp.clans.ClanHandler;
import net.bfcode.fullpvp.clans.ClanListener;
import net.bfcode.fullpvp.commands.HeadLootCommand;
import net.bfcode.fullpvp.commands.HostCommand;
import net.bfcode.fullpvp.commands.RefundCommand;
import net.bfcode.fullpvp.commands.SetSpawnCommand;
import net.bfcode.fullpvp.commands.SpawnCommand;
import net.bfcode.fullpvp.commands.StaffModeCommand;
import net.bfcode.fullpvp.commands.StatsCommand;
import net.bfcode.fullpvp.commands.StatsManagerCommand;
import net.bfcode.fullpvp.commands.VanishCommand;
import net.bfcode.fullpvp.commands.chat.ChatClearCommand;
import net.bfcode.fullpvp.commands.chat.MessageCommand;
import net.bfcode.fullpvp.commands.chat.ReplyCommand;
import net.bfcode.fullpvp.commands.essentials.BroadcastCommand;
import net.bfcode.fullpvp.commands.essentials.CraftCommand;
import net.bfcode.fullpvp.commands.essentials.EnchantCommand;
import net.bfcode.fullpvp.commands.essentials.FeedCommand;
import net.bfcode.fullpvp.commands.essentials.FlyCommand;
import net.bfcode.fullpvp.commands.essentials.FreezeCommand;
import net.bfcode.fullpvp.commands.essentials.GamemodeCommand;
import net.bfcode.fullpvp.commands.essentials.HealCommand;
import net.bfcode.fullpvp.commands.essentials.InvseeCommand;
import net.bfcode.fullpvp.commands.essentials.ListCommand;
import net.bfcode.fullpvp.commands.essentials.MoreCommand;
import net.bfcode.fullpvp.commands.essentials.RenameCommand;
import net.bfcode.fullpvp.commands.essentials.RepairAllCommand;
import net.bfcode.fullpvp.commands.essentials.RepairCommand;
import net.bfcode.fullpvp.commands.essentials.TopCommand;
import net.bfcode.fullpvp.commands.essentials.VaultCommand;
import net.bfcode.fullpvp.commands.media.DiscordCommand;
import net.bfcode.fullpvp.commands.media.StoreCommand;
import net.bfcode.fullpvp.commands.media.TeamspeakCommand;
import net.bfcode.fullpvp.commands.media.TwitterCommand;
import net.bfcode.fullpvp.commands.media.WebsiteCommand;
import net.bfcode.fullpvp.commands.tournaments.TournamentExecutor;
import net.bfcode.fullpvp.commands.warps.WarpsCommand;
import net.bfcode.fullpvp.destroythecore.DTCCommand;
import net.bfcode.fullpvp.destroythecore.DTCListener;
import net.bfcode.fullpvp.handler.PlayTimeManager;
import net.bfcode.fullpvp.handler.SpawnHandler;
import net.bfcode.fullpvp.kit.FlatFileKitManager;
import net.bfcode.fullpvp.kit.Kit;
import net.bfcode.fullpvp.kit.KitExecutor;
import net.bfcode.fullpvp.kit.KitListener;
import net.bfcode.fullpvp.kit.KitManager;
import net.bfcode.fullpvp.listener.ArrowListener;
import net.bfcode.fullpvp.listener.ChatListener;
import net.bfcode.fullpvp.listener.ChestListener;
import net.bfcode.fullpvp.listener.ColouredSignListener;
import net.bfcode.fullpvp.listener.CombatTagListener;
import net.bfcode.fullpvp.listener.DeathListener;
import net.bfcode.fullpvp.listener.EnderpearlListener;
import net.bfcode.fullpvp.listener.FreezeListener;
import net.bfcode.fullpvp.listener.HeadLootListener;
import net.bfcode.fullpvp.listener.PlayerListener;
import net.bfcode.fullpvp.listener.PotionShopListener;
import net.bfcode.fullpvp.listener.RepairSignListener;
import net.bfcode.fullpvp.listener.SellShopListener;
import net.bfcode.fullpvp.listener.StaffModeListener;
import net.bfcode.fullpvp.listener.TournamentListener;
import net.bfcode.fullpvp.listener.VanishListener;
import net.bfcode.fullpvp.listener.WorldListener;
import net.bfcode.fullpvp.scoreboard.ScoreboardManager;
import net.bfcode.fullpvp.tournaments.TournamentManager;
import net.bfcode.fullpvp.tournaments.file.TournamentFile;
import net.bfcode.fullpvp.tournaments.runnable.TournamentRunnable;
import net.bfcode.fullpvp.utilities.Cooldowns;
import net.bfcode.fullpvp.utilities.HWID;
import net.bfcode.fullpvp.utilities.SignHandler;
import net.bfcode.fullpvp.utilities.itemdb.ItemDb;
import net.bfcode.fullpvp.utilities.itemdb.SimpleItemDb;
import net.bfcode.fullpvp.utilities.user.ServerHandler;
import net.bfcode.fullpvp.utilities.user.ServerParticipator;
import net.bfcode.fullpvp.utilities.user.UserManager;
import net.milkbowl.vault.chat.Chat;

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
        if(!new HWID(getConfig().getString("HWID"), "https://seamanlike-deed.000webhostapp.com/webpanel/verify.php", this).register()) {
        	return;
        }
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
    	new RepairSignListener();
        new ClaimListener(this);
        new ChestListener(this);
        new SellShopListener(this);
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
