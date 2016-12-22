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
	 * @param state
	 * @param clazz
	 */
	public void step(Class<?> clazz) {
		step(toStringMod(clazz));
	}

	/**
	 * Advance one step.
	 *
	 * The default implementation is the same as calling:
	 * {@link #step(java.lang.String) ProgressBar.step(state + ": " + clazz)}
	 *
	 * @param state
	 * @param clazz
	 */
	public void step(String state, Class<?> clazz) {
		step((state == null || state.isEmpty() ? "" : state + ": ") + toStringMod(clazz));
	}

	protected static String toStringMod(Class<?> clazz) {
		if (clazz == null) return null;
		if (clazz.isAnnotationPresent(Mod.class)) return clazz.getAnnotation(Mod.class).name();
		return clazz.getSimpleName();
	}
}
