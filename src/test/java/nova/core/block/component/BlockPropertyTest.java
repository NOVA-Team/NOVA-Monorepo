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

import nova.core.sound.Sound;
import org.junit.Test;

import static nova.testutils.NovaAssertions.assertThat;

/**
 * @author ExE Boss
 */
public class BlockPropertyTest {

	@Test
	public void testHardness() {
		BlockProperty.Hardness hardness = new BlockProperty.Hardness();
		assertThat(hardness.getHardness()).isEqualTo(1.0);

		hardness.setHardness(10.0);
		assertThat(hardness.getHardness()).isEqualTo(10.0);
	}

	@Test
	public void testResistance() {
		BlockProperty.Resistance resistance = new BlockProperty.Resistance();
		assertThat(resistance.getResistance()).isEqualTo(1.0);

		resistance.setResistance(10.0);
		assertThat(resistance.getResistance()).isEqualTo(10.0);
	}

	@Test
	public void testBlockSound() {
		BlockProperty.BlockSound sound = new BlockProperty.BlockSound();
		sound.setBlockSound(BlockProperty.BlockSound.BlockSoundTrigger.BREAK, new Sound("test", "break"));
		sound.setBlockSound(BlockProperty.BlockSound.BlockSoundTrigger.PLACE, new Sound("test", "place"));
		sound.setBlockSound(BlockProperty.BlockSound.BlockSoundTrigger.WALK, new Sound("test", "walk"));
		sound.setBlockSound(BlockProperty.BlockSound.BlockSoundTrigger.CUSTOM_TRIGGER, new Sound("test", "custom"));
		sound.setCustomBlockSound("test:custom", new Sound("test", "custom"));

		assertThat(sound.getSound(BlockProperty.BlockSound.BlockSoundTrigger.BREAK)).contains(new Sound("test", "break"));
		assertThat(sound.getSound(BlockProperty.BlockSound.BlockSoundTrigger.PLACE)).contains(new Sound("test", "place"));
		assertThat(sound.getSound(BlockProperty.BlockSound.BlockSoundTrigger.WALK)).contains(new Sound("test", "walk"));
		assertThat(sound.getSound(BlockProperty.BlockSound.BlockSoundTrigger.CUSTOM_TRIGGER)).isEmpty();
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

		opacity.setOpacity(-0.5);
		assertThat(opacity.getOpacity()).isEqualTo(0.0);
		assertThat(opacity.isOpaque()).isFalse();
		assertThat(opacity.isTransparent()).isTrue();

		opacity.setOpacity(1.5);
		assertThat(opacity.getOpacity()).isEqualTo(1.0);
		assertThat(opacity.isOpaque()).isTrue();
		assertThat(opacity.isTransparent()).isFalse();
	}

	@Test
	public void testReplaceable() {
		BlockProperty.Replaceable replaceable = new BlockProperty.Replaceable();
		assertThat(replaceable.isReplaceable()).isTrue();

		replaceable.setReplaceable(() -> false);
		assertThat(replaceable.isReplaceable()).isFalse();
	}
}
