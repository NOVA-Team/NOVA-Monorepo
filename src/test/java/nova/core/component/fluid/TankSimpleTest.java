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

import nova.core.retention.Data;
import nova.internal.core.Game;
import nova.wrappertests.NovaLauncherTestFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static nova.testutils.NovaAssertions.assertThat;

/**
 * @author magik6k
 */
public class TankSimpleTest {

	@Before
	public void setUp() {
		NovaLauncherTestFactory.createDummyLauncher();
	}

	@Test
	public void testBasic() {
		TankSimple tank = new TankSimple(150);

		assertThat(tank.hasFluid()).isFalse();
		assertThat(tank.addFluid(Game.fluids().water.build().setAmount(100))).isEqualTo(100);
		assertThat(tank.getFluidAmount()).isEqualTo(100);
		assertThat(tank.addFluid(Game.fluids().lava.build().setAmount(100))).isEqualTo(0);
		assertThat(tank.getFluidAmount()).isEqualTo(100);
		assertThat(tank.addFluid(Game.fluids().water.build().setAmount(100))).isEqualTo(50);
		assertThat(tank.getFluidAmount()).isEqualTo(150);

		assertThat(tank.removeFluid(100)).isEqualTo(Optional.of(Game.fluids().water.build().withAmount(100)));
		assertThat(tank.getFluidAmount()).isEqualTo(50);
		assertThat(tank.removeFluid(100)).isEqualTo(Optional.of(Game.fluids().water.build().withAmount(50)));
		assertThat(tank.getFluidAmount()).isEqualTo(0);

		assertThat(tank.addFluid(Game.fluids().lava.build().setAmount(100))).isEqualTo(100);
		assertThat(tank.getFluidAmount()).isEqualTo(100);
	}

	@Test
	public void storeTest() {
		TankSimple tank = new TankSimple(150);
		assertThat(tank.addFluid(Game.fluids().water.build().setAmount(100))).isEqualTo(100);

		Data tankData = new Data();
		tank.save(tankData);

		//TODO: Should store capacity?
		tank = new TankSimple(150);
		tank.load(tankData);

		assertThat(tank.hasFluid()).isTrue();
		assertThat(tank.hasFluidType(Game.fluids().water.build())).isTrue();
		assertThat(tank.getFluid().get().equals(Game.fluids().water.build().setAmount(100))).isTrue();
	}

}
