package nova.wrapper.mc18.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nova.core.component.renderer.ItemRenderer;
import nova.core.component.renderer.StaticRenderer;
import nova.core.render.texture.BlockTexture;
import nova.core.render.texture.ItemTexture;
import nova.core.render.texture.Texture;
import nova.internal.core.Game;
import nova.wrapper.mc18.wrapper.block.forward.FWBlock;
import nova.wrapper.mc18.wrapper.item.FWItem;
import nova.wrapper.mc18.wrapper.render.FWEmptyModel;
import nova.wrapper.mc18.wrapper.render.FWSmartBlockModel;
import nova.wrapper.mc18.wrapper.render.FWSmartItemModel;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_FLAT;
import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_POLYGON_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glShadeModel;

/**
 * @author Calclavia
 */
@SideOnly(Side.CLIENT)
public class RenderUtility {

	public static final ResourceLocation particleResource = new ResourceLocation("textures/particle/particles.png");

	public static final RenderUtility instance = new RenderUtility();

	//NOVA Texture to MC TextureAtlasSprite
	private final HashMap<Texture, TextureAtlasSprite> textureMap = new HashMap<>();

	/**
	 * Enables blending.
	 */
	public static void enableBlending() {
		glShadeModel(GL_SMOOTH);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	/**
	 * Disables blending.
	 */
	public static void disableBlending() {
		glShadeModel(GL_FLAT);
		glDisable(GL_LINE_SMOOTH);
		glDisable(GL_POLYGON_SMOOTH);
		glDisable(GL_BLEND);
	}

	public static void enableLighting() {
		RenderHelper.enableStandardItemLighting();
	}

	/**
	 * Disables lighting and turns glow on.
	 */
	public static void disableLighting() {
		RenderHelper.disableStandardItemLighting();
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
	}

	public static void disableLightmap() {
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		glDisable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	public static void enableLightmap() {
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		glEnable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	public TextureAtlasSprite getTexture(Texture texture) {
		if (textureMap.containsKey(texture)) {
			return textureMap.get(texture);
		}

		//Fallback to MC texture
		return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(texture.domain + ":" + texture.getPath().replaceFirst("textures/", "").replace(".png", ""));
	}

	/**
	 * Handles NOVA texture registration.
	 *
	 * @param event Event
	 */
	@SubscribeEvent
	public void preTextureHook(TextureStitchEvent.Pre event) {
		if (event.map == Minecraft.getMinecraft().getTextureMapBlocks()) {
			Game.render().blockTextures.forEach(t -> registerIcon(t, event));
			Game.render().itemTextures.forEach(t -> registerIcon(t, event));
			//TODO: This is HACKS. We should create custom sprite sheets for entities.
			Game.render().entityTextures.forEach(t -> registerIcon(t, event));
		}
	}

	public void registerIcon(Texture texture, TextureStitchEvent.Pre event) {
		String resPath = (texture instanceof BlockTexture ? "blocks" : texture instanceof ItemTexture ? "items" : "entities") + "/" + texture.resource;
		textureMap.put(texture, event.map.registerSprite(new ResourceLocation(texture.domain, resPath)));
	}

	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent event) {
		//Register all blocks
		Game.blocks().registry.forEach(blockFactory -> {
			Object blockObj = Game.natives().toNative(blockFactory.getDummy());
			if (blockObj instanceof FWBlock) {
				FWBlock block = (FWBlock) blockObj;
				ResourceLocation blockRL = (ResourceLocation) net.minecraft.block.Block.blockRegistry.getNameForObject(block);
				Item itemFromBlock = Item.getItemFromBlock(block);
				ResourceLocation itemRL = (ResourceLocation) Item.itemRegistry.getNameForObject(itemFromBlock);
				ModelResourceLocation blockLocation = new ModelResourceLocation(blockRL, "normal");
				ModelResourceLocation itemLocation = new ModelResourceLocation(itemRL, "inventory");
				if (block.block.has(StaticRenderer.class)) {
					event.modelRegistry.putObject(blockLocation, new FWSmartBlockModel(block.block, true));
				} else {
					event.modelRegistry.putObject(blockLocation, new FWEmptyModel());
				}
				event.modelRegistry.putObject(itemLocation, new FWSmartBlockModel(block.block, true));
			}
		});

		//Register all items
		Game.items().registry.forEach(itemFactory -> {
			Object stackObj = Game.natives().toNative(itemFactory.getDummy());
			if (stackObj instanceof ItemStack) {
				Item itemObj = ((ItemStack) stackObj).getItem();
				if (itemObj instanceof FWItem) {
					FWItem item = (FWItem) itemObj;
					ResourceLocation objRL = (ResourceLocation) Item.itemRegistry.getNameForObject(item);
					ModelResourceLocation itemLocation = new ModelResourceLocation(objRL, "inventory");

					nova.core.item.Item dummy = item.getItemFactory().getDummy();

					if (dummy.has(ItemRenderer.class)) {
						Optional<Texture> texture = dummy.get(ItemRenderer.class).texture;

						if (texture.isPresent()) {
							//Default item rendering hack
							ModelBlock modelGenerated = ModelBakery.MODEL_GENERATED;
							modelGenerated.name = itemLocation.toString();
							modelGenerated.textures.put("layer0", texture.get().getResource());
							ModelBlock modelBlock = event.modelBakery.itemModelGenerator.makeItemModel(event.modelBakery.textureMap, modelGenerated);
							TextureAtlasSprite mcTexture = RenderUtility.instance.getTexture(texture.get());
							event.modelBakery.sprites.put(new ResourceLocation(mcTexture.getIconName().replace("items/", "")), mcTexture);
							IBakedModel iBakedModel = event.modelBakery.bakeModel(modelBlock, ModelRotation.X0_Y0, true);
							event.modelRegistry.putObject(itemLocation, iBakedModel);
						} else {
							//The item has a custom renderer
							event.modelRegistry.putObject(itemLocation, new FWSmartItemModel(dummy));
						}
					}
				}
			}
		});
	}

	public void preInit() {
		//Load models
		Game.render().modelProviders.forEach(m -> {
			ResourceLocation resource = new ResourceLocation(m.domain, "models/" + m.name + "." + m.getType());
			try {
				IResource res = Minecraft.getMinecraft().getResourceManager().getResource(resource);
				m.load(res.getInputStream());
			} catch (IOException e) {
				throw new RuntimeException("IO Exception reading model format", e);
			}
		});
	}
}
