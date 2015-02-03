/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nova.core.recipes.crafting;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import nova.core.item.ItemStack;

/**
 *
 * @author Stan
 */
public class ShapelessCraftingRecipe implements CraftingRecipe {
    private final ItemStack nominalOutput;
	private final RecipeFunction recipeFunction;
	private final ItemIngredient[] ingredients;
	
	public ShapelessCraftingRecipe(ItemStack output, ItemIngredient[] ingredients) {
        this(output, (crafting, tagged) -> Optional.of(output), ingredients);
    }

    public ShapelessCraftingRecipe(ItemStack nominalOutput, RecipeFunction recipeFunction, ItemIngredient[] ingredients) {
        this.nominalOutput = nominalOutput;
        this.recipeFunction = recipeFunction;
		this.ingredients = ingredients;
	}
	
	public int size() {
		return ingredients.length;
	}
	
	public ItemIngredient[] getIngredients() {
		return ingredients;
	}

	@Override
	public boolean matches(CraftingGrid craftingGrid) {
		return matchShapeless(ingredients, craftingGrid) != null;
	}
	
	@Override
	public Optional<ItemStack> getCraftingResult(CraftingGrid craftingGrid) {
		RecipeMatching matching = matchShapeless(ingredients, craftingGrid);

        Map<String, ItemStack> map = new HashMap<>();
        for (int i = 0; i < ingredients.length; i++) {
            if (ingredients[i].getTag().isPresent())
                map.put(ingredients[i].getTag().get(), matching.inputs[i]);
        }

        return recipeFunction.doCrafting(craftingGrid, map);
	}
	
	@Override
	public void consumeItems(CraftingGrid craftingGrid) {
		RecipeMatching matching = matchShapeless(ingredients, craftingGrid);
		
		for (int i = 0; i < ingredients.length; i++) {
			ItemIngredient ingredient = ingredients[i];
			ItemStack transformed = ingredient.consumeOnCrafting(matching.inputs[i], craftingGrid);
            craftingGrid.setStack(matching.indices[i], Optional.ofNullable(transformed));
		}
	}

    @Override
    public Optional<ItemStack> getNominalOutput() {
        return Optional.of(nominalOutput);
    }
	
	/**
	 * Reorders ingredients to match the given shapeless recipe.
	 * 
	 * @param recipe shapeless recipe
	 * @param craftingGrid crafting input
	 * @return reordered inputs, or null if no match was found
	 */
	private static RecipeMatching matchShapeless(
			ItemIngredient[] recipe,
			CraftingGrid craftingGrid) {
		if (craftingGrid.countFilledStacks() != recipe.length)
			return null;
		
		ItemStack[] matched = new ItemStack[recipe.length];
		int[] indices = new int[recipe.length];
		
		outer: for (int i = 0; i < craftingGrid.size(); i++) {
			Optional<ItemStack> ingredient = craftingGrid.getStack(i);
			if (!ingredient.isPresent())
                continue;
			
			for (int j = 0; j < recipe.length; j++) {
				if (matched[j] != null) continue;
				
				if (recipe[j].matches(ingredient.get())) {
					matched[j] = ingredient.get();
					indices[j] = i;
					continue outer;
				}
			}
			
			return null;
		}
		
		return new RecipeMatching(matched, indices);
	}
	
	private static class RecipeMatching {
		public final ItemStack[] inputs;
		public final int[] indices;

		public RecipeMatching(ItemStack[] inputs, int[] indices) {
			this.inputs = inputs;
			this.indices = indices;
		}
	}
}
