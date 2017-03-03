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

package nova.core.wrapper.mc.forge.v18.wrapper.render.backward;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import nova.core.render.Color;
import nova.core.render.model.Face;
import nova.core.render.model.MeshModel;
import nova.core.render.model.Model;
import nova.core.render.model.Vertex;
import nova.core.util.Direction;
import nova.core.util.math.MatrixStack;
import nova.core.util.math.TransformUtil;
import nova.core.util.math.Vector3DUtil;
import nova.core.wrapper.mc.forge.v18.wrapper.assets.AssetConverter;
import nova.core.wrapper.mc.forge.v18.wrapper.render.BWModel;
import nova.internal.core.Game;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ExE Boss
 */
public class BWBakedModel extends BWModel {

	@SuppressWarnings("deprecation")
	public final IBakedModel wrapped;

	public final VertexFormat format;

	public BWBakedModel(@SuppressWarnings("deprecation") IBakedModel wrapped) {
		this(wrapped, DefaultVertexFormats.ITEM);
	}

	public BWBakedModel(@SuppressWarnings("deprecation") IBakedModel wrapped, VertexFormat format) {
		this.wrapped = wrapped;
		this.format = format;
		this.matrix.translate(-0.5, -0.5, -0.5);

		getGeneralQuads().stream()
			.map(this::quadToFace)
			.forEachOrdered(faces::add);

		Arrays.stream(Direction.VALID_DIRECTIONS)
			.map(this::getFaceQuads)
			.flatMap(Collection::stream)
			.map(this::quadToFace)
			.forEachOrdered(faces::add);
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
				f.vertices.forEach(v -> v.vec = matrixStack.apply(v.vec));
			}
		);

		models.add(transformedModel);
		//Flatten child models
		matrixStack.pushMatrix();
		matrixStack.translate(0.5, 0.5, 0.5);
		models.addAll(children.stream().flatMap(m -> m.flatten(matrixStack).stream()).collect(Collectors.toSet()));
		matrixStack.popMatrix().popMatrix();
		return models;
	}

	public List<BakedQuad> getFaceQuads(Direction direction) {
		if (direction == Direction.UNKNOWN)
			return getGeneralQuads();
		return getFaceQuads((EnumFacing) Game.natives().toNative(direction));
	}

	@SuppressWarnings("unchecked")
	public List<BakedQuad> getFaceQuads(EnumFacing direction) {
		return wrapped.getFaceQuads(direction);
	}

	@SuppressWarnings("unchecked")
	public List<BakedQuad> getGeneralQuads() {
		return wrapped.getGeneralQuads();
	}

	public Face quadToFace(BakedQuad quad) {
		// TODO: Parse according to format
		Face face = new Face();
		int[] data = quad.getVertexData();
		List<Vector3D> normals = new LinkedList<>();
		Optional<TextureAtlasSprite> texture = Optional.ofNullable(wrapped.getTexture());
		face.texture = texture.map(TextureAtlasSprite::getIconName).map(ResourceLocation::new).map(AssetConverter.instance()::toNovaTexture);
		for (int i = 0; i < data.length; i += 7) {
			Vector3D pos = new Vector3D(Float.intBitsToFloat(data[i]),
				Float.intBitsToFloat(data[i + 1]), Float.intBitsToFloat(data[i + 2]));
			Vector2D uv = new Vector2D(
				deinterpolateU(Float.intBitsToFloat(data[i + 4]), texture),
				deinterpolateV(Float.intBitsToFloat(data[i + 5]), texture));
			int mergedNormal = data[i + 6];
			Optional<Vector3D> normal = Optional.empty();
			if (mergedNormal != 0)
				normal = Optional.of(new Vector3D(((byte)(mergedNormal & 0xFF)) / 127D,
					((byte)((mergedNormal >> 8) & 0xFF)) / 127D,
					((byte)((mergedNormal >> 16) & 0xFF)) / 127D));

			Vertex vertex = new Vertex(pos, uv);
			if (DefaultVertexFormats.BLOCK.equals(format))
				vertex.color = Color.argb(data[i + 3]);
			else
				vertex.color = Color.rgba(data[i + 3]);

			if (DefaultVertexFormats.ITEM.equals(format))
				vertex.normal = normal;
			face.drawVertex(vertex);
		}
		face.normal = Vector3DUtil.calculateNormal(face);
		return face;
	}

	private double deinterpolateU(double u, Optional<TextureAtlasSprite> texture) {
		if (!texture.isPresent())
			return u;
		TextureAtlasSprite tex = texture.get();
		return (u - tex.getMinU()) / (tex.getMaxU() - tex.getMinU());
	}

	private double deinterpolateV(double v, Optional<TextureAtlasSprite> texture) {
		if (!texture.isPresent())
			return v;
		TextureAtlasSprite tex = texture.get();
		return (v - tex.getMinV()) / (tex.getMaxV() - tex.getMinV());
	}
}
