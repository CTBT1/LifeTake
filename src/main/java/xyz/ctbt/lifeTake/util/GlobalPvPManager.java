package xyz.ctbt.lifeTake.util;

public class GlobalPvPManager {
    private static boolean pvpEnabled = true;

    public static boolean isPvPEnabled() {
        return pvpEnabled;
    }

    public static boolean togglePvP() {
        pvpEnabled = !pvpEnabled;
        return pvpEnabled;
    }

    public static void setPvP(boolean enabled) {
        pvpEnabled = enabled;
    }
}
