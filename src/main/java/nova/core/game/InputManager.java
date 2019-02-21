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

package nova.core.game;

import com.google.common.collect.HashBiMap;

/**
 * Maps native key strokes to the internal NOVA key enum.
 *
 * @author Vic Nightfall
 * @see InputManager.Key
 */
public abstract class InputManager {

	protected HashBiMap<Integer, Key> keys = HashBiMap.create(Key.values().length);

	protected InputManager() {
		mapKeys();
	}

	/**
	 * Extend this in order to map native key codes to NOVA's key enum.
	 */
	public void mapKeys() {
		Key[] keyEnum = Key.values();
		for (int i = 0; i < keyEnum.length; i++) {
			keys.put(i, keyEnum[i]);
		}
	}

	public Key getKey(int nativeKeyCode) {
		return keys.getOrDefault(nativeKeyCode, Key.KEY_NONE);
	}

	public int getNativeKeyCode(Key key) {
		return keys.inverse().getOrDefault(key, 0);
	}

	/**
	 * Is the key current down?
	 *
	 * @param key The {@link Key key} to check.
	 * @return The pressed state of the key.
	 */
	public abstract boolean isKeyDown(Key key);

	public static enum Key {

		KEY_NONE, KEY_ESCAPE,

		KEY_1, KEY_2, KEY_3, KEY_4, KEY_5,
		KEY_6, KEY_7, KEY_8, KEY_9, KEY_0,

		KEY_Q, KEY_W, KEY_E, KEY_R, KEY_T,
		KEY_Y, KEY_U, KEY_I, KEY_O, KEY_P,
		KEY_A, KEY_S, KEY_D, KEY_F, KEY_G,
		KEY_H, KEY_J, KEY_K, KEY_L, KEY_Z,
		KEY_X, KEY_C, KEY_V, KEY_B, KEY_N,
		KEY_M,

		KEY_F1, KEY_F2, KEY_F3, KEY_F4, KEY_F5,
		KEY_F6, KEY_F7, KEY_F8, KEY_F9, KEY_F10,
		KEY_F11, KEY_F12,

		KEY_NUMPAD7, KEY_NUMPAD8, KEY_NUMPAD9,
		KEY_NUMPAD4, KEY_NUMPAD5, KEY_NUMPAD6,
		KEY_NUMPAD1, KEY_NUMPAD2, KEY_NUMPAD3,
		KEY_NUMPAD0,

		KEY_LEFT, KEY_UP, KEY_DOWN, KEY_RIGHT,

		KEY_RETURN, KEY_BACK, KEY_TAB, KEY_HOME,

		/**
		 * left alt key
		 */
		KEY_LMENU,

		/**
		 * right alt key
		 */
		KEY_RMENU,

		/**
		 * left windows key / meta key
		 */
		KEY_LMETA,

		/**
		 * right windows key / meta key
		 */
		KEY_RMETA,

		KEY_LSHIFT, KEY_RSHIFT,
		KEY_LCONTROL, KEY_RCONTROL,
		KEY_LBRACKET, KEY_RBRACKET,
		KEY_PRIOR, KEY_NEXT,

		KEY_COMMA, KEY_PERIOD, KEY_MINUS,
		KEY_EQUALS, KEY_CAPITAL, KEY_SPACE,
		KEY_SEMICOLON, KEY_APOSTROPHE, KEY_GRAVE,
		KEY_BACKSLASH, KEY_SLASH,
		KEY_MULTIPLY, KEY_NUMLOCK,
		KEY_SCROLL, KEY_ADD, KEY_SUBTRACT
	}
}
