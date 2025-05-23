package xyz.ctbt.lifeTake;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private static int lifeTokenThreshold;
    private static int heartTokenThreshold;
    private static Main plugin;

    public static void initialize(Main mainPlugin) {
        plugin = mainPlugin;

        // Load config or set default values
        FileConfiguration config = plugin.getConfig();
        lifeTokenThreshold = config.getInt("thresholds.life", 2);   // default to 3
        heartTokenThreshold = config.getInt("thresholds.heart", 1); // default to 5
    }

    public static int getLifeTokenThreshold() {
        return lifeTokenThreshold;
    }

    public static int getHeartTokenThreshold() {
        return heartTokenThreshold;
    }

    public static void setLifeTokenThreshold(int value) {
        lifeTokenThreshold = value;
        plugin.getConfig().set("thresholds.life", value);
        plugin.saveConfig();
    }

    public static void setHeartTokenThreshold(int value) {
        heartTokenThreshold = value;
        plugin.getConfig().set("thresholds.heart", value);
        plugin.saveConfig();
    }
}
