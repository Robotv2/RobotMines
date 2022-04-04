package fr.robotv2.robotmines.mine;

import org.bukkit.Location;

import java.util.Objects;

public class MineBuilder {

    private String name;

    private Location firstBound;
    private Location secondBound;

    public MineBuilder setName(String name) {
        this.name = name.toLowerCase();
        return this;
    }

    public MineBuilder setFirstBound(Location firstBound) {
        this.firstBound = firstBound;
        return this;
    }

    public MineBuilder setSecondBound(Location secondBound) {
        this.secondBound = secondBound;
        return this;
    }

    public Mine build() throws NullPointerException {
        Objects.requireNonNull(name, "name can't be null");
        Objects.requireNonNull(firstBound, "First bound can't be null");
        Objects.requireNonNull(secondBound, "Second bound can't be null");

        Mine mine = new Mine(name, firstBound, secondBound);
        mine.saveToFile();

        return mine;
    }
}
