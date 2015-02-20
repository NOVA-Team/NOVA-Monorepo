package nova.wrapper.mc1710.asm;

import net.minecraft.block.Block;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import nova.wrapper.mc1710.asm.ChunkModifiedEvent.ChunkSetBlockEvent;

/**
 * Static forwarder forwards injected methods.
 *
 * @author Calclavia
 */
public class StaticForwarder {
	public static void chunkSetBlockEvent(Chunk chunk, int x, int y, int z, Block block, int blockMetadata) {
		//TODO: Switch to better event
		MinecraftForge.EVENT_BUS.post(new ChunkSetBlockEvent(chunk, x, y, z, block, blockMetadata));
	}
}
