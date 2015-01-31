package nova.wrapper.mc1710.launcher;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import nova.wrapper.mc1710.forward.block.BlockWrapper;
import nova.wrapper.mc1710.util.RenderUtil;

/**
 * @author Calclavia
 */
public class ClientProxy extends CommonProxy {

	@Override
	public void preInit() {
		MinecraftForge.EVENT_BUS.register(RenderUtil.instance);
	}

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
