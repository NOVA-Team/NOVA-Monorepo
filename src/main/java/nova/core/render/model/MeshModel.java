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
 */package nova.core.render.model;

import nova.core.render.texture.Texture;
import nova.core.util.math.MatrixStack;
import nova.core.util.math.TransformUtil;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A vertex model takes faces, which contain vertices and draws them.
 *
 * @author Calclavia
 */
public class MeshModel extends Model {

	/**
	 * A list of all the faces to be drawn.
	 */
	public final Set<Face> faces = new HashSet<>();
	public Vector2D textureOffset = Vector2D.ZERO;

	public MeshModel() {
	}

	public MeshModel(String name) {
		super(name);
	}

	/**
	 * Binds all the faces and all child models with this texture.
	 *
	 * @param texture The texture
	 */
	public void bind(Texture texture) {
		faces.forEach(f -> f.texture = Optional.of(texture));
	}

	/**
	 * Binds the texture to the model, and all its children.
	 *
	 * @param texture to be used to as for this model and sub-models.
	 */
	public void bindAll(Texture texture) {
		bind(texture);
		stream()
			.filter(m -> m instanceof MeshModel)
			.map(m -> (MeshModel) m)
			.forEach(m -> m.bindAll(texture));
	}

	/**
	 * Finish drawing the {@link Face} by adding it into the list of faces.
	 *
	 * @param Face - The finished masterpiece.
	 */
	public void drawFace(Face Face) {
		faces.add(Face);
	}

	@Override
	public Set<Model> flatten(MatrixStack matrixStack) {
		Set<Model> models = new HashSet<>();

		matrixStack.pushMatrix();
		matrixStack.transform(matrix.getMatrix());
		//Create a new model with transformation applied.
		MeshModel transformedModel = clone();
		// correct formula for Normal Matrix is transpose(inverse(mat3(model_mat))
		// we have to augemnt that to 4x4
		RealMatrix normalMatrix3x3 = new LUDecomposition(matrixStack.getMatrix().getSubMatrix(0, 2, 0, 2), 1e-5).getSolver().getInverse().transpose();
		RealMatrix normalMatrix = MatrixUtils.createRealMatrix(4, 4);
		normalMatrix.setSubMatrix(normalMatrix3x3.getData(), 0, 0);
		normalMatrix.setEntry(3, 3, 1);

		transformedModel.faces.stream().forEach(f -> {
				f.normal = TransformUtil.transform(f.normal, normalMatrix);
				f.vertices.forEach(v -> {
					v.vec = matrixStack.apply(v.vec);
					v.normal = v.normal.map(n -> TransformUtil.transform(n, normalMatrix));
				});
			}
		);

		models.add(transformedModel);
		//Flatten child models
		models.addAll(children.stream().flatMap(m -> m.flatten(matrixStack).stream()).collect(Collectors.toSet()));
		matrixStack.popMatrix();
		return models;
	}

	@Override
	protected MeshModel newModel(String name) {
		return new MeshModel(name);
	}

	@Override
	public MeshModel clone() {
		MeshModel model = (MeshModel) super.clone();
		model.faces.addAll(faces.stream().map(Face::clone).collect(Collectors.toSet()));
		return model;
	}

	@Override
	public String toString() {
		return "VertexModel['" + name + "', " + faces.size() + " faces, " + children.size() + " children]";
	}
}
