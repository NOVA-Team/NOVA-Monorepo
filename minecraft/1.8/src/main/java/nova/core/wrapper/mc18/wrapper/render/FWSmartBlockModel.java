package nova.core.wrapper.mc18.wrapper.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.client.model.ISmartItemModel;
import nova.core.block.Block;
import nova.core.component.renderer.ItemRenderer;
import nova.core.component.renderer.StaticRenderer;
import nova.core.item.ItemBlock;
import nova.core.wrapper.mc18.render.RenderUtility;
import nova.core.wrapper.mc18.wrapper.block.forward.FWBlock;
import nova.internal.core.Game;

import javax.vecmath.Vector3f;
import java.util.List;

/**
 * Generates a smart model based on a NOVA Model
 *
 * @author Calclavia
 */
public class FWSmartBlockModel extends FWSmartModel implements ISmartBlockModel, ISmartItemModel, IFlexibleBakedModel {

	private final Block block;
	private final boolean isItem;

	public FWSmartBlockModel(Block block, boolean isDummy) {
		super();
		this.block = block;
		this.isItem = isDummy;
		// Change the default transforms to the default full Block transforms
		this.itemCameraTransforms = new ItemCameraTransforms(
			new ItemTransformVec3f(new Vector3f(10, -45, 170), new Vector3f(0, 0.09375f, -0.171875f), new Vector3f(0.375f, 0.375f, 0.375f)), // Third Person
			ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT);
	}

	//Block rendering
	@Override
	public IBakedModel handleBlockState(IBlockState state) {
		FWBlock block = (FWBlock) state.getBlock();

		Block blockInstance = block.getBlockInstance(block.lastExtendedWorld, Game.natives().toNova(block.lastExtendedStatePos));

		if (blockInstance.has(StaticRenderer.class)) {
			return new FWSmartBlockModel(blockInstance, false);
		}

		return new FWEmptyModel();
	}

	//Itemblock rendering
	@Override
	public IBakedModel handleItemState(ItemStack stack) {
		ItemBlock item = Game.natives().toNova(stack);
		ItemRenderer renderer = item.has(ItemRenderer.class) ? item.get(ItemRenderer.class) : block.has(ItemRenderer.class) ? block.get(ItemRenderer.class) : null;

		if (renderer != null) {
			return new FWSmartBlockModel(block, true);
		}

		return new FWEmptyModel();
	}

	@Override
	public List<BakedQuad> getGeneralQuads() {
		BWModel blockModel = new BWModel();
		blockModel.matrix.translate(0.5, 0.5, 0.5);

		if (isItem) {
			ItemRenderer renderer = block.get(ItemRenderer.class);
			renderer.onRender.accept(blockModel);
		} else {
			StaticRenderer renderer = block.get(StaticRenderer.class);
			renderer.onRender.accept(blockModel);
		}

		return modelToQuads(blockModel);
	}

	@Override
	public TextureAtlasSprite getTexture() {
		/*
		if (block.has(StaticRenderer.class)) {
			Optional<Texture> apply = block.get(StaticRenderer.class).texture.apply(Direction.UNKNOWN);
			if (apply.isPresent()) {
				return RenderUtility.instance.getTexture(apply.get());
			}
		}*/

		if (block.has(ItemRenderer.class)) {
			ItemRenderer itemRenderer = block.get(ItemRenderer.class);
			if (itemRenderer.texture.isPresent()) {
				return RenderUtility.instance.getTexture(itemRenderer.texture.get());
			}
		}

		return null;
	}
}
