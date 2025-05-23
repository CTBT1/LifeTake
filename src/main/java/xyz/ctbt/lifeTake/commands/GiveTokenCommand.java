package xyz.ctbt.lifeTake.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GiveTokenCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Permission check
        if (!sender.hasPermission("lifetake.givetoken")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /givetoken <player>");
            return true;
        }

        Player targetPlayer = Bukkit.getPlayerExact(args[0]);
        if (targetPlayer == null || !targetPlayer.isOnline()) {
            sender.sendMessage(ChatColor.RED + "Player not found or not online.");
            return true;
        }

        // Create the Life Token (Nether Star)
        ItemStack token = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = token.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Life Token");
            token.setItemMeta(meta);
        }

        // Give token
        targetPlayer.getInventory().addItem(token);
        targetPlayer.sendMessage(ChatColor.GREEN + "You have received a Life Token!");
        sender.sendMessage(ChatColor.GREEN + "You gave a Life Token to " + targetPlayer.getName() + ".");

        return true;
    }
}
