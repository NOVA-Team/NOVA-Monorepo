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

package nova.core.wrapper.mc.forge.v1_8.wrapper.assets;

import net.minecraft.util.ResourceLocation;
import nova.core.nativewrapper.NativeConverter;
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

	public Texture toNovaTexture(ResourceLocation resource) {
		return new Texture(resource.getResourceDomain(), resource.getResourcePath());
	}

	@Override
	public ResourceLocation toNative(Asset asset) {
		return new ResourceLocation(asset.domain.toLowerCase(), asset.path());
	}

	public ResourceLocation toNativeTexture(Asset asset) {
		return toNativeTexture(asset, false);
	}

	public ResourceLocation toNativeTexture(Asset asset, boolean preserveExtension) {
		if (asset instanceof Texture)
			return new ResourceLocation(asset.domain.toLowerCase(), asset.path().replaceFirst("^textures/", "").replaceFirst("\\.(\\w+)$", preserveExtension ? ".$1" : ""));
		return new ResourceLocation(asset.domain.toLowerCase(), asset.path());
	}
}
