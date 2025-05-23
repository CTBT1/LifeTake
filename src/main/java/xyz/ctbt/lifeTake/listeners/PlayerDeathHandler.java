package xyz.ctbt.lifeTake.listeners;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import xyz.ctbt.lifeTake.Main;
import xyz.ctbt.lifeTake.data.PlayerDataManager;
import xyz.ctbt.lifeTake.util.TablistManager;

public class PlayerDeathHandler implements Listener {
    private final Main plugin;

    public PlayerDeathHandler(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        // Reduce hearts
        var attr = player.getAttribute(Attribute.MAX_HEALTH);
        if (attr != null) {
            double currentMax = attr.getBaseValue();
            if (currentMax > 20.0) {
                attr.setBaseValue(currentMax - 2.0);
                player.sendMessage(ChatColor.RED + "You lost a heart! Max hearts: " + (int)(attr.getBaseValue() / 2));
            } else {
                player.sendMessage(ChatColor.YELLOW + "You are at the minimum heart limit (10 hearts).");
            }
        }

        // Reduce lives or ban
        int lives = PlayerDataManager.getLives(plugin, player);
        if (lives <= 1) {
            player.kickPlayer(ChatColor.DARK_RED + "You have run out of lives.");
            Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), "Out of lives", null, null);
        } else {
            PlayerDataManager.setLives(plugin, player, lives - 1);
            player.sendMessage(ChatColor.RED + "You lost a life. Lives remaining: " + (lives - 1));
        }

        TablistManager.updatePlayer(plugin, player);
    }
}
