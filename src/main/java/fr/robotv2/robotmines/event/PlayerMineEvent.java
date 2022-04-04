package fr.robotv2.robotmines.event;

import fr.robotv2.robotmines.mine.Mine;
import org.bukkit.entity.Player;

public abstract class PlayerMineEvent extends MineEvent {

    private final Player player;

    public PlayerMineEvent(Mine mine, Player player) {
        super(mine);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
