package xyz.ctbt.lifeTake.commands;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHeartsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be executed by a player.");
            return true;
        }

        if (args.length != 2) {
            player.sendMessage(ChatColor.RED + "Usage: /sethearts <player> <hearts>");
            return true;
        }

        Player targetPlayer = player.getServer().getPlayer(args[0]);
        if (targetPlayer == null) {
            player.sendMessage(ChatColor.RED + "Player not found.");
            return true;
        }

        int hearts;
        try {
            hearts = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Invalid number of hearts.");
            return true;
        }

        if (hearts < 1 || hearts > 20) {
            player.sendMessage(ChatColor.RED + "Please enter a number of hearts between 1 and 20.");
            return true;
        }

        // Set the max health attribute (1 heart = 2 health points)
        double health = hearts * 2.0;
        targetPlayer.getAttribute(Attribute.MAX_HEALTH).setBaseValue(health);

        // Clamp the player's current health to the new max
        if (targetPlayer.getHealth() > health) {
            targetPlayer.setHealth(health);
        }

        targetPlayer.sendMessage(ChatColor.GREEN + "Your hearts have been set to " + hearts + ".");
        player.sendMessage(ChatColor.GREEN + "You set " + targetPlayer.getName() + "'s hearts to " + hearts + ".");

        return true;
    }
}
