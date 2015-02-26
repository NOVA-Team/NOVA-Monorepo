package nova.core.passthrough;


import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.HashMap;
import java.util.Map;

/**
 * @author TheSandromatic
 */
public class NativeManager {
    /**
     * A map from a Nova written interface, to a Native written interface.
     */
    private static final BiMap<Class<?>,Class<?>> passthroughInterfaceNovaToNative = HashBiMap.create();
    /**
     * A map from a Native written type to a Converter.
     */
    private static final Map<Class<?>, NativeConverter> nativeConverterNative = new HashMap<>();
    /**
     * A map from a Nova written type to a Converter.
     */
    private static final Map<Class<?>, NativeConverter> nativeConverterNova = new HashMap<>();
    public static void registerPassthroughInterface(Class<?> novaSide, Class<?> nativeSide) {
        passthroughInterfaceNovaToNative.put(novaSide, nativeSide);
    }
    public static void registerNativeConverter(NativeConverter converter) {
        nativeConverterNative.put(converter.getNativeSide(), converter);
        nativeConverterNova.put(converter.getNovaSide(), converter);
    }
    public static Class<?> getNativeInterface(Class<?> novaInterface) {
        return passthroughInterfaceNovaToNative.get(novaInterface);
    }
    public static Object convertToNova(Object nativeObject) {
        try {
            return nativeConverterNative.get(nativeObject.getClass()).convertToNova(nativeObject);
        }
        catch (Exception e)
        {
            return null;
        }
    }
    public static Object convertToNative(Object novaObject) {
        try {
            return nativeConverterNova.get(novaObject.getClass()).convertToNative(novaObject);
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
