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

package nova.core.component.fluid;

import nova.internal.core.Game;
import nova.wrappertests.NovaLauncherTestFactory;
import org.junit.Before;
import org.junit.Test;

import static nova.testutils.NovaAssertions.assertThat;

/**
 * @author magik6k
 * @date 4/18/15.
 */
public class FluidTest {
	@Before
	public void setUp() {
		NovaLauncherTestFactory.createDummyLauncher();
	}

	@Test
	public void testTypes() {
		Fluid fluid1 = Game.fluids().water.build().setAmount(100);
		Fluid fluid2 = Game.fluids().water.build().setAmount(10);
		Fluid fluid3 = Game.fluids().lava.build().setAmount(100);

		assertThat(fluid1.sameType(fluid2)).isTrue();
		assertThat(fluid1.sameType(fluid3)).isFalse();
	}

	@Test
	public void testEqual() {
		Fluid fluid1 = Game.fluids().water.build().setAmount(100);
		Fluid fluid2 = Game.fluids().water.build().setAmount(100);
		Fluid fluid3 = Game.fluids().water.build().setAmount(10);
		Fluid fluid4 = Game.fluids().lava.build().setAmount(100);

		assertThat(fluid1 == null).isFalse();
		assertThat(fluid1.equals(new Object())).isFalse();
		assertThat(fluid1.equals(fluid2)).isTrue();
		assertThat(fluid1.equals(fluid3)).isFalse();
		assertThat(fluid1.equals(fluid4)).isFalse();
	}

	@Test
	public void testClone() {
		Fluid fluid1 = Game.fluids().water.build().setAmount(100);
		Fluid cloned = fluid1.clone();

		assertThat(fluid1.equals(cloned)).isTrue();
		assertThat(fluid1).isNotSameAs(cloned);
	}

	@Test
	public void testWithAmount() {
		Fluid fluid1 = Game.fluids().water.build().setAmount(100);
		Fluid fluid2 = fluid1.withAmount(10);

		assertThat(fluid1.sameType(fluid2)).isTrue();
		assertThat(fluid1.equals(fluid2)).isFalse();
		assertThat(fluid2.amount()).isEqualTo(10);
	}

	@Test
	public void testAmountSetting() {
		Fluid fluid1 = Game.fluids().water.build().setAmount(100);

		fluid1.setAmount(10);
		assertThat(fluid1.amount()).isEqualTo(10);

		fluid1.setAmount(-10);
		assertThat(fluid1.amount()).isEqualTo(1);
	}

	@Test
	public void testAmountAdding() {
		Fluid fluid1 = Game.fluids().water.build().setAmount(10);

		assertThat(fluid1.add(10)).isEqualTo(10);
		assertThat(fluid1.amount()).isEqualTo(20);

		assertThat(fluid1.add(-30)).isEqualTo(-19);
		assertThat(fluid1.amount()).isEqualTo(1);

		fluid1.setAmount(10);

		assertThat(fluid1.remove(5)).isEqualTo(5);
		assertThat(fluid1.amount()).isEqualTo(5);

		assertThat(fluid1.remove(10)).isEqualTo(4);
		assertThat(fluid1.amount()).isEqualTo(1);
	}
}
