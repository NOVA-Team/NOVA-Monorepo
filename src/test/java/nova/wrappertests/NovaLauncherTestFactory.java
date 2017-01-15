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

package nova.wrappertests;

import nova.core.block.Block;
import nova.core.block.BlockFactory;
import nova.core.util.id.StringIdentifier;
import nova.internal.core.Game;
import nova.internal.core.bootstrap.DependencyInjectionEntryPoint;
import nova.internal.core.launch.NovaLauncher;
import nova.wrappertests.depmodules.FakeClientModule;
import nova.wrappertests.depmodules.FakeComponentModule;
import nova.wrappertests.depmodules.FakeGameInfoModule;
import nova.wrappertests.depmodules.FakeKeyModule;
import nova.wrappertests.depmodules.FakeLanguageModule;
import nova.wrappertests.depmodules.FakeNetworkModule;
import nova.wrappertests.depmodules.FakeRenderModule;
import nova.wrappertests.depmodules.FakeSaveModule;
import nova.wrappertests.depmodules.FakeTickerModule;
import se.jbee.inject.bootstrap.Bundle;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * A factory that allows a mod to easily unit test launching with NOVA.
 * @author Calclavia
 */
public class NovaLauncherTestFactory {

	public final Class<?>[] modClasses;

	public NovaLauncherTestFactory(Class<?>... modClasses) {
		this.modClasses = modClasses;
	}

	public List<Class<? extends Bundle>> getModules() {
		return Arrays.<Class<? extends Bundle>>asList(
			FakeClientModule.class,
			FakeKeyModule.class,
			FakeLanguageModule.class,
			FakeNetworkModule.class, //NetworkManager calls into FML code in the class instantiation, so we create a fake.
			FakeRenderModule.class,
			FakeSaveModule.class,
			FakeTickerModule.class,
			FakeComponentModule.class,
			FakeGameInfoModule.class
		);
	}

	/**
	 * Creates a fake launcher to allow mods to unit test.
	 * @return
	 */
	public NovaLauncher createLauncher() {
		DependencyInjectionEntryPoint diep = new DependencyInjectionEntryPoint();

		getModules().forEach(diep::install);

		NovaLauncher launcher = new NovaLauncher(diep, new HashSet<>(Arrays.asList(modClasses)));

		Game.inject(diep);

		/**
		 * Register fake air block
		 */
		Game.blocks().register(new BlockFactory(new StringIdentifier("air"), Block::new, evt -> {}));

		launcher.generateDependencies();
		launcher.load();

		//Handle all content loads
		Game.blocks().init();
		Game.items().init();
		Game.entities().init();
		Game.render().init();
		Game.language().init();
		Game.recipes().init();

		launcher.preInit();
		launcher.init();
		launcher.postInit();
		return launcher;
	}

}
