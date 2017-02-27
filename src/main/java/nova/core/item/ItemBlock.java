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
 */package nova.core.item;

import nova.core.block.Block;
import nova.core.block.BlockFactory;
import nova.core.block.component.BlockProperty;
import nova.core.entity.Entity;
import nova.core.util.Direction;
import nova.core.world.World;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.Optional;

/**
 * An ItemBlock is an Item that is meant to be used to place blocks.
 *
 * @author Calclavia
 */
public class ItemBlock extends Item {

	public final BlockFactory blockFactory;

	public ItemBlock(BlockFactory blockFactory) {
		this.blockFactory = blockFactory;
		events.on(UseEvent.class).bind(this::onUse);
	}

	@Override
	public String getUnlocalizedName() {
		return blockFactory.getLocalizedName();
	}

	@Override
	public String getLocalizedName() {
		return blockFactory.getLocalizedName();
	}

	protected void onUse(UseEvent evt) {
		Optional<Block> opBlock = evt.entity.world().getBlock(evt.position);

		if (opBlock.isPresent()) {
			Block block = opBlock.get();
			Vector3D placePos = block.shouldDisplacePlacement() ? evt.position.add(evt.side.toVector()) : evt.position;
			if (onPrePlace(evt.entity, evt.entity.world(), placePos, evt.side, evt.hit)) {
				evt.action = onPostPlace(evt.entity, evt.entity.world(), placePos, evt.side, evt.hit);
			}
		}
	}

	protected boolean onPrePlace(Entity entity, World world, Vector3D placePos, Direction side, Vector3D hit) {
		Optional<Block> checkBlock = world.getBlock(placePos);
		if (checkBlock.isPresent() && checkBlock.get().canReplace()) {
			return world.setBlock(placePos, blockFactory);
		}
		return false;
	}

	protected boolean onPostPlace(Entity entity, World world, Vector3D placePos, Direction side, Vector3D hit) {
		Optional<Block> opBlock = world.getBlock(placePos);
		if (opBlock.isPresent() && opBlock.get().sameType(blockFactory)) {
			//TODO: What if the block is NOT placed by a player?
			opBlock.get().events.publish(new Block.PlaceEvent(entity, side, hit, this));
			opBlock.get().components.getOp(BlockProperty.BlockSound.class)
				.flatMap(sound -> sound.getSound(BlockProperty.BlockSound.BlockSoundTrigger.PLACE))
				.ifPresent(sound -> world.playSoundAtPosition(placePos, sound));
		}

		addCount(-1);

		return true;
	}
}
