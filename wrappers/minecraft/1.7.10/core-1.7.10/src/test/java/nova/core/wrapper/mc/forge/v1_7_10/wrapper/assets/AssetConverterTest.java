/*
 * Copyright (c) 2017 NOVA, All rights reserved.
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

package nova.core.wrapper.mc.forge.v1_7_10.wrapper.assets;

import nova.core.wrapper.mc.forge.v1_7_10.wrapper.assets.AssetConverter;
import net.minecraft.util.ResourceLocation;
import nova.core.render.texture.BlockTexture;
import nova.core.render.texture.EntityTexture;
import nova.core.render.texture.ItemTexture;
import nova.core.render.texture.Texture;
import nova.core.util.Asset;
import org.junit.Before;
import org.junit.Test;

import static nova.testutils.NovaAssertions.assertThat;

/**
 * Used to test {@link AssetConverter}.
 *
 * @author ExE Boss
 */
public class AssetConverterTest {

	AssetConverter converter;

	@Before
	public void setUp() {
		converter = new AssetConverter();
	}

	@Test
	public void testClasses() {
		assertThat(converter.getNovaSide()).isEqualTo(Asset.class);
		assertThat(converter.getNativeSide()).isEqualTo(ResourceLocation.class);
	}

	@Test
	public void testToNova() {
		assertThat(converter.toNova(new ResourceLocation("nova", "stuff"))).isEqualTo(new Asset("nova", "stuff"));
		assertThat(converter.toNova(new ResourceLocation("nova:otherStuff"))).isEqualTo(new Asset("nova", "otherStuff"));
		assertThat(converter.toNova(new ResourceLocation("nova:otherStuff"))).isEqualTo(new Asset("nova", "otherstuff"));
		// NOVA's Assets are entirely case-insensitive
		assertThat(converter.toNovaTexture(new ResourceLocation("nova", "stuff"))).isEqualTo(new Texture("nova", "stuff"));
		assertThat(converter.toNovaTexture(new ResourceLocation("nova:otherStuff"))).isEqualTo(new Texture("nova", "otherStuff"));
		assertThat(converter.toNovaTexture(new ResourceLocation("nova:otherStuff"))).isEqualTo(new Texture("nova", "otherstuff"));
	}

	@Test
	public void testToNative() {
		assertThat(converter.toNative(new Asset("nova", "stuff"))).isEqualTo(new ResourceLocation("nova", "stuff"));
		assertThat(converter.toNative(new Asset("nova", "otherStuff"))).isNotEqualTo(new ResourceLocation("nova:otherstuff"));
		assertThat(converter.toNative(new Asset("nova", "otherStuff"))).isEqualTo(new ResourceLocation("nova:otherStuff"));
		// 1.8 ResourceLocation is partially case sensitive. 1.11 ResourceLocation is all lowercase.
		assertThat(converter.toNativeTexture(new Asset("nova", "stuff"))).isEqualTo(new ResourceLocation("nova", "stuff"));
		assertThat(converter.toNativeTexture(new Texture("nova", "otherStuff.png"))).isEqualTo(new ResourceLocation("nova:otherStuff"));
		assertThat(converter.toNativeTexture(new Texture("nova", "otherStuff.png"), true)).isEqualTo(new ResourceLocation("nova:otherStuff.png"));
		assertThat(converter.toNativeTexture(new BlockTexture("nova", "otherStuff.png"))).isEqualTo(new ResourceLocation("nova:otherStuff"));
		assertThat(converter.toNativeTexture(new BlockTexture("nova", "otherStuff.png"), true)).isEqualTo(new ResourceLocation("nova:otherStuff.png"));
		assertThat(converter.toNativeTexture(new ItemTexture("nova", "otherStuff.png"))).isEqualTo(new ResourceLocation("nova:otherStuff"));
		assertThat(converter.toNativeTexture(new ItemTexture("nova", "otherStuff.png"), true)).isEqualTo(new ResourceLocation("nova:otherStuff.png"));
		assertThat(converter.toNativeTexture(new EntityTexture("nova", "otherStuff.png"))).isEqualTo(new ResourceLocation("nova:../entities/otherStuff"));
		assertThat(converter.toNativeTexture(new EntityTexture("nova", "otherStuff.png"), true)).isEqualTo(new ResourceLocation("nova:../entities/otherStuff.png"));
	}
}
