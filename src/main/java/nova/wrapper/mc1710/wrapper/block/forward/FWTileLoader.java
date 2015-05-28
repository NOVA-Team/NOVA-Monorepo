package nova.wrapper.mc1710.wrapper.block.forward;

import net.minecraft.nbt.NBTTagCompound;
import nova.core.block.Block;
import nova.core.component.ComponentProvider;
import nova.core.util.exception.NovaException;
import nova.wrapper.mc1710.asm.lib.ComponentInjector;
import nova.wrapper.mc1710.wrapper.block.forward.FWTile;

/**
 * @author Vic Nightfall
 */
public final class FWTileLoader {

	private FWTileLoader() {
	}

	public static FWTile loadTile(NBTTagCompound data) {
		try {
			return createWrapperClass(null).newInstance();
		} catch (Exception e) {
			throw new NovaException("Fatal error when trying to create a new NOVA tile.", e);
		}
	}

	public static FWTile loadTile(Block block) {
		try {
			return createWrapperClass(block).getConstructor(String.class).newInstance(block.getID());
		} catch (Exception e) {
			throw new NovaException("Fatal error when trying to create a new NOVA tile.", e);
		}
	}

	private static Class<? extends FWTile> createWrapperClass(ComponentProvider provider) {
		return new ComponentInjector(provider).inject(FWTile.class);
	}
}
