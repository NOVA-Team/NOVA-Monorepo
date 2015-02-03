package nova.core.recipes.crafting;

import java.util.*;

import nova.core.item.ItemStack;
import nova.core.util.transform.Vector2i;

/**
 * Contains a single shaped crafting recipe. Can contain custom crafting logic.
 *
 * Crafting recipes can be specified as a 2-dimensional array of ingredients (with Optional.empty() being used for empty spots)
 * or as a 1-dimensional array of ingredients and a pattern string (lines are separated by -, spaces for empty spots, A-Z for ingredients.
 *
 * For instance, to define a stick recipe with a pattern string:
 *
 * new ShapedCraftingRecipe("A - A", ItemIngredient.forDictionary("plankWood"));
 *
 * Two kinds of recipes can be defined: basic or advanced. Basic recipes always return the same item, while advanced
 * recipes have their output defined by a lambda expression. RecipeFunctions will receive information about the 
 *
 * @author Stan Hebben
 */
public class ShapedCraftingRecipe implements CraftingRecipe {
    /*
     * This class contains an optimized recipe resolution algorithm.
     *
     * This algorithms works as follows:
     * A: During construction, the 2D ingredients array is transformed to a flat list of ingredients and their positions
     * B: During recipe resolution, the first non-empty crafting grid spot is searched
     * C: That position marks the position of the recipe inside the crafting grid (a 2x2 recipe has 4 possible positions in a 3x3 crafting grid)
     * D: The list of ingredients is run over and filled into an array for later processing
     * E: If any of the ingredients doesn't match, the crafting is rejected
     * F: For mirrored recipes B-E are repeated in the mirrored direction
     */

	private final int width;
	private final int height;
	private final int[] posx;
	private final int[] posy;
	private final boolean mirrored;
    private final int lastIngredientIndexOnFirstLine; // only actually matters for mirrored recipes
	private final RecipeFunction recipeFunction;
    private final ItemStack nominalOutput;
	
	private final ItemIngredient[] ingredients;

    /**
     * Defines a basic structured crafting recipe, using a format string.
     *
     * @param output Output {@link ItemStack} of the recipe
     * @param format Format
     * @param ingredients {@link ItemIngredient ItemIngredients}
     */
    public ShapedCraftingRecipe(ItemStack output, String format, ItemIngredient... ingredients) {
        this(output, format, false, ingredients);
    }

    /**
     * Defines a basic structured crafting recipe, possibly mirrored, using a format string.
     *
     * @param output Output {@link ItemStack} of the recipe
     * @param format Format
     * @param mirrored Whether this recipe is mirrored
     * @param ingredients {@link ItemIngredient ItemIngredients}
     */
    public ShapedCraftingRecipe(ItemStack output, String format, boolean mirrored, ItemIngredient... ingredients) {
        this(output, (grid, tagged) -> Optional.of(output), format, mirrored, ingredients);
    }

    /**
     * Defines an advanced crafting recipe, using a format string.
     *
     * @param recipeFunction {@link RecipeFunction}
     * @param format Format
     * @param mirrored Whether this recipe is mirrored
     * @param ingredients {@link ItemIngredient ItemIngredients}
     */
    public ShapedCraftingRecipe(ItemStack nominalOutput, RecipeFunction recipeFunction, String format, boolean mirrored, ItemIngredient... ingredients) {
        this.nominalOutput = nominalOutput;

        String[] formatLines = format.split("\\-");
        int numIngredients = 0;
        int width = 0;
        for (String formatLine : formatLines) {
            width = Math.max(width, formatLine.length());
            for (char c : formatLine.toCharArray()) {
                if (c == ' ')
                    continue;
                else if (c >= 'A' && c <= 'Z')
                    numIngredients++;
                else
                    throw new IllegalArgumentException("Invalid character in format string " + format + ": " + c);
            }
        }

        this.width = width;
        this.height = formatLines.length;
        this.posx = new int[numIngredients];
        this.posy = new int[numIngredients];
        this.ingredients = new ItemIngredient[numIngredients];
        this.mirrored = mirrored;

        int ingredientIndex = 0;
        for (int y = 0; y < this.height; y++) {
            String formatLine = formatLines[y];
            for (int x = 0; x < formatLine.length(); x++) {
                char c = formatLine.charAt(x);
                if (c == ' ')
                    continue;

                this.posx[ingredientIndex] = x;
                this.posy[ingredientIndex] = y;
                this.ingredients[ingredientIndex] = ingredients[c - 'A'];
            }
        }

        this.recipeFunction = recipeFunction;
        this.lastIngredientIndexOnFirstLine = getLastIngredientIndexOnFirstLine();
    }

    /**
     * Defines a basic crafting recipe, using a 2D ingredients array.
     *
     * @param output Output {@link ItemStack} of the recipe
     * @param ingredients {@link ItemIngredient ItemIngredients}
     * @param mirrored Whether this recipe is mirrored
     */
    public ShapedCraftingRecipe(ItemStack output, Optional<ItemIngredient>[][] ingredients, boolean mirrored) {
        this(output, (grid, tagged) -> Optional.of(output), ingredients, mirrored);
    }

    /**
     * Defines an advanced crafting recipe, using a 2D ingredients array.
     *
     * @param recipeFunction {@link RecipeFunction}
     * @param ingredients {@link ItemIngredient ItemIngredients}
     * @param mirrored Whether this recipe is mirrored
     */
	public ShapedCraftingRecipe(ItemStack nominalOutput, RecipeFunction recipeFunction, Optional<ItemIngredient>[][] ingredients, boolean mirrored) {
        this.nominalOutput = nominalOutput;

		int numIngredients = 0;
		for (Optional<ItemIngredient>[] row : ingredients) {
			for (Optional<ItemIngredient> ingredient : row) {
				if (ingredient.isPresent())
					numIngredients++;
			}
		}

        if (numIngredients == 0)
            throw new IllegalArgumentException("Recipe has no ingredients");

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
            ItemStack original = mapping.itemStacks[i];
            ItemStack consumed = ingredients[i].consumeOnCrafting(original, craftingGrid);

            // -- only works if ItemStack is immutable
            //if (original == consumed)
            //    continue;

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

    @Override
    public Optional<ItemStack> getNominalOutput() {
        return Optional.of(nominalOutput);
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
