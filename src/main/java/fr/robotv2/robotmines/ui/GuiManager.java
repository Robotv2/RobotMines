package fr.robotv2.robotmines.ui;

import fr.robotv2.robotmines.RobotMines;
import fr.robotv2.robotmines.ui.stock.MineBlockUi;
import fr.robotv2.robotmines.ui.stock.MineMenuUi;
import fr.robotv2.robotmines.ui.stock.MineOptionUi;
import fr.robotv2.robotmines.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class GuiManager implements Listener {

    private final HashMap<Class<? extends Gui>, Gui> menus = new HashMap<>();
    private final HashMap<UUID, Gui> players = new HashMap<>();

    public GuiManager(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        //Adding all the default GUIs
        this.addMenu(new MineMenuUi());
        this.addMenu(new MineBlockUi(plugin));
        this.addMenu(new MineOptionUi(plugin));
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        UUID playerUUID = player.getUniqueId();
        ItemStack item = e.getCurrentItem();

        if(item == null) return;
        if(!players.containsKey(playerUUID)) return;

        e.setCancelled(true);
        Gui menu = players.get(playerUUID);
        menu.onClick(player, e.getInventory(), item, e.getRawSlot(), e.getClick());
    }

    public void addMenu(Gui gui){
        menus.put(gui.getClass(), gui);
    }

    public void open(Player player, Class<? extends Gui> gClass, Object... objects){
        if(!menus.containsKey(gClass)) {
            ColorUtil.log(Level.SEVERE, "&cVous devez enregistrer l'inventaire avant de pouvoir l'ouvrir");
            return;
        }

        Gui menu = menus.get(gClass);
        Inventory inv = Bukkit.createInventory(null, menu.getSize(), ColorUtil.colorize(menu.getName(player, objects)));
        menu.contents(player, inv, objects);
        players.put(player.getUniqueId(), menu);

        Bukkit.getScheduler().runTaskLater(RobotMines.get(), () -> {
            player.openInventory(inv);
        }, 2L);
    }
}
