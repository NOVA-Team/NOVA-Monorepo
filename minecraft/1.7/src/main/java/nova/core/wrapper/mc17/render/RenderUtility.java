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

package nova.core.wrapper.mc17.render;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelFormatException;
import nova.core.render.texture.Texture;
import nova.internal.core.Game;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
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
public class RenderUtility {

	public static final ResourceLocation particleResource = new ResourceLocation("textures/particle/particles.png");

	public static final RenderUtility instance = new RenderUtility();
	private final HashMap<Texture, IIcon> iconMap = new HashMap<>();

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

	public IIcon getIcon(Texture texture) {
		if (iconMap.containsKey(texture)) {
			return iconMap.get(texture);
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
		if (event.map.getTextureType() == 0) {
			Game.render().blockTextures.forEach(t -> registerIcon(t, event));
			//TODO: This is HACKS. We should create custom sprite sheets for entities.
			Game.render().entityTextures.forEach(t -> registerIcon(t, event));
		} else if (event.map.getTextureType() == 1) {
			Game.render().itemTextures.forEach(t -> registerIcon(t, event));
		}
	}

	public void registerIcon(Texture texture, TextureStitchEvent.Pre event) {
		iconMap.put(texture, event.map.registerIcon(texture.getResource()));
	}

	@SubscribeEvent
	public void textureHook(TextureStitchEvent.Post event) {

	}

	public void preInit() {
		//Load models
		Game.render().modelProviders.forEach(m -> {
			ResourceLocation resource = new ResourceLocation(m.domain, "models/" + m.name + "." + m.getType());
			try {
				IResource res = Minecraft.getMinecraft().getResourceManager().getResource(resource);
				m.load(res.getInputStream());
			} catch (IOException e) {
				throw new ModelFormatException("IO Exception reading model format", e);
			}
		});
	}
}
