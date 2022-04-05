package fr.robotv2.robotmines.ui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface Gui {
    String getName(Player player, Object... objects);
    int getSize();
    void contents(Player player, Inventory inv, Object... objects);
    void onClick(Player player, Inventory inv, ItemStack current, int slot, ClickType type);
}
