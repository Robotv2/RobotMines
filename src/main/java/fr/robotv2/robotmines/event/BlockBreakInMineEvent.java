package fr.robotv2.robotmines.event;

import fr.robotv2.robotmines.mine.Mine;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class BlockBreakInMineEvent extends PlayerMineEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Block block;
    private boolean cancel;

    public BlockBreakInMineEvent(@NotNull Player who, Mine mine, Block block) {
        super(mine, who);
        this.block = block;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Block getBlock() {
        return block;
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
