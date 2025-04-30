package xyz.ctbt.lifeTake.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import xyz.ctbt.lifeTake.ConfigManager;

public class UseHeartTokenCommand implements CommandExecutor {

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
        int requiredTokens = ConfigManager.getHeartTokenThreshold();

        if (countTokens(player) < requiredTokens) {
            player.sendMessage(ChatColor.RED + "You need " + requiredTokens + " Life Tokens to gain an extra heart.");
            return true;
        }

        AttributeInstance healthAttr = player.getAttribute(Attribute.MAX_HEALTH);
        if (healthAttr == null) {
            player.sendMessage(ChatColor.RED + "An error occurred: Could not access health attribute.");
            return true;
        }

        double currentMaxHealth = healthAttr.getBaseValue();
        if (currentMaxHealth >= 40.0) {
            player.sendMessage(ChatColor.YELLOW + "You already have the maximum number of hearts.");
            return true;
        }

        // Remove tokens and increase max health
        removeTokens(player, requiredTokens);
        double newMaxHealth = Math.min(currentMaxHealth + 2.0, 40.0);
        healthAttr.setBaseValue(newMaxHealth);
        player.setHealth(Math.min(player.getHealth(), newMaxHealth));

        player.sendMessage(ChatColor.GREEN + "You have gained an extra heart! Your max health is now: " + (newMaxHealth / 2.0) + " hearts.");
        return true;
    }
}
