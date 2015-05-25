package nova.core.util;

import nova.core.component.ComponentProvider;

/**
 * Applied to any object that can provides a wrapper that wraps the object.
 * @author Calclavia
 */
public class WrapperProvider<W> extends ComponentProvider {

	/**
	 * The wrapper is injected from positioned objectFactory.
	 * The wrapper may be null in cases where a backward wrapper is created for native entities.
	 */
	public final W wrapper = null;
}
