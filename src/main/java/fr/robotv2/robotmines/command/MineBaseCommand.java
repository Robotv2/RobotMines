package fr.robotv2.robotmines.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import fr.robotv2.robotmines.RobotMines;
import fr.robotv2.robotmines.mine.Mine;
import fr.robotv2.robotmines.mine.MineHelper;
import fr.robotv2.robotmines.mine.MineResetType;
import fr.robotv2.robotmines.ui.stock.MineMenuUi;
import fr.robotv2.robotmines.util.ColorUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("mine")
public class MineBaseCommand extends BaseCommand {

    private final MineHelper mineHelper;

    public MineBaseCommand(MineHelper mineHelper) {
        this.mineHelper = mineHelper;
    }

    @Subcommand("create")
    @CommandPermission("robotmines.command.create")
    public void onCreate(Player player, String mine) {

        if(mineHelper.exist(mine)) {
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
            mineHelper.loadMine(mine.getName());
            ColorUtil.sendMessage(player, "&aVous venez de créer une nouvelle mine avec succès !");
        } catch (NullPointerException exception) {
            ColorUtil.sendMessage(player, "&cVous ne pouvez pas créer la mine: des éléments sont manquants.");
        }
    }

    @Subcommand("delete")
    @CommandPermission("robotmines.command.delete")
    @CommandCompletion("@mines")
    public void onDelete(CommandSender sender, Mine mine) {

        if(!mineHelper.exist(mine)) {
            ColorUtil.sendMessage(sender, "&cCette mine n'existe pas.");
            return;
        }

        mineHelper.deleteMine(mine);
        ColorUtil.sendMessage(sender, "&aVous venez de supprimer la mine: " + mine.getName());
    }

    @Subcommand("reset")
    @CommandPermission("robotmines.command.reset")
    @CommandCompletion("@mines")
    public void onReset(CommandSender sender, Mine mine, @Optional MineResetType type) {

        if(!mineHelper.exist(mine)) {
            ColorUtil.sendMessage(sender, "&cCette mine n'existe pas.");
            return;
        }

        if(type == null) {
            mine.reset();
        } else {
            mine.reset(type);
        }
    }

    @Subcommand("menu")
    @CommandPermission("robotmines.command.menu")
    @CommandCompletion("@mines")
    public void onOpenMenu(Player player, Mine mine) {

        if(!mineHelper.exist(mine)) {
            ColorUtil.sendMessage(player, "&cCette mine n'existe pas.");
            return;
        }

        RobotMines.get().getGuiManager().open(player, MineMenuUi.class, mine);
    }

    @Subcommand("info")
    @CommandPermission("robotmines.command.info")
    @CommandCompletion("@mines")
    public void onInfo(Player player, Mine mine) {

        if(!mineHelper.exist(mine)) {
            ColorUtil.sendMessage(player, "&cCette mine n'existe pas.");
            return;
        }

        int requiredBlock = (int) ((mine.getBlocks().size() - mine.getAirBlocks().size()) - (mine.getBlocks().size() * (mine.getResetPourcentage() / 100)));

        ColorUtil.sendMessage(player, "&8&l&m--------------", false);
        ColorUtil.sendMessage(player, "&e&lBlocs: &e" + mine.getBlocks().size(), false);
        ColorUtil.sendMessage(player, "&e&lAir: &e" + mine.getBlocks().size(), false);
        ColorUtil.sendMessage(player, "&e&lBloc(s) nécessaire(s): &e" + Math.max(0, requiredBlock), false);
        ColorUtil.sendMessage(player, "&8&l&m--------------", false);
    }
}
