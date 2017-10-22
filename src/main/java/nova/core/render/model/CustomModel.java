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

import nova.core.util.math.MatrixStack;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * A Model where the render method has no specific definition, except that it's a method.
 * Essentially this is a black box model where the definition is unspecified and may depend on the game.
 * The render method will not be called until it is actually being rendered at the instant.
 *
 * @author Calclavia
 */
public class CustomModel extends Model {

	public final Consumer<CustomModel> render;

	public CustomModel(String name, Consumer<CustomModel> render) {
		super(name);
		this.render = render;
	}

	public CustomModel(Consumer<CustomModel> render) {
		this.render = render;
	}

	@Override
	protected CustomModel newModel(String name) {
		return new CustomModel(name, render);
	}

	@Override
	public Set<Model> flatten(MatrixStack matrixStack) {
		Set<Model> models = new HashSet<>();

		matrixStack.pushMatrix();
		matrixStack.transform(matrix.getMatrix());
		//Create a new model with transformation applied.
		Model transformedModel = clone();
		transformedModel.matrix.loadMatrix(matrixStack.getMatrix());
		models.add(transformedModel);

		//Flatten child models
		models.addAll(children.stream().flatMap(m -> m.flatten(matrixStack).stream()).collect(Collectors.toSet()));
		matrixStack.popMatrix();
		return models;
	}
}
