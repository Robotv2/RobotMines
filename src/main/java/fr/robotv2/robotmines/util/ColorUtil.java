package fr.robotv2.robotmines.util;

import fr.robotv2.robotmines.RobotMines;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.logging.Level;

public class ColorUtil {

    public static void sendMessage(CommandSender sender, String message, boolean prefix) {
        if(prefix)
            message = "&c&lMINES &8- " + message;
        sender.sendMessage(colorize(message));
    }

    public static void sendMessage(CommandSender sender, String message) {
        ColorUtil.sendMessage(sender, message, true);
    }

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void log(Level level, String message) {
        RobotMines.get().getLogger().log(level, colorize(message));
    }
}
