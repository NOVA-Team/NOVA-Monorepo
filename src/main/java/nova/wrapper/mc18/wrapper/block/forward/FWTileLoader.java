package nova.wrapper.mc18.wrapper.block.forward;

import net.minecraft.nbt.NBTTagCompound;
import nova.core.block.Block;
import nova.core.block.BlockFactory;
import nova.internal.core.Game;
import nova.wrapper.mc18.asm.lib.ComponentInjector;

import java.util.Optional;

/**
 * @author Vic Nightfall
 */
public final class FWTileLoader {

	private static ComponentInjector<FWTile> injector = new ComponentInjector<>(FWTile.class);

	private FWTileLoader() {
	}

	public static FWTile loadTile(NBTTagCompound data) {
		try {
			String blockID = data.getString("novaID");
			Block block = createBlock(blockID);
			FWTile tile = injector.inject(block, new Class[0], new Object[0]);
			tile.setBlock(block);
			return tile;
		} catch (Exception e) {
			throw new RuntimeException("Fatal error when trying to create a new NOVA tile.", e);
		}
	}

	public static FWTile loadTile(String blockID) {
		try {
			Block block = createBlock(blockID);
			FWTile tile = injector.inject(block, new Class[] { String.class }, new Object[] { blockID });
			tile.setBlock(block);
			return tile;
		} catch (Exception e) {
			throw new RuntimeException("Fatal error when trying to create a new NOVA tile.", e);
		}
	}

	private static Block createBlock(String blockID) {
		Optional<BlockFactory> blockFactory = Game.blocks().getFactory(blockID);
		if (blockFactory.isPresent()) {
			return blockFactory.get().makeBlock();
		} else {
			throw new RuntimeException("Error! Invalid NOVA block ID");
		}
	}
}
