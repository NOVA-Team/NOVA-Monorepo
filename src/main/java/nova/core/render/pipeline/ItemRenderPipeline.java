/*
 * Copyright (c) 2017 NOVA, All rights reserved.
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

package nova.core.render.pipeline;

import nova.core.component.ComponentProvider;
import nova.core.render.Color;
import nova.core.render.model.Face;
import nova.core.render.model.MeshModel;
import nova.core.render.model.Vertex;
import nova.core.render.texture.Texture;
import nova.core.util.Direction;
import nova.core.util.math.Vector2DUtil;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

/**
 * An item rendering builder that generates a function that renders an item model.
 *
 * @author ExE Boss
 */
public class ItemRenderPipeline extends RenderPipeline {

	public final ComponentProvider<?> componentProvider;

	/**
	 * Called to get the texture of this item.
	 * Returns - An optional of the texture.
	 */
	public Supplier<Optional<Texture>> texture = () -> Optional.empty();

	/**
	 * Called to get the size of this item to be rendered.
	 * Defaults to (1, 1).
	 */
	public Supplier<Vector2D> size;

	/**
	 * Gets the color of the item. This is called by the default item renderer.
	 *
	 * Returns the color
	 */
	public Supplier<Color> colorMultiplier = () -> Color.white;

	public ItemRenderPipeline(ComponentProvider<?> componentProvider) {
		this.componentProvider = componentProvider;
		size = () -> new Vector2D(1, 1);
		consumer = model -> model.addChild(draw(new MeshModel()));
	}

	/**
	 * This method is called to specify a texture to use for the item.
	 *
	 * @param texture The {@link nova.core.render.texture.Texture} for the item.
	 * @return this
	 */
	public ItemRenderPipeline withTexture(Supplier<Optional<Texture>> texture) {
		this.texture = texture;
		return this;
	}

	/**
	 * This method is called to specify a texture to use for the item.
	 *
	 * @param texture The {@link nova.core.render.texture.Texture} to use for all sides.
	 * @return this
	 */
	public ItemRenderPipeline withTexture(Texture texture) {
		Objects.requireNonNull(texture, "Texture is null, please initiate the texture before the item");
		this.texture = () -> Optional.of(texture);
		return this;
	}

	/**
	 * This method is called to specify the size of the item, defaults to a 1×1 square.
	 *
	 * @param size A supplier that returns
	 * the {@link org.apache.commons.math3.geometry.euclidean.twod.Vector2D} which specifies the item size.
	 * @return this
	 */
	public ItemRenderPipeline withSize(Supplier<Vector2D> size) {
		this.size = size;
		return this;
	}

	/**
	 * This method is called to specify the size of the item, defaults to a 1×1 square.
	 *
	 * @param size The {@link org.apache.commons.math3.geometry.euclidean.twod.Vector2D} which specifies the item size.
	 * @return this
	 */
	public ItemRenderPipeline withSize(Vector2D size) {
		this.size = () -> size;
		return this;
	}

	/**
	 * This method is called to specify a color multiplier to use for the item.
	 *
	 *
	 * @param colorMultiplier A supplier that returns
	 * the {@link nova.core.render.Color} multiplier for the item.
	 * @return this
	 */
	public ItemRenderPipeline withColor(Supplier<Color> colorMultiplier) {
		this.colorMultiplier = colorMultiplier;
		return this;
	}

	/**
	 * This method is called to specify a color multiplier to use for the item.
	 *
	 * @param colorMultiplier The {@link nova.core.render.Color} multiplier for the item.
	 * @return this
	 */
	public ItemRenderPipeline withColor(Color colorMultiplier) {
		this.colorMultiplier = () -> colorMultiplier;
		return this;
	}

	public MeshModel draw(MeshModel model) {
		Vector2D size = this.size.get();
		double minX = -size.getX() / 2;
		double minY = -size.getY() / 2;
		double minZ = -0.5 / 16;
		double maxX = size.getX() / 2;
		double maxY = size.getY() / 2;
		double maxZ = 0.5 / 16;

		Color color = colorMultiplier.get();
		Optional<Texture> texture = this.texture.get();
		Face face;
		Set<Face> faces;

		face = drawFront(model, minX, minY, minZ, maxX, maxY, maxZ, texture);
		face.vertices.forEach(v -> v.color = color);
		face = drawBack(model, minX, minY, minZ, maxX, maxY, maxZ, texture);
		face.vertices.forEach(v -> v.color = color);
		faces = drawUpAndDown(model, minX, minY, minZ, maxX, maxY, maxZ, texture);
		faces.stream().flatMap(f -> f.vertices.stream()).forEach(v -> v.color = color);
		faces = drawLeftAndRight(model, minX, minY, minZ, maxX, maxY, maxZ, texture);
		faces.stream().flatMap(f -> f.vertices.stream()).forEach(v -> v.color = color);

		return model;
	}

	public static Face drawBack(
		MeshModel model,
		double minX, double minY, double minZ,
		double maxX, double maxY, double maxZ,
		Optional<Texture> texture) {

		Vector2D minUV;
		Vector2D maxUV;

		if (texture.isPresent()) {
			minUV = texture.get().minUV;
			maxUV = texture.get().maxUV;
		} else {
			minUV = Vector2D.ZERO;
			maxUV = Vector2DUtil.ONE;
		}

		Face back = new Face();
		back.texture = texture;
		back.normal = Direction.NORTH.toVector();
		//Top-left corner
		back.drawVertex(new Vertex(minX, maxY, minZ, maxUV.getX(), maxUV.getY()));
		//Top-right corner
		back.drawVertex(new Vertex(maxX, maxY, minZ, minUV.getX(), maxUV.getY()));
		//Bottom-right corner
		back.drawVertex(new Vertex(maxX, minY, minZ, minUV.getX(), minUV.getY()));
		//Bottom-left corner
		back.drawVertex(new Vertex(minX, minY, minZ, maxUV.getX(), minUV.getY()));
		model.drawFace(back);

		return back;
	}

	public static Face drawFront(
		MeshModel model,
		double minX, double minY, double minZ,
		double maxX, double maxY, double maxZ,
		Optional<Texture> texture) {

		Vector2D minUV;
		Vector2D maxUV;

		if (texture.isPresent()) {
			minUV = texture.get().minUV;
			maxUV = texture.get().maxUV;
		} else {
			minUV = Vector2D.ZERO;
			maxUV = Vector2DUtil.ONE;
		}

		Face front = new Face();
		front.texture = texture;
		front.normal = Direction.SOUTH.toVector();
		//Bottom-left corner
		front.drawVertex(new Vertex(minX, minY, maxZ, maxUV.getX(), minUV.getY()));
		//Bottom-right corner
		front.drawVertex(new Vertex(maxX, minY, maxZ, minUV.getX(), minUV.getY()));
		//Top-right corner
		front.drawVertex(new Vertex(maxX, maxY, maxZ, minUV.getX(), maxUV.getY()));
		//Top-left corner
		front.drawVertex(new Vertex(minX, maxY, maxZ, maxUV.getX(), maxUV.getY()));
		model.drawFace(front);

		return front;
	}

	public static Set<Face> drawUpAndDown(
		MeshModel model,
		double minX, double minY, double minZ,
		double maxX, double maxY, double maxZ,
		Optional<Texture> texture) {

		Vector2D minUV;
		Vector2D maxUV;
		Vector2D dimensions;

		if (texture.isPresent()) {
			minUV = texture.get().minUV;
			maxUV = texture.get().maxUV;
			dimensions = texture.get().dimension;
		} else {
			minUV = Vector2D.ZERO;
			maxUV = Vector2DUtil.ONE;
			dimensions = new Vector2D(1, 1);
		}

		Set<Face> faces = new HashSet<>();

		double pixelHeight = (maxUV.getY() - minUV.getY()) / dimensions.getY();
		double voxelHeight = Math.abs(maxY - minY) / dimensions.getY();

		for (int i = 0; i < dimensions.getY(); i++) {
			Face up = new Face();
			up.texture = texture;
			up.normal = Direction.UP.toVector();
			//Bottom-left corner
			up.drawVertex(new Vertex(maxX, interpolate(minY, maxY, i + 1, voxelHeight), minZ, minUV.getX(), interpolate(minUV.getY(), maxUV.getY(), ((int)dimensions.getY()) - i - 1, pixelHeight)));
			//Bottom-right corner
			up.drawVertex(new Vertex(minX, interpolate(minY, maxY, i + 1, voxelHeight), minZ, maxUV.getX(), interpolate(minUV.getY(), maxUV.getY(), ((int)dimensions.getY()) - i - 1, pixelHeight)));
			//Top-right corner
			up.drawVertex(new Vertex(minX, interpolate(minY, maxY, i + 1, voxelHeight), maxZ, maxUV.getX(), interpolate(minUV.getY(), maxUV.getY(), ((int)dimensions.getY()) - i, pixelHeight)));
			//Top-left corner
			up.drawVertex(new Vertex(maxX, interpolate(minY, maxY, i + 1, voxelHeight), maxZ, minUV.getX(), interpolate(minUV.getY(), maxUV.getY(), ((int)dimensions.getY()) - i, pixelHeight)));
			model.drawFace(up);
			faces.add(up);

			Face down = new Face();
			down.texture = texture;
			down.normal = Direction.DOWN.toVector();
			//Top-left corner
			down.drawVertex(new Vertex(maxX, interpolate(minY, maxY, i, voxelHeight), maxZ, minUV.getX(), interpolate(minUV.getY(), maxUV.getY(), ((int)dimensions.getY()) - i, pixelHeight)));
			//Top-right corner
			down.drawVertex(new Vertex(minX, interpolate(minY, maxY, i, voxelHeight), maxZ, maxUV.getX(), interpolate(minUV.getY(), maxUV.getY(), ((int)dimensions.getY()) - i, pixelHeight)));
			//Bottom-right corner
			down.drawVertex(new Vertex(minX, interpolate(minY, maxY, i, voxelHeight), minZ, maxUV.getX(), interpolate(minUV.getY(), maxUV.getY(), ((int)dimensions.getY()) - i - 1, pixelHeight)));
			//Bottom-left corner
			down.drawVertex(new Vertex(maxX, interpolate(minY, maxY, i, voxelHeight), minZ, minUV.getX(), interpolate(minUV.getY(), maxUV.getY(), ((int)dimensions.getY()) - i - 1, pixelHeight)));
			model.drawFace(down);
			faces.add(down);
		}

		return faces;
	}

	public static Set<Face> drawLeftAndRight(
		MeshModel model,
		double minX, double minY, double minZ,
		double maxX, double maxY, double maxZ,
		Optional<Texture> texture) {

		Vector2D minUV;
		Vector2D maxUV;
		Vector2D dimensions;

		if (texture.isPresent()) {
			minUV = texture.get().minUV;
			maxUV = texture.get().maxUV;
			dimensions = texture.get().dimension;
		} else {
			minUV = Vector2D.ZERO;
			maxUV = Vector2DUtil.ONE;
			dimensions = new Vector2D(1, 1);
		}

		Set<Face> faces = new HashSet<>();

		double pixelWidth = (maxUV.getX() - minUV.getX()) / dimensions.getX();
		double voxelWidth = Math.abs(maxX - minX) / dimensions.getX();


		for (int i = 0; i < dimensions.getX(); i++) {
			Face left = new Face();
			left.texture = texture;
			left.normal = Direction.WEST.toVector();
			//Bottom-left corner
			left.drawVertex(new Vertex(interpolate(minX, maxX, i, voxelWidth), minY, minZ, interpolate(minUV.getX(), maxUV.getX(), i, pixelWidth), minUV.getY()));
			//Bottom-right corner
			left.drawVertex(new Vertex(interpolate(minX, maxX, i, voxelWidth), minY, maxZ, interpolate(minUV.getX(), maxUV.getX(), i + 1, pixelWidth), minUV.getY()));
			//Top-right corner
			left.drawVertex(new Vertex(interpolate(minX, maxX, i, voxelWidth), maxY, maxZ, interpolate(minUV.getX(), maxUV.getX(), i + 1, pixelWidth), maxUV.getY()));
			//Top-left corner
			left.drawVertex(new Vertex(interpolate(minX, maxX, i, voxelWidth), maxY, minZ, interpolate(minUV.getX(), maxUV.getX(), i, pixelWidth), maxUV.getY()));
			model.drawFace(left);
			faces.add(left);

			Face right = new Face();
			right.texture = texture;
			right.normal = Direction.EAST.toVector();
			//Top-left corner
			right.drawVertex(new Vertex(interpolate(minX, maxX, i + 1, voxelWidth), maxY, minZ, interpolate(minUV.getX(), maxUV.getX(), i, pixelWidth), maxUV.getY()));
			//Top-right corner
			right.drawVertex(new Vertex(interpolate(minX, maxX, i + 1, voxelWidth), maxY, maxZ, interpolate(minUV.getX(), maxUV.getX(), i + 1, pixelWidth), maxUV.getY()));
			//Bottom-right corner
			right.drawVertex(new Vertex(interpolate(minX, maxX, i + 1, voxelWidth), minY, maxZ, interpolate(minUV.getX(), maxUV.getX(), i + 1, pixelWidth), minUV.getY()));
			//Bottom-left corner
			right.drawVertex(new Vertex(interpolate(minX, maxX, i + 1, voxelWidth), minY, minZ, interpolate(minUV.getX(), maxUV.getX(), i, pixelWidth), minUV.getY()));
			model.drawFace(right);
			faces.add(right);
		}

		return faces;
	}

	private static double interpolate(double min, double max, int index, double indexSize) {
		if (indexSize > 0) {
			if (max > min) {
				return min + indexSize * index;
			} else if (max < min) {
				return max + indexSize * index;
			}
		} else if (indexSize < 0) {
			if (max > min) {
				return min - indexSize * index;
			} else if (max < min) {
				return max - indexSize * index;
			}
		}

		return min == max ? min : max;
	}
}
