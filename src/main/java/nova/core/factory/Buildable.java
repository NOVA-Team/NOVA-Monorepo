package nova.core.factory;

public interface Buildable {
    Factory factory();
    default void arguments(Object ... args) {}
}
