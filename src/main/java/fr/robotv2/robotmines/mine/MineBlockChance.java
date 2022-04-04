package fr.robotv2.robotmines.mine;

import org.bukkit.Material;

import java.util.List;
import java.util.stream.Collectors;

public class MineBlockChance {

    private final Material material;
    private final double chance;

    public MineBlockChance(Material material, double chance) {
        this.material = material;
        this.chance = chance;
    }

    public Material getMaterial() {
        return material;
    }

    public double getChance() {
        return chance;
    }

    public String serialize() {
        return getMaterial().name() + ":" + getChance();
    }

    @Override
    public String toString() {
        return "BlockMineChance{Material:" + getMaterial().name() + ";Chance:" + getChance() + "}";
    }

    public static MineBlockChance fromString(String data) {
        String[] args = data.split(":");
        Material material = Material.valueOf(args[0].toUpperCase());
        double chance = Double.parseDouble(args[1]);
        return new MineBlockChance(material, chance);
    }

    public static List<MineBlockChance> fromStringList(List<String> datas) {
        return datas.stream().map(MineBlockChance::fromString).collect(Collectors.toList());
    }

    public static List<String> toStringList(List<MineBlockChance> datas) {
        return datas.stream().map(MineBlockChance::serialize).collect(Collectors.toList());
    }
}
