package nova.core.network;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@java.lang.annotation.Target({ ElementType.METHOD, ElementType.FIELD, ElementType.TYPE })
public @interface Target {

	public Side side();

	public static enum Side {
		CLIENT, SERVER, BOTH;
	}
}
