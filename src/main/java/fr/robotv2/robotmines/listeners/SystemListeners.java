package fr.robotv2.robotmines.listeners;

import fr.robotv2.robotmines.event.BlockBreakInMineEvent;
import fr.robotv2.robotmines.event.MineEnteredEvent;
import fr.robotv2.robotmines.event.MineLeftEvent;
import fr.robotv2.robotmines.mine.Mine;
import fr.robotv2.robotmines.mine.MineHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SystemListeners implements Listener {

    private final MineHelper mineHelper;

    public SystemListeners(JavaPlugin plugin, MineHelper mineHelper) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.mineHelper = mineHelper;
    }

    public void callEvent(Event event) {
        Bukkit.getPluginManager().callEvent(event);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        this.mineHelper.getMines().stream().filter(mine -> !mine.contains(player)).forEach(mine -> {

            BlockBreakInMineEvent blockBreakInMineEvent = new BlockBreakInMineEvent(player, mine, event.getBlock());
            this.callEvent(blockBreakInMineEvent);

            if(blockBreakInMineEvent.isCancelled()) {
                event.setCancelled(true);
            }

        });
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onMoveFullXYZEvent(PlayerMoveEvent event) {

        Player player = event.getPlayer();

        double blockXFrom = event.getFrom().getX();
        double blockYFrom = event.getFrom().getY();
        double blockZFrom = event.getFrom().getZ();

        double blockXTo = event.getTo().getX();
        double blockYTo = event.getTo().getY();
        double blockZTo = event.getTo().getZ();

        if (blockXFrom != blockXTo || blockYFrom != blockYTo || blockZFrom != blockZTo) {
            Mine mineFrom = mineHelper.getByLocation(event.getFrom());
            Mine mineTo = mineHelper.getByLocation(event.getTo());

            if(mineFrom != null && mineTo == null) {

                MineLeftEvent mineLeftEvent = new MineLeftEvent(mineFrom, player);
                this.callEvent(mineLeftEvent);
                if(mineLeftEvent.isCancelled()) {
                    event.setCancelled(true);
                }

            } else if(mineFrom == null && mineTo != null) {

                MineEnteredEvent mineEnteredEvent = new MineEnteredEvent(mineTo, player);
                this.callEvent(mineEnteredEvent);
                if(mineEnteredEvent.isCancelled()) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
