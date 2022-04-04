package fr.robotv2.robotmines.ui.stock;

import fr.robotv2.robotmines.mine.Mine;
import fr.robotv2.robotmines.ui.Gui;
import fr.robotv2.robotmines.ui.ItemConstant;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class MineMenuUi implements Gui {

    private ItemStack empty;
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

        inv.setItem(10, ItemConstant.getResetItem());
    }

    @Override
    public void onClick(Player player, Inventory inv, ItemStack current, int slot) {
        Mine mine = selected.get(player);

        switch (slot) {
            case 10:
                mine.reset();
                player.closeInventory();
                break;
        }

    }

    @Override
    public void onClose(Player player, InventoryCloseEvent event) {
        selected.remove(player);
    }
}
