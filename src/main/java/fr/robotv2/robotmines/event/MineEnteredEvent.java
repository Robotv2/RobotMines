package fr.robotv2.robotmines.event;

import fr.robotv2.robotmines.mine.Mine;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class MineEnteredEvent extends PlayerMineEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancel;

    public MineEnteredEvent(Mine mine, Player player) {
        super(mine, player);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
