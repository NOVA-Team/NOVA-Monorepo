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

package nova.core.render.pipeline;

import nova.core.block.Block;
import nova.core.component.transform.BlockTransform;
import nova.core.render.model.Face;
import nova.core.render.model.MeshModel;
import nova.core.render.model.Model;
import nova.core.render.texture.Texture;
import nova.core.util.Direction;
import nova.core.util.math.RotationUtil;
import nova.core.util.shape.Cuboid;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ConnectedTextureRenderPipeline extends BlockRenderPipeline {

	public final Block block;
	public final Texture edgeTexture;

	/**
	 * The mask the represents which sides the block should render its connected texture.
	 * E.g: 000000 will render all directions
	 */
	public Supplier<Integer> connectMask;

	/**
	 * A mask of which sides the connected texture renderer should render.
	 * Each bit corresponds to a direction.
	 * E.g: 000011 will render top and bottom
	 */
	public Predicate<Direction> faceMask = dir -> true;

	public ConnectedTextureRenderPipeline(Block block, Texture edgeTexture) {
		super(block);
		this.block = block;
		this.edgeTexture = edgeTexture;

		connectMask = () -> {
			if (this.block.components.has(BlockTransform.class)) {
				return Arrays.stream(Direction.VALID_DIRECTIONS)
					.filter(d -> this.block.world().getBlock(this.block.position().add(d.toVector())).get().sameType(this.block))
					.map(d -> 1 << d.ordinal())
					.reduce(0, (b, a) -> a | b);
			}
			return 0x0;
		};

		consumer = model -> {
			//Render the block face
			MeshModel vModel = new MeshModel();
			draw(vModel);
			model.addChild(vModel);

			//Render the block edge
			for (Direction dir : Direction.VALID_DIRECTIONS)
				if (faceMask.test(dir)) {
					renderFace(dir, model);
				}
		};
	}

	public ConnectedTextureRenderPipeline withConnectMask(Supplier<Integer> connectMask) {
		this.connectMask = connectMask;
		return this;
	}

	/**
	 * Set the mask used to determine if this pipeline should handle these sides.
	 *
	 * @param faceMask A mask of which sides the connected texture renderer should render.
	 * @return this
	 */
	public ConnectedTextureRenderPipeline withFaceMask(Predicate<Direction> faceMask) {
		this.faceMask = faceMask;
		return this;
	}

	/**
	 * Set the mask used to determine if this pipeline should handle these sides.
	 *
	 * @param faceMask A mask of which sides the connected texture renderer should render.
	 * @return this
	 */
	public ConnectedTextureRenderPipeline withFaceMask(Direction... faceMask) {
		this.faceMask = dir -> Arrays.stream(faceMask).anyMatch(d -> d == dir);
		return this;
	}

	/**
	 * Set the mask used to determine if this pipeline should handle these sides.
	 *
	 * @param faceMask A mask of which sides the connected texture renderer should render.
	 * Each bit corresponds to a direction.
	 * E.g: 000011 will render top and bottom
	 * @return this
	 */
	public ConnectedTextureRenderPipeline withFaceMask(int faceMask) {
		this.faceMask = dir -> (faceMask & (1 << dir.ordinal())) != 0;
		return this;
	}

	//Apply connected texture on top face
	protected void renderFace(Direction direction, Model model) {
		for (int r = 0; r < 4; r++) {
			Cuboid bound = bounds.get()
				.subtract(0.5) //Correct translation
				.add(direction.toVector().scalarMultiply(r * 0.0001d)); //Lift up texture slightly, preventing z-fighting

			Direction absDir = Direction.fromOrdinal(RotationUtil.rotateSide(direction.opposite().ordinal(), r));

			int mask = connectMask.get();
			if ((mask & (1 << absDir.ordinal())) == 0) {
				MeshModel innerModel = new MeshModel();
				innerModel.matrix.rotate(direction.toVector(), Math.PI / 2 * r);
				Face face = drawDir(direction, innerModel, bound.min.getX(), bound.min.getY(), bound.min.getZ(), bound.max.getX(), bound.max.getY(), bound.max.getZ(), StaticCubeTextureCoordinates.instance);
				face.texture = Optional.of(edgeTexture);
				//TODO: Support colors
				model.children.add(innerModel);
			}
		}
	}
}