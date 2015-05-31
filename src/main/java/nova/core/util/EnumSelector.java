package nova.core.util;


import java.util.EnumSet;

public final class EnumSelector<T extends Enum<T>> {
    private EnumSet<T> exceptions;
    private boolean defaultAllow, defaultBlock = false;
    private boolean locked = false;

    private EnumSelector(Class<T> enumClass) {
        exceptions = EnumSet.noneOf(enumClass);
    }

    public static <T extends Enum<T>> EnumSelector<T> of(Class<T> enumClass) {
        return new EnumSelector(enumClass);
    }

    private void checkLocked() {
        if (locked)
            throw new IllegalStateException("No edits are allowed after EnumSelector has been locked.");
    }
    public void allowAll() {
        checkLocked();
        if (!defaultBlock)
            defaultAllow = true;
        else
            throw new IllegalStateException("You can't allow all enum values when you are already blocking them.");
    }

    public void blockAll() {
        checkLocked();
        if (!defaultAllow)
            defaultBlock = true;
        else
            throw new IllegalStateException("You can't block all enum values when you are already allowing them.");
    }

    public void apart(T value) {
        checkLocked();
        exceptions.add(value);
    }

    public void lock() {
        if (defaultAllow || defaultBlock)
            locked = true;
        else
            throw new IllegalStateException("Cannot lock EnumSelector without specifying default behaviour.");
    }

    public boolean locked() {

        return locked;
    }

    public boolean allows(T value) {
        if(!locked)
            throw new IllegalStateException("Cannot use EnumSelector that is not locked.");
        else
            return defaultAllow ^ exceptions.contains(value);
    }

}
