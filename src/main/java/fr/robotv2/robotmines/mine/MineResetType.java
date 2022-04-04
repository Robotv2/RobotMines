package fr.robotv2.robotmines.mine;

import fr.robotv2.robotmines.RobotMines;
import fr.robotv2.robotmines.mine.task.ResetPreparationRunnable;
import fr.robotv2.robotmines.mine.task.ResetRunnable;
import org.bukkit.Bukkit;
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

        if(!preparation.containsKey(mine)) {
            ResetPreparationRunnable resetPreparationRunnable = new ResetPreparationRunnable(mine, this);
            BukkitTask task = Bukkit.getScheduler().runTaskTimer(RobotMines.get(), resetPreparationRunnable, 20 , 20);
            preparation.put(mine, task);
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

    private void resetGradual(Mine mine) {
        ResetRunnable runnable = new ResetRunnable(mine);
        Bukkit.getScheduler().runTaskTimer(RobotMines.get(), runnable, 1L, 1L);
    }
}
