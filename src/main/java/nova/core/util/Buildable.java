package nova.core.util;

import com.google.common.collect.Lists;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public interface Buildable<T extends Buildable<T>> extends Identifiable {
	Factory<T> factory();

	default void afterConstruction(Optional<Object[]> typeArguments, Optional<Object[]> instanceArguments) {}

	default void afterFinalizers() {}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@Repeatable(IDs.class)
	@interface ID {
		String value();

		String[] arguments() default {};
	}
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.PARAMETER)
	@interface WithID {
		String value();
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@interface IDs {
		ID[] value();
	}

	static <T extends Buildable<T>> Set<Factory<T>> factoriesFor(Class<T> clazz) {
		List<ID> listID = Lists.newLinkedList();

		Optional<IDs> IDs = Optional.ofNullable(clazz.getAnnotation(IDs.class));
		IDs.ifPresent(ids -> Collections.addAll(listID, ids.value()));

		Optional<ID> ID = Optional.ofNullable(clazz.getAnnotation(ID.class));
		ID.ifPresent(listID::add);

		Factory<T> base = Factory.of(clazz);
		if (listID.isEmpty()) {
			return Collections.singleton(base);
		}
		return listID.stream().map(id -> {
			if (id.arguments().length != 0) {
				return base.ID(id.value()).typeArguments(id.arguments());
			} else {
				return base.ID(id.value());
			}
		}).collect(Collectors.toSet());
	}
}
