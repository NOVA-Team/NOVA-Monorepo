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

package nova.core.wrapper.mc.forge.v1_8.depmodules;

import nova.core.game.GameInfo;
import se.jbee.inject.Dependency;
import se.jbee.inject.Injector;
import se.jbee.inject.Supplier;
import se.jbee.inject.bind.BinderModule;

public class GameInfoModule extends BinderModule {
	private static final GameInfo minecraft = new GameInfo("minecraft", "1.7.10");

	@Override
	protected void declare() {
		bind(GameInfo.class).toSupplier(GameInfoSupplier.class);
	}

	public static class GameInfoSupplier implements Supplier<GameInfo> {
		@Override
		public GameInfo supply(Dependency<? super GameInfo> dependency, Injector injector) {
			return minecraft;
		}
	}
}
