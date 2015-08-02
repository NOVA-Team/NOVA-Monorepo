package nova.core.wrapper.mc17.recipes;

import net.minecraft.inventory.InventoryCrafting;
import nova.core.recipes.crafting.CraftingGrid;

public class NovaCraftingGrid extends InventoryCrafting {
    private final CraftingGrid craftingGrid;

    public NovaCraftingGrid(CraftingGrid craftingGrid) {
        super(new NovaCraftingGridContainer(craftingGrid), craftingGrid.getWidth(), craftingGrid.getHeight());
        this.craftingGrid = craftingGrid;
    }
}
