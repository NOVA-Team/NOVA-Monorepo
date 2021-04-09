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
package nova.testutils.mod;

import nova.core.loader.Mod;
import org.slf4j.Logger;

import static nova.testutils.NovaAssertions.assertThat;

/**
 * @author ExE Boss
 */
@Mod(id = TestModWithLogger.MOD_ID, name = TestModWithLogger.NAME, version = TestModWithLogger.VERSION, novaVersion = "0.1.0")
public class TestModWithLogger {

	public static final String MOD_ID = "testModWithLogger";
	public static final String NAME = "Test Mod With Logger";
	public static final String VERSION = "0.1.0";

	public TestModWithLogger(Logger logger) {
		assertThat(logger.getName()).isEqualTo(MOD_ID + " (" + NAME + ')');
		logger.info("Hello World!");
	}
}
