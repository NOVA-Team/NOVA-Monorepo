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
package nova.wrappertests;

import nova.internal.core.Game;
import nova.internal.core.bootstrap.DependencyInjectionEntryPoint;
import nova.internal.core.launch.NovaLauncher;
import nova.testutils.mod.NoLoadableTestMod;
import nova.testutils.mod.NonAnnotatedTestMod;
import nova.testutils.mod.TestMod;
import org.junit.Test;

import static nova.testutils.NovaAssertions.assertThat;

/**
 * @author ExE Boss
 */
public abstract class AbstractNovaLauncherTest extends NovaLauncherTestFactory {

	public AbstractNovaLauncherTest() {
		super(TestMod.class, NonAnnotatedTestMod.class, NoLoadableTestMod.class);
	}

	public void doLaunchAssert(NovaLauncher launcher) {
		assertThat(launcher.getModClasses())
			.hasSize(2)
			.containsValue(TestMod.class)
			.containsValue(NoLoadableTestMod.class);
	}

	@Test
	public void testLaunching() {
		doLaunchAssert(createLauncher());
	}

	@Test
	public void testResolveGame() {
		DependencyInjectionEntryPoint diep = new DependencyInjectionEntryPoint();

		getModules().forEach(diep::install);

		Game game = diep.init();

		assertThat(game).isNotNull();
	}
}
