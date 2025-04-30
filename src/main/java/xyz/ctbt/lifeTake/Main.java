package xyz.ctbt.lifeTake;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.ctbt.lifeTake.commands.*;
import xyz.ctbt.lifeTake.data.PlayerDataManager;
import xyz.ctbt.lifeTake.listeners.PlayerKillListener;

public class Main extends JavaPlugin {

    private int lifeTokenThreshold;
    private int heartTokenThreshold;

    @Override
    public void onEnable() {
        // Set defaults if not set in config
        saveDefaultConfig();

        // Load thresholds from config
        this.lifeTokenThreshold = getConfig().getInt("life-token-threshold", 3);
        this.heartTokenThreshold = getConfig().getInt("heart-token-threshold", 5);

        getLogger().info("LifeTake plugin enabled!");

        getServer().getPluginManager().registerEvents(new PlayerKillListener(this, new PlayerDataManager()), this);

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
            getCommand("setthreshold").setExecutor(new SetThresholdCommand());
        else
            getLogger().warning("Command 'setthreshold' not found in plugin.yml!");

    }

    public int getLifeTokenThreshold() {
        return lifeTokenThreshold;
    }

    public void setLifeTokenThreshold(int value) {
        this.lifeTokenThreshold = value;
        getConfig().set("life-token-threshold", value);
        saveConfig();
    }

    public int getHeartTokenThreshold() {
        return heartTokenThreshold;
    }

    public void setHeartTokenThreshold(int value) {
        this.heartTokenThreshold = value;
        getConfig().set("heart-token-threshold", value);
        saveConfig();
    }
}
