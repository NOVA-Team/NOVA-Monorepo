package nova.wrapper.mc18.wrapper.render;

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
import nova.wrapper.mc18.render.RenderUtility;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Calclavia
 */
public class SmartBlockModel implements ISmartBlockModel, IFlexibleBakedModel {

	private final Model model;

	public SmartBlockModel(Model model) {
		this.model = model;
	}

	@Override
	public IBakedModel handleBlockState(IBlockState state) {
		return this;
	}

	@Override
	public VertexFormat getFormat() {
		return null;
	}

	@Override
	public List getFaceQuads(EnumFacing p_177551_1_) {
		return Collections.emptyList();
	}

	@Override
	public List getGeneralQuads() {
		List<BakedQuad> quads = new LinkedList<>();
		Set<Model> flatten = model.flatten();
		flatten.stream()
			.map(
				model ->
					model.faces
						.stream()
						.map(face -> BWModel.vertexToInts(face.vertices.stream(), RenderUtility.instance.getTexture(face.texture.get())))
			);

		return quads;
	}

	@Override
	public boolean isAmbientOcclusion() {
		return false;
	}

	@Override
	public boolean isGui3d() {
		return false;
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
		return null;
	}
}
