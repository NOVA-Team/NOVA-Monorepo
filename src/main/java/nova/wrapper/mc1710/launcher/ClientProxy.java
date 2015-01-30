package nova.wrapper.mc1710.launcher;

import cpw.mods.fml.client.registry.RenderingRegistry;
import nova.wrapper.mc1710.forward.block.BlockWrapper;

/**
 * @author Calclavia
 */
public class ClientProxy extends CommonProxy {
	@Override
	public void registerBlock(BlockWrapper block) {
		super.registerBlock(block);

		/**
		 * Registers a block rendering handler for this block
		 */
		RenderingRegistry.registerBlockHandler(block);
		//TODO: Implement this
		//MinecraftForgeClient.registerItemRenderer(new ItemWrapper(Game.instance.get().itemManager.getItemFromBlock(block.block)), block);
	}
}
