package xyz.ctbt.lifeTake.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import xyz.ctbt.lifeTake.ConfigManager;
import xyz.ctbt.lifeTake.Main;
import xyz.ctbt.lifeTake.data.PlayerDataManager;


public class UseLifeTokenCommand implements CommandExecutor {
    private final Main plugin;

    // Constructor to get the plugin instance
    public UseLifeTokenCommand(Main plugin) {
        this.plugin = plugin;
    }

    // Count the Life Tokens (Nether Stars) the player has
    private int countTokens(Player player) {
        int count = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == Material.NETHER_STAR && item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                if (meta != null && meta.hasDisplayName() && meta.getDisplayName().equals(ChatColor.RED + "Life Token")) {
                    count += item.getAmount();
                }
            }
        }
        return count;
    }

    // Remove tokens from the player's inventory
    private void removeTokens(Player player, int amount) {
        int remaining = amount;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == Material.NETHER_STAR && item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                if (meta != null && meta.hasDisplayName() && meta.getDisplayName().equals(ChatColor.RED + "Life Token")) {
                    int itemAmount = item.getAmount();
                    if (itemAmount <= remaining) {
                        player.getInventory().remove(item);
                        remaining -= itemAmount;
                    } else {
                        item.setAmount(itemAmount - remaining);
                        remaining = 0;
                    }
                }
            }
            if (remaining <= 0) break;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can execute this command.");
            return true;
        }

        Player player = (Player) sender;
        int requiredTokens = ConfigManager.getLifeTokenThreshold(); // Get threshold from config

        if (countTokens(player) < requiredTokens) {
            player.sendMessage(ChatColor.RED + "You need " + requiredTokens + " Life Tokens to gain an extra life.");
            return true;
        }

        // Remove the tokens
        removeTokens(player, requiredTokens);

        // Increase the player's lives (using the PlayerDataManager)
        int currentLives = PlayerDataManager.getLives(plugin, player);
        currentLives++;
        PlayerDataManager.setLives(plugin, player, currentLives);  // Save new lives count

        player.sendMessage(ChatColor.GREEN + "You have gained an extra life! Your current lives: " + currentLives);

        return true;
    }
}
