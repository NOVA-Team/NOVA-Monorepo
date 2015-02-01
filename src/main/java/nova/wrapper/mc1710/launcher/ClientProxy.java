package nova.wrapper.mc1710.launcher;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import nova.wrapper.mc1710.forward.block.BlockWrapper;
import nova.wrapper.mc1710.forward.block.TESRWrapper;
import nova.wrapper.mc1710.forward.block.TileWrapper;
import nova.wrapper.mc1710.forward.item.ItemWrapper;
import nova.wrapper.mc1710.util.RenderUtility;

/**
 * @author Calclavia
 */
public class ClientProxy extends CommonProxy {
	@Override
	public void preInit() {
		MinecraftForge.EVENT_BUS.register(RenderUtility.instance);
		ClientRegistry.registerTileEntity(TileWrapper.class, "novaTile", TESRWrapper.instance);
	}

	@Override
	public void registerItem(ItemWrapper item) {
		super.registerItem(item);
		MinecraftForgeClient.registerItemRenderer(item, item);
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

	@Override
	public EntityPlayer getClientPlayer() {
		return Minecraft.getMinecraft().thePlayer;
	}
}
