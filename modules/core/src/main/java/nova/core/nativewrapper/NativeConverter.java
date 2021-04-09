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

package nova.core.nativewrapper;

/**
 * A dual wrapper converts between a nova object and native object.
 *
 * Implementing a Loadable on a NativeConverter will allow it to handle loading events.
 *
 * @author TheSandromatic, Calclavia
 * @param <NOVA> The NOVA implementation class.
 * @param <NATIVE> The game implementation class.
 */
public interface NativeConverter<NOVA, NATIVE> {

	/**
	 * Get the class of the NOVA implementation.
	 *
	 * @return The class of the NOVA implementation.
	 */
	Class<NOVA> getNovaSide();

	/**
	 * Get the class of the game implementation.
	 *
	 * @return The class of the game implementation.
	 */
	Class<NATIVE> getNativeSide();

	/**
	 * Convert a game implementation object to the NOVA equivalent.
	 *
	 * @param nativeObj A game implementation object.
	 * @return The NOVA equivalent object.
	 */
	NOVA toNova(NATIVE nativeObj);

	/**
	 * Convert a NOVA implementation object to the game equivalent.
	 *
	 * @param novaObj A NOVA implementation object.
	 * @return The game equivalent object.
	 */
	NATIVE toNative(NOVA novaObj);
}
