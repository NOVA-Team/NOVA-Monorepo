package nova.wrapper.mc1710.asm;

import net.minecraft.block.Block;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkEvent;

/**
 * Called when the chunk is modified. More precisely, this event is called when a block is world.setBlock is called.
 *
 * @author Calclavia
 */
public abstract class ChunkModifiedEvent extends ChunkEvent {
	public ChunkModifiedEvent(Chunk chunk) {
		super(chunk);
	}

	public static class ChunkSetBlockEvent extends ChunkModifiedEvent {
		public final int x, y, z, blockMetadata;
		public final Block block;

		public ChunkSetBlockEvent(Chunk chunk, int chunkX, int y, int chunkZ, Block block, int blockMetadata) {
			super(chunk);
			this.x = (chunk.xPosition << 4) + chunkX;
			this.y = y;
			this.z = (chunk.zPosition << 4) + chunkZ;
			this.block = block;
			this.blockMetadata = blockMetadata;
		}

	}
}
