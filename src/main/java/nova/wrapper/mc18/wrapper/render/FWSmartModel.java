package nova.wrapper.mc18.wrapper.render;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IFlexibleBakedModel;

import java.util.Collections;
import java.util.List;

/**
 * Generates a smart model based on a NOVA Model
 * @author Calclavia
 */
public abstract class FWSmartModel implements IFlexibleBakedModel {

	protected final VertexFormat format;

	public FWSmartModel() {
		this.format = new VertexFormat();
	}
/*
	@Override
	public IBakedModel handleItemState(ItemStack stack) {
		Item item = Game.natives().toNova(stack);

		if (item.has(ItemRenderer.class)) {
			ItemRenderer renderer = item.get(ItemRenderer.class);
			BWModel model = new BWModel();
			renderer.onRender.accept(model);
			return new FWSmartModel(model);
		}

		return this;
	}*/


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
	public TextureAtlasSprite getTexture() {
		return null;
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return ItemCameraTransforms.DEFAULT;
	}
}
