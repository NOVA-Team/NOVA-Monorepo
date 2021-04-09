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

package nova.core.wrapper.mc.forge.v1_8.wrapper;

import nova.core.wrapper.mc.forge.v1_8.wrapper.DirectionConverter;
import net.minecraft.util.EnumFacing;
import nova.core.util.Direction;
import org.junit.Before;
import org.junit.Test;

import static nova.testutils.NovaAssertions.assertThat;

/**
 * Used to test {@link DirectionConverter}.
 *
 * @author ExE Boss
 */
public class DirectionConverterTest {

	DirectionConverter converter;

	@Before
	public void setUp() {
		converter = new DirectionConverter();
	}

	@Test
	public void testClasses() {
		assertThat(converter.getNovaSide()).isEqualTo(Direction.class);
		assertThat(converter.getNativeSide()).isEqualTo(EnumFacing.class);
	}

	@Test
	public void testToNova() {
		assertThat(converter.toNova(EnumFacing.DOWN)).isEqualTo(Direction.DOWN);
		assertThat(converter.toNova(EnumFacing.UP)).isEqualTo(Direction.UP);
		assertThat(converter.toNova(EnumFacing.NORTH)).isEqualTo(Direction.NORTH);
		assertThat(converter.toNova(EnumFacing.SOUTH)).isEqualTo(Direction.SOUTH);
		assertThat(converter.toNova(EnumFacing.WEST)).isEqualTo(Direction.WEST);
		assertThat(converter.toNova(EnumFacing.EAST)).isEqualTo(Direction.EAST);
		assertThat(converter.toNova(null)).isEqualTo(Direction.UNKNOWN);
	}

	@Test
	public void testToNative() {
		assertThat(converter.toNative(Direction.DOWN)).isEqualTo(EnumFacing.DOWN);
		assertThat(converter.toNative(Direction.UP)).isEqualTo(EnumFacing.UP);
		assertThat(converter.toNative(Direction.NORTH)).isEqualTo(EnumFacing.NORTH);
		assertThat(converter.toNative(Direction.SOUTH)).isEqualTo(EnumFacing.SOUTH);
		assertThat(converter.toNative(Direction.WEST)).isEqualTo(EnumFacing.WEST);
		assertThat(converter.toNative(Direction.EAST)).isEqualTo(EnumFacing.EAST);
		assertThat(converter.toNative(Direction.UNKNOWN)).isNull();
	}
}
