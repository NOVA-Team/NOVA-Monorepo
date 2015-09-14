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

package nova.core.component.renderer;

import nova.core.component.ComponentProvider;
import nova.core.render.texture.ItemTexture;
import nova.core.render.texture.Texture;

import java.util.Optional;

/**
 * This interface specifies that a block requires custom static item rendering.
 * (That is, called upon item render or network synchronization.)
 */
//Item renderer should not exist. Instead, blocks should just edit the default ItemBlock renderer...
@Deprecated
public class ItemRenderer extends Renderer {

	/**
	 * If there is no texture provided, it will not render any and default to onRender() method for custom item rendering.
	 * <p>
	 * return - {@link ItemTexture} instance
	 */
	public Optional<Texture> texture = Optional.empty();

	public ItemRenderer() {

	}

	public ItemRenderer(ComponentProvider provider) {
		onRender = model -> {
			Optional<StaticRenderer> opComponent = provider.components.getOp(StaticRenderer.class);
			if (opComponent.isPresent()) {
				opComponent.get().onRender.accept(model);
			} else {
				provider.components.getOp(DynamicRenderer.class).ifPresent(c -> c.onRender.accept(model));
			}
		};
	}

	public ItemRenderer setTexture(Texture texture) {
		this.texture = Optional.of(texture);
		return this;
	}
}
