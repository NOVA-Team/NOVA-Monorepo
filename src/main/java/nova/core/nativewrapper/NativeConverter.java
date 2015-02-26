package nova.core.nativewrapper;

/**
 * @author TheSandromatic
 */
public interface NativeConverter {
    Class<?> getNovaSide();
    Class<?> getNativeSide();
    Object convertToNova(Object nativeObj);
    Object convertToNative(Object novaObj);
}
