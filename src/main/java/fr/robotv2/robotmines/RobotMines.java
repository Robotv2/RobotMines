package fr.robotv2.robotmines;

import co.aikar.commands.PaperCommandManager;
import fr.robotv2.robotmines.block.BlockAdapter;
import fr.robotv2.robotmines.block.InternalBlockAdapter;
import fr.robotv2.robotmines.block.WorldEditBlockAdapter;
import fr.robotv2.robotmines.command.MineBaseCommand;
import fr.robotv2.robotmines.listeners.SystemListeners;
import fr.robotv2.robotmines.mine.Mine;
import fr.robotv2.robotmines.mine.Mines;
import fr.robotv2.robotmines.ui.GuiManager;
import fr.robotv2.robotmines.util.config.ConfigAPI;
import fr.robotv2.robotmines.wand.WandManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.stream.Collectors;

public final class RobotMines extends JavaPlugin {

    private static RobotMines instance;
    private BlockAdapter blockAdapter;

    private WandManager wandManager;
    private GuiManager guiManager;

    @Override
    public void onEnable() {

        ConfigAPI.init(this);
        Mines.loadMines();

        new SystemListeners(this);
        this.wandManager = new WandManager(this);
        this.guiManager = new GuiManager(this);

        this.registerCommand();
    }

    @Override
    public void onDisable() {
        getLogger().info("Cancelling all tasks.");
        Bukkit.getScheduler().cancelTasks(this);

        getLogger().info("Re-filling all the mines.");
        Mines.getMines().stream().filter(Mine::isBeingReset).forEach(getBlockAdapter()::fillMine);
        getLogger().info("Re-filling of all the mines done.");
    }

    public static RobotMines get() {
        if(instance == null)
            instance = new RobotMines();
        return instance;
    }

    public BlockAdapter getBlockAdapter() {
        if(blockAdapter == null) {
            if(getServer().getPluginManager().isPluginEnabled("WorldEdit"))
                this.blockAdapter = new WorldEditBlockAdapter();
            else
                this.blockAdapter = new InternalBlockAdapter();
        }
        return blockAdapter;
    }

    public WandManager getWandManager() {
        return wandManager;
    }

    public GuiManager getGuiManager() {
        return guiManager;
    }

    //<- MINES FILE ->

    public FileConfiguration getMinesFile() {
        return ConfigAPI.getConfig("mines").get();
    }

    public void saveMinesFile() {
        ConfigAPI.getConfig("mines").save();
    }

    //<- COMMANDS ->

    public void registerCommand() {
        PaperCommandManager manager = new PaperCommandManager(this);
        manager.getCommandContexts().registerContext(Mine.class, c -> {
            return Mines.getByName(c.getFirstArg());
        });
        manager.getCommandCompletions().registerCompletion("mines", c -> {
            return Mines.getMines().stream().map(Mine::getName).collect(Collectors.toSet());
        });
        manager.registerCommand(new MineBaseCommand());
    }
}
