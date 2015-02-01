package nova.wrapper.mc1710.forward.block;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import nova.core.block.Block;
import nova.core.util.transform.Vector3d;
import nova.wrapper.mc1710.backward.render.MinecraftArtist;

/**
 * @author Calclavia
 */
public class TESRRenderer extends TileEntitySpecialRenderer {

	final Block block;

	public TESRRenderer(Block block) {
		this.block = block;
	}

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float p_147500_8_) {
		MinecraftArtist artist = new MinecraftArtist();
		artist.accessHack = tile.getWorldObj();
		this.block.renderDynamic(artist);
		//		Tessellator.instance.setBrightness(block.getMixedBrightnessForBlock(world, x, y + 1, z));
		artist.complete(new Vector3d(x + 0.5, y + 0.5, z + 0.5));
	}
}
