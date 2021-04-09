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

package nova.testutils;

import nova.core.block.Block;
import nova.core.block.BlockFactory;
import nova.core.component.transform.BlockTransform;
import nova.core.component.transform.EntityTransform;
import nova.core.entity.Entity;
import nova.core.entity.EntityFactory;
import nova.core.item.Item;
import nova.core.sound.Sound;
import nova.core.util.shape.Cuboid;
import nova.core.world.World;
import nova.internal.core.Game;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author Calclavia
 */
public class FakeWorld extends World {

	public final Map<Vector3D, Block> blockMap = new HashMap<>();
	public final Set<Entity> entities = new HashSet<>();

	@Override
	public void markStaticRender(Vector3D position) {

	}

	@Override
	public void markChange(Vector3D position) {

	}

	@Override
	public Optional<Block> getBlock(Vector3D position) {
		//Gives a fake block to represent air
		Block air = Game.blocks().getAirBlock().build();
		BlockTransform component = new BlockTransform();
		component.setPosition(position);
		component.setWorld(this);
		air.components.add(component);
		return Optional.of(blockMap.getOrDefault(position, air));
	}

	@Override
	public boolean setBlock(Vector3D position, BlockFactory factory) {
		Block newBlock = factory.build();
		BlockTransform component = new BlockTransform();
		component.setPosition(position);
		component.setWorld(this);
		newBlock.components.add(component);
		blockMap.put(position, newBlock);
		return true;
	}

	@Override
	public Entity addEntity(EntityFactory factory) {
		Entity make = factory.build();
		EntityTransform component = new EntityTransform();
		component.setWorld(this);
		make.components.add(component);
		entities.add(make);
		return make;
	}

	@Override
	public Optional<Entity> getEntity(String UUID) {
		return entities.stream()
			.filter(entity -> entity.getUniqueID().equals(UUID))
			.findAny();
	}

	@Override
	public Entity addEntity(Vector3D position, Item item) {
		//TODO: Implement
		return null;
	}

	@Override
	public Entity addClientEntity(EntityFactory factory) {
		//TODO: Implement
		return null;
	}

	@Override
	public <T extends Entity> T addClientEntity(T entity) {
		//TODO: Implement
		return null;
	}

	@Override
	public void removeEntity(Entity entity) {
		entities.remove(entity);
	}

	@Override
	public Set<Entity> getEntities(Cuboid bound) {
		//TODO: Implement
		return null;
	}

	@Override
	public void playSoundAtPosition(Vector3D position, Sound sound) {
		//TODO: Implement
	}

	@Override
	public String getID() {
		return "fakeWorld";
	}
}
