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

package nova.internal.core.launch;

import nova.core.util.ProgressBar;
import nova.internal.core.bootstrap.DependencyInjectionEntryPoint;
import nova.internal.core.util.InjectionUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Calclavia
 */
public class ModLoader<ANNOTATION extends Annotation> {

	protected final DependencyInjectionEntryPoint diep;
	private Optional<ANNOTATION> currentMod = Optional.empty();

	/**
	 * The type of the annotation
	 */
	protected final Class<ANNOTATION> annotationType;

	/**
	 * Mod Java classes
	 */
	protected final Map<ANNOTATION, Class<?>> javaClasses;

	/**
	 * Mod Scala classes
	 */
	protected final Map<ANNOTATION, Class<?>> scalaClasses;

	/**
	 * Holds the instances of mods
	 */
	protected Map<ANNOTATION, Object> mods;

	protected List<Object> orderedMods;

	public ModLoader(Class<ANNOTATION> annotationType, DependencyInjectionEntryPoint diep, Set<Class<?>> modClasses) {
		this.diep = diep;
		this.annotationType = annotationType;

		/**
		 * Final Java Classes
		 */
		javaClasses = modClasses.stream()
			.filter(clazz -> clazz.getAnnotation(annotationType) != null)
			.collect(Collectors.toMap((clazz) -> clazz.getAnnotation(annotationType), Function.identity()));

		/**
		 * Find Scala Singleton Classes
		 */
		scalaClasses = modClasses.stream()
			.filter(c -> {
				try {
					Class.forName((c.getCanonicalName() + "$"));
					return true;
				} catch (Exception e) {
					return false;
				}
			})
			.collect(Collectors.toMap(c -> c.getAnnotation(annotationType),
					c -> {
						try {
							return Class.forName(c.getCanonicalName() + "$");
						} catch (Exception e) {
							e.printStackTrace();
							throw new ExceptionInInitializerError("Failed to load NOVA mod: " + c);
						}
					}
				)
			);
	}

	public void load() {
		this.load(new ProgressBar.NullProgressBar(), true);
	}

	public void load(ProgressBar progressBar) {
		this.load(progressBar, true);
	}

	public void load(ProgressBar progressBar, boolean finish) {
		mods = new HashMap<>();

		/**
		 * Instantiate Java mods
		 */
		mods.putAll(
			javaClasses.entrySet().stream()
				.collect(Collectors.<Map.Entry<ANNOTATION, Class<?>>, ANNOTATION, Object>toMap(Map.Entry::getKey,
						entry -> {
							try {
								currentMod = Optional.of(entry.getKey());
								progressBar.step(entry.getValue());
								return InjectionUtil.newInstanceOrThrow(entry.getValue());
							} catch (Exception ex) {
								ex.printStackTrace();
								System.out.println("Failed to load NOVA Java mod: " + entry);
								throw new ExceptionInInitializerError(ex);
							}
						}
					)
				)
		);

		/**
		 * Instantiate Scala singleton mods
		 */
		mods.putAll(
			scalaClasses.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey,
						entry -> {
							try {
								currentMod = Optional.of(entry.getKey());
								progressBar.step(entry.getValue());
								Field field = entry.getValue().getField("MODULE$");
								Object loadable = field.get(null);

								//Inject dependencies to Scala singleton variables
								//TODO: Does not work recursively for all hierarchy
								Field[] fields = loadable.getClass().getDeclaredFields();

								for (Field f : fields) {
									f.setAccessible(true);
									if (f.get(loadable) == null) {
										try {
											f.set(loadable, diep.getInjector().get().resolve(se.jbee.inject.Dependency.dependency(f.getType())));
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
									f.setAccessible(false);
								}

								return loadable;
							} catch (Exception ex) {
								ex.printStackTrace();
								System.out.println("Failed to load NOVA Scala mod: " + entry);
								throw new ExceptionInInitializerError(ex);
							}
						}
					)
				)
		);

		currentMod = Optional.empty();
		orderedMods = mods.values()
			.stream()
			.collect(Collectors.toList());

		if (finish) progressBar.finish();
	}

	public Set<ANNOTATION> getLoadedMods() {
		return mods.keySet();
	}

	public Map<ANNOTATION, Object> getLoadedModMap() {
		return new HashMap<>(mods);
	}

	public Map<ANNOTATION, Class<?>> getModClasses() {
		return new HashMap<>(javaClasses);
	}

	public Map<ANNOTATION, Class<?>> getScalaClassesMap() {
		return new HashMap<>(scalaClasses);
	}

	public Optional<ANNOTATION> getCurrentMod() {
		return this.currentMod;
	}

	public List<Object> getOrdererdMods() {
		return Collections.unmodifiableList(orderedMods);
	}
}
