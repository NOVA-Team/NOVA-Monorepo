/*
 * Copyright (c) 2015 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License version 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
 */

package nova.core.recipes;

import nova.core.event.bus.EventBus;
import nova.core.event.bus.EventListener;
import nova.core.event.bus.EventListenerHandle;
import nova.core.util.Manager;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The RecipeManager manages all recipes (of any type) in the game.
 * @author Stan Hebben
 */
public class RecipeManager extends Manager<RecipeManager> {

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

	public <T extends Recipe> EventListenerHandle<RecipeAddedEvent<T>> whenRecipeAdded(
		Class<T> type,
		EventListener<RecipeAddedEvent<T>> listener) {
		return getRecipeList(type).recipeAddedListeners.on().bind(listener);
	}

	public <T extends Recipe> EventListenerHandle<RecipeRemovedEvent<T>> whenRecipeRemoved(
		Class<T> type,
		EventListener<RecipeRemovedEvent<T>> listener) {
		return getRecipeList(type).recipeRemovedListeners.on().bind(listener);
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
		private EventBus<RecipeAddedEvent<T>> recipeAddedListeners;
		private EventBus<RecipeRemovedEvent<T>> recipeRemovedListeners;

		private RecipeList(Set<T> recipes) {
			this.recipes = recipes;
			this.unmodifiableRecipes = Collections.unmodifiableSet(recipes);
			this.recipeAddedListeners = new EventBus<>();
			this.recipeRemovedListeners = new EventBus<>();
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
