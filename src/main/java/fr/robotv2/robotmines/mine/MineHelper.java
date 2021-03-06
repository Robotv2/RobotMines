package fr.robotv2.robotmines.mine;

import fr.robotv2.robotmines.RobotMines;
import fr.robotv2.robotmines.util.ColorUtil;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class MineHelper {

    private final Map<String, Mine> mines = new ConcurrentHashMap<>();

    public Collection<Mine> getMines() {
        return mines.values();
    }

    @Nullable
    public Mine getByName(String name) {
        Mine mine = mines.get(name.toLowerCase());
        if(mine == null) {
            mine = this.loadMine(name);
        }
        return mine;
    }

    @Nullable
    public Mine getByLocation(Location location) {
        return this.getMines().stream().filter(mine -> mine.contains(location)).findFirst().orElse(null);
    }

    public boolean exist(String name) {
        return mines.containsKey(name.toLowerCase());
    }

    /**
     * Permit to check if a mine object is valid.
     */
    public boolean exist(Mine mine) {
        return mine != null && exist(mine.getName());
    }

    @Nullable
    public Mine loadMine(String name) {

        name = name.toLowerCase();
        if(mines.containsKey(name)) {
            return mines.get(name);
        }

        Location firstBound = RobotMines.get().getMinesFile().getLocation(name + ".first-bound");
        Location secondBound = RobotMines.get().getMinesFile().getLocation(name + ".second-bound");

        if(firstBound == null || secondBound == null) {
            return null;
        }

        Mine mine = new Mine(name, firstBound, secondBound);
        mines.put(name, mine);
        ColorUtil.log(Level.INFO, "&eLa mine " + name + " a été initialisé avec succès.");

        return mine;
    }

    public void loadMines() {
        ConfigurationSection section = RobotMines.get().getMinesFile().getConfigurationSection("");

        if(section == null) {
            return;
        }

        section.getKeys(false).forEach(this::loadMine);
    }

    public void deleteMine(Mine mine) {
        mines.remove(mine.getName());
        mine.stopTask();
        RobotMines.get().getMinesFile().set(mine.getName() + ".first-bound", null);
        RobotMines.get().getMinesFile().set(mine.getName() + ".second-bound", null);
        RobotMines.get().getMinesFile().set(mine.getName() + ".reset-type", null);
        RobotMines.get().getMinesFile().set(mine.getName() + ".block-chances", null);
        RobotMines.get().getMinesFile().set(mine.getName() + ".reset-pourcentage", null);
        RobotMines.get().getMinesFile().set(mine.getName() + ".max-block-per-tick", null);
        RobotMines.get().getMinesFile().set(mine.getName(), null);
        RobotMines.get().saveMinesFile();
    }
}
