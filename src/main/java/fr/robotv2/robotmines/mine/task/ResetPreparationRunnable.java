package fr.robotv2.robotmines.mine.task;

import fr.robotv2.robotmines.mine.Mine;
import fr.robotv2.robotmines.mine.MineResetType;
import fr.robotv2.robotmines.util.MineUtil;
import org.bukkit.scheduler.BukkitRunnable;

public class ResetPreparationRunnable extends BukkitRunnable {

    private final Mine mine;
    private final MineResetType type;
    private int timer = 10;

    public ResetPreparationRunnable(Mine mine, MineResetType type) {
        this.mine = mine;
        this.type = type;
    }

    @Override
    public void run() {

        if(timer != 0) {
            --timer;
            MineUtil.broadcast(mine, "&eLa mine &f" + mine.getName() + "&e se reinitialise dans &f" + timer + " &eseconde(s)");
            return;
        }

        type.reset(mine);
    }
}
