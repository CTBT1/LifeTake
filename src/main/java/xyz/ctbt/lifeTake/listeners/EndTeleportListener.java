package xyz.ctbt.lifeTake.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.World;
import xyz.ctbt.lifeTake.Main;

import java.util.UUID;
import java.util.HashSet;
import java.util.Set;

public class EndTeleportListener implements Listener {

    private final Main plugin;
    final Set<UUID> recentlyTeleportedFromEnd = new HashSet<>();

    public EndTeleportListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        World.Environment fromEnv = event.getFrom().getWorld().getEnvironment();
        World.Environment toEnv = event.getTo().getWorld().getEnvironment();

        if (fromEnv == World.Environment.THE_END && toEnv == World.Environment.NORMAL) {
            // Mark player to skip death penalty on next death/respawn
            recentlyTeleportedFromEnd.add(event.getPlayer().getUniqueId());

            // Schedule removal after some time (e.g., 5 seconds)
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                recentlyTeleportedFromEnd.remove(event.getPlayer().getUniqueId());
            }, 100L); // 100 ticks = 5 seconds
        }
    }

    public boolean hasJustTeleportedFromEnd(UUID uuid) {
        return recentlyTeleportedFromEnd.contains(uuid);
    }

    public void removeTeleportFlag(UUID uuid) {
        recentlyTeleportedFromEnd.remove(uuid);
    }

}
