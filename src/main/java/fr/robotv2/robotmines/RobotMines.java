package fr.robotv2.robotmines;

import co.aikar.commands.PaperCommandManager;
import fr.robotv2.robotmines.block.BlockAdapter;
import fr.robotv2.robotmines.block.InternalBlockAdapter;
import fr.robotv2.robotmines.block.WorldEditBlockAdapter;
import fr.robotv2.robotmines.command.MineBaseCommand;
import fr.robotv2.robotmines.listeners.MineListeners;
import fr.robotv2.robotmines.listeners.SystemListeners;
import fr.robotv2.robotmines.mine.Mine;
import fr.robotv2.robotmines.mine.MineHelper;
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

    private MineHelper mineHelper;
    private WandManager wandManager;
    private GuiManager guiManager;

    @Override
    public void onEnable() {

        instance = this;
        ConfigAPI.init(this);

        this.mineHelper = new MineHelper();
        mineHelper.loadMines();

        new SystemListeners(this, this.mineHelper);
        new MineListeners(this);

        this.wandManager = new WandManager(this);
        this.guiManager = new GuiManager(this);

        this.registerCommand();
    }

    @Override
    public void onDisable() {
        getLogger().info("Cancelling all tasks...");
        Bukkit.getScheduler().cancelTasks(this);

        getLogger().info("Saving all mines to files...");
        mineHelper.getMines().forEach(Mine::saveToFile);

        getLogger().info("Re-filling of all the mines...");
        mineHelper.getMines().forEach(getBlockAdapter()::fillMine);

        instance = null;
        getLogger().info("Disabling plugin...");
    }

    public static RobotMines get() {
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

    public MineHelper getMineHelper() {
        return mineHelper;
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
            return getMineHelper().getByName(c.popFirstArg());
        });
        manager.getCommandCompletions().registerCompletion("mines", c -> {
            return getMineHelper().getMines().stream().map(Mine::getName).collect(Collectors.toSet());
        });
        manager.registerCommand(new MineBaseCommand(this.mineHelper));
    }
}
