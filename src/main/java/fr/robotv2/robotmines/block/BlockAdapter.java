package fr.robotv2.robotmines.block;

import fr.robotv2.robotmines.mine.Mine;
import org.bukkit.block.Block;

import java.util.Set;

public interface BlockAdapter {
    Set<Block> getBlocks(Mine mine);
    void fillMine(Mine mine);
}
