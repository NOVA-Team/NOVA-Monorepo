/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nova.core.recipes;

import com.google.inject.Singleton;
import java.io.Closeable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import nova.core.event.EventListener;
import nova.core.event.EventListenerHandle;
import nova.core.event.EventListenerList;

/**
 *
 * @author Stan Hebben
 */
@Singleton
public class RecipeManager {
	
	private final Set<IRecipe> recipes;
	private final Map<Class<? extends IRecipe>, RecipeList<IRecipe>> recipesForType;
	
	public RecipeManager() {
		recipes = new HashSet<>();
		recipesForType = new HashMap<>();
	}
	
	public void addRecipe(IRecipe recipe) {
		recipes.add(recipe);
		
		recipesForType
				.keySet()
				.stream()
				.filter(cls -> cls.isInstance(recipe))
				.forEach(cls -> recipesForType.get(cls).add(recipe));
	}
	
	public void removeRecipe(IRecipe recipe) {
		recipes.remove(recipe);
		
		recipesForType.values().forEach(entry -> entry.remove(recipe));
	}
	
	public <T extends IRecipe> Collection<T> getRecipes(Class<T> type) {
		return getRecipeList(type).unmodifyableRecipes;
	}
	
	public <T extends IRecipe> EventListenerHandle addRecipeAddedListener(
			Class<T> type,
			EventListener<RecipeAddedEvent<T>> listener) {
		return getRecipeList(type).recipeAddedListeners.add(listener);
	}
	
	public <T extends IRecipe> void removeRecipeAddedListener(
			Class<T> type,
			EventListener<RecipeAddedEvent<T>> listener) {
		getRecipeList(type).recipeAddedListeners.remove(listener);
	}
	
	public <T extends IRecipe> EventListenerHandle addRecipeRemovedListener(
			Class<T> type,
			EventListener<RecipeRemovedEvent<T>> listener) {
		return getRecipeList(type).recipeRemovedListeners.add(listener);
	}
	
	public <T extends IRecipe> void removeRecipeRemovedListener(
			Class<T> type,
			EventListener<RecipeRemovedEvent<T>> listener) {
		getRecipeList(type).recipeRemovedListeners.remove(listener);
	}
	
	@SuppressWarnings("unchecked")
	private <T extends IRecipe> RecipeList<T> getRecipeList(Class<T> type) {
		if (!recipesForType.containsKey(type))
			recipesForType.put(type, (RecipeList<IRecipe>) collectRecipes(type));
		
		return (RecipeList<T>) recipesForType.get(type);
	}
	
	// #######################
	// ### Private methods ###
	// #######################
	
	@SuppressWarnings("unchecked")
	private <T extends IRecipe> RecipeList<T> collectRecipes(Class<T> type) {
		Set<T> result = new HashSet<>();
		
		recipes.stream()
				.filter(recipe -> type.isInstance(recipe))
				.forEach(recipe -> result.add((T) recipe));
		
		return new RecipeList<>(result);
	}
	
	private class RecipeList<T extends IRecipe>
	{
		private Set<T> recipes;
		private Set<T> unmodifyableRecipes;
		private EventListenerList<RecipeAddedEvent<T>> recipeAddedListeners;
		private EventListenerList<RecipeRemovedEvent<T>> recipeRemovedListeners;
		
		private RecipeList(Set<T> recipes) {
			this.recipes = recipes;
			this.unmodifyableRecipes = Collections.unmodifiableSet(recipes);
			this.recipeAddedListeners = new EventListenerList<>();
			this.recipeRemovedListeners = new EventListenerList<>();
		}
		
		private void add(T recipe) {
			if (recipes.add(recipe))
				recipeAddedListeners.publish(new RecipeAddedEvent<>(recipe));
		}
		
		private void remove(T recipe) {
			if (recipes.remove(recipe))
				recipeRemovedListeners.publish(new RecipeRemovedEvent<>(recipe));
		}
	}
}
