package fr.robotv2.robotmines.util.sort;

import fr.robotv2.robotmines.mine.MineBlockChance;

import java.util.Comparator;

public class BlockChanceComparator implements Comparator<MineBlockChance> {

    @Override
    public int compare(MineBlockChance o1, MineBlockChance o2) {
        return (int) (o2.getChance() - o1.getChance());
    }
}
