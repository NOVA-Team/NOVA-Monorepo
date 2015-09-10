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

package nova.core.wrapper.mc18.wrapper.render;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nova.core.render.RenderException;
import nova.core.render.texture.Texture;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.util.Iterator;

/**
 * @author Calclavia
 */
public class BWClientRenderManager extends BWRenderManager {
	@SideOnly(Side.CLIENT)
	@Override
	public Vector2D getDimension(Texture texture) {
		ResourceLocation loc = toResourceLocation(texture);

		try {
			ImageInputStream in = ImageIO.createImageInputStream(Minecraft.getMinecraft().getResourceManager().getResource(loc).getInputStream());
			Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
			if (readers.hasNext()) {
				ImageReader reader = readers.next();
				try {
					reader.setInput(in);
					return new Vector2D(reader.getWidth(0), reader.getHeight(0));
				} finally {
					reader.dispose();
				}
			}
		} catch (Exception e) {
			throw new RenderException("Couldn't load texture " + texture.getPath(), e);
		}
		return new Vector2D(16, 16);
	}
}
