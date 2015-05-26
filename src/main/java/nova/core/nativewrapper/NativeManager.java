package nova.core.nativewrapper;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import nova.core.util.exception.NovaException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author TheSandromatic
 */
public class NativeManager {
	/**
	 * A map from a Nova written interface, to a Native written interface.
	 */
	private final BiMap<Class<?>, Class<?>> passthroughInterfaceNovaToNative = HashBiMap.create();

	private final List<NativeConverter> converters = new ArrayList<>();
	/**
	 * A map from a Native written type to a Converter.
	 */
	private final Map<Class<?>, NativeConverter> nativeConverters = new HashMap<>();
	/**
	 * A map from a Nova written type to a Converter.
	 */
	private final Map<Class<?>, NativeConverter> novaConverters = new HashMap<>();

	public void registerPassthroughInterface(Class<?> novaSide, Class<?> nativeSide) {
		passthroughInterfaceNovaToNative.put(novaSide, nativeSide);
	}

	public Class<?> getNativeInterface(Class<?> novaInterface) {
		return passthroughInterfaceNovaToNative.get(novaInterface);
	}

	public void registerConverter(NativeConverter<?, ?> converter) {
		nativeConverters.put(converter.getNativeSide(), converter);
		novaConverters.put(converter.getNovaSide(), converter);
		converters.add(converter);
	}

	public <NOVA, NATIVE> NativeConverter<NOVA, NATIVE> getNative(Class<NOVA> novaClass, Class<NATIVE> nativeClass) {
		NativeConverter nativeConverter = novaConverters.get(novaClass);

		if (nativeConverter == nativeConverters.get(nativeClass)) {
			return nativeConverter;
		}

		throw new NovaException("Cannot find native converter for: " + novaClass);
	}

	private NativeConverter findConverter(Map<Class<?>, NativeConverter> map, Object obj) {
		Class<?> clazz = obj.getClass();
		NativeConverter nc = map.get(clazz);
		while (nc == null) {
			clazz = clazz.getSuperclass();
			if (clazz == Object.class) {
				return null;
			}
			nc = map.get(clazz);
		}
		return nc;
	}

	/**
	 * Converts a native object to a nova object. This method has autocast, is DANGEROUS and may crash.
	 */
	public <T> T toNova(Object nativeObject) {
		NativeConverter converter = findConverter(nativeConverters, nativeObject);
		if (converter == null) {
			throw new NovaException("NativeManager.toNova: Converter for " + nativeObject + " with class " + nativeObject.getClass() + " does not exist!");
		}

		return (T) converter.toNova(nativeObject);
	}

	/**
	 * Converts a nova object to a native object. This method has autocast, is DANGEROUS and may crash.
	 */
	public <T> T toNative(Object novaObject) {
		NativeConverter converter = findConverter(novaConverters, novaObject);
		if (converter == null) {
			throw new NovaException("NativeManager.toNative: Converter for " + novaObject + " with class " + novaObject.getClass() + " does not exist!");
		}

		return (T) converter.toNative(novaObject);
	}

	public List<NativeConverter> getNativeConverters() {
		return converters;
	}
}
