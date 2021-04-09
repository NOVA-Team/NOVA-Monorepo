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

import nova.core.wrapper.mc.forge.v1_8.wrapper.VectorConverter;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.Before;
import org.junit.Test;

import static nova.testutils.NovaAssertions.assertThat;

/**
 * Used to test {@link VectorConverter}.
 *
 * @author ExE Boss
 */
public class VectorConverterTest {

	VectorConverter converter;

	@Before
	public void setUp() {
		converter = new VectorConverter();
	}

	@Test
	public void testClasses() {
		assertThat(converter.getNovaSide()).isEqualTo(Vector3D.class);
		assertThat(converter.getNativeSide()).isEqualTo(BlockPos.class);
	}

	@Test
	public void testToNova() {
		for (int x = -1; x <= 1; x++)
			for (int y = -1; y <= 1; y++)
				for (int z = -1; z <= 1; z++)
					assertThat(converter.toNova(new BlockPos(x, y, z))).isEqualTo(new Vector3D(x, y, z));

		for (int x = -1; x <= 1; x++)
			for (int y = -1; y <= 1; y++)
				for (int z = -1; z <= 1; z++)
					assertThat(converter.toNova(new Vec3(x, y, z))).isEqualTo(new Vector3D(x, y, z));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testToNative() {
		for (int x = -1; x <= 1; x++)
			for (int y = -1; y <= 1; y++)
				for (int z = -1; z <= 1; z++)
					assertThat(converter.toNative(new Vector3D(x, y, z))).isEqualTo(new BlockPos(x, y, z));
	}
}
