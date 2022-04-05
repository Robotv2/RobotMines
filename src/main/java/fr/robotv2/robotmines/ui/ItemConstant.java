package fr.robotv2.robotmines.ui;

import fr.robotv2.robotmines.util.ItemAPI;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemConstant {

    private final static ItemStack EMPTY = new ItemAPI.ItemBuilder().setType(Material.GRAY_STAINED_GLASS_PANE).setName("&8").build();

    public static ItemStack getEmpty() {
        return EMPTY;
    }
}
