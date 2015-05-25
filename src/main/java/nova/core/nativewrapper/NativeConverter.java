package nova.core.nativewrapper;

/**
 * A dual wrapper converts between a nova object and native object.
 * @author TheSandromatic, Calclavia
 */
public interface NativeConverter<NOVA, NATIVE> {
	Class<NOVA> getNovaSide();

	Class<NATIVE> getNativeSide();

	NOVA toNova(NATIVE nativeObj);

	NATIVE toNative(NOVA novaObj);
}
