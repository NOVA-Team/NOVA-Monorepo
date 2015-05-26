package nova.core.nativewrapper;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import nova.core.util.exception.NovaException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author TheSandromatic
 */
public class NativeManager {
	/**
	 * A map from a Nova written interface, to a Native written interface.
	 */
	private final BiMap<Class<?>, Class<?>> passthroughInterfaceNovaToNative = HashBiMap.create();
	/**
	 * A map from a Native written type to a Converter.
	 */
	private final Map<Class<?>, NativeConverter> novaToNative = new HashMap<>();
	/**
	 * A map from a Nova written type to a Converter.
	 */
	private final Map<Class<?>, NativeConverter> nativeToNova = new HashMap<>();

	public void registerPassthroughInterface(Class<?> novaSide, Class<?> nativeSide) {
		passthroughInterfaceNovaToNative.put(novaSide, nativeSide);
	}

	public Class<?> getNativeInterface(Class<?> novaInterface) {
		return passthroughInterfaceNovaToNative.get(novaInterface);
	}

	public void registerConverter(NativeConverter<?, ?> converter) {
		novaToNative.put(converter.getNativeSide(), converter);
		nativeToNova.put(converter.getNovaSide(), converter);
	}

	public <NOVA, NATIVE> NativeConverter<NOVA, NATIVE> getNative(Class<NOVA> novaClass, Class<NATIVE> nativeClass) {
		return novaToNative.get(novaClass);
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
		NativeConverter converter = findConverter(novaToNative, nativeObject);
		if (converter == null) {
			throw new NovaException("Converter for " + nativeObject.getClass() + " does not exist!");
		}

		return (T) converter.toNova(nativeObject);
	}

	/**
	 * Converts a nova object to a native object. This method has autocast, is DANGEROUS and may crash.
	 */
	public <T> T toNative(Object novaObject) {
		NativeConverter converter = findConverter(nativeToNova, novaObject);
		if (converter == null) {
			throw new NovaException("Converter for " + novaObject.getClass() + " does not exist!");
		}

		return (T) converter.toNative(novaObject);
	}

	public Collection<NativeConverter> getNativeConverters() {
		return novaToNative.values();
	}
}
