package xyz.ctbt.lifeTake;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.ctbt.lifeTake.commands.*;
import xyz.ctbt.lifeTake.data.PlayerDataManager;
import xyz.ctbt.lifeTake.items.EnchantedGoldenTotemAppleManager;
import xyz.ctbt.lifeTake.items.GoldenTotemAppleManager;
import xyz.ctbt.lifeTake.items.LifeTokenItem;
import xyz.ctbt.lifeTake.items.TotemAppleManager;
import xyz.ctbt.lifeTake.listeners.*;
import xyz.ctbt.lifeTake.util.TablistManager;

public class Main extends JavaPlugin {

    private int lifeTokenThreshold;
    private int heartTokenThreshold;
    private boolean crystalPvPEnabled = false;

    @Override
    public void onEnable() {
        // Set defaults if not set in config
        saveDefaultConfig(); // creates config.yml with defaults if not exists
        ConfigManager.initialize(this);

        // Load thresholds from config
        this.lifeTokenThreshold = getConfig().getInt("life-token-threshold", 3);
        this.heartTokenThreshold = getConfig().getInt("heart-token-threshold", 5);

        getLogger().info("LifeTake plugin enabled!");

        // Shared PlayerDataManager instance
        PlayerDataManager dataManager = new PlayerDataManager();

        // Register event listeners
        getServer().getPluginManager().registerEvents(new PlayerKillListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this, dataManager), this);
        getServer().getPluginManager().registerEvents(new VillagerTradeBlocker(), this);
        getServer().getPluginManager().registerEvents(new ElytraGenerationBlocker(), this);
        getServer().getPluginManager().registerEvents(new PvPDamageListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawnListener(this), this);
        getServer().getPluginManager().registerEvents(new EndCrystalDamageBlocker(this), this);
        getServer().getPluginManager().registerEvents(new NetheriteDisabler(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathHandler(this), this);
        getServer().getPluginManager().registerEvents(new TotemUseListener(), this);
        getServer().getPluginManager().registerEvents(new DamageImmunityListener(this), this);

        // Register commands
        if(getCommand("togglecrystal") != null)
            getCommand("togglecrystal").setExecutor(new ToggleCrystalPvpCommand(this));

        if (getCommand("toggleglobalpvp") != null)
            getCommand("toggleglobalpvp").setExecutor(new ToggleGlobalPvPCommand());

        if (getCommand("usehearttoken") != null)
            getCommand("usehearttoken").setExecutor(new UseHeartTokenCommand());
        else
            getLogger().warning("Command 'usehearttoken' not found in plugin.yml!");

        if (getCommand("uselifetoken") != null)
            getCommand("uselifetoken").setExecutor(new UseLifeTokenCommand(this));
        else
            getLogger().warning("Command 'uselifetoken' not found in plugin.yml!");

        if (getCommand("givetoken") != null)
            getCommand("givetoken").setExecutor(new GiveTokenCommand());
        else
            getLogger().warning("Command 'givetoken' not found in plugin.yml!");

        if (getCommand("sethearts") != null)
            getCommand("sethearts").setExecutor(new SetHeartsCommand());
        else
            getLogger().warning("Command 'sethearts' not found in plugin.yml!");

        if (getCommand("setthreshold") != null)
            getCommand("setthreshold").setExecutor(new SetThresholdCommand(this));
        else
            getLogger().warning("Command 'setthreshold' not found in plugin.yml!");

        if (getCommand("togglepvpimmunity") != null)
            getCommand("togglepvpimmunity").setExecutor(new TogglePvPImmunityCommand());

        // Register custom items
        new TotemAppleManager(this).register();
        new GoldenTotemAppleManager(this).register();
        new EnchantedGoldenTotemAppleManager(this).register();
        LifeTokenItem.registerRecipe(this);


        // Update tablist for all players
        TablistManager.updateAll(this);
    }

    public void setLifeTokenThreshold(int value) {
        this.lifeTokenThreshold = value;
        getConfig().set("life-token-threshold", value);
        saveConfig();
    }

    public void setHeartTokenThreshold(int value) {
        this.heartTokenThreshold = value;
        getConfig().set("heart-token-threshold", value);
        saveConfig();
    }

    public boolean isCrystalPvPEnabled() {
        return crystalPvPEnabled;
    }

    public void toggleCrystalPvP() {
        crystalPvPEnabled = !crystalPvPEnabled;
    }

}
