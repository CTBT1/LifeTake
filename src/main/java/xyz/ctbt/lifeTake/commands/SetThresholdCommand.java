package xyz.ctbt.lifeTake.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import xyz.ctbt.lifeTake.Main;

public class SetThresholdCommand implements CommandExecutor {

    private final Main plugin;

    public SetThresholdCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage("§cYou do not have permission to use this command.");
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage("§eUsage: /setthreshold <life|heart> <amount>");
            return true;
        }

        String type = args[0].toLowerCase();
        int value;
        try {
            value = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage("§cPlease enter a valid number.");
            return true;
        }

        if (value <= 0) {
            sender.sendMessage("§cThreshold must be greater than 0.");
            return true;
        }

        switch (type) {
            case "life" -> {
                plugin.setLifeTokenThreshold(value);
                sender.sendMessage("§aLife token threshold set to " + value);
            }
            case "heart" -> {
                plugin.setHeartTokenThreshold(value);
                sender.sendMessage("§aHeart token threshold set to " + value);
            }
            default -> sender.sendMessage("§cUnknown type. Use 'life' or 'heart'.");
        }

        return true;
    }
}
