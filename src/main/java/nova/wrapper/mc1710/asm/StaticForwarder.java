package nova.wrapper.mc1710.asm;

import net.minecraft.block.Block;
import net.minecraft.world.chunk.Chunk;
import nova.core.event.EventManager;
import nova.core.game.Game;
import nova.core.util.transform.Vector3i;
import nova.wrapper.mc1710.backward.block.BWBlock;

/**
 * Static forwarder forwards injected methods.
 *
 * @author Calclavia
 */
public class StaticForwarder {
	public static void chunkSetBlockEvent(Chunk chunk, int x, int y, int z, Block block, int blockMetadata) {
		//Publish the event
		Game.instance.get().eventManager.blockChange.publish(new EventManager.BlockChangeEvent(new BWBlock(block), new Vector3i((chunk.xPosition << 4) + x, y, (chunk.zPosition << 4) + z)));
	}

}
