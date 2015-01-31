package nova.wrapper.mc1710.launcher;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.TextureStitchEvent;
import nova.core.game.Game;
import nova.wrapper.mc1710.forward.block.BlockWrapper;

import java.util.HashMap;

/**
 * @author Calclavia
 */
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	public static final HashMap<String, IIcon> iconMap = new HashMap<>();

	@Override
	public void registerBlock(BlockWrapper block) {
		super.registerBlock(block);

		/**
		 * Registers a block rendering handler for this block
		 */
		RenderingRegistry.registerBlockHandler(block);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(block), block);
	}

	public void registerIcon(String name, TextureStitchEvent.Pre event) {
		iconMap.put(name, event.map.registerIcon(name));
	}

	/**
	 * Handles NOVA texture registration.
	 */
	@SubscribeEvent
	public void preTextureHook(TextureStitchEvent.Pre event) {
		if (event.map.getTextureType() == 0) {
			Game.instance.get().renderManager.textures.forEach(t -> registerIcon(t.resource, event));
		}
	}

	@SubscribeEvent
	public void textureHook(TextureStitchEvent.Post event) {

	}
}
