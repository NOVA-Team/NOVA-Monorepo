package nova.wrapper.mc18.wrapper.render;

import com.google.common.primitives.Ints;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.client.model.ISmartItemModel;
import nova.core.block.Block;
import nova.core.component.renderer.ItemRenderer;
import nova.core.component.renderer.StaticRenderer;
import nova.core.item.ItemBlock;
import nova.core.util.Direction;
import nova.internal.core.Game;
import nova.wrapper.mc18.render.RenderUtility;
import nova.wrapper.mc18.wrapper.block.forward.FWBlock;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Generates a smart model based on a NOVA Model
 * @author Calclavia
 */
public class FWSmartBlockModel extends FWSmartModel implements ISmartBlockModel, ISmartItemModel, IFlexibleBakedModel {

	private final Block block;
	private final boolean isDummy;

	public FWSmartBlockModel(Block block, boolean isDummy) {
		super();
		this.block = block;
		this.isDummy = isDummy;
	}

	//Block rendering
	@Override
	public IBakedModel handleBlockState(IBlockState state) {
		FWBlock block = (FWBlock) state.getBlock();

		Block blockInstance = block.getBlockInstance(block.lastExtendedWorld, Game.natives().toNova(block.lastExtendedStatePos));

		if (blockInstance.has(StaticRenderer.class)) {
			return new FWSmartBlockModel(blockInstance, false);
		}

		return this;
	}

	//Itemblock rendering
	@Override
	public IBakedModel handleItemState(ItemStack stack) {
		ItemBlock item = Game.natives().toNova(stack);
		ItemRenderer renderer = item.has(ItemRenderer.class) ? item.get(ItemRenderer.class) : block.has(ItemRenderer.class) ? block.get(ItemRenderer.class) : null;

		if (renderer != null) {
			return new FWSmartBlockModel(block, true);
		}

		return this;
	}

	@Override
	public List<BakedQuad> getGeneralQuads() {
		BWModel blockModel = new BWModel();
		blockModel.matrix.translate(0.5, 0.5, 0.5);

		if (isDummy) {
			ItemRenderer renderer = block.get(ItemRenderer.class);
			renderer.onRender.accept(blockModel);
		} else {
			StaticRenderer renderer = block.get(StaticRenderer.class);
			renderer.onRender.accept(blockModel);
		}

		return blockModel
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

								int[] data = Ints.concat(vertexData.toArray(new int[][] {}));
								//TODO: The facing might be wrong
								return new BakedQuad(data, -1, EnumFacing.values()[Direction.fromVector(face.normal).ordinal()]);
							}
						)
			)
			.collect(Collectors.toList());
	}
}
