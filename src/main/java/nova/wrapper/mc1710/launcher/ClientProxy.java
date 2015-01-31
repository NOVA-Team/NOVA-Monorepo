package nova.wrapper.mc1710.launcher;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
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
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(block), block);
	}
}
