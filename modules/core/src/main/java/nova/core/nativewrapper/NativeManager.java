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

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import nova.core.util.exception.NovaException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author TheSandromatic
 */
public class NativeManager {
	/**
	 * A map from a Nova component, to a Native interface.
	 */
	private final BiMap<Class<?>, Class<?>> novaComponentToNativeInterface = HashBiMap.create();

	private final List<NativeConverter<?, ?>> converters = new ArrayList<>();
	/**
	 * A map from a Native written type to a Converter.
	 */
	private final Map<Class<?>, NativeConverter<?, ?>> nativeConverters = new HashMap<>();
	/**
	 * A map from a Nova written type to a Converter.
	 */
	private final Map<Class<?>, NativeConverter<?, ?>> novaConverters = new HashMap<>();

	/**
	 * Registers a component to a native interface.
	 *
	 * @param <INTERFACE> The interface of the component.
	 * @param component A component. Must extend {@code INTERFACE}.
	 * @param nativeInterface the class of the {@code INTERFACE}.
	 */
	public <INTERFACE> void registerComponentToInterface(Class<? extends INTERFACE> component, Class<INTERFACE> nativeInterface) {
		novaComponentToNativeInterface.put(component, nativeInterface);
	}

	/**
	 * Gets the interface registered for a component.
	 * @param component the component.
	 * @return The interface on the native side.
	 */
	public Class<?> getNativeInterface(Class<?> component) {
		return novaComponentToNativeInterface.get(component);
	}

	/**
	 * Gets the component registered for an interface.
	 * @param nativeInterface the interface.
	 * @return the component registered to it.
	 */
	public Class<?> getNovaComponent(Class<?> nativeInterface) {
		return novaComponentToNativeInterface.inverse().get(nativeInterface);
	}

	public void registerConverter(NativeConverter<?, ?> converter) {
		nativeConverters.put(converter.getNativeSide(), converter);
		novaConverters.put(converter.getNovaSide(), converter);
		converters.add(converter);
	}

	@SuppressWarnings("unchecked")
	public <NOVA, NATIVE, CONVERTER extends NativeConverter<NOVA, NATIVE>> CONVERTER getNative(Class<NOVA> novaClass, Class<NATIVE> nativeClass) {
		NativeConverter<NOVA, NATIVE> nativeConverter = (NativeConverter<NOVA, NATIVE>) novaConverters.get(novaClass);

		if (nativeConverter == nativeConverters.get(nativeClass)) {
			return (CONVERTER) nativeConverter;
		}

		throw new NativeException("Cannot find native converter for: " + novaClass);
	}

	@SuppressWarnings("unchecked")
	private <NOVA, NATIVE, CONVERTER extends NativeConverter<NOVA, NATIVE>> CONVERTER findConverter(Map<Class<?>, NativeConverter<?, ?>> map, Object obj) {
		Objects.requireNonNull(obj);
		Class<?> clazz = obj.getClass();

		return (CONVERTER) map
			.entrySet()
			.stream()
			.filter(e -> e.getKey().isAssignableFrom(clazz))
			.findFirst()
			.map(Map.Entry::getValue)
			.orElse(null);
	}

	/**
	 * Converts a native object to a nova object. This method has autocast, is DANGEROUS and may crash.
	 *
	 * @param nativeObject A game implementation object.
	 * @param <T> The NOVA equivalent type.
	 * @return The NOVA equivalent object.
	 */
	public <T> T toNova(Object nativeObject) {
		Objects.requireNonNull(nativeObject);
		NativeConverter<T, Object> converter = findConverter(nativeConverters, nativeObject);
		if (converter == null) {
			throw new NativeException("NativeManager.toNova: Converter for " + nativeObject + " with class " + nativeObject.getClass() + " does not exist!");
		}

		return converter.toNova(nativeObject);
	}

	/**
	 * Converts a nova object to a native object. This method has autocast, is DANGEROUS and may crash.
	 *
	 * @param novaObject A NOVA implementation object.
	 * @param <T> The game equivalent type.
	 * @return The game equivalent object.
	 */
	public <T> T toNative(Object novaObject) {
		Objects.requireNonNull(novaObject);
		NativeConverter<Object, T> converter = findConverter(novaConverters, novaObject);
		if (converter == null) {
			throw new NativeException("NativeManager.toNative: Converter for " + novaObject + " with class " + novaObject.getClass() + " does not exist!");
		}

		return converter.toNative(novaObject);
	}

	public List<NativeConverter<?, ?>> getNativeConverters() {
		return converters;
	}

	public static class NativeException extends NovaException {
		private static final long serialVersionUID = 1L;

		public NativeException() {
			super();
		}

		public NativeException(String message, Object... parameters) {
			super(message, parameters);
		}

		public NativeException(String message) {
			super(message);
		}

		public NativeException(String message, Throwable cause) {
			super(message, cause);
		}

		public NativeException(Throwable cause) {
			super(cause);
		}
	}
}
