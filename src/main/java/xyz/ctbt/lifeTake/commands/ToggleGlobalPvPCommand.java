package xyz.ctbt.lifeTake.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import xyz.ctbt.lifeTake.util.GlobalPvPManager;

public class ToggleGlobalPvPCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender.isOp() || sender.hasPermission("lifetake.admin"))) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        boolean newState = GlobalPvPManager.togglePvP();
        String message = ChatColor.YELLOW + "Global PvP is now " + (newState ? ChatColor.GREEN + "ENABLED" : ChatColor.RED + "DISABLED");

        Bukkit.broadcastMessage(message);
        return true;
    }
}
