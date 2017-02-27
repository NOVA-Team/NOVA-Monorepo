/*
 * Copyright (c) 2015 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License version 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
 */

package nova.core.wrapper.mc.forge.v1_11.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nova.core.component.renderer.Renderer;
import nova.core.component.renderer.StaticRenderer;
import nova.core.render.texture.BlockTexture;
import nova.core.render.texture.ItemTexture;
import nova.core.render.texture.Texture;
import nova.core.wrapper.mc.forge.v1_11.launcher.ForgeLoadable;
import nova.core.wrapper.mc.forge.v1_11.wrapper.block.forward.FWBlock;
import nova.core.wrapper.mc.forge.v1_11.wrapper.item.forward.FWItem;
import nova.core.wrapper.mc.forge.v1_11.wrapper.render.forward.FWEmptyModel;
import nova.core.wrapper.mc.forge.v1_11.wrapper.render.forward.FWSmartBlockModel;
import nova.core.wrapper.mc.forge.v1_11.wrapper.render.forward.FWSmartItemModel;
import nova.internal.core.Game;
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
public class RenderUtility implements ForgeLoadable {

	public static final ResourceLocation particleResource = new ResourceLocation("textures/particle/particles.png");

	public static final RenderUtility instance = new RenderUtility();
	// Cruft needed to generate default item models
	protected static final ItemModelGenerator ITEM_MODEL_GENERATOR = new ItemModelGenerator();
	protected static final FaceBakery FACE_BAKERY = new FaceBakery();
	// Ugly D:
	protected static final ModelBlock MODEL_GENERATED = ModelBlock.deserialize(
			"{" +
			"	\"elements\":[{\n" +
			"		\"from\": [0, 0, 0],\n" +
			"		\"to\": [16, 16, 16],\n" +
			"		\"faces\": {\n" +
			"			\"down\": {\"uv\": [0, 0, 16, 16], \"texture\":\"\"}\n" +
			"		}\n" +
			"	}],\n" +
			"	\"display\": {\n" +
			"		\"thirdperson_righthand\": {\n" +
			"			\"rotation\": [ -90, 0, 0 ],\n" +
			"			\"translation\": [ 0, 1, -3 ],\n" +
			"			\"scale\": [ 0.55, 0.55, 0.55 ]\n" +
			"		},\n" +
			"		\"firstperson_righthand\": {\n" +
			"			\"rotation\": [ 0, -90, 25 ],\n" +
			"			\"translation\": [ 0, 3.75, 2.3125 ],\n" +
			"			\"scale\": [ 0.6, 0.6, 0.6 ]\n" +
			"		},\n" +
			"		\"firstperson_lefthand\": {\n" +
			"			\"rotation\": [ 0, 90, -25 ],\n" +
			"			\"translation\": [ 0, 3.75, 2.3125 ],\n" +
			"			\"scale\": [ 0.6, 0.6, 0.6 ]\n" +
			"		}\n" +
			"	}\n" +
			"}");
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

	public TextureAtlasSprite getTexture(Optional<Texture> texture) {
		if (!texture.isPresent())
			return Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
		return getTexture(texture.get());
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
	 * @param event Event
	 */
	@SubscribeEvent
	public void preTextureHook(TextureStitchEvent.Pre event) {
		if (event.getMap() == Minecraft.getMinecraft().getTextureMapBlocks()) {
			Game.render().blockTextures.forEach(t -> registerIcon(t, event));
			Game.render().itemTextures.forEach(t -> registerIcon(t, event));
			//TODO: This is HACKS. We should create custom sprite sheets for entities.
			Game.render().entityTextures.forEach(t -> registerIcon(t, event));
		}
	}

	public void registerIcon(Texture texture, TextureStitchEvent.Pre event) {
		String resPath = (texture instanceof BlockTexture ? "blocks" : texture instanceof ItemTexture ? "items" : "entities") + "/" + texture.resource;
		System.out.println(texture + " (" + texture.domain + ':' + resPath + ')');
		TextureAtlasSprite sprite = event.getMap().registerSprite(new ResourceLocation(texture.domain, resPath));
		textureMap.put(texture, sprite);
		System.out.println(sprite);
	}

	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent event) {
		//Register all blocks
		Game.blocks().registry.forEach(blockFactory -> {
			Object blockObj = Game.natives().toNative(blockFactory.build());
			if (blockObj instanceof FWBlock) {
				FWBlock block = (FWBlock) blockObj;
				ResourceLocation blockRL = net.minecraft.block.Block.REGISTRY.getNameForObject(block);
				Item itemFromBlock = Item.getItemFromBlock(block);
				ResourceLocation itemRL = Item.REGISTRY.getNameForObject(itemFromBlock);
				ModelResourceLocation blockLocation = new ModelResourceLocation(blockRL, "normal");
				ModelResourceLocation itemLocation = new ModelResourceLocation(itemRL, "inventory");
				if (block.dummy.components.has(StaticRenderer.class)) {
					event.getModelRegistry().putObject(blockLocation, new FWSmartBlockModel(block.dummy, true));
				} else {
					event.getModelRegistry().putObject(blockLocation, new FWEmptyModel());
				}
				event.getModelRegistry().putObject(itemLocation, new FWSmartBlockModel(block.dummy, true));
			}
		});

		//Register all items
		Game.items().registry.forEach(itemFactory -> {
			Object stackObj = Game.natives().toNative(itemFactory.build());
			if (stackObj instanceof ItemStack) {
				Item itemObj = ((ItemStack) stackObj).getItem();
				if (itemObj instanceof FWItem) {
					FWItem item = (FWItem) itemObj;
					ResourceLocation objRL = Item.REGISTRY.getNameForObject(item);
					ModelResourceLocation itemLocation = new ModelResourceLocation(objRL, "inventory");

					nova.core.item.Item dummy = item.getItemFactory().build();

					if (dummy.components.has(Renderer.class)) {
						event.getModelRegistry().putObject(itemLocation, new FWSmartItemModel(dummy));
					}
				}
			}
		});
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
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
