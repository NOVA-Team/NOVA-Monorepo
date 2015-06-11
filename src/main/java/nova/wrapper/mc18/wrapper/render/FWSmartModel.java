package nova.wrapper.mc18.wrapper.render;

import com.google.common.primitives.Ints;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.client.model.ISmartItemModel;
import nova.core.component.renderer.ItemRenderer;
import nova.core.component.renderer.StaticRenderer;
import nova.core.item.Item;
import nova.core.render.model.Model;
import nova.core.util.Direction;
import nova.internal.core.Game;
import nova.wrapper.mc18.render.RenderUtility;
import nova.wrapper.mc18.wrapper.block.forward.FWBlock;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Generates a smart model based on a NOVA Model
 * @author Calclavia
 */
public class FWSmartModel implements ISmartBlockModel, ISmartItemModel, IFlexibleBakedModel {

	private final Model model;
	private final VertexFormat format;

	public FWSmartModel(Model model) {
		this.model = model;
		this.format = new VertexFormat();
	}

	@Override
	public IBakedModel handleBlockState(IBlockState state) {
		FWBlock block = (FWBlock) state.getBlock();

		if (block.block.has(StaticRenderer.class)) {
			StaticRenderer renderer = block.block.get(StaticRenderer.class);
			BWModel model = new BWModel();
			//model.matrix.translate(x + 0.5, y + 0.5, z + 0.5);
			renderer.onRender.accept(model);
			return new FWSmartModel(model);
		}

		return null;
	}

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
	}

	/*

	@Override
	public IIcon getIcon(IBlockAccess access, int x, int y, int z, int side) {
		Block blockInstance = getBlockInstance(access, new Vector3D(pos.getX(), pos.getY(), pos.getZ()));
		Optional<StaticBlockRenderer> opRenderer = blockInstance.getOp(StaticBlockRenderer.class);
		if (opRenderer.isPresent()) {
			Optional<Texture> texture = opRenderer.get().texture.apply(Direction.values()[side]);
			if (texture.isPresent()) {
				return RenderUtility.instance.getTexture(texture.get());
			}
		}
		return null;
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		Optional<StaticBlockRenderer> opRenderer = block.getOp(StaticBlockRenderer.class);
		if (opRenderer.isPresent()) {
			Optional<Texture> texture = opRenderer.get().texture.apply(Direction.values()[side]);
			if (texture.isPresent()) {
				return RenderUtility.instance.getTexture(texture.get());
			}
		}
		return null;
	}
	 */
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
