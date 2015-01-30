/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nova.core.recipes;

/**
 *
 * @author Stan Hebben
 * @param <T> recipe type
 */
public class RecipeAddedEvent<T extends IRecipe>
{
	private final T recipe;
	
	public RecipeAddedEvent(T recipe)
	{
		this.recipe = recipe;
	}
	
	public T getRecipe()
	{
		return recipe;
	}
}
