package nova.core.recipes;

import nova.core.event.EventListener;
import nova.core.event.EventListenerHandle;
import nova.core.event.EventListenerList;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The RecipeManager manages all recipes (of any type) in the game.
 *
 * @author Stan Hebben
 */
public class RecipeManager {

	private final Set<Recipe> recipes;
	private final Map<Class<? extends Recipe>, RecipeList<Recipe>> recipesForType;

	public RecipeManager() {
		recipes = new HashSet<>();
		recipesForType = new HashMap<>();
	}

	public void addRecipe(Recipe recipe) {
		recipes.add(recipe);

		recipesForType
			.keySet()
			.stream()
			.filter(cls -> cls.isInstance(recipe))
			.forEach(cls -> recipesForType.get(cls).add(recipe));
	}

	public void removeRecipe(Recipe recipe) {
		recipes.remove(recipe);

		recipesForType.values().forEach(entry -> entry.remove(recipe));
	}

	public <T extends Recipe> Collection<T> getRecipes(Class<T> type) {
		return getRecipeList(type).unmodifiableRecipes;
	}

	public <T extends Recipe> EventListenerHandle<RecipeAddedEvent<T>>
	addRecipeAddedListener(
		Class<T> type,
		EventListener<RecipeAddedEvent<T>> listener) {
		return getRecipeList(type).recipeAddedListeners.add(listener);
	}

	public <T extends Recipe> void removeRecipeAddedListener(
		Class<T> type,
		EventListener<RecipeAddedEvent<T>> listener) {
		getRecipeList(type).recipeAddedListeners.remove(listener);
	}

	public <T extends Recipe> EventListenerHandle<RecipeRemovedEvent<T>>
	addRecipeRemovedListener(
		Class<T> type,
		EventListener<RecipeRemovedEvent<T>> listener) {
		return getRecipeList(type).recipeRemovedListeners.add(listener);
	}

	public <T extends Recipe> void removeRecipeRemovedListener(
		Class<T> type,
		EventListener<RecipeRemovedEvent<T>> listener) {
		getRecipeList(type).recipeRemovedListeners.remove(listener);
	}

	// #######################
	// ### Private methods ###
	// #######################

	@SuppressWarnings("unchecked")
	private <T extends Recipe> RecipeList<T> getRecipeList(Class<T> type) {
		if (!recipesForType.containsKey(type)) {
			recipesForType.put(type, (RecipeList<Recipe>) collectRecipes(type));
		}

		return (RecipeList<T>) recipesForType.get(type);
	}

	@SuppressWarnings("unchecked")
	private <T extends Recipe> RecipeList<T> collectRecipes(Class<T> type) {
		Set<T> result = new HashSet<>();

		recipes.stream()
			.filter(type::isInstance)
			.forEach(recipe -> result.add((T) recipe));

		return new RecipeList<>(result);
	}

	private class RecipeList<T extends Recipe> {
		private Set<T> recipes;
		private Set<T> unmodifiableRecipes;
		private EventListenerList<RecipeAddedEvent<T>> recipeAddedListeners;
		private EventListenerList<RecipeRemovedEvent<T>> recipeRemovedListeners;

		private RecipeList(Set<T> recipes) {
			this.recipes = recipes;
			this.unmodifiableRecipes = Collections.unmodifiableSet(recipes);
			this.recipeAddedListeners = new EventListenerList<>();
			this.recipeRemovedListeners = new EventListenerList<>();
		}

		private void add(T recipe) {
			if (recipes.add(recipe)) {
				recipeAddedListeners.publish(new RecipeAddedEvent<>(recipe));
			}
		}

		private void remove(T recipe) {
			if (recipes.remove(recipe)) {
				recipeRemovedListeners.publish(new RecipeRemovedEvent<>(recipe));
			}
		}
	}
}
