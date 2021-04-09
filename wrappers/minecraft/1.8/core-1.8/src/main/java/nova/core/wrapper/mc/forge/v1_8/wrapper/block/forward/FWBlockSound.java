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

import net.minecraft.block.Block;
import nova.core.block.component.BlockProperty;

/**
 * @author winsock, soniex2, ExE Boss
 */
public class FWBlockSound extends Block.SoundType {
	private final BlockProperty.BlockSound blockSound;

	/**
	 * Construct a new FWBlockSound
	 * @param blockSound The BlockSound to use.
	 */
	public FWBlockSound(BlockProperty.BlockSound blockSound) {
		super("", 1f, 1f);
		this.blockSound = blockSound;
	}

	@Override
	public String getBreakSound() {
		return blockSound.getSound(BlockProperty.BlockSound.BlockSoundTrigger.BREAK)
			.map(sound -> (sound.domain.isEmpty() && !sound.name.contains(".")) ? "dig." + sound.name : sound.getID())
			.orElseGet(super::getBreakSound);
	}

	@Override
	public String getStepSound() {
		return blockSound.getSound(BlockProperty.BlockSound.BlockSoundTrigger.WALK)
			.map(sound -> (sound.domain.isEmpty() && !sound.name.contains(".")) ? "step." + sound.name : sound.getID())
			.orElseGet(super::getStepSound);
	}

	@Override
	public String getPlaceSound() {
		return blockSound.getSound(BlockProperty.BlockSound.BlockSoundTrigger.PLACE)
			.map(sound -> sound.domain.isEmpty() ? sound.name : sound.getID())
			.orElseGet(this::getBreakSound); // By default MC uses the block break sound for block placement
	}
}
