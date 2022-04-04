package fr.robotv2.robotmines.mine.task;

import fr.robotv2.robotmines.mine.Mine;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedList;
import java.util.Queue;

public class ResetRunnable extends BukkitRunnable {

    private final int MAX_BLOCK_PER_TICK = 20;

    private final Mine mine;
    private final Queue<Block> queue;

    public ResetRunnable(Mine mine) {
        this.mine = mine;
        this.mine.setIsBeingReset(true);
        this.queue = new LinkedList<>(mine.getBlocks());
    }

    @Override
    public void run() {
        for(int i = 0; i < MAX_BLOCK_PER_TICK; i++) {

            Block block = queue.poll();

            if(block == null) {
                this.cancel();
                this.mine.setIsBeingReset(false);
                return;
            }

            Material material = mine.getRandomMaterial();
            block.setType(material);
        }
    }
}
