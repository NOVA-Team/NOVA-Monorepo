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

import net.minecraft.util.ResourceLocation;
import nova.core.nativewrapper.NativeConverter;
import nova.core.render.texture.BlockTexture;
import nova.core.render.texture.EntityTexture;
import nova.core.render.texture.ItemTexture;
import nova.core.render.texture.Texture;
import nova.core.util.Asset;
import nova.internal.core.Game;

/**
 * @author ExE Boss
 */
public final class AssetConverter implements NativeConverter<Asset, ResourceLocation> {

	public static AssetConverter instance() {
		return Game.natives().getNative(Asset.class, ResourceLocation.class);
	}

	@Override
	public Class<Asset> getNovaSide() {
		return Asset.class;
	}

	@Override
	public Class<ResourceLocation> getNativeSide() {
		return ResourceLocation.class;
	}

	@Override
	public Asset toNova(ResourceLocation resource) {
		return new Asset(resource.getResourceDomain(), resource.getResourcePath());
	}

	public Asset toNovaTexture(ResourceLocation resource) {
		return new Texture(resource.getResourceDomain(), resource.getResourcePath());
	}

	@Override
	public ResourceLocation toNative(Asset asset) {
		return new ResourceLocation(asset.domain.toLowerCase(), asset.path());
	}

	public ResourceLocation toNativeTexture(Asset asset) {
		return toNativeTexture(asset, false, true);
	}

	public ResourceLocation toNativeTexture(Asset asset, boolean preserveExtension) {
		return toNativeTexture(asset, preserveExtension, true);
	}

	public ResourceLocation toNativeTexture(Asset asset, boolean preserveExtension, boolean removeType) {
		if (asset instanceof Texture) {
			if (removeType) {
				if (asset instanceof BlockTexture)
					return new ResourceLocation(asset.domain.toLowerCase(), asset.path().replaceFirst("^textures/blocks/", "").replaceFirst("\\.(\\w+)$", preserveExtension ? ".$1" : ""));
				else if (asset instanceof ItemTexture)
					return new ResourceLocation(asset.domain.toLowerCase(), asset.path().replaceFirst("^textures/items/", "").replaceFirst("\\.(\\w+)$", preserveExtension ? ".$1" : ""));
				else if (asset instanceof EntityTexture)
					return new ResourceLocation(asset.domain.toLowerCase(), asset.path().replaceFirst("^textures/", "../").replaceFirst("\\.(\\w+)$", preserveExtension ? ".$1" : ""));
			}
			return new ResourceLocation(asset.domain.toLowerCase(), asset.path().replaceFirst("^textures/", "").replaceFirst("\\.(\\w+)$", preserveExtension ? ".$1" : ""));
		}
		return new ResourceLocation(asset.domain.toLowerCase(), asset.path());
	}
}
