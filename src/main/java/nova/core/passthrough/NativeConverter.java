package nova.core.passthrough;

public interface NativeConverter {
    Class<?> getNovaSide();
    Class<?> getNativeSide();
    Object convertToNova(Object nativeObj);
    Object convertToNative(Object novaObj);
}
