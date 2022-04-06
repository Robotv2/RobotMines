package fr.robotv2.robotmines.listeners;

import fr.robotv2.robotmines.event.MineEnteredEvent;
import fr.robotv2.robotmines.event.MineLeftEvent;
import fr.robotv2.robotmines.util.ColorUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class MineListeners implements Listener {

    public MineListeners(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onMineEnter(MineEnteredEvent event) {
        ColorUtil.sendMessage(event.getPlayer(), "&aVous venez de rentrer dans la mine: " + event.getMine().getName());
    }

    @EventHandler
    public void onMineLeave(MineLeftEvent event) {
        ColorUtil.sendMessage(event.getPlayer(), "&cVous venez de sortir de la mine: " + event.getMine().getName());
    }
}
