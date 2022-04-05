package fr.robotv2.robotmines.mine;

import fr.robotv2.robotmines.RobotMines;
import fr.robotv2.robotmines.mine.task.ResetPreparationRunnable;
import fr.robotv2.robotmines.mine.task.ResetRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public enum MineResetType {

    GRADUAL,
    FULL;

    private final Map<Mine, BukkitTask> preparation = new HashMap<>();

    public void reset(Mine mine) {

        if(mine.isBeingReset()) {
            return;
        }

        if(needPreparation(mine)) {
            this.prepareMineToReset(mine);
            return;
        }

        preparation.get(mine).cancel();
        preparation.remove(mine);

        switch (this) {
            case GRADUAL:
                this.resetGradual(mine);
                break;
            case FULL:
                RobotMines.get().getBlockAdapter().fillMine(mine);
                break;
        }
    }

    private boolean needPreparation(Mine mine) {
        return !preparation.containsKey(mine);
    }

    private void prepareMineToReset(Mine mine) {
        ResetPreparationRunnable resetPreparationRunnable = new ResetPreparationRunnable(mine, this);
        BukkitTask task = resetPreparationRunnable.runTaskTimer(RobotMines.get(), 20L, 20L);
        preparation.put(mine, task);
    }

    private void resetGradual(Mine mine) {
        ResetRunnable runnable = new ResetRunnable(mine);
        runnable.runTaskTimer(RobotMines.get(), 1, 1);
    }
}
