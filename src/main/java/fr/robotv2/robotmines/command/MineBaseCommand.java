package fr.robotv2.robotmines.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import fr.robotv2.robotmines.RobotMines;
import fr.robotv2.robotmines.mine.Mine;
import fr.robotv2.robotmines.mine.Mines;
import fr.robotv2.robotmines.ui.stock.MineMenuUi;
import fr.robotv2.robotmines.util.ColorUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("mine")
public class MineBaseCommand extends BaseCommand {

    @Subcommand("create")
    @CommandPermission("robotmines.command.create")
    public void onCreate(Player player, String mine) {

        if(Mines.exist(mine)) {
            ColorUtil.sendMessage(player, "&cCette mine existe déjà.");
            return;
        }

        RobotMines.get().getWandManager().startCreation(player, mine);
        ColorUtil.sendMessage(player, "&aMerci d'utiliser la &e&lWAND &adans votre inventaire pour définir les deux bords de la mine.");
        ColorUtil.sendMessage(player, "&aVous pourrez ensuite effectuer la commande: /mine confirm");
    }

    @Subcommand("confirm")
    @CommandPermission("robotmines.command.create")
    public void onConfirm(Player player) {

        if(!RobotMines.get().getWandManager().hasBuilder(player)) {
            ColorUtil.sendMessage(player, "&cVous n'avez aucune mine en cours de création.");
            return;
        }

        try {
            Mine mine = RobotMines.get().getWandManager().getBuilder(player).build();
            Mines.loadMine(mine.getName());
            ColorUtil.sendMessage(player, "&aVous venez de créer une nouvelle mine avec succès !");
        } catch (NullPointerException exception) {
            ColorUtil.sendMessage(player, "&cVous ne pouvez pas créer la mine: des éléments sont manquants.");
        }
    }

    @Subcommand("delete")
    @CommandPermission("robotmines.command.delete")
    @CommandCompletion("@mines")
    public void onDelete(CommandSender sender, Mine mine) {

        if(mine == null) {
            ColorUtil.sendMessage(sender, "&cCette mine n'existe pas.");
            return;
        }

        Mines.deleteMine(mine);
        ColorUtil.sendMessage(sender, "&aVous venez de supprimer la mine: " + mine.getName());
    }

    @Subcommand("forcereset")
    @CommandPermission("robotmines.command.reset")
    @CommandCompletion("@mines")
    public void onReset(Player player) {
        Mine mine = Mines.getByLocation(player.getLocation());
        if(mine == null) {
            ColorUtil.sendMessage(player, "&cVous ne vous trouvez dans aucune mine.");
        } else {
            mine.reset();
        }
    }

    @Subcommand("menu")
    @CommandPermission("robotmines.command.menu")
    @CommandCompletion("@mines")
    public void onOpenMenu(Player player, Mine mine) {
        RobotMines.get().getGuiManager().open(player, MineMenuUi.class, mine);
    }
}
