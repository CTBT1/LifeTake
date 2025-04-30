package xyz.ctbt.lifeTake.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.ctbt.lifeTake.Main;
import xyz.ctbt.lifeTake.data.PlayerDataManager;
import xyz.ctbt.lifeTake.util.TablistManager;

public class PlayerJoinListener implements Listener {
    private final Main plugin;
    private final PlayerDataManager dataManager;

    public PlayerJoinListener(Main plugin, PlayerDataManager dataManager) {
        this.plugin = plugin;
        this.dataManager = dataManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        TablistManager.updatePlayer(plugin, event.getPlayer());
    }
}
