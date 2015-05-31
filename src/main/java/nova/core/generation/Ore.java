package nova.core.generation;


import nova.core.block.BlockFactory;
import nova.core.util.EnumSelector;
import nova.core.util.Identifiable;

public final class Ore implements Identifiable {
    public final String oreGenName;
    public final BlockFactory block;
    public double height;
    public double rarity;
    public double clusterSize;
    public EnumSelector<WorldType> selector;

    public Ore(String oreGenName, BlockFactory block, double height, double rarity, double clusterSize, EnumSelector<WorldType> selector) {
        this.oreGenName = oreGenName;
        this.block = block;
        this.height = height;
        this.rarity = rarity;
        this.clusterSize = clusterSize;
        this.selector = selector;
    }

    @Override
    public String getID() {
        return oreGenName;
    }
}
