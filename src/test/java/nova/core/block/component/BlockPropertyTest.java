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

package nova.core.block.component;

import nova.core.block.Block;
import nova.core.block.BlockFactory;
import nova.core.block.component.BlockProperty.BlockSound.BlockSoundTrigger;
import nova.core.sound.Sound;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author ExE Boss
 */
public class BlockPropertyTest {

	@Test
	public void testHardness() {
		BlockProperty.Hardness hardness = new BlockProperty.Hardness().setHardness(1.0);
		assertThat(hardness.getHardness()).isEqualTo(1.0);
	}

	@Test
	public void testResistance() {
		BlockProperty.Resistance resistance = new BlockProperty.Resistance().setResistance(1.0);
		assertThat(resistance.getResistance()).isEqualTo(1.0);
	}

	@Test
	public void testBlockSound() {
		BlockProperty.BlockSound sound = new BlockProperty.BlockSound();
		sound.setBlockSound(BlockSoundTrigger.BREAK, new Sound("test", "break"));
		sound.setBlockSound(BlockSoundTrigger.PLACE, new Sound("test", "place"));
		sound.setBlockSound(BlockSoundTrigger.WALK, new Sound("test", "walk"));
		sound.setBlockSound(BlockSoundTrigger.CUSTOM_TRIGGER, new Sound("test", "custom"));
		sound.setCustomBlockSound("test:custom", new Sound("test", "custom"));

		assertThat(sound.getSound(BlockSoundTrigger.BREAK)).contains(new Sound("test", "break"));
		assertThat(sound.getSound(BlockSoundTrigger.PLACE)).contains(new Sound("test", "place"));
		assertThat(sound.getSound(BlockSoundTrigger.WALK)).contains(new Sound("test", "walk"));
		assertThat(sound.getSound(BlockSoundTrigger.CUSTOM_TRIGGER)).isEmpty();
		assertThat(sound.getCustomSound("test:custom")).contains(new Sound("test", "custom"));
	}

	@Test
	public void testOpacity() {
		BlockProperty.Opacity opacity = new BlockProperty.Opacity().setOpaque();
		assertThat(opacity.getOpacity()).isEqualTo(1.0);
		assertThat(opacity.isOpaque()).isTrue();
		assertThat(opacity.isTransparent()).isFalse();

		opacity.setTransparent();
		assertThat(opacity.getOpacity()).isEqualTo(0.0);
		assertThat(opacity.isOpaque()).isFalse();
		assertThat(opacity.isTransparent()).isTrue();

		opacity.setOpacity(0.5);
		assertThat(opacity.getOpacity()).isEqualTo(0.5);
		assertThat(opacity.isOpaque()).isFalse();
		assertThat(opacity.isTransparent()).isTrue();
	}

	@Test
	public void testReplaceable() {
		BlockProperty.Replaceable replaceable = new BlockProperty.Replaceable();
		assertThat(replaceable.testBlock(Optional.empty())).isTrue();

		replaceable.setReplaceFilter(block -> block.isPresent());
		assertThat(replaceable.testBlock(Optional.empty())).isFalse();
		assertThat(replaceable.testBlock(Optional.of(new BlockFactory("test", Block::new)))).isTrue();
	}
}
