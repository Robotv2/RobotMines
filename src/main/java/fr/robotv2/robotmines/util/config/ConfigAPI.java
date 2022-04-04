package fr.robotv2.robotmines.util.config;

import fr.robotv2.robotmines.RobotMines;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class ConfigAPI {
    private static Plugin plugin;
    private static final Map<String, Config> configs = new HashMap<>();

    public static Config getConfig(String name) {
        if(plugin == null) {
            RobotMines.get().getLogger().warning("You can't access this config as the configAPI hasn't been init yet. Use the method: ConfigAPI#init() before accessing any config");
        }
        Config config = configs.get(name);
        if(config == null) {
            config = new Config(ConfigAPI.plugin, name);
            configs.put(name, config);
        }
        return config;
    }

    public static void init(Plugin plugin) {
        ConfigAPI.plugin = plugin;
    }
}
