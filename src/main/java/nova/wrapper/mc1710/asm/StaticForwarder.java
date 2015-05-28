package nova.wrapper.mc1710.asm;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.chunk.Chunk;
import nova.core.event.EventManager;
import nova.core.game.Game;
import nova.core.util.transform.vector.Vector3i;
import nova.wrapper.mc1710.wrapper.block.backward.BWBlock;
import nova.wrapper.mc1710.wrapper.block.forward.FWTile;
import nova.wrapper.mc1710.wrapper.block.forward.FWTileLoader;
import nova.wrapper.mc1710.wrapper.block.world.BWWorld;

/**
 * Static forwarder forwards injected methods.
 * 
 * @author Calclavia
 */
public class StaticForwarder {
	public static void chunkSetBlockEvent(Chunk chunk, int x, int y, int z, Block oldBlock, int oldMeta, Block newBlock, int newMeta) {
		// Publish the event
		Game.instance.eventManager.blockChange.publish(
			new EventManager.BlockChangeEvent(new BWWorld(chunk.worldObj), new Vector3i((chunk.xPosition << 4) + x, y, (chunk.zPosition << 4) + z), new BWBlock(oldBlock), new BWBlock(newBlock))
			);
	}

	/**
	 * Used to inject forwarded TileEntites
	 * 
	 * @param data
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static TileEntity loadTileEntityHook(NBTTagCompound data, Class<? extends TileEntity> clazz) throws Exception {
		if (clazz.equals(FWTile.class)) {
			return FWTileLoader.loadTile(data);
		} else {
			return clazz.newInstance();
		}
	}
}
