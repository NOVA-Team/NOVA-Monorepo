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
package nova.core.util.unit;

import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author ExE Boss
 */
public class UnitPrefixTest {

	public UnitPrefixTest() {
	}

	@Test
	public void testGetName() {
		assertThat(UnitPrefix.MEGA.getName(false)).isEqualTo(UnitPrefix.MEGA.name);
		assertThat(UnitPrefix.MEGA.getName(true)).isEqualTo(UnitPrefix.MEGA.symbol);
	}

	@Test
	public void testProcess() {
		assertThat(UnitPrefix.MILLI.process(0.001)).isEqualTo(1);
		assertThat(UnitPrefix.BASE.process(1)).isEqualTo(1);
		assertThat(UnitPrefix.KILO.process(1000)).isEqualTo(1);
	}

	@Test
	public void testAboveBellow() {
		assertThat(UnitPrefix.BASE.isAbove(10)).isTrue();
		assertThat(UnitPrefix.BASE.isAbove(0.1)).isFalse();

		assertThat(UnitPrefix.BASE.isBellow(10)).isFalse();
		assertThat(UnitPrefix.BASE.isBellow(0.1)).isTrue();
	}

	@Test
	public void testGetPrefixes() {
		List<UnitPrefix> prefixes = UnitPrefix.getPrefixes();
		assertThat(prefixes).contains(UnitPrefix.MILLI,
		                              UnitPrefix.MICRO,
		                              UnitPrefix.BASE,
		                              UnitPrefix.KILO,
		                              UnitPrefix.MEGA,
		                              UnitPrefix.GIGA,
		                              UnitPrefix.TERA,
		                              UnitPrefix.PETA,
		                              UnitPrefix.EXA,
		                              UnitPrefix.ZETTA,
		                              UnitPrefix.YOTTA);
	}
}
