package xyz.ctbt.lifeTake.data;

import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import xyz.ctbt.lifeTake.Main;

public class PlayerDataManager {
    private static final int STARTING_LIVES = 6;

    private static NamespacedKey livesKey(Main plugin) {
        return new NamespacedKey(plugin, "lives");
    }

    private static NamespacedKey tokensKey(Main plugin) {
        return new NamespacedKey(plugin, "tokens");
    }

    public static int getLives(Main plugin, Player player) {
        PersistentDataContainer data = player.getPersistentDataContainer();
        return data.getOrDefault(livesKey(plugin), PersistentDataType.INTEGER, STARTING_LIVES);
    }

    public static void setLives(Main plugin, Player player, int lives) {
        player.getPersistentDataContainer().set(livesKey(plugin), PersistentDataType.INTEGER, lives);
    }

    public static int getTokens(Main plugin, Player player) {
        PersistentDataContainer data = player.getPersistentDataContainer();
        return data.getOrDefault(tokensKey(plugin), PersistentDataType.INTEGER, 0);
    }

    public static void setTokens(Main plugin, Player player, int tokens) {
        player.getPersistentDataContainer().set(tokensKey(plugin), PersistentDataType.INTEGER, tokens);
    }

    public static void addTokens(Main plugin, Player player, int amount) {
        setTokens(plugin, player, getTokens(plugin, player) + amount);
    }

    public static void adjustHearts(Player player, int change) {
        double current = player.getAttribute(Attribute.MAX_HEALTH).getBaseValue();
        double newVal = Math.max(20.0, Math.min(40.0, current + (change * 2.0))); // Clamp between 10 and 20 hearts
        player.getAttribute(Attribute.MAX_HEALTH).setBaseValue(newVal);
        if (player.getHealth() > newVal) {
            player.setHealth(newVal);
        }
    }
}