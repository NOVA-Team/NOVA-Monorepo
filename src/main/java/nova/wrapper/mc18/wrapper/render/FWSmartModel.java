package nova.wrapper.mc18.wrapper.render;

import com.google.common.primitives.Ints;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import nova.core.render.model.Model;
import nova.core.render.model.Vertex;
import nova.core.util.Direction;
import nova.wrapper.mc18.render.RenderUtility;

import javax.vecmath.Vector3f;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Generates a smart model based on a NOVA Model
 * @author Calclavia
 */
public abstract class FWSmartModel implements IFlexibleBakedModel {

	protected final VertexFormat format;
	// Default item transforms. Can be changed in subclasses.
	protected ItemCameraTransforms itemCameraTransforms = ItemCameraTransforms.DEFAULT;

	public FWSmartModel() {
		this.format = new VertexFormat();
	}

	protected List<BakedQuad> modelToQuads(Model modelIn) {
		return modelIn
			.flatten()
			.stream()
			.flatMap(
				model ->
					model.faces
						.stream()
						.map(
							face -> {
								List<int[]> vertexData = face.vertices
									.stream()
									.map(v -> vertexToInts(v, RenderUtility.instance.getTexture(face.texture.get())))
									.collect(Collectors.toList());

								int[] data = Ints.concat(vertexData.toArray(new int[][] {}));
								//TODO: The facing might be wrong
								return new BakedQuad(data, -1, EnumFacing.values()[Direction.fromVector(face.normal).ordinal()]);
							}
						)
			)
			.collect(Collectors.toList());
	}

	public static int[] vertexToInts(Vertex vertex, TextureAtlasSprite texture) {
		return new int[] {
			Float.floatToRawIntBits((float) vertex.vec.getX()),
			Float.floatToRawIntBits((float) vertex.vec.getY()),
			Float.floatToRawIntBits((float) vertex.vec.getZ()),
			vertex.color.rgba(),
			Float.floatToRawIntBits(texture.getInterpolatedU(16 * vertex.uv.getX())),
			Float.floatToRawIntBits(texture.getInterpolatedV(16 * vertex.uv.getY())),
			0
		};
	}

	@Override
	public VertexFormat getFormat() {
		return format;
	}

	@Override
	public List getFaceQuads(EnumFacing p_177551_1_) {
		return Collections.emptyList();
	}

	@Override
	public boolean isAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean isGui3d() {
		return true;
	}

	@Override
	public boolean isBuiltInRenderer() {
		return false;
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return itemCameraTransforms;
	}
}
