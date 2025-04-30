package xyz.ctbt.lifeTake;

public class ConfigManager {
    private static int lifeTokenThreshold = 3;
    private static int heartTokenThreshold = 5;

    public static int getLifeTokenThreshold() {
        return lifeTokenThreshold;
    }

    public static int getHeartTokenThreshold() {
        return heartTokenThreshold;
    }

    public static void setLifeTokenThreshold(int value) {
        lifeTokenThreshold = value;
    }

    public static void setHeartTokenThreshold(int value) {
        heartTokenThreshold = value;
    }
}
