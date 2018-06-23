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
package nova.internal.core.util;

import nova.internal.core.Game;
import se.jbee.inject.Dependency;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A utility class for easy Dependency Injection usage.
 *
 * @author Calclavia, ExE Boss
 */
public class InjectionUtil {

	private InjectionUtil() {}

	/**
	 * Attempt to construct the required class using Dependency Injection.
	 *
	 * Used by {@link nova.internal.core.launch.ModLoader#load(nova.core.util.ProgressBar, boolean) ModLoader.load()}
	 *
	 * @param <T> The object type
	 * @param classToConstruct The class to construct
	 * @return A new instance of the class or an empty {@link Optional}
	 * @see Constructor#newInstance(java.lang.Object...)
	 */
	public static <T> Optional<T> newInstanceOp(Class<T> classToConstruct) {
		return newInstanceOp(classToConstruct, true);
	}

	/**
	 * Attempt to construct the required class using Dependency Injection.
	 *
	 * Used by {@link nova.internal.core.launch.ModLoader#load(nova.core.util.ProgressBar, boolean) ModLoader.load()}
	 *
	 * @param <T> The object type
	 * @param classToConstruct The class to construct
	 * @param printStackTrace If the stack trace should be printed in full
	 * @return A new instance of the class or an empty {@link Optional}
	 * @see Constructor#newInstance(java.lang.Object...)
	 */
	public static <T> Optional<T> newInstanceOp(Class<T> classToConstruct, boolean printStackTrace) {
		return newInstanceOp(classToConstruct, printStackTrace, clazz -> Optional.empty());
	}

	/**
	 * Attempt to construct the required class using Dependency Injection.
	 *
	 * Used by {@link nova.internal.core.launch.ModLoader#load(nova.core.util.ProgressBar, boolean) ModLoader.load()}
	 *
	 * @param <T> The object type
	 * @param classToConstruct The class to construct
	 * @param mapping Custom DI mapping
	 * @return A new instance of the class or an empty {@link Optional}
	 * @see Constructor#newInstance(java.lang.Object...)
	 */
	public static <T> Optional<T> newInstanceOp(Class<T> classToConstruct, Function<Class<?>, Optional<?>> mapping) {
		return newInstanceOp(classToConstruct, true, mapping);
	}

	/**
	 * Attempt to construct the required class using Dependency Injection.
	 *
	 * Used by {@link nova.internal.core.launch.ModLoader#load(nova.core.util.ProgressBar, boolean) ModLoader.load()}
	 *
	 * @param <T> The object type
	 * @param classToConstruct The class to construct
	 * @param printStackTrace If the stack trace should be printed in full
	 * @param mapping Custom DI mapping
	 * @return A new instance of the class or an empty {@link Optional}
	 * @see Constructor#newInstance(java.lang.Object...)
	 */
	public static <T> Optional<T> newInstanceOp(Class<T> classToConstruct, boolean printStackTrace, Function<Class<?>, Optional<?>> mapping) {
		try {
			return Optional.of(newInstanceOrThrow(classToConstruct, mapping));
		} catch (Exception ex) {
			if (printStackTrace)
				ex.printStackTrace();
			return Optional.empty();
		}
	}

	/**
	 * Attempt to construct the required class using Dependency Injection.
	 *
	 * Used by {@link nova.internal.core.launch.ModLoader#load(nova.core.util.ProgressBar, boolean) ModLoader.load()}
	 *
	 * @param <T> The object type
	 * @param classToConstruct The class to construct
	 * @return A new instance of the class
	 * @throws ExceptionInInitializerError If an exception is thrown
	 * @see Constructor#newInstance(java.lang.Object...)
	 */
	public static <T> T newInstance(Class<T> classToConstruct) {
		return newInstance(classToConstruct, true);
	}

	/**
	 * Attempt to construct the required class using Dependency Injection.
	 *
	 * Used by {@link nova.internal.core.launch.ModLoader#load(nova.core.util.ProgressBar, boolean) ModLoader.load()}
	 *
	 * @param <T> The object type
	 * @param classToConstruct The class to construct
	 * @param printStackTrace If the stack trace should be printed in full
	 * @return A new instance of the class
	 * @throws ExceptionInInitializerError If an exception is thrown
	 * @see Constructor#newInstance(java.lang.Object...)
	 */
	public static <T> T newInstance(Class<T> classToConstruct, boolean printStackTrace) {
		return newInstance(classToConstruct, printStackTrace, clazz -> Optional.empty());
	}

	/**
	 * Attempt to construct the required class using Dependency Injection.
	 *
	 * Used by {@link nova.internal.core.launch.ModLoader#load(nova.core.util.ProgressBar, boolean) ModLoader.load()}
	 *
	 * @param <T> The object type
	 * @param classToConstruct The class to construct
	 * @param mapping Custom DI mapping
	 * @return A new instance of the class
	 * @throws ExceptionInInitializerError If an exception is thrown
	 * @see Constructor#newInstance(java.lang.Object...)
	 */
	public static <T> T newInstance(Class<T> classToConstruct, Function<Class<?>, Optional<?>> mapping) {
		return newInstance(classToConstruct, true, mapping);
	}

	/**
	 * Attempt to construct the required class using Dependency Injection.
	 *
	 * Used by {@link nova.internal.core.launch.ModLoader#load(nova.core.util.ProgressBar, boolean) ModLoader.load()}
	 *
	 * @param <T> The object type
	 * @param classToConstruct The class to construct
	 * @param printStackTrace If the stack trace should be printed in full
	 * @param mapping Custom DI mapping
	 * @return A new instance of the class
	 * @throws ExceptionInInitializerError If an exception is thrown
	 * @see Constructor#newInstance(java.lang.Object...)
	 */
	public static <T> T newInstance(Class<T> classToConstruct, boolean printStackTrace, Function<Class<?>, Optional<?>> mapping) {
		try {
			return newInstanceOrThrow(classToConstruct, mapping);
		} catch (Exception ex) {
			if (printStackTrace)
				ex.printStackTrace();
			ExceptionInInitializerError e = new ExceptionInInitializerError("Could not instantiate " + classToConstruct);
			e.addSuppressed(ex);
			throw e;
		}
	}

	/**
	 * Attempt to construct the required class using Dependency Injection.
	 *
	 * Used by {@link nova.internal.core.launch.ModLoader#load(nova.core.util.ProgressBar, boolean) ModLoader.load()}
	 *
	 * @param <T> The object type
	 * @param classToConstruct The class to construct
	 * @return A new instance of the class
	 * @throws InstantiationException If an {@link InstantiationException} occurs
	 * @throws IllegalAccessException If an {@link IllegalAccessException} occurs
	 * @throws InvocationTargetException If an {@link InvocationTargetException} occurs
	 * @see Constructor#newInstance(java.lang.Object...)
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstanceOrThrow(Class<T> classToConstruct) throws InstantiationException, IllegalAccessException, InvocationTargetException {
		return newInstanceOrThrow(classToConstruct, clazz -> Optional.empty());
	}

	/**
	 * Attempt to construct the required class using Dependency Injection.
	 *
	 * Used by {@link nova.internal.core.launch.ModLoader#load(nova.core.util.ProgressBar, boolean) ModLoader.load()}
	 *
	 * @param <T> The object type
	 * @param classToConstruct The class to construct
	 * @param mapping Custom DI mapping
	 * @return A new instance of the class
	 * @throws InstantiationException If an {@link InstantiationException} occurs
	 * @throws IllegalAccessException If an {@link IllegalAccessException} occurs
	 * @throws InvocationTargetException If an {@link InvocationTargetException} occurs
	 * @see Constructor#newInstance(java.lang.Object...)
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstanceOrThrow(Class<T> classToConstruct, Function<Class<?>, Optional<?>> mapping) throws InstantiationException, IllegalAccessException, InvocationTargetException {
		//get constructor with most parameters.
		Constructor<?> cons = Arrays.stream(classToConstruct.getConstructors())
			.max(Comparator.comparingInt((constructor) -> constructor.getParameterTypes().length)).get();

		Object[] parameters = Arrays.stream(cons.getParameterTypes())
			.map(clazz -> ((Optional<Object>) mapping.apply(clazz)).orElseGet(() -> Game.injector().resolve(Dependency.dependency(clazz).injectingInto(classToConstruct))))
			.collect(Collectors.toList()).toArray();

		try {
			cons.setAccessible(true);
			return (T) cons.newInstance(parameters);
		} finally {
			cons.setAccessible(false);
		}
	}

}
