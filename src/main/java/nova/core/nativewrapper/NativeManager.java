package nova.core.nativewrapper;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
	private final Map<Class<?>, NativeConverter> nativeConverterNative = new HashMap<>();
	/**
	 * A map from a Nova written type to a Converter.
	 */
	private final Map<Class<?>, NativeConverter> nativeConverterNova = new HashMap<>();

	public void registerPassthroughInterface(Class<?> novaSide, Class<?> nativeSide) {
		passthroughInterfaceNovaToNative.put(novaSide, nativeSide);
	}

	public void registerNativeConverter(NativeConverter converter) {
		nativeConverterNative.put(converter.getNativeSide(), converter);
		nativeConverterNova.put(converter.getNovaSide(), converter);
	}

	public Class<?> getNativeInterface(Class<?> novaInterface) {
		return passthroughInterfaceNovaToNative.get(novaInterface);
	}

	private NativeConverter findNativeConverter(Map<Class<?>, NativeConverter> map, Object obj)
	{
		Class<?> clazz = obj.getClass();
		NativeConverter nc = map.get(clazz);
		while (nc == null)
		{
			clazz = clazz.getSuperclass();
			if (clazz == Object.class)
				return null;
			nc = map.get(clazz);
		}
		return nc;
	}
	
	public Optional<Object> convertToNova(Object nativeObject) {
		try {
			return Optional.of(findNativeConverter(nativeConverterNative,nativeObject).convertToNova(nativeObject));
		} catch (NullPointerException e) {
			return Optional.empty();
		}
	}

	public Optional<Object> convertToNative(Object novaObject) {
		try {
			return Optional.of(findNativeConverter(nativeConverterNova,novaObject).convertToNative(novaObject));
		} catch (NullPointerException e) {
			return Optional.empty();
		}
	}
}
