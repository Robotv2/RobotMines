package fr.robotv2.robotmines.listeners;

import fr.robotv2.robotmines.event.MineEnteredEvent;
import fr.robotv2.robotmines.event.MineLeftEvent;
import fr.robotv2.robotmines.mine.Mine;
import fr.robotv2.robotmines.util.ColorUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class MineListeners implements Listener {

    public MineListeners(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onMineEnter(MineEnteredEvent event) {

        Player player = event.getPlayer();
        Mine mine = event.getMine();

        ColorUtil.sendMessage(player, "&aVous venez de rentrer dans la mine: " + mine.getName());
    }

    @EventHandler
    public void onMineLeave(MineLeftEvent event) {

        Player player = event.getPlayer();
        Mine mine = event.getMine();

        ColorUtil.sendMessage(player, "&cVous venez de sortir de la mine: " + mine.getName());
    }
}
