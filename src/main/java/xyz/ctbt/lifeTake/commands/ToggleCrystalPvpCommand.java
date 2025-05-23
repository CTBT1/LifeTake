package xyz.ctbt.lifeTake.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import xyz.ctbt.lifeTake.Main;

public class ToggleCrystalPvpCommand implements CommandExecutor {

    private final Main plugin;

    public ToggleCrystalPvpCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage("§cYou do not have permission to use this command.");
            return true;
        }

        plugin.toggleCrystalPvP();
        boolean enabled = plugin.isCrystalPvPEnabled();
        sender.sendMessage("§aCrystal PvP is now " + (enabled ? "enabled" : "disabled") + ".");
        return true;
    }
}
