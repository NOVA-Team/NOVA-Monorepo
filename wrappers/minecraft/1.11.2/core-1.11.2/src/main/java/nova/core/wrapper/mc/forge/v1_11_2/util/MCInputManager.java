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

package nova.core.wrapper.mc.forge.v1_11_2.util;

import nova.core.game.InputManager;
import org.lwjgl.input.Keyboard;

/**
 * The MC KeyManager
 * @author Calclavia
 */
// TODO: Does not work yet. Need a full map between the input and LWGL input.
public class MCInputManager extends InputManager {

	@Override
	public void mapKeys() {
		// Map jlwgl input to NOVA Keys, slightly hacky but functional.
		for (Key key : Key.values()) {
			try {
				keys.put(Keyboard.class.getDeclaredField(key.name()).getInt(null), key);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean isKeyDown(Key key) {
		// TODO: Sync this withPriority server side for server-side events. Need a packet manager
		return Keyboard.isKeyDown(getNativeKeyCode(key));
	}
}
