package xyz.ctbt.lifeTake.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import xyz.ctbt.lifeTake.Main;
import xyz.ctbt.lifeTake.util.TablistManager;

public class PlayerRespawnListener implements Listener {

    private final Main plugin;

    public PlayerRespawnListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

            TablistManager.updatePlayer(plugin, player);
    }
}
