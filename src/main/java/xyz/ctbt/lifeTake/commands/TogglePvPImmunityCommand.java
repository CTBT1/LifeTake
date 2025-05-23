package xyz.ctbt.lifeTake.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import xyz.ctbt.lifeTake.util.PvPImmunityManager;

public class TogglePvPImmunityCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender.isOp() || sender.hasPermission("lifetake.admin"))) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        PvPImmunityManager.toggleImmunity();
        sender.sendMessage(ChatColor.YELLOW + "PvP Immunity after kills is now: " +
                (PvPImmunityManager.isImmunityEnabled() ? ChatColor.GREEN + "ENABLED" : ChatColor.RED + "DISABLED"));
        return true;
    }
}
