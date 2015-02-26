package nova.core.nativewrapper;


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
    private final BiMap<Class<?>,Class<?>> passthroughInterfaceNovaToNative = HashBiMap.create();
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
    public Object convertToNova(Object nativeObject) {
        try {
            return nativeConverterNative.get(nativeObject.getClass()).convertToNova(nativeObject);
        }
        catch (NullPointerException e)
        {
            return null;
        }
    }
    public Object convertToNative(Object novaObject) {
        try {
            return nativeConverterNova.get(novaObject.getClass()).convertToNative(novaObject);
        }
        catch (NullPointerException e)
        {
            return null;
        }
    }
}
