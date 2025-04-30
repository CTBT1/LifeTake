package xyz.ctbt.lifeTake.commands;

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
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be executed by a player.");
            return false;
        }

        if (args.length != 1) {
            player.sendMessage("Usage: /givetoken <player>");
            return false;
        }

        Player targetPlayer = player.getServer().getPlayer(args[0]);
        if (targetPlayer == null) {
            player.sendMessage(ChatColor.RED + "Player not found.");
            return false;
        }

        // Give Life Token (Nether Star)
        ItemStack token = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = token.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Life Token");
            token.setItemMeta(meta);
        }

        targetPlayer.getInventory().addItem(token);
        targetPlayer.sendMessage(ChatColor.GREEN + "You have received a Life Token!");
        player.sendMessage(ChatColor.GREEN + "You gave a Life Token to " + targetPlayer.getName());

        return true;
    }
}
