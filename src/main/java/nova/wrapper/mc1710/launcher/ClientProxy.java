package nova.wrapper.mc1710.launcher;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import nova.core.entity.Entity;
import nova.core.entity.EntityFactory;
import nova.wrapper.mc1710.forward.block.FWBlock;
import nova.wrapper.mc1710.forward.block.FWTile;
import nova.wrapper.mc1710.forward.block.FWTileRenderer;
import nova.wrapper.mc1710.forward.entity.FWEntity;
import nova.wrapper.mc1710.forward.entity.FWEntityFX;
import nova.wrapper.mc1710.forward.entity.FWEntityRenderer;
import nova.wrapper.mc1710.item.FWItem;
import nova.wrapper.mc1710.render.RenderUtility;

/**
 * @author Calclavia
 */
public class ClientProxy extends CommonProxy {
	@Override
	public void preInit() {
		super.preInit();
		MinecraftForge.EVENT_BUS.register(RenderUtility.instance);
		ClientRegistry.bindTileEntitySpecialRenderer(FWTile.class, FWTileRenderer.instance);
		RenderingRegistry.registerEntityRenderingHandler(FWEntity.class, FWEntityRenderer.instance);
		RenderUtility.instance.loadModels();
	}

	@Override
	public void registerItem(FWItem item) {
		super.registerItem(item);
		MinecraftForgeClient.registerItemRenderer(item, item);
	}

	@Override
	public void registerBlock(FWBlock block) {
		super.registerBlock(block);

		/**
		 * Registers a block rendering handler for this block
		 */
		RenderingRegistry.registerBlockHandler(block);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(block), block);
	}

	@Override
	public boolean isPaused() {
		if (FMLClientHandler.instance().getClient().isSingleplayer() && !FMLClientHandler.instance().getClient().getIntegratedServer().getPublic()) {
			GuiScreen screen = FMLClientHandler.instance().getClient().currentScreen;
			if (screen != null) {
				if (screen.doesGuiPauseGame()) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public Entity spawnParticle(net.minecraft.world.World world, EntityFactory factory) {
		FWEntityFX bwEntityFX = new FWEntityFX(world, factory);
		FMLClientHandler.instance().getClient().effectRenderer.addEffect(bwEntityFX);
		return bwEntityFX.wrapped;
	}

	@Override
	public Entity spawnParticle(net.minecraft.world.World world, Entity entity) {
		FWEntityFX bwEntityFX = new FWEntityFX(world, entity);
		FMLClientHandler.instance().getClient().effectRenderer.addEffect(bwEntityFX);
		return bwEntityFX.wrapped;
	}

	@Override
	public EntityPlayer getClientPlayer() {
		return Minecraft.getMinecraft().thePlayer;
	}
}
