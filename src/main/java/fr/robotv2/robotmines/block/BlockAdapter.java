package fr.robotv2.robotmines.block;

import fr.robotv2.robotmines.mine.Mine;
import org.bukkit.block.Block;

import java.util.LinkedList;

public interface BlockAdapter {
    LinkedList<Block> getBlocks(Mine mine);
    void fillMine(Mine mine);
}
