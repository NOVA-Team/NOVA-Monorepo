package nova.core.factory;

public interface Buildable {
    Factory factory();
    default void afterConstruction() {}
    default void arguments(Object ... args) {}
	default void afterFinalizers() {}

}
