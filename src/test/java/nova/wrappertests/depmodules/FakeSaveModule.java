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

package nova.wrappertests.depmodules;

import nova.core.retention.Storable;
import nova.core.util.registry.RetentionManager;
import se.jbee.inject.bind.BinderModule;

import java.io.File;

/**
 * @author Calclavia
 */
public class FakeSaveModule extends BinderModule {

	@Override
	protected void declare() {
		bind(RetentionManager.class).to(FakeRetentionManager.class);
	}

	public static class FakeRetentionManager extends RetentionManager {
		@Override
		public void save(String filename, Storable storable) {

		}

		@Override
		public void load(String filename, Storable storable) {

		}

		@Override
		public File getSaveDirectory() {
			return null;
		}

		@Override
		public void init() {

		}
	}

}
