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

import nova.core.event.RecipeEvent;
import nova.core.event.bus.EventBus;
import nova.core.event.bus.EventListener;
import nova.core.event.bus.EventListenerHandle;
import nova.core.util.registry.Manager;
import nova.internal.core.Game;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.stream.Stream;

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

	/**
	 * Adds a recipe.
	 *
	 * @param recipe The recipe to add.
	 */
	public void addRecipe(Recipe recipe) {
		recipes.add(recipe);

		recipesForType
			.keySet()
			.stream()
			.filter(cls -> cls.isInstance(recipe))
			.forEach(cls -> recipesForType.get(cls).add(recipe));
	}

	/**
	 * Removes a recipe.
	 *
	 * @param recipe The recipe to remove.
	 */
	public void removeRecipe(Recipe recipe) {
		recipes.remove(recipe);

		recipesForType.values().forEach(entry -> entry.remove(recipe));
	}

	/**
	 * Returns an unmodifiable set view of all the recipes for the type.
	 * <p>
	 * To modify the underlying recipe list, you have to use
	 * {@link #addRecipe(Recipe)} and {@link #removeRecipe(Recipe)} instead.
	 *
	 * @param <T> The recipe type
	 * @param type The recipe class
	 * @return An unmodifiable set view of all the recipes for the type.
	 */
	public <T extends Recipe> Set<T> getRecipes(Class<T> type) {
		return getRecipeList(type).unmodifiableRecipes;
	}

	@SuppressWarnings("unchecked")
	public <T extends Recipe> EventListenerHandle<RecipeEvent.Add<T>> whenRecipeAdded(
		Class<T> type, EventListener<RecipeEvent.Add<T>> listener) {
		return getRecipeList(type).events.on(RecipeEvent.Add.class).bind(listener);
	}

	@SuppressWarnings("unchecked")
	public <T extends Recipe> EventListenerHandle<RecipeEvent.Remove<T>> whenRecipeRemoved(
		Class<T> type, EventListener<RecipeEvent.Remove<T>> listener) {
		return getRecipeList(type).events.on(RecipeEvent.Remove.class).bind(listener);
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

	private class RecipeList<T extends Recipe> implements Iterable<T> {
		private Set<T> recipes;
		private Set<T> unmodifiableRecipes;
		private EventBus<RecipeEvent<T>> events;

		private RecipeList(Set<T> recipes) {
			this.recipes = recipes;
			this.unmodifiableRecipes = Collections.unmodifiableSet(recipes);
			this.events = new EventBus<>();
		}

		private void add(T recipe) {
			if (recipes.add(recipe)) {
				events.publish(new RecipeEvent.Add<>(recipe));
			}
		}

		private void remove(T recipe) {
			if (recipes.remove(recipe)) {
				events.publish(new RecipeEvent.Remove<>(recipe));
			}
		}

		@Override
		public Iterator<T> iterator() {
			return this.unmodifiableRecipes.iterator();
		}

		@Override
		public Spliterator<T> spliterator() {
			return this.unmodifiableRecipes.spliterator();
		}

		public Stream<T> stream() {
			return this.unmodifiableRecipes.stream();
		}

		public Stream<T> parallelStream() {
			return this.unmodifiableRecipes.parallelStream();
		}
	}

	@Override
	public void init() {
		Game.events().publish(new Init(this));
	}

	public class Init extends ManagerEvent<RecipeManager> {
		public Init(RecipeManager manager) {
			super(manager);
		}
	}
}
