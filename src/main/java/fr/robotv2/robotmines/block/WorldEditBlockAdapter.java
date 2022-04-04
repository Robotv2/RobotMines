package fr.robotv2.robotmines.block;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.pattern.RandomPattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.block.BlockState;
import fr.robotv2.robotmines.mine.Mine;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WorldEditBlockAdapter implements BlockAdapter {

    @Override
    public Set<Block> getBlocks(Mine mine) {

        Set<Block> blocks = new HashSet<>();
        World world = mine.getFirstBound().getWorld();

        BlockVector3 firstBound = BukkitAdapter.asBlockVector(mine.getFirstBound());
        BlockVector3 secondBound = BukkitAdapter.asBlockVector(mine.getSecondBound());
        Region region = new CuboidRegion(firstBound, secondBound);

        for(BlockVector3 vector : region.getChunkCubes()) {

            Location blockLocation = BukkitAdapter.adapt(world, vector);
            Block block = world.getBlockAt(blockLocation);
            blocks.add(block);

        }

        return blocks;
    }

    @Override
    public void fillMine(Mine mine) {
        com.sk89q.worldedit.world.World world = BukkitAdapter.adapt(mine.getFirstBound().getWorld());

        BlockVector3 firstBound = BukkitAdapter.asBlockVector(mine.getFirstBound());
        BlockVector3 secondBound = BukkitAdapter.asBlockVector(mine.getSecondBound());
        Region region = new CuboidRegion(firstBound, secondBound);

        RandomPattern randomPattern = new RandomPattern();

        for(Map.Entry<Material, Double> entry : mine.getBlockChance().entrySet()) {

            BlockState blockState = BukkitAdapter.adapt(entry.getKey().createBlockData());
            double chance = entry.getValue() / 100;
            randomPattern.add(blockState, chance);

        }

        try {
            EditSession session = WorldEdit.getInstance().newEditSession(world);
            session.setBlocks(region, randomPattern);
        } catch (MaxChangedBlocksException ignored) {
        }
    }
}
