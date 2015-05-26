package nova.core.nativewrapper;

import nova.core.loader.NativeLoader;

/**
 * A dual wrapper converts between a nova object and native object.
 *
 * Implementing a Loadable on a NativeConverter will allow it to handle loading events.
 *
 * NativeConverters must be registered by {@link NativeLoader} loaders in preInit() stage in order to be properly registered.
 *
 * @author TheSandromatic, Calclavia
 */
public interface NativeConverter<NOVA, NATIVE> {
	Class<NOVA> getNovaSide();

	Class<NATIVE> getNativeSide();

	NOVA toNova(NATIVE nativeObj);

	NATIVE toNative(NOVA novaObj);
}
