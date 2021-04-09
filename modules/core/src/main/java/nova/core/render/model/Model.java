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

package nova.core.render.model;

import nova.core.util.collection.TreeNode;
import nova.core.util.math.MatrixStack;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A model is a 3D object capable of taking transformations.
 *
 * @author Calclavia
 */
public abstract class Model extends TreeNode<Model> implements Cloneable {

	/**
	 * The name of the model
	 */
	public final String name;

	/**
	 * The transformation matrix.
	 */
	public MatrixStack matrix = new MatrixStack();

	//TODO: There should be a better method to handle this.
	//GL Blending
	public int blendSFactor = -1;
	public int blendDFactor = -1;

	/**
	 * Creates named model.
	 *
	 * @param name to be used.
	 */
	public Model(String name) {
		this.name = Objects.requireNonNull(name, "Model name cannot be null!");
	}

	/**
	 * Creates unnamed model.
	 */
	public Model() {
		this("");
	}

	public Set<Model> flatten() {
		return flatten(new MatrixStack());
	}

	/**
	 * Flattens the model into a set of models with no additional transformations,
	 * applying all the transformations into the individual vertices.
	 *
	 * @param matrixStack transformation matrix.
	 * @return Resulting set of models
	 */
	public abstract Set<Model> flatten(MatrixStack matrixStack);

	/**
	 * Combines child models with names into one model with its children being the children selected.
	 *
	 * @param newModelName The new name for the model
	 * @param names The names of the child models
	 * @return The new model containing all the children.
	 */
	public Model combineChildren(String newModelName, String... names) {
		return combineChildren(newModelName, m -> Arrays.asList(names).contains(m.name));
	}

	/**
	 * Combines child models with names into one model with its children being the children selected.
	 *
	 * @param newModelName The new name for the model
	 * @param predicate The condition to select children
	 * @return The new model containing all the children.
	 */
	public Model combineChildren(String newModelName, Predicate<Model> predicate) {
		Model newModel = newModel(newModelName);

		Set<Model> combineChildren = children
			.stream()
			.filter(predicate)
			.collect(Collectors.toSet());

		newModel.children.addAll(combineChildren);
		children.removeAll(combineChildren);
		children.add(newModel);
		return newModel;
	}

	protected abstract Model newModel(String name);

	@Override
	public Model clone() {
		Model model = newModel(name);
		model.children.addAll(stream().map(Model::clone).collect(Collectors.toSet()));
		model.matrix = new MatrixStack(matrix);
		return model;
	}


	@Override
	public String toString() {
		return "Model['" + name + "', " + children.size() + " children]";
	}
}
