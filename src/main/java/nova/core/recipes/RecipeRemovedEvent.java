package nova.core.recipes;

/**
 * A recipe removed event is fired when a recipe of the right type has been
 * removed from the RecipeManager.
 *
 * @param <T> recipe type
 * @author Stan Hebben
 */
public class RecipeRemovedEvent<T extends Recipe> {
	private final T recipe;

	public RecipeRemovedEvent(T recipe) {
		this.recipe = recipe;
	}

	public T getRecipe() {
		return recipe;
	}
}
