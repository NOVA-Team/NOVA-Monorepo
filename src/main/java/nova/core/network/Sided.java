package nova.core.network;

import nova.core.network.NetworkTarget.IllegalSideException;
import nova.core.network.NetworkTarget.Side;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// TODO source only or should we modify the ClassLoader to reject sided methods?
/**
 * Sided is used to mark methods, classes and other types in order to clarify
 * that it should only be used on the specified side. <b>Referencing such a type
 * will most likely trigger an {@link IllegalSideException} or will malfunction
 * at worst!</b>
 *
 * @author Vic Nightfall
 */
@Retention(RetentionPolicy.SOURCE)
public @interface Sided {
	public Side value();
}
