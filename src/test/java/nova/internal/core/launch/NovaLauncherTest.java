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

package nova.internal.core.launch;

import nova.internal.core.Game;
import nova.internal.core.bootstrap.DependencyInjectionEntryPoint;
import nova.internal.core.di.LoggerModule;
import nova.testutils.mod.TestMod;
import nova.testutils.mod.TestModDuplicate;
import nova.testutils.mod.TestModWithLogger;
import nova.testutils.mod.TestModWithMismatchedDependency;
import nova.testutils.mod.TestModWithMismatchedDependencyPattern;
import nova.testutils.mod.TestModWithMissingDependency;
import nova.testutils.mod.TestModWithMissingOptionalDependency;
import nova.wrappertests.AbstractNovaLauncherTest;
import nova.wrappertests.depmodules.FakeClientModule;
import nova.wrappertests.depmodules.FakeComponentModule;
import nova.wrappertests.depmodules.FakeGameInfoModule;
import nova.wrappertests.depmodules.FakeKeyModule;
import nova.wrappertests.depmodules.FakeLanguageModule;
import nova.wrappertests.depmodules.FakeNetworkModule;
import nova.wrappertests.depmodules.FakeSaveModule;
import nova.wrappertests.depmodules.FakeTickerModule;
import org.junit.Test;
import se.jbee.inject.bootstrap.Bundle;

import java.util.Arrays;
import java.util.List;

import static nova.testutils.NovaAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

public class NovaLauncherTest extends AbstractNovaLauncherTest {

	@Override
	public List<Class<? extends Bundle>> getModules() {
		return Arrays.<Class<? extends Bundle>>asList(
			FakeClientModule.class,
			FakeKeyModule.class,
			FakeLanguageModule.class,
			FakeNetworkModule.class,
			FakeSaveModule.class,
			FakeTickerModule.class,
			FakeComponentModule.class,
			FakeGameInfoModule.class
		);
	}

	@Override
	@Test
	public void testLaunching() {
		doLaunchAssert(createLauncher());
	}

	@Override
	@Test
	public void testResolveGame() {
		DependencyInjectionEntryPoint diep = new DependencyInjectionEntryPoint();

		getModules().forEach(diep::install);

		Game game = diep.init();

		assertThat(game).isNotNull();
	}

	@Test(expected = InitializationException.class)
	public void testMissingDepencency() {
		createLauncher(TestModWithMissingDependency.class);
	}

	@Test
	public void testMissingOptionalDepencency() {
		NovaLauncher launcher = createLauncher(TestModWithMissingOptionalDependency.class);
		assertThat(launcher.getModClasses())
			.hasSize(1)
			.containsValue(TestModWithMissingOptionalDependency.class);
	}

	@Test(expected = InitializationException.class)
	public void testMismatchedDepencency() {
		createLauncher(TestModWithMismatchedDependency.class, TestModWithMismatchedDependencyPattern.class, TestMod.class);
	}

	@Test(expected = InitializationException.class)
	public void testDuplicateModIDs() {
		createLauncher(TestMod.class, TestModDuplicate.class);
	}

	@Test
	public void testModLogger() {
		createLauncher(TestModWithLogger.class);
	}
}
