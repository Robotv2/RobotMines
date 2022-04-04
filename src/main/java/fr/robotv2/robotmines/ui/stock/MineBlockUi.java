package fr.robotv2.robotmines.ui.stock;

import fr.robotv2.robotmines.mine.Mine;
import fr.robotv2.robotmines.mine.MineBlockChance;
import fr.robotv2.robotmines.ui.Gui;
import fr.robotv2.robotmines.ui.ItemConstant;
import fr.robotv2.robotmines.util.ItemAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MineBlockUi implements Gui, Listener {

    private final Map<Player, Mine> selected = new HashMap<>();

    @Override
    public String getName(Player player, Object... objects) {
        return ((Mine) objects[0]).getName() + " &8> &7Blocs";
    }

    @Override
    public int getSize() {
        return 54;
    }

    @Override
    public void contents(Player player, Inventory inv, Object... objects) {

        Mine mine = (Mine) objects[0];
        this.selected.put(player, mine);

        for(int i = 0; i < getSize(); i++) {
            inv.setItem(i, ItemConstant.getEmpty());
        }

        int index = 0;
        mine.sortBlockChance();

        for(MineBlockChance mineBlockChance : mine.getBlockChance()) {

            ItemStack item = new ItemAPI.ItemBuilder().setType(mineBlockChance.getMaterial()).setLore("", "&8> &cChance: " + mineBlockChance.getChance() + "%", "").build();
            inv.setItem(index, item);
            ++index;

        }
    }

    @Override
    public void onClick(Player player, Inventory inv, ItemStack current, int slot) {

        Mine mine = this.selected.get(player);
        Material material = current.getType();

        if(slot >= 54) {

            if(!material.isBlock()) return;
            if(mine.containsMaterial(material)) return;
            mine.addBlockChance(material, 10);

        } else {
            if(current.isSimilar(ItemConstant.getEmpty())) return;
            mine.removeBlockChance(material);

        }

        this.contents(player, inv, mine);
    }

    @Override
    public void onClose(Player player, InventoryCloseEvent event) {}
}
