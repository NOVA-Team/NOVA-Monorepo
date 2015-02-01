package nova.wrapper.mc1710.forward.block;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import nova.core.util.transform.Vector3d;
import nova.wrapper.mc1710.backward.render.MinecraftArtist;

/**
 * @author Calclavia
 */
public class TESRWrapper extends TileEntitySpecialRenderer {

	public static final TESRWrapper instance = new TESRWrapper();

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float p_147500_8_) {
		MinecraftArtist artist = new MinecraftArtist();
		artist.accessHack = tile.getWorldObj();
		((TileWrapper) tile).block.renderDynamic(artist);
		artist.complete(new Vector3d(x + 0.5, y + 0.5, z + 0.5));
	}
}
