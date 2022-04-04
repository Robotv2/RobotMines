package fr.robotv2.robotmines.wand;

import fr.robotv2.robotmines.mine.MineBuilder;
import fr.robotv2.robotmines.util.ItemAPI;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WandManager {

    private final Map<UUID, MineBuilder> builders = new HashMap<>();
    private ItemStack wand;

    public WandManager(JavaPlugin plugin) {
        this.buildWand();
        plugin.getServer().getPluginManager().registerEvents(new WandListeners(this), plugin);
    }

    public void buildWand() {
        this.wand = new ItemAPI.ItemBuilder().setType(Material.STICK).setName("&d&lMine Wand")
                .setLore("&eRight-click to define the first-bound", "&eLeft-click to define the second-bound")
                .addEnchant(Enchantment.ARROW_FIRE, 1, false).addFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES)
                .build();
    }

    public ItemStack getWand() {
        return wand;
    }

    public boolean isWand(ItemStack item) {
        if(item == null)
            return false;
        return item.isSimilar(wand);
    }

    public boolean hasBuilder(Player player) {
        return builders.containsKey(player.getUniqueId());
    }

    public MineBuilder getBuilder(Player player) {
        return builders.get(player.getUniqueId());
    }

    public void startCreation(Player player, String name) {
        builders.put(player.getUniqueId(), new MineBuilder().setName(name));
        if(!player.getInventory().containsAtLeast(getWand(), 1)) {
            player.getInventory().addItem(getWand());
            player.updateInventory();
        }
    }
}
