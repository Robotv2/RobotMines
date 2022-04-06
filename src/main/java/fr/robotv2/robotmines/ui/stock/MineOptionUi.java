package fr.robotv2.robotmines.ui.stock;

import fr.robotv2.robotmines.RobotMines;
import fr.robotv2.robotmines.mine.Mine;
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

public class MineOptionUi implements Gui, Listener {

    private final Map<Player, Mine> selected = new HashMap<>();
    private final Map<Player, MineOption> options = new HashMap<>();

    public MineOptionUi(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public enum MineOption {
        MAX_BLOCK_PER_TICK,
        RESET_POURCENTAGE;
    }

    @Override
    public String getName(Player player, Object... objects) {
        return ((Mine) objects[0]).getName() + " &8> &7Options";
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }

    @Override
    public void contents(Player player, Inventory inv, Object... objects) {

        Mine mine = (Mine) objects[0];
        this.selected.put(player, mine);

        for(int i = 0; i < getSize(); i++) {
            inv.setItem(i, ItemConstant.getEmpty());
        }

        inv.setItem(10, getMaxBlockPerTick(mine));
        inv.setItem(13, getResetPourcentage(mine));
    }

    @Override
    public void onClick(Player player, Inventory inv, ItemStack current, int slot, ClickType type) {

        if(slot != 10 && slot != 13)
            return;

        MineOption option = slot == 10 ? MineOption.MAX_BLOCK_PER_TICK : MineOption.RESET_POURCENTAGE;
        options.put(player, option);
        player.closeInventory();
        ColorUtil.sendMessage(player, "&aÉcrivez la nouvelle valeur de l'option dans le chat.");
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();

        if(options.containsKey(event.getPlayer())) {

            event.setCancelled(true);

            Mine mine = selected.get(player);
            MineOption option = options.get(player);
            String toParse = event.getMessage().split(" ")[0];

            try {
                double value = Double.parseDouble(toParse);
                mine.setOption(option, value);
                RobotMines.get().getGuiManager().open(player, MineOptionUi.class, mine);
            } catch (NumberFormatException exception) {
                ColorUtil.sendMessage(player, "&c'" + toParse + "' n'est pas un nombre valide !");
            }

            options.remove(player);
        }
    }

    public ItemStack getMaxBlockPerTick(Mine mine) {
        return new ItemAPI.ItemBuilder().setType(Material.OAK_SIGN)
                .setName("&eBloc(s) maximum placé(s) par tick: " + mine.getMaxBlockPerTick()).build();
    }

    public ItemStack getResetPourcentage(Mine mine) {
        return new ItemAPI.ItemBuilder().setType(Material.OAK_SIGN)
                .setName("&ePourcentage de bloc(s) d'air pour reset la mine: " + mine.getResetPourcentage()).build();
    }
}
