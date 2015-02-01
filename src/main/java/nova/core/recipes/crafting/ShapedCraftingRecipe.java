package nova.core.recipes.crafting;

import java.util.*;

import nova.core.item.ItemStack;
import nova.core.recipes.ItemIngredient;
import nova.core.util.transform.Vector2i;

/**
 *
 * @author Stan Hebben
 */
public class ShapedCraftingRecipe implements CraftingRecipe {
    /*
     * This class contains an optimized recipe resolution algorithm.
     *
     * This algorithms works as follows:
     * - During construction, the 2D ingredients array is transformed to a flat list of ingredients and their positions
     * - During recipe resolution, the first non-empty crafting grid spot is searched
     * -
     */

	private final int width;
	private final int height;
	private final int[] posx;
	private final int[] posy;
	private final boolean mirrored;
    private final int lastIngredientIndexOnFirstLine; // only actually matters for mirrored recipes
	private final RecipeFunction recipeFunction;
	
	private final ItemIngredient[] ingredients;

    public ShapedCraftingRecipe(ItemStack output, Optional<ItemIngredient>[][] ingredients, boolean mirrored) {
        this((grid, tagged) -> Optional.of(output), ingredients, mirrored);
    }

	public ShapedCraftingRecipe(RecipeFunction recipeFunction, Optional<ItemIngredient>[][] ingredients, boolean mirrored) {
		int numIngredients = 0;
		for (Optional<ItemIngredient>[] row : ingredients) {
			for (Optional<ItemIngredient> ingredient : row) {
				if (ingredient.isPresent())
					numIngredients++;
			}
		}

        // translate 2d ingredient array to ingredient list
		this.posx = new int[numIngredients];
		this.posy = new int[numIngredients];
		this.ingredients = new ItemIngredient[numIngredients];
        this.recipeFunction = recipeFunction;

		int width1 = 0;
		int height1 = ingredients.length;
		
		int ix = 0;
		for (int j = 0; j < ingredients.length; j++) {
			Optional<ItemIngredient>[] row = ingredients[j];
			width1 = Math.max(width1, row.length);
			
			for (int i = 0; i < row.length; i++) {
				if (row[i].isPresent()) {
					this.posx[ix] = (byte) i;
					this.posy[ix] = (byte) j;
					this.ingredients[ix] = row[i].get();
					ix++;
				}
			}
		}

		this.width = width1;
		this.height = height1;
		this.mirrored = mirrored;
        this.lastIngredientIndexOnFirstLine = getLastIngredientIndexOnFirstLine();
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public boolean isMirrored() {
		return mirrored;
	}
	
	public ItemIngredient[] getIngredients() {
		return ingredients;
	}
	
	public int[] getIngredientsX() {
		return posx;
	}
	
	public int[] getIngredientsY() {
		return posy;
	}
	
	@Override
	public boolean matches(CraftingGrid inventory) {
        return findIngredientMapping(inventory) != null;
	}

	@Override
	public Optional<ItemStack> getCraftingResult(CraftingGrid craftingGrid) {
        ShapedMapping mapping = findIngredientMapping(craftingGrid);
        if (mapping == null)
            return Optional.empty();

        return getRecipeOutput(craftingGrid, mapping);
	}
	
	@Override
	public void consumeItems(CraftingGrid craftingGrid) {
        ShapedMapping mapping = findIngredientMapping(craftingGrid);
        if (mapping == null)
            return;

        for (int i = 0; i < ingredients.length; i++) {
            ItemStack consumed = ingredients[i].consumeOnCrafting(mapping.itemStacks[i], craftingGrid);
            if (consumed.getStackSize() == 0)
                consumed = null;

            mapping.setStack(craftingGrid, i, Optional.ofNullable(consumed));
        }
	}

    @Override
    public Optional<Collection<String>> getPossibleItemsInFirstSlot() {
        if (isMirrored()) {
            Optional<Collection<String>> optionsForFirstItem = ingredients[0].getPossibleItemIds();
            if (!optionsForFirstItem.isPresent())
                return Optional.empty();

            Optional<Collection<String>> optionsForSecondItem = ingredients[lastIngredientIndexOnFirstLine].getPossibleItemIds();
            if (!optionsForSecondItem.isPresent())
                return Optional.empty();

            Set<String> result = new HashSet<>();
            result.addAll(optionsForFirstItem.get());
            result.addAll(optionsForSecondItem.get());
            return Optional.of(result);
        } else {
            return ingredients[0].getPossibleItemIds();
        }
    }

    // #######################
    // ### Private methods ###
    // #######################

    private int getLastIngredientIndexOnFirstLine() {
        int firstLineIndex = posy[0];
        int result = 0;
        for (int i = 0; i < ingredients.length; i++) {
            if (posy[0] == firstLineIndex)
                result = i;
        }

        return result;
    }

    private ShapedMapping findIngredientMapping(CraftingGrid craftingGrid) {
        if (craftingGrid.countFilledStacks() != ingredients.length)
            return null;

        ShapedMapping mapping = findIngredientMapping(craftingGrid, false);
        if (mapping == null && isMirrored())
            mapping = findIngredientMapping(craftingGrid, true);

        return mapping;
    }

    private ShapedMapping findIngredientMapping(CraftingGrid craftingGrid, boolean mirrored) {
        Optional<Vector2i> optOffset = craftingGrid.getFirstNonEmptyPosition();
        if (!optOffset.isPresent())
            return null;

        ShapedMapping mapping;
        if (mirrored)
            mapping = new MirroredMapping(optOffset.get(), craftingGrid.getWidth());
        else
            mapping = new NonMirroredMapping(optOffset.get());

        for (int i = 0; i < ingredients.length; i++) {
            Optional<ItemStack> item = mapping.getStack(craftingGrid, i);
            if (!item.isPresent())
                return null;

            if (!ingredients[i].matches(item.get()))
                return null;

            mapping.itemStacks[i] = item.get();
        }

        return mapping;
    }

    private Optional<ItemStack> getRecipeOutput(
            CraftingGrid craftingGrid,
            ShapedMapping shapedMapping) {
        Map<String, ItemStack> tagged = new HashMap<>();
        for (int k = 0; k < ingredients.length; k++) {
            if (ingredients[k].getTag().isPresent())
                tagged.put(ingredients[k].getTag().get(), shapedMapping.itemStacks[k]);
        }

        return recipeFunction.doCrafting(craftingGrid, tagged);
	}

    private abstract class ShapedMapping {
        public final int offsetX;
        public final int offsetY;
        public final ItemStack[] itemStacks;

        private ShapedMapping(Vector2i offset) {
            this.offsetX = offset.x;
            this.offsetY = offset.y;
            this.itemStacks = new ItemStack[ingredients.length];
        }

        public abstract Optional<ItemStack> getStack(CraftingGrid craftingGrid, int ingredient);

        public abstract void setStack(CraftingGrid craftingGrid, int ingredient, Optional<ItemStack> value);
    }

    private class NonMirroredMapping extends ShapedMapping {
        public NonMirroredMapping(Vector2i firstItemOffset) {
            super(new Vector2i(
                    firstItemOffset.x - posx[0],
                    firstItemOffset.y - posy[0]));
        }

        public Optional<ItemStack> getStack(CraftingGrid craftingGrid, int ingredient) {
            return craftingGrid.getStack(
                    offsetX + posx[ingredient],
                    offsetY + posy[ingredient]);
        }

        public void setStack(CraftingGrid craftingGrid, int ingredient, Optional<ItemStack> value) {
            craftingGrid.setStack(
                    offsetX + posx[ingredient],
                    offsetY + posy[ingredient],
                    value);
        }
    }

    private class MirroredMapping extends ShapedMapping {
        public MirroredMapping(Vector2i firstItemOffset, int craftingGridWidth) {
            super(new Vector2i(
                    // dark magic converting the offset of the first non-empty item slot to the mapping offset
                    // in a mirrored recipe mapping
                    (craftingGridWidth - posx[lastIngredientIndexOnFirstLine] - 1) - (firstItemOffset.x - posx[0]),
                    firstItemOffset.y - posy[0]));
        }

        public Optional<ItemStack> getStack(CraftingGrid craftingGrid, int ingredient) {
            return craftingGrid.getStack(
                    craftingGrid.getWidth() - (offsetX + posx[ingredient]) - 1,
                    offsetY + posy[ingredient]);
        }

        public void setStack(CraftingGrid craftingGrid, int ingredient, Optional<ItemStack> value) {
            craftingGrid.setStack(
                    craftingGrid.getWidth() - (offsetX + posx[ingredient]) - 1,
                    offsetY + posy[ingredient],
                    value);
        }
    }
}
