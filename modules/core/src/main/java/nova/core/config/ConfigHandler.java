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

package nova.core.config;

import java.util.Optional;

/**
 * Implement this on classes annotated with {@link ConfigHolder}
 * to add additional custom handling to your config files.
 *
 * {@link #handle(com.typesafe.config.Config) handle(Config)}
 * is called after {@link Configuration} loads all fields
 * annotated with {@link Config}.
 *
 * @author ExE Boss
 */
public interface ConfigHandler {

	/**
	 * This method allows you to do custom handling on your config files.
	 *
	 * It is called after {@link Configuration} loads all fields
	 * annotated with {@link Config}.
	 *
	 * @param config The config object, which is actually immutable,
	 * and any setter call creates a new instance with the change.
	 * @return The modified config object, or an empty optional
	 * to discard your changes.
	 */
	Optional<com.typesafe.config.Config> handle(com.typesafe.config.Config config);
}
