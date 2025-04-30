package xyz.ctbt.lifeTake.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import xyz.ctbt.lifeTake.ConfigManager;

public class SetThresholdCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage("You do not have permission to use this command.");
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage("Usage: /setthreshold <life|heart> <amount>");
            return true;
        }

        String type = args[0].toLowerCase();
        int value;
        try {
            value = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage("Please enter a valid number.");
            return true;
        }

        if (value <= 0) {
            sender.sendMessage("Threshold must be greater than 0.");
            return true;
        }

        switch (type) {
            case "life":
                ConfigManager.setLifeTokenThreshold(value);
                sender.sendMessage("Life token threshold set to " + value);
                break;
            case "heart":
                ConfigManager.setHeartTokenThreshold(value);
                sender.sendMessage("Heart token threshold set to " + value);
                break;
            default:
                sender.sendMessage("Unknown type. Use 'life' or 'heart'.");
        }

        return true;
    }
}
