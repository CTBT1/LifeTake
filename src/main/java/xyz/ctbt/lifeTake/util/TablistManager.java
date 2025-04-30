package xyz.ctbt.lifeTake.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import xyz.ctbt.lifeTake.Main;
import xyz.ctbt.lifeTake.data.PlayerDataManager;

public class TablistManager {

    public static void updateAll(Main plugin) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updatePlayer(plugin, player);
        }
    }

    public static void updatePlayer(Main plugin, Player player) {
        int lives = PlayerDataManager.getLives(plugin, player);
        double hearts = player.getAttribute(Attribute.MAX_HEALTH).getBaseValue() / 2.0;
        player.setPlayerListName(ChatColor.GREEN + player.getName() + ChatColor.GRAY + " [❤ " + (int) hearts + " | ♡ " + lives + "]");
    }
}
