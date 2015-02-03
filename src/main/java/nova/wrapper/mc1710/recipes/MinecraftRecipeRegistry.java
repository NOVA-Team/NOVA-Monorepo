package nova.wrapper.mc1710.recipes;

import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import nova.core.game.Game;
import nova.core.recipes.RecipeAddedEvent;
import nova.core.recipes.RecipeManager;
import nova.core.recipes.RecipeRemovedEvent;
import nova.core.recipes.crafting.CraftingRecipe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Stan on 1/02/2015.
 */
public class MinecraftRecipeRegistry {
    public static final MinecraftRecipeRegistry instance = new MinecraftRecipeRegistry();

    private final Map<CraftingRecipe, IRecipe> forwardWrappers = new HashMap<>();
    private final Map<IRecipe, CraftingRecipe> backwardWrappers = new HashMap<>();

    private MinecraftRecipeRegistry() {}

    public void registerRecipes() {
        long startTime = System.currentTimeMillis();

        RecipeManager recipeManager = Game.instance.get().recipeManager;

        List<IRecipe> recipes = (List<IRecipe>) CraftingManager.getInstance().getRecipeList();
        for (IRecipe recipe : recipes) {
            CraftingRecipe converted = convert(recipe);
            if (converted != null) {
                recipeManager.addRecipe(converted);
                backwardWrappers.put(recipe, converted);
                forwardWrappers.put(converted, recipe);
            }
        }

        System.out.println("Initialized recipes in " + (System.currentTimeMillis() - startTime) + " ms");

        recipeManager.addRecipeAddedListener(CraftingRecipe.class, this::onRecipeAdded);
        recipeManager.addRecipeRemovedListener(CraftingRecipe.class, this::onRecipeRemoved);
    }

    private CraftingRecipe convert(IRecipe recipe) {
        return RecipeConverter.toNova(recipe);
    }

    private IRecipe convert(CraftingRecipe recipe) {
        return RecipeConverter.toMinecraft(recipe);
    }

    private void onRecipeAdded(RecipeAddedEvent<CraftingRecipe> e) {
        CraftingRecipe recipe = e.getRecipe();
        IRecipe minecraftRecipe = convert(recipe);
        CraftingManager.getInstance().getRecipeList().add(minecraftRecipe);

        backwardWrappers.put(minecraftRecipe, recipe);
        forwardWrappers.put(recipe, minecraftRecipe);
    }

    private void onRecipeRemoved(RecipeRemovedEvent<CraftingRecipe> e) {
        IRecipe minecraftRecipe = forwardWrappers.get(e.getRecipe());
        CraftingManager.getInstance().getRecipeList().remove(minecraftRecipe);

        forwardWrappers.remove(e.getRecipe());
        backwardWrappers.remove(minecraftRecipe);
    }
}
