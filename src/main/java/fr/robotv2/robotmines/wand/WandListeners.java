package fr.robotv2.robotmines.wand;

import fr.robotv2.robotmines.mine.MineBuilder;
import fr.robotv2.robotmines.util.ColorUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class WandListeners implements Listener {

    private final WandManager manager;
    public WandListeners(WandManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        if(event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }



        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        ItemStack item = player.getInventory().getItemInMainHand();

        if(block == null || block.getType() == Material.AIR) {
            return;
        }

        if(!manager.isWand(item)) {
            return;
        }

        event.setCancelled(true);

        if(!manager.hasBuilder(player)) {
            ColorUtil.sendMessage(player, "&cMerci de créer une mine avant de selectionner les bordures");
            return;
        }

        MineBuilder builder = manager.getBuilder(player);

        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            builder.setSecondBound(block.getLocation());
            ColorUtil.sendMessage(player, "&aVous venez de sélectionner ce block comme seconde bordure.");
        } else {
            builder.setFirstBound(block.getLocation());
            ColorUtil.sendMessage(player, "&aVous venez de sélectionner ce block comme première bordure.");
        }
    }
}
