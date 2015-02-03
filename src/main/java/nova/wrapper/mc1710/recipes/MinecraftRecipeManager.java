package nova.wrapper.mc1710.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import nova.core.recipes.RecipeManager;
import nova.core.recipes.crafting.CraftingRecipe;
import nova.core.recipes.crafting.ItemIngredient;
import nova.core.recipes.crafting.ShapedCraftingRecipe;
import nova.wrapper.mc1710.util.WrapUtility;

import java.util.List;
import java.util.Optional;

/**
 * Created by Stan on 1/02/2015.
 */
public class MinecraftRecipeManager extends RecipeManager{

    public MinecraftRecipeManager() {
        List<IRecipe> recipes = (List<IRecipe>) CraftingManager.getInstance().getRecipeList();
        for (IRecipe recipe : recipes) {
            CraftingRecipe converted = convert(recipe);
            if (converted != null)
                addRecipe(converted);
        }
    }

    private CraftingRecipe convert(IRecipe recipe) {
        if (recipe.getClass() == ShapedRecipes.class)
            return convertFromShapedRecipes((ShapedRecipes) recipe);
        else if (recipe.getClass() == ShapelessRecipes.class) {
            return null;
        } else if (recipe.getClass() == ShapedOreRecipe.class) {
            return null;
        } else if (recipe.getClass() == ShapelessOreRecipe.class) {
            return null;
        } else {
            return null;
        }
    }

    private CraftingRecipe convertFromShapedRecipes(ShapedRecipes recipe) {
        Optional<ItemIngredient>[][] ingredients = new Optional[recipe.recipeHeight][recipe.recipeWidth];
        for (int x = 0; x < recipe.recipeWidth; x++) {
            for (int y = 0; y < recipe.recipeHeight; y++) {
                ItemStack stack = recipe.recipeItems[y * recipe.recipeWidth + x];
                if (stack == null)
                    ingredients[y][x] = Optional.empty();
                else
                    ingredients[y][x] = Optional.of(new MinecraftItemIngredient(stack));
            }
        }

        return new ShapedCraftingRecipe(WrapUtility.unwrapItemStack(recipe.getRecipeOutput()), ingredients, false);
    }
}
