package nova.wrapper.mc1710.wrapper.block.forward;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import nova.core.block.Block;
import nova.core.component.renderer.DynamicRenderer;
import nova.wrapper.mc1710.backward.render.BWModel;
import nova.wrapper.mc1710.render.RenderUtility;

import java.util.Optional;

/**
 * @author Calclavia
 */
public class FWTileRenderer extends TileEntitySpecialRenderer {

	public static final FWTileRenderer instance = new FWTileRenderer();

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float p_147500_8_) {
		Block block = ((FWTile) tile).getBlock();
		Optional<DynamicRenderer> opRenderer = block.getOp(DynamicRenderer.class);
		if (opRenderer.isPresent()) {
			BWModel model = new BWModel();
			model.matrix.translate(x + 0.5, y + 0.5, z + 0.5);
			opRenderer.get().onRender.accept(model);
			bindTexture(TextureMap.locationBlocksTexture);
			RenderUtility.enableBlending();
			Tessellator.instance.startDrawingQuads();
			model.render();
			Tessellator.instance.draw();
			RenderUtility.disableBlending();
		}
	}
}
