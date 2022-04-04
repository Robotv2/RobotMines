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
import fr.robotv2.robotmines.mine.MineBlockChance;
import fr.robotv2.robotmines.util.ColorUtil;
import org.bukkit.Material;

import java.util.logging.Level;

public class WorldEditBlockAdapter extends InternalBlockAdapter implements BlockAdapter {

    @Override
    public void fillMine(Mine mine) {

        if(mine.getBlockChance().isEmpty()) {
            return;
        }

        com.sk89q.worldedit.world.World world = BukkitAdapter.adapt(mine.getFirstBound().getWorld());

        BlockVector3 firstBound = BukkitAdapter.asBlockVector(mine.getFirstBound());
        BlockVector3 secondBound = BukkitAdapter.asBlockVector(mine.getSecondBound());
        Region region = new CuboidRegion(firstBound, secondBound);

        RandomPattern randomPattern = new RandomPattern();
        double air = 1;

        for(MineBlockChance blockChance : mine.getBlockChance()) {

            BlockState blockState = BukkitAdapter.adapt(blockChance.getMaterial().createBlockData());
            double chance = blockChance.getChance() / 100;
            air -= chance;
            randomPattern.add(blockState, chance);

        }

        randomPattern.add(BukkitAdapter.adapt(Material.AIR.createBlockData()), air);

        try {
            EditSession session = WorldEdit.getInstance().newEditSession(world);
            session.setBlocks(region, randomPattern);
            session.close();
        } catch (MaxChangedBlocksException exception) {
            ColorUtil.log(Level.SEVERE, "&cNombre maximum de bloc pouvant être placé atteint.");
        }
    }
}
