package fr.robotv2.robotmines.ui.stock;

import fr.robotv2.robotmines.RobotMines;
import fr.robotv2.robotmines.mine.Mine;
import fr.robotv2.robotmines.mine.MineBlockChance;
import fr.robotv2.robotmines.ui.Gui;
import fr.robotv2.robotmines.ui.ItemConstant;
import fr.robotv2.robotmines.util.ColorUtil;
import fr.robotv2.robotmines.util.ItemAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class MineBlockUi implements Gui, Listener {

    private final Map<Player, Mine> selected = new HashMap<>();
    private final Map<Player, Material> blockChance = new HashMap<>();

    public MineBlockUi(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

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

            ItemStack item = new ItemAPI.ItemBuilder().setType(mineBlockChance.getMaterial())
                    .setLore(
                            "",
                            "&8> &cChance: " + mineBlockChance.getChance() + "%",
                            "",
                            "&eClic-gauche pour retirer",
                            "&eClic-droit pour re-définir la valeur")
                    .build();

            inv.setItem(index, item);
            ++index;

        }
    }

    @Override
    public void onClick(Player player, Inventory inv, ItemStack current, int slot, ClickType type) {

        Mine mine = this.selected.get(player);
        Material material = current.getType();

        if(slot >= 54) {

            if(!material.isBlock()) return;
            if(mine.containsMaterial(material)) return;

            this.needChanceValue(player, material);

        } else {
            if(current.isSimilar(ItemConstant.getEmpty())) return;
            mine.removeBlockChance(material);

            if(type == ClickType.RIGHT) {
                this.needChanceValue(player, material);
                return;
            }
        }

        this.contents(player, inv, mine);
    }

    public void needChanceValue(Player player, Material material) {
        this.blockChance.put(player, material);
        player.closeInventory();
        ColorUtil.sendMessage(player, "&aÉcrivez dans le chat le pourcentage souhaité.");
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();

        if(blockChance.containsKey(event.getPlayer())) {

            event.setCancelled(true);

            Mine mine = selected.get(player);
            Material material = blockChance.get(player);
            String toParse = event.getMessage().split(" ")[0];

            try {
                double chance = Double.parseDouble(toParse);
                mine.addBlockChance(material, chance);
                RobotMines.get().getGuiManager().open(player, MineBlockUi.class, mine);
            } catch (NumberFormatException exception) {
                ColorUtil.sendMessage(player, "&c'" + toParse + "' n'est pas un nombre valide !");
            }

            blockChance.remove(player);
        }
    }
}
