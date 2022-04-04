package fr.robotv2.robotmines.ui;

import fr.robotv2.robotmines.util.ItemAPI;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemConstant {

    private final static ItemStack EMPTY = new ItemAPI.ItemBuilder().setType(Material.GRAY_STAINED_GLASS_PANE).setName("&8").build();
    private static ItemStack RESET_ITEM = new ItemAPI.ItemBuilder().setType(Material.EMERALD_BLOCK)
            .setName("&aCliquez-ici pour reinitialiser la mine.").build();

    public static ItemStack getEmpty() {
        return EMPTY;
    }

    public static ItemStack getResetItem() {
        return RESET_ITEM;
    }
}
