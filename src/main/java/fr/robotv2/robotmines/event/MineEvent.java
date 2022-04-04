package fr.robotv2.robotmines.event;

import fr.robotv2.robotmines.mine.Mine;
import org.bukkit.event.Event;

public abstract class MineEvent extends Event {

    private final Mine mine;

    public MineEvent(Mine mine) {
        this.mine = mine;
    }

    public Mine getMine() {
        return mine;
    }
}
