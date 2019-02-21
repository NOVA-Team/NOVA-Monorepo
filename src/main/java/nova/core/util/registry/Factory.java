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

package nova.core.util.registry;

import net.jodah.typetools.TypeResolver;
import nova.core.util.Identifiable;
import nova.internal.core.util.InjectionUtil;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Factories are immutable object builders that create objects.
 * @param <S> The self type
 * @param <T> Type of produced object
 * @author Calclavia
 */
public abstract class Factory<S extends Factory<S, T>, T extends Identifiable> implements Identifiable {
	/** The ID of the factory */
	protected final String id;

	/** The constructor function */
	protected final Supplier<T> constructor;

	/** The processor function */
	protected final Function<T, T> processor;

	/** The type of the object to construct */
	protected final Class<? extends T> type;

	/**
	 * Creates a new factory with a constructor function that instantiates the factory object,
	 * and with a processor that is capable of mutating the instantiated object after its initialization.
	 *
	 * A factory's processor may be modified to allow specific customization of instantiated objects before it is used.
	 * @param id The identifier for this factory type
	 * @param type The implementation class
	 * @param processor The processor function
	 * @param mapping The custom DI mapping
	 */
	public Factory(String id, Class<? extends T> type, Function<T, T> processor, Function<Class<?>, Optional<?>> mapping) {
		this.id = id;
		this.constructor = () -> InjectionUtil.newInstance(type,
			clazz -> clazz.isAssignableFrom(getClass()) ? Optional.of(this) : mapping.apply(clazz));
		this.processor = processor;
		this.type = type;
	}

	/**
	 * Creates a new factory with a constructor function that instantiates the factory object,
	 * and with a processor that is capable of mutating the instantiated object after its initialization.
	 *
	 * A factory's processor may be modified to allow specific customization of instantiated objects before it is used.
	 * @param id The identifier for this factory type
	 * @param type The implementation class
	 * @param processor The processor function
	 */
	public Factory(String id, Class<? extends T> type, Function<T, T> processor) {
		this(id, type, processor, clazz -> Optional.empty());
	}

	/**
	 * Creates a new factory with a constructor function that instantiates the factory object,
	 * and with a processor that is capable of mutating the instantiated object after its initialization.
	 *
	 * A factory's processor may be modified to allow specific customization of instantiated objects before it is used.
	 * @param id The identifier for this factory type
	 * @param type The implementation class
	 */
	public Factory(String id, Class<? extends T> type) {
		this(id, type, obj -> obj);
	}

	/**
	 * Creates a new factory with a constructor function that instantiates the factory object,
	 * and with a processor that is capable of mutating the instantiated object after its initialization.
	 *
	 * A factory's processor may be modified to allow specific customization of instantiated objects before it is used.
	 * @param id The identifier for this factory type
	 * @param constructor The construction function
	 * @param processor The processor function
	 */
	@SuppressWarnings("unchecked")
	public Factory(String id, Supplier<T> constructor, Function<T, T> processor) {
		this.id = id;
		this.constructor = constructor;
		this.processor = processor;
		Class<?> type = TypeResolver.resolveRawArguments(Function.class, processor.getClass())[1];
		if (type == TypeResolver.Unknown.class || type == Identifiable.class) {
			type = TypeResolver.resolveRawArgument(Supplier.class, constructor.getClass());
		}
		this.type = (Class<? extends T>) type;
	}

	/**
	 * Creates a new factory with a constructor function that instantiates the factory object,
	 * and with a processor that is capable of mutating the instantiated object after its initialization.
	 *
	 * A factory's processor may be modified to allow specific customization of instantiated objects before it is used.
	 * @param id The identifier for this factory type
	 * @param constructor The construction function
	 */
	public Factory(String id, Supplier<T> constructor) {
		this(id, constructor, obj -> obj);
	}

	/**
	 * Adds a processor to the factory
	 * @param processor A processor that mutates the construction
	 * @return Self
	 */
	public S process(Function<T, T> processor) {
		return selfConstructor(id, constructor, this.processor.compose(processor));
	}

	protected S selfConstructor(String id, Class<T> type, Function<T, T> processor) {
		return selfConstructor(id, () -> InjectionUtil.newInstance(type), processor);
	}

	protected abstract S selfConstructor(String id, Supplier<T> constructor, Function<T, T> processor);

	/**
	 * Get the class of the type argument T.
	 *
	 * @return The class of T.
	 */
	public Class<? extends T> getType() {
		return type;
	}

	/**
	 * @return A new instance of T based on the construction method
	 */
	public T build() {
		return processor.apply(constructor.get());
	}

	@Override
	public String getID() {
		return id;
	}
}
