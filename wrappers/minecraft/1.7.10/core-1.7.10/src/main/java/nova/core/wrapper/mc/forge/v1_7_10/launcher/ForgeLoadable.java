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

package nova.core.wrapper.mc.forge.v1_7_10.launcher;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
 * A mod interface implemented to receive FML mod loading event calls.
 *
 * @author ExE Boss
 */
public interface ForgeLoadable {

	/**
	 * Pre‑initialize the wrapper code.
	 *
	 * @param evt The Minecraft Forge pre-initialization event
	 */
	default void preInit(FMLPreInitializationEvent evt) {
	}

	/**
	 * Initialize the wrapper code.
	 *
	 * @param evt The Minecraft Forge initialization event
	 */
	default void init(FMLInitializationEvent evt) {
	}

	/**
	 * Post-initialize the wrapper code.
	 *
	 * @param evt The Minecraft Forge post-initialization event
	 */
	default void postInit(FMLPostInitializationEvent evt) {
	}
}
