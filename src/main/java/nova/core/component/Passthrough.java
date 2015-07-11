package nova.core.component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * This annotation can be used together with {@link Component} to specify an
 * interface to be passed through to the wrapper implementation. This can be
 * used to achieve basic native compatibility, for example for energy systems.
 * </p>
 * 
 * <p>
 * A component with a passthrough will cause the wrapper to implement an
 * interface on the native representation and respectively creates a new
 * instance of said passthrough component for every native representation that
 * implements the interface and isn't added by NOVA.
 * </p>
 * 
 * <p>
 * A passthrough component has to be registered with
 * {@link ComponentManager#registerNativePassthrough(Class)}
 * </p>
 * 
 * @author Vic Nightfall
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Passthrough {

	String value();
}
