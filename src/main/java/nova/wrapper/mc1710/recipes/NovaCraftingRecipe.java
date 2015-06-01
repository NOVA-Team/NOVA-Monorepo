package nova.wrapper.mc1710.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import nova.core.game.Game;
import nova.core.item.Item;
import nova.core.recipes.crafting.CraftingRecipe;

import java.util.Optional;

public class NovaCraftingRecipe implements IRecipe {
	private final CraftingRecipe recipe;

	public NovaCraftingRecipe(CraftingRecipe recipe) {
		this.recipe = recipe;
	}

	@Override
	public boolean matches(InventoryCrafting inventoryCrafting, World world) {
		return recipe.matches(MCCraftingGrid.get(inventoryCrafting));
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting) {
		Optional<Item> craftingResult = recipe.getCraftingResult(MCCraftingGrid.get(inventoryCrafting));
		if (craftingResult.isPresent()) {
			return Game.nativeManager().toNative(craftingResult.get());
		} else {
			return null;
		}
	}

	@Override
	public int getRecipeSize() {
		return 1;
	}

	@Override
	public ItemStack getRecipeOutput() {
		Optional<Item> nominalOutput = recipe.getNominalOutput();
		if (nominalOutput.isPresent()) {
			return Game.nativeManager().toNative(nominalOutput.get());
		}
		return null;
	}
}
