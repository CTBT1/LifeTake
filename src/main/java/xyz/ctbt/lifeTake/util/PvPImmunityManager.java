package xyz.ctbt.lifeTake.util;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PvPImmunityManager {
    private static final Set<UUID> immunePlayers = new HashSet<>();
    private static boolean immunityEnabled = true;

    public static boolean isImmunityEnabled() {
        return immunityEnabled;
    }

    public static void toggleImmunity() {
        immunityEnabled = !immunityEnabled;
    }

    public static void grantImmunity(Player player, long durationTicks) {
        if (!immunityEnabled) return;

        UUID uuid = player.getUniqueId();
        immunePlayers.add(uuid);

        player.getServer().getScheduler().runTaskLater(
                player.getServer().getPluginManager().getPlugin("LifeTake"),
                () -> immunePlayers.remove(uuid),
                durationTicks
        );
    }

    public static boolean isImmune(Player player) {
        return immunePlayers.contains(player.getUniqueId());
    }
}
