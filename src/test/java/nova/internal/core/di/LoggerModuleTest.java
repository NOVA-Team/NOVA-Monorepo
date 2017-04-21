/*
 * Copyright (c) 2017 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License version 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
 */
package nova.internal.core.di;

import nova.internal.core.Game;
import nova.wrappertests.NovaLauncherTestFactory;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author ExE Boss
 */
public class LoggerModuleTest {

	@Before
	public void setup() throws NoSuchFieldException, IllegalAccessException {
		Field field = LoggerModule.class.getDeclaredField("defaultNameChangeable");
		field.setAccessible(true);
		field.set(null, true);
		field.setAccessible(false);
	}

	@Test
	public void testCustomLoggerName() {
		assertThat(LoggerModule.getDefaultName()).isEqualTo("NOVA");
		LoggerModule.setDefaultName("Unset");
		assertThat(LoggerModule.getDefaultName()).isEqualTo("Unset");
		LoggerModule.setDefaultName("Custom");
		NovaLauncherTestFactory.createDummyLauncher();
		assertThat(Game.logger().getName()).isEqualTo("Custom");
		assertThat(LoggerModule.getDefaultName()).isEqualTo("Custom").isEqualTo(Game.logger().getName());
	}
}
