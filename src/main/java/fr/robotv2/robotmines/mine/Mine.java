package fr.robotv2.robotmines.mine;

import fr.robotv2.robotmines.RobotMines;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Mine {

    private final MineResetType type = MineResetType.FULL;
    private final String name;

    private final BukkitTask resetTask;
    private boolean reset;

    private Set<Block> blocks;
    private Location firstBound;
    private Location secondBound;

    private int minX;
    private int maxX;
    private int minY;
    private int maxY;
    private int minZ;
    private int maxZ;

    public Mine(String name, Location firstBound, Location secondBound) {
        this.name = name;

        this.validateBounds(firstBound, secondBound);
        this.firstBound = firstBound;
        this.secondBound = secondBound;

        this.calculateAxis();
        this.calculateBlocks();

        //Calculate new blocks & check for air blocks every 30 seconds.
        this.resetTask = Bukkit.getScheduler().runTaskTimer(RobotMines.get(), () -> {
            this.calculateBlocks();
            if(this.needReset()) {
                this.reset();
            }
        }, 20 * 30, 20 * 30);
    }

    private void calculateAxis() {
        minX = Math.min(firstBound.getBlockX(), secondBound.getBlockX());
        minY = Math.min(firstBound.getBlockY(), secondBound.getBlockY());
        minZ = Math.min(firstBound.getBlockZ(), secondBound.getBlockZ());
        maxX = Math.max(firstBound.getBlockX(), secondBound.getBlockX());
        maxY = Math.max(firstBound.getBlockY(), secondBound.getBlockY());
        maxZ = Math.max(firstBound.getBlockZ(), secondBound.getBlockZ());
    }

    public String getName() {
        return name;
    }

    public World getWorld() {
        return firstBound.getWorld();
    }

    public MineResetType getResetType() {
        return type;
    }

    //Blocks

    private void calculateBlocks() {
        this.blocks = RobotMines.get().getBlockAdapter().getBlocks(this);
    }

    public Set<Block> getBlocks() {
        return blocks;
    }

    public Set<Block> getAirBlocks() {
        return blocks.stream().filter(block -> Objects.equals(block.getType(), Material.AIR)).collect(Collectors.toSet());
    }

    public boolean needReset() {
        return getBlocks().size() / 2 > getAirBlocks().size();
    }

    //Boundaries

    private void validateBounds(Location firstBound, Location secondBound) {
        Validate.isTrue(Objects.equals(firstBound.getWorld(), secondBound.getWorld()), "First Bound and Second bound must be in the same world.");
    }

    public Location getFirstBound() {
        return firstBound;
    }

    public void setFirstBound(Location firstBound) {
        this.validateBounds(firstBound, this.secondBound);
        this.firstBound = firstBound;
        this.calculateAxis();
        this.calculateBlocks();
    }

    public Location getSecondBound() {
        return secondBound;
    }

    public void setSecondBound(Location secondBound) {
        this.validateBounds(this.firstBound, secondBound);
        this.secondBound = secondBound;
        this.calculateAxis();
        this.calculateBlocks();
    }

    //Reset

    public void reset(MineResetType type) {
        type.reset(this);
    }

    public void reset() {
        getResetType().reset(this);
    }

    public boolean isBeingReset() {
        return reset;
    }

    public void setIsBeingReset(boolean reset) {
        this.reset = reset;
    }

    //Material

    public Map<Material, Double> getBlockChance() {
        return Collections.singletonMap(Material.STONE, 100D);
    }

    public Material getRandomMaterial() {
        return getBlockChance().entrySet().stream()
                .filter(entry -> entry.getValue() > ThreadLocalRandom.current().nextInt(101))
                .map(Map.Entry::getKey).findFirst()
                .orElse(Material.AIR);
    }

    public boolean contains(int x, int y, int z) {
        return x >= minX && x <= maxX && y >= minY && y <= maxY && z >= minZ && z <= maxZ;
    }

    public boolean contains(Location location) {
        return contains(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public boolean contains(Player player) {
        return contains(player.getLocation());
    }

    public boolean contains(Block block) {
        return contains(block.getLocation());
    }

    public void stopTask() {
        this.resetTask.cancel();
    }

    public void saveToFile() {
        RobotMines.get().getMinesFile().set(name + ".first-bound", getWorld().getBlockAt(firstBound).getLocation());
        RobotMines.get().getMinesFile().set(name + ".second-bound", getWorld().getBlockAt(secondBound).getLocation());
        RobotMines.get().saveMinesFile();
    }
}
