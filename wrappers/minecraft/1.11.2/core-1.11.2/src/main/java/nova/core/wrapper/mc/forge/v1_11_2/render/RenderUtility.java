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

package nova.core.wrapper.mc.forge.v1_11_2.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
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
import nova.core.component.renderer.StaticRenderer;
import nova.core.item.ItemFactory;
import nova.core.render.texture.Texture;
import nova.core.wrapper.mc.forge.v1_11_2.launcher.ForgeLoadable;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.assets.AssetConverter;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.block.BlockConverter;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.block.forward.FWBlock;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.item.ItemConverter;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.item.forward.FWItem;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.item.forward.IFWItem;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.render.forward.FWEmptyModel;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.render.forward.FWSmartBlockModel;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.render.forward.FWSmartItemModel;
import nova.internal.core.Game;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;

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
		return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(AssetConverter.instance().toNativeTexture(texture).toString());
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
			Game.render().entityTextures.forEach(t -> registerIcon(t, event));
		}
	}

	public void registerIcon(Texture texture, TextureStitchEvent.Pre event) {
		textureMap.put(texture, event.getMap().registerSprite(AssetConverter.instance().toNativeTexture(texture)));
	}

	@SubscribeEvent
	public void textureHook(TextureStitchEvent.Post event) {
		Game.render().blockTextures.forEach(this::updateTexureDimensions);
		Game.render().itemTextures.forEach(this::updateTexureDimensions);
		Game.render().entityTextures.forEach(this::updateTexureDimensions);
	}

	/**
	 * Update the texture dimensions for the given texture.
	 * @param texture The texture to update.
	 * @throws RuntimeException If the texture update fails.
	 * @see <a href="https://github.com/NOVA-Team/NOVA-Core/pull/265#discussion_r103739268">PR review</a>
	 */
	private void updateTexureDimensions(Texture texture) {
		// NOTE: This is the only way to update the `dimension` field without breaking anything.
		//       https://github.com/NOVA-Team/NOVA-Core/pull/265#discussion_r103739268
		try {
			Field dimension = Texture.class.getDeclaredField("dimension");
			dimension.setAccessible(true);
			TextureAtlasSprite icon = getTexture(texture);
			dimension.set(texture, new Vector2D(icon.getIconWidth(), icon.getIconHeight()));
			dimension.setAccessible(false);
		} catch (Exception ex) {
			throw new RuntimeException("Cannot set dimension of texture " + texture, ex);
		}
	}

	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent event) {
		//Register all blocks
		Game.blocks().registry.forEach(blockFactory -> {
			Object blockObj = BlockConverter.instance().toNative(blockFactory);
			if (blockObj instanceof FWBlock) {
				FWBlock block = (FWBlock) blockObj;
				ResourceLocation blockRL = net.minecraft.block.Block.REGISTRY.getNameForObject(block);
				Item itemFromBlock = Item.getItemFromBlock(block);
				ResourceLocation itemRL = Item.REGISTRY.getNameForObject(itemFromBlock);
				ModelResourceLocation blockLocation = new ModelResourceLocation(blockRL, "normal");
				ModelResourceLocation itemLocation = new ModelResourceLocation(itemRL, "inventory");
				ItemFactory itemFactory = ((IFWItem)itemFromBlock).getItemFactory();
				nova.core.item.Item dummy = itemFactory.build();
				if (block.dummy.components.has(StaticRenderer.class)) {
					event.getModelRegistry().putObject(blockLocation, new FWSmartBlockModel(block.dummy));
				} else {
					event.getModelRegistry().putObject(blockLocation, FWEmptyModel.INSTANCE);
				}
				event.getModelRegistry().putObject(itemLocation, new FWSmartBlockModel(block.dummy, dummy));
			}
		});

		//Register all items
		Game.items().registry.forEach(itemFactory -> {
			ItemStack stack = ItemConverter.instance().toNative(itemFactory);
			Item itemObj = stack.getItem();
			if (itemObj instanceof FWItem) {
				FWItem item = (FWItem) itemObj;
				ResourceLocation objRL = Item.REGISTRY.getNameForObject(item);
				ModelResourceLocation itemLocation = new ModelResourceLocation(objRL, "inventory");
				event.getModelRegistry().putObject(itemLocation, new FWSmartItemModel(item.getItemFactory().build()));
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
