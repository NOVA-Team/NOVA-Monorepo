package nova.wrapper.mc18.wrapper.render;

import com.google.common.primitives.Ints;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.ISmartBlockModel;
import nova.core.render.model.Model;
import nova.core.util.Direction;
import nova.wrapper.mc18.render.RenderUtility;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Calclavia
 */
public class FWBlockModel implements ISmartBlockModel, IFlexibleBakedModel {

	private final Model model;
	private final VertexFormat format;

	public FWBlockModel(Model model) {
		this.model = model;
		this.format = new VertexFormat();
	}

	@Override
	public IBakedModel handleBlockState(IBlockState state) {
		return this;
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
	public List getGeneralQuads() {
		return model
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
									.map(v -> BWModel.vertexToInts(v, RenderUtility.instance.getTexture(face.texture.get())))
									.collect(Collectors.toList());

								int[] data = Ints.concat((int[][]) vertexData.toArray());
								//TODO: The facing might be wrong
								return new BakedQuad(data, -1, EnumFacing.values()[Direction.fromVector(face.normal).ordinal()]);
							}
						)
			)
			.collect(Collectors.toList());
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
	public TextureAtlasSprite getTexture() {
		return null;
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return ItemCameraTransforms.DEFAULT;
	}
}
