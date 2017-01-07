/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nova.internal.core.launch;

import nova.core.loader.Mod;

/**
 * A progress bar used to show mod loading.
 *
 * The default implementation does nothing.
 * It is up to wrappers to implement functionality.
 *
 * @author ExE Boss
 */
public abstract class ProgressBar {

	/**
	 * A progress bar implementation that does nothing.
	 */
	public static final ProgressBar NULL_PROGRESS_BAR = new ProgressBar() {
		@Override
		public void step(String s) {}
	};

	/**
	 * Advance one step.
	 *
	 * @param message The message to show for the current step.
	 */
	public abstract void step(String message);

	/**
	 * Advance one step.
	 *
	 * The default implementation is the same as calling:
	 * {@link #step(java.lang.String) ProgressBar.step(state + ": " + clazz)}
	 *
	 * @param clazz The mod class
	 */
	public void step(Class<?> clazz) {
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
	 */
	public void step(String message, Class<?> clazz) {
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
	 */
	public void step(Class<?> clazz, String message) {
		step(toStringMod(clazz) + (message == null || message.isEmpty() ? "" : ": " + message));
	}

	protected static String toStringMod(Class<?> clazz) {
		if (clazz == null) return null;
		if (clazz.isAnnotationPresent(Mod.class)) return clazz.getAnnotation(Mod.class).name();
		return clazz.getSimpleName();
	}
}
