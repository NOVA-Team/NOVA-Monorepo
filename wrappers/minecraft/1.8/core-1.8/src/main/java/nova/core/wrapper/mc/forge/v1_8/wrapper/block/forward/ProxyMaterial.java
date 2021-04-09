/*
 * Copyright (c) 2016 NOVA, All rights reserved.
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

package nova.core.wrapper.mc.forge.v1_8.wrapper.block.forward;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import nova.core.block.component.BlockProperty;

import java.util.Optional;

/**
 * @author soniex2, ExE Boss
 */
public class ProxyMaterial extends Material {
	private final Optional<BlockProperty.Opacity> opacity;
	private final Optional<BlockProperty.Replaceable> replaceable;

	/**
	 * Construct a new proxy material.
	 * @param color The map color.
	 * @param opacity The Opacity to use.
	 * @param replaceable If this block is replaceable.
	 */
	public ProxyMaterial(MapColor color, Optional<BlockProperty.Opacity> opacity, Optional<BlockProperty.Replaceable> replaceable) {
		super(color);
		this.opacity = opacity;
		this.replaceable = replaceable;
	}

	@Override
	public boolean blocksLight() {
		return opacity.isPresent() ? opacity.get().isOpaque() : super.blocksLight();
	}

	@Override
	public boolean isOpaque() {
		return opacity.isPresent() ? opacity.get().isOpaque() : super.isOpaque();
	}

	@Override
	public boolean isReplaceable() {
		return replaceable.map(BlockProperty.Replaceable::isReplaceable).orElseGet(super::isReplaceable);
	}
}
