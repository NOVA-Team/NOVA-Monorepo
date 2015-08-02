package nova.core.nativewrapper;

/**
 * A dual wrapper converts between a nova object and native object.
 *
 * Implementing a Loadable on a NativeConverter will allow it to handle loading events.
 *
 * @author TheSandromatic, Calclavia
 */
public interface NativeConverter<NOVA, NATIVE> {
	Class<NOVA> getNovaSide();

	Class<NATIVE> getNativeSide();

	NOVA toNova(NATIVE nativeObj);

	NATIVE toNative(NOVA novaObj);
}
