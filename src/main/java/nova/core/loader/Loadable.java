package nova.core.loader;

/**
 * A mod interface implemented to receive mod load event calls.
 *
 * @author Calclavia
 */
public interface Loadable {
	default void preInit() {
	}

	default void init() {
	}

	default void postInit() {
	}
}
