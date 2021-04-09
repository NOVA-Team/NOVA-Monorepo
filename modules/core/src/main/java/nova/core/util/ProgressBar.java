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

package nova.core.util;

import nova.core.loader.Mod;

/**
 * A progress bar used to show mod loading.
 *
 * The default implementation does nothing.
 * It is up to wrappers to implement functionality.
 *
 * @author ExE Boss
 */
public interface ProgressBar {

	/**
	 * A progress bar implementation that does nothing.
	 */
	public static final class NullProgressBar extends AbstractProgressBar {
		@Override
		protected void stepImpl(String message) {}
	};

	/**
	 * Advance one step.
	 *
	 * @param message The message to show for the current step.
	 *
	 * @throws IllegalStateException If {@link #finish()} has been called.
	 */
	void step(String message);

	/**
	 * Finish the progress bar.
	 *
	 * Calling this method a second time does nothing.
	 */
	void finish();

	/**
	 * Check if the progress bar has been finished.
	 *
	 * @return If the progress bar has finished.
	 */
	boolean isFinished();

	/**
	 * Advance one step.
	 *
	 * The default implementation is the same as calling:
	 * {@link #step(java.lang.String) ProgressBar.step(state + ": " + clazz)}
	 *
	 * @param clazz The mod class
	 *
	 * @throws IllegalStateException If {@link #finish()} has been called.
	 */
	default void step(Class<?> clazz) {
		step(toStringMod(clazz));
	}

	/**
	 * Advance one step.
	 *
	 * The default implementation is the same as calling:
	 * {@link #step(java.lang.String) ProgressBar.step(message + ": " + clazz)}
	 *
	 * @param message The message to display before {@code clazz}
	 * @param clazz The mod class
	 *
	 * @throws IllegalStateException If {@link #finish()} has been called.
	 */
	default void step(String message, Class<?> clazz) {
		step((message == null || message.isEmpty() ? "" : message + ": ") + toStringMod(clazz));
	}

	/**
	 * Advance one step.
	 *
	 * The default implementation is the same as calling:
	 * {@link #step(java.lang.String) ProgressBar.step(clazz + ": " + message)}
	 *
	 * @param message The message to display before {@code clazz}
	 * @param clazz The mod class
	 *
	 * @throws IllegalStateException If {@link #finish()} has been called.
	 */
	default void step(Class<?> clazz, String message) {
		step(toStringMod(clazz) + (message == null || message.isEmpty() ? "" : ": " + message));
	}

	static String toStringMod(Class<?> clazz) {
		if (clazz == null) return null;
		if (clazz.isAnnotationPresent(Mod.class)) return clazz.getAnnotation(Mod.class).name();
		return clazz.getSimpleName();
	}
}
