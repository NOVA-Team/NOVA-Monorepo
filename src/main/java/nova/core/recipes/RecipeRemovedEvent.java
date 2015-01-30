package nova.core.recipes;

/**
 * A recipe removed event is fired when a recipe of the right type has been
 * removed from the RecipeManager.
 * 
 * @author Stan Hebben
 * @param <T> recipe type
 */
public class RecipeRemovedEvent<T extends Recipe>
{
	private final T recipe;
	
	public RecipeRemovedEvent(T recipe)
	{
		this.recipe = recipe;
	}
	
	public T getRecipe()
	{
		return recipe;
	}
}
