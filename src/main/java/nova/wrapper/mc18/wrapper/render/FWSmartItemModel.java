package nova.wrapper.mc18.wrapper.render;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.ISmartItemModel;
import nova.core.component.renderer.ItemRenderer;
import nova.core.item.Item;
import nova.internal.core.Game;

import java.util.List;

/**
 * Generates a smart model based on a NOVA Model
 * @author Calclavia
 */
public class FWSmartItemModel extends FWSmartModel implements ISmartItemModel, IFlexibleBakedModel {

	private final Item item;

	public FWSmartItemModel(Item item) {
		super();
		this.item = item;
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
		BWModel model = new BWModel();
		model.matrix.translate(0.5, 0.5, 0.5);

		ItemRenderer renderer = item.get(ItemRenderer.class);
		renderer.onRender.accept(model);

		return modelToQuads(model);
	}
}
