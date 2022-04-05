package fr.robotv2.robotmines.mine;

import fr.robotv2.robotmines.RobotMines;
import fr.robotv2.robotmines.ui.stock.MineOptionUi;
import fr.robotv2.robotmines.util.sort.BlockChanceComparator;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Mine {

    private final String name;
    private MineResetType type;

    private final List<MineBlockChance> chances = new LinkedList<>();

    private final BukkitTask resetTask;
    private boolean reset;

    private LinkedList<Block> blocks;
    private Location firstBound;
    private Location secondBound;

    //options

    private int maxBlockPerTick;
    private double resetPourcentage;

    //Axis

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

        this.type = MineResetType.valueOf(RobotMines.get().getMinesFile().getString(name + ".reset-type", "FULL").toUpperCase());
        this.chances.addAll(MineBlockChance.fromStringList(RobotMines.get().getMinesFile().getStringList(name + ".block-chances")));
        this.resetPourcentage = RobotMines.get().getMinesFile().getDouble(name + ".reset-pourcentage", 50);
        this.maxBlockPerTick = RobotMines.get().getMinesFile().getInt(name + ".max-block-per-tick", 20);

        //Calculate new blocks & check for air blocks every 30 seconds.
        this.resetTask = Bukkit.getScheduler().runTaskTimer(RobotMines.get(), () -> {
            if(this.needReset()) {
                this.reset();
            }
        }, 20 * 20, 20 * 20);
    }

    public String getName() {
        return name;
    }

    public World getWorld() {
        return firstBound.getWorld();
    }

    //options

    public void setOption(MineOptionUi.MineOption option, double value) {
        switch (option) {
            case RESET_POURCENTAGE:
                this.setResetPourcentage(value);
                break;
            case MAX_BLOCK_PER_TICK:
                this.setMaxBlockPerTick((int) value);
                break;
        }
    }

    public double getResetPourcentage() {
        return resetPourcentage;
    }

    public void setResetPourcentage(double resetPourcentage) {
        this.resetPourcentage = resetPourcentage;
    }

    public int getMaxBlockPerTick() {
        return maxBlockPerTick;
    }

    public void setMaxBlockPerTick(int maxBlockPerTick) {
        this.maxBlockPerTick = maxBlockPerTick;
    }

    //Reset type

    public MineResetType getResetType() {
        return type;
    }

    public void setResetType(MineResetType type) {
        this.type = type;
    }

    //Blocks

    private void  calculateBlocks() {
        this.blocks = RobotMines.get().getBlockAdapter().getBlocks(this);
    }

    public LinkedList<Block> getBlocks() {
        return blocks;
    }

    public Set<Block> getAirBlocks() {
        return blocks.stream().filter(block -> Objects.equals(block.getType(), Material.AIR)).collect(Collectors.toSet());
    }

    public boolean needReset() {
        return getBlocks().size() * (resetPourcentage / 100) >= (getBlocks().size() - getAirBlocks().size());
    }

    //Boundaries

    private void validateBounds(Location firstBound, Location secondBound) throws IllegalArgumentException {
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

    public void sortBlockChance() {
        this.chances.sort(new BlockChanceComparator());
    }

    public List<MineBlockChance> getBlockChance() {
        return chances;
    }

    public void addBlockChance(Material material, double chance) {
        MineBlockChance blockChance = new MineBlockChance(material, chance);
        this.chances.add(blockChance);
        this.sortBlockChance();
    }

    public void removeBlockChance(Material material) {
        getBlockChance().removeIf(blockChance -> blockChance.getMaterial() == material);
    }

    public boolean containsMaterial(Material material) {
        return getBlockChance().stream().anyMatch(blockChance -> blockChance.getMaterial() == material);
    }

    public Material getRandomMaterial() {

        double currentChance = 0;
        double roundedDouble = Math.round(ThreadLocalRandom.current().nextDouble(100) * 100D) / 100D;

        for(MineBlockChance blockChance : getBlockChance()) {
            if(blockChance.getChance() + currentChance >= roundedDouble) {
                return blockChance.getMaterial();
            } else {
                currentChance += blockChance.getChance();
            }
        }

        return Material.AIR;
    }

    private void calculateAxis() {
        minX = Math.min(firstBound.getBlockX(), secondBound.getBlockX());
        minY = Math.min(firstBound.getBlockY(), secondBound.getBlockY());
        minZ = Math.min(firstBound.getBlockZ(), secondBound.getBlockZ());
        maxX = Math.max(firstBound.getBlockX(), secondBound.getBlockX());
        maxY = Math.max(firstBound.getBlockY(), secondBound.getBlockY());
        maxZ = Math.max(firstBound.getBlockZ(), secondBound.getBlockZ());
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
        RobotMines.get().getMinesFile().set(name + ".reset-type", this.type.name());
        RobotMines.get().getMinesFile().set(name + ".block-chances", MineBlockChance.toStringList(this.chances));
        RobotMines.get().getMinesFile().set(name + ".reset-pourcentage", resetPourcentage);
        RobotMines.get().getMinesFile().set(name + ".max-block-per-tick", maxBlockPerTick);
        RobotMines.get().saveMinesFile();
    }
}
