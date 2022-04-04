package fr.robotv2.robotmines.block;

import fr.robotv2.robotmines.mine.Mine;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.LinkedList;

public class InternalBlockAdapter implements BlockAdapter {

    @Override
    public LinkedList<Block> getBlocks(Mine mine) {

        World world = mine.getFirstBound().getWorld();
        Location firstBound = mine.getFirstBound();
        Location secondBound = mine.getSecondBound();

        LinkedList<Block> blocks = new LinkedList<>();

        int topBlockX = Math.max(firstBound.getBlockX(), secondBound.getBlockX());
        int bottomBlockX = Math.min(firstBound.getBlockX(), secondBound.getBlockX());

        int topBlockY = Math.max(firstBound.getBlockY(), secondBound.getBlockY());
        int bottomBlockY = Math.min(firstBound.getBlockY(), secondBound.getBlockY());

        int topBlockZ = Math.max(firstBound.getBlockZ(), secondBound.getBlockZ());
        int bottomBlockZ = Math.min(firstBound.getBlockZ(), secondBound.getBlockZ());

        for(int x = bottomBlockX; x <= topBlockX; x++) {
            for(int z = bottomBlockZ; z <= topBlockZ; z++) {
                for(int y = bottomBlockY; y <= topBlockY; y++) {
                    Block block = world.getBlockAt(x, y, z);
                    blocks.add(block);
                }
            }
        }

        return blocks;
    }

    @Override
    public void fillMine(Mine mine) {
        mine.getBlocks().forEach(block -> {
            Material material = mine.getRandomMaterial();
            block.setType(material);
        });
    }
}
