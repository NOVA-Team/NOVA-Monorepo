package nova.core.component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be used together with {@link Component} to specify an
 * interface to be passed through to the wrapper implementation. This can be
 * used to achieve basic native compatibility, for example for energy systems.
 * 
 * @author Vic Nightfall
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Passthrough {

	String value();
}
