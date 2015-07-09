package nova.wrapper.mc18.wrapper.render;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.ISmartItemModel;
import nova.core.component.renderer.ItemRenderer;
import nova.core.item.Item;
import nova.internal.core.Game;
import nova.wrapper.mc18.render.RenderUtility;

import javax.vecmath.Vector3f;
import java.util.Collections;
import java.util.List;

/**
 * Generates a smart model based on a NOVA Model
 *
 * @author Calclavia
 */
public class FWSmartItemModel extends FWSmartModel implements ISmartItemModel, IFlexibleBakedModel {

	private final Item item;

	public FWSmartItemModel(Item item) {
		super();
		this.item = item;
		// Change the default transforms to the default Item transforms
		this.itemCameraTransforms = new ItemCameraTransforms(
			new ItemTransformVec3f(new Vector3f(-90, 0, 0), new Vector3f(0, 1, -3), new Vector3f(0.55f, 0.55f, 0.55f)), // Third Person
			new ItemTransformVec3f(new Vector3f(0, -135, 25), new Vector3f(0, 4, 2), new Vector3f(1.7f, 1.7f, 1.7f)), // First Person
			ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT);
	}

	@Override
	public IBakedModel handleItemState(ItemStack stack) {
		Item item = Game.natives().toNova(stack);

		if (item.has(ItemRenderer.class)) {
			return new FWSmartItemModel(item);
		}

		return this;
	}

	@Override
	public List<BakedQuad> getGeneralQuads() {
		if (item.has(ItemRenderer.class)) {
			BWModel model = new BWModel();
			ItemRenderer renderer = item.get(ItemRenderer.class);
			model.matrix.translate(0.5, 0.5, 0.5);
			renderer.onRender.accept(model);
			return modelToQuads(model);
		}

		return Collections.emptyList();
	}

	@Override
	public TextureAtlasSprite getTexture() {
		if (item.has(ItemRenderer.class)) {
			ItemRenderer itemRenderer = item.get(ItemRenderer.class);
			if (itemRenderer.texture.isPresent()) {
				return RenderUtility.instance.getTexture(itemRenderer.texture.get());
			}
		}

		return null;
	}

	@Override
	public boolean isGui3d() {
		return item.has(ItemRenderer.class);
	}
}
