package fr.robotv2.robotmines.util;

import fr.robotv2.robotmines.mine.Mine;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.stream.Collectors;

public class MineUtil {

    private MineUtil() {}

    public static Set<Player> getPlayersInMine(Mine mine) {
        return Bukkit.getOnlinePlayers().stream().filter(mine::contains).collect(Collectors.toSet());
    }

    public static boolean isInMine(Player player, Mine mine) {
        return mine.contains(player);
    }

    public static void broadcast(Mine mine, String message) {
        String broadcast = ColorUtil.colorize(message);
        MineUtil.getPlayersInMine(mine).forEach(player -> player.sendMessage(broadcast));
    }
}
