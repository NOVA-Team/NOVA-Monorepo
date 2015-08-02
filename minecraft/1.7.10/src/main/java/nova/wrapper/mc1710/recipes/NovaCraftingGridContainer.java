package nova.wrapper.mc1710.recipes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import nova.core.recipes.crafting.CraftingGrid;

/**
 * TODO: complete this class. without it, modded recipes will not work when crafted from NOVA mods.
 *
 * @author Stan Hebben
 */
public class NovaCraftingGridContainer extends Container {
    private final CraftingGrid craftingGrid;

    public NovaCraftingGridContainer(CraftingGrid craftingGrid) {
        this.craftingGrid = craftingGrid;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return true;
    }
}
