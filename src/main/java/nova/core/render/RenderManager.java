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

package nova.core.render;

import nova.core.render.model.ModelProvider;
import nova.core.render.texture.BlockTexture;
import nova.core.render.texture.EntityTexture;
import nova.core.render.texture.ItemTexture;
import nova.core.render.texture.Texture;
import nova.core.util.registry.Manager;
import nova.core.util.registry.Registry;
import nova.internal.core.Game;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.Optional;

/**
 * @author Calclavia
 */
public class RenderManager extends Manager<RenderManager> {

	public final Registry<BlockTexture> blockTextures = new Registry<>();
	public final Registry<ItemTexture> itemTextures = new Registry<>();
	public final Registry<EntityTexture> entityTextures = new Registry<>();
	public final Registry<ModelProvider> modelProviders = new Registry<>();

	public ItemTexture registerTexture(ItemTexture texture) {
		Optional<ItemTexture> itemTexture = itemTextures.get(texture.getID());
		if (itemTexture.isPresent()) {
			Game.logger().error("Attempt to register the same texture twice: " + texture);
			return itemTexture.get();
		}
		itemTextures.register(texture);
		return texture;
	}

	public BlockTexture registerTexture(BlockTexture texture) {
		Optional<BlockTexture> blockTexture = blockTextures.get(texture.getID());
		if (blockTexture.isPresent()) {
			Game.logger().error("Attempt to register the same texture twice: " + texture);
			return blockTexture.get();
		}
		blockTextures.register(texture);
		return texture;
	}

	public EntityTexture registerTexture(EntityTexture texture) {
		Optional<EntityTexture> entityTexture = entityTextures.get(texture.getID());
		if (entityTexture.isPresent()) {
			Game.logger().error("Attempt to register the same texture twice: " + texture);
			return entityTexture.get();
		}
		entityTextures.register(texture);
		return texture;
	}

	public ModelProvider registerModel(ModelProvider modelProvider) {
		Optional<ModelProvider> modelProviderCheck = modelProviders.get(modelProvider.getID());
		if (modelProviderCheck.isPresent()) {
			Game.logger().error("Attempt to register the same model twice: " + modelProvider);
			return modelProviderCheck.get();
		}
		modelProviders.register(modelProvider);
		return modelProvider;
	}

	@Override
	public void init() {
		Game.events().publish(new Init(this));
	}

	public class Init extends ManagerEvent<RenderManager> {
		public Init(RenderManager manager) {
			super(manager);
		}
	}
}
