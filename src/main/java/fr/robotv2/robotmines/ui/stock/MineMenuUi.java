package fr.robotv2.robotmines.ui.stock;

import fr.robotv2.robotmines.RobotMines;
import fr.robotv2.robotmines.mine.Mine;
import fr.robotv2.robotmines.mine.MineResetType;
import fr.robotv2.robotmines.ui.Gui;
import fr.robotv2.robotmines.ui.ItemConstant;
import fr.robotv2.robotmines.util.ItemAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class MineMenuUi implements Gui {

    private final ItemStack RESET_TYPE_GRADUAL = new ItemAPI.ItemBuilder().setType(Material.GOLD_BLOCK)
            .setName("&eMode de reset: &e&lGRADUAL").build();
    private final ItemStack RESET_TYPE_FULL = new ItemAPI.ItemBuilder().setType(Material.GOLD_BLOCK)
            .setName("&eMode de reset: &e&lFULL").build();
    private final ItemStack BLOCK_CHANCE = new ItemAPI.ItemBuilder().setType(Material.STONE)
            .setName("&fChanger les blocs dans la mine.").build();
    private final ItemStack RESET_ITEM = new ItemAPI.ItemBuilder().setType(Material.EMERALD_BLOCK)
            .setName("&aCliquez-ici pour reinitialiser la mine.").build();
    private final ItemStack OPTION_ITEM = new ItemAPI.ItemBuilder().setType(Material.REPEATER)
            .setName("&cOptions de la mine.").build();

    private final Map<Player, Mine> selected = new HashMap<>();

    @Override
    public String getName(Player player, Object... objects) {
        return ((Mine) objects[0]).getName();
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }

    @Override
    public void contents(Player player, Inventory inv, Object... objects) {

        Mine mine = (Mine) objects[0];
        selected.put(player, mine);

        for(int i = 0; i < getSize(); i++) {
            inv.setItem(i, ItemConstant.getEmpty());
        }

        inv.setItem(0, OPTION_ITEM);
        inv.setItem(10, RESET_ITEM);
        inv.setItem(13, BLOCK_CHANCE);
        inv.setItem(16, this.getResetType(mine.getResetType()));
    }

    @Override
    public void onClick(Player player, Inventory inv, ItemStack current, int slot, ClickType type) {

        Mine mine = selected.get(player);

        switch (slot) {
            case 0:
                RobotMines.get().getGuiManager().open(player, MineOptionUi.class, mine);
                break;
            case 10:
                mine.reset();
                player.closeInventory();
                break;
            case 13:
                RobotMines.get().getGuiManager().open(player, MineBlockUi.class, mine);
                break;
            case 16:
                MineResetType change = mine.getResetType() == MineResetType.FULL ? MineResetType.GRADUAL : MineResetType.FULL;
                mine.setResetType(change);
                inv.setItem(16, this.getResetType(change));
                break;
        }
    }

    public ItemStack getResetType(MineResetType type) {
        switch (type) {
            case FULL:
                return RESET_TYPE_FULL;
            case GRADUAL:
                return RESET_TYPE_GRADUAL;
            default:
                throw new IllegalArgumentException();
        }
    }
}
