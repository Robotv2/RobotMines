package fr.robotv2.robotmines.ui;

import fr.robotv2.robotmines.mine.MineResetType;
import fr.robotv2.robotmines.util.ItemAPI;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemConstant {

    private final static ItemStack EMPTY = new ItemAPI.ItemBuilder().setType(Material.GRAY_STAINED_GLASS_PANE).setName("&8").build();
    private final static ItemStack RESET_ITEM = new ItemAPI.ItemBuilder().setType(Material.EMERALD_BLOCK)
            .setName("&aCliquez-ici pour reinitialiser la mine.").build();

    private final static ItemStack RESET_TYPE_GRADUAL = new ItemAPI.ItemBuilder().setType(Material.GOLD_BLOCK)
            .setName("&eMode de reset: &e&lGRADUAL").build();
    private final static ItemStack RESET_TYPE_FULL = new ItemAPI.ItemBuilder().setType(Material.GOLD_BLOCK)
            .setName("&eMode de reset: &e&lFULL").build();

    private final static ItemStack BLOCK_CHANCE = new ItemAPI.ItemBuilder().setType(Material.STONE)
            .setName("&fChanger les blocs dans la mine.").build();

    public static ItemStack getEmpty() {
        return EMPTY;
    }

    public static ItemStack getResetItem() {
        return RESET_ITEM;
    }

    public static ItemStack getResetType(MineResetType type) {
        switch (type) {
            case FULL:
                return RESET_TYPE_FULL;
            case GRADUAL:
                return RESET_TYPE_GRADUAL;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static ItemStack getBlockChance() {
        return BLOCK_CHANCE;
    }
}
