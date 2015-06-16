package nova.core.util;

import com.google.common.base.Throwables;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import com.google.common.collect.ObjectArrays;
import com.google.common.primitives.Primitives;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Utility class containing muliple reflection helpers.
 */
public class ReflectionUtil {

	/**
	 * Array that contains the primitive wrappers in the order they can be
	 * converted without a type-cast.
	 */
	private static final List<Class<?>> PRIMITIVE_WIDENING = Arrays.asList(Byte.class, Short.class, Character.class, Integer.class, Long.class, Float.class, Double.class);

	private ReflectionUtil() {
	}

	private static Class<?>[] types(Object... args) {
		return Arrays.stream(args).map(Object::getClass).toArray(Class[]::new);
	}

	static float calculateDistancePrimitive(Class<?> a, Class<?> b) {

		// Nothing can be converted to a char or a boolean.
		if (b == Character.class || b == Boolean.class)
			return -1F;
		// char acts like a short.
		if (a == Character.class)
			a = Short.class;

		int indexA = PRIMITIVE_WIDENING.indexOf(a);
		int indexB = PRIMITIVE_WIDENING.indexOf(b);

		if (indexA < 0 || indexB < 0)
			return -1F;
		return (indexB - indexA) / 10F;
	}

	static float calculateDistance(Class<?> a, Class<?> b) {
		float cost = 0F;
		if (b.isPrimitive()) {
			b = Primitives.wrap(b);
			if (!a.isPrimitive()) {
				cost += 0.1F; // Penalty for unwrapping
			}
			a = Primitives.wrap(a);

			cost += calculateDistancePrimitive(a, b);
			return cost;
		} else {
			while (a != null && !a.equals(b)) {
				if (a.isInterface() && a.isAssignableFrom(b)) {
					// Interface match
					cost += 0.25F;
					break;
				}
				cost++;
				a = a.getSuperclass();
			}
			if (a == null) {
				// Object match
				cost += 1.5F;
			}
		}
		return cost;
	}

	static float calculateDistance(Executable exec, Class<?>[] parameterTypes) {
		float cost = 0;

		Class<?>[] execTypes = exec.getParameterTypes();
		for (int i = 0; i < exec.getParameterCount(); i++) {
			if (i >= parameterTypes.length && exec.isVarArgs())
				break;

			Class<?> a = parameterTypes[i];
			Class<?> b = execTypes[i];

			if (i == exec.getParameterCount() - 1 && exec.isVarArgs()) {
				if (isAssignmentCompatible(a, b)) {
					// Passed array for var-args.
					cost += calculateDistance(a, b);
				} else {
					cost += calculateDistance(a, b.getComponentType());
					// Penalty for every parameter that wasn't used.
					cost += (parameterTypes.length - exec.getParameterCount()) * 3F;
					// Death penalty for using var-args.
					cost += 10F;
				}
			} else {
				cost += calculateDistance(a, b);
			}
		}
		return cost;
	}

	static int compareDistance(Executable a, Executable b, Class<?>[] parameterTypes) {
		float distA = calculateDistance(a, parameterTypes);
		float distB = calculateDistance(b, parameterTypes);
		return Float.compare(distA, distB);
	}

	static boolean isAssignmentCompatible(Executable exec, Class<?>... parameterTypes) {

		// First checks to eliminate an obvious mismatch.
		if (exec.isVarArgs()) {
			if (exec.getParameterCount() == 1 && parameterTypes.length == 0)
				return true;
			if (parameterTypes.length < exec.getParameterCount() - 1)
				return false;
		} else if (parameterTypes.length != exec.getParameterCount()) {
			return false;
		}

		Class<?>[] execTypes = exec.getParameterTypes();
		for (int i = 0; i < exec.getParameterCount(); i++) {

			Class<?> a = parameterTypes[i];
			Class<?> b = execTypes[i];

			if (i == exec.getParameterCount() - 1 && exec.isVarArgs()) {

				// Passed array type for var-args
				if (isAssignmentCompatible(a, b)) {
					return true;
				}

				// Var-args, have to check every element against the array type.
				b = b.getComponentType();
				for (int j = i; j < parameterTypes.length; j++) {
					a = parameterTypes[j];
					if (!isAssignmentCompatible(a, b))
						return false;
				}
				return true;
			} else if (!isAssignmentCompatible(a, b)) {
				return false;
			}
		}
		return true;
	}

	static boolean isAssignmentCompatible(Class<?> a, Class<?> b) {
		if (a == b)
			return true;

		// Wrap primitive type
		a = Primitives.wrap(a);

		// Check primitive type assignment compatibility
		if (b.isPrimitive()) {
			b = Primitives.wrap(b);

			float distance = calculateDistancePrimitive(a, b);
			return distance >= 0;

		} else {
			return b.isInstance(b);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> Optional<Constructor<T>> findMatchingConstructor(Class<T> clazz, Class<?>... parameterTypes) {

		try {
			// Try the default method as it is much faster, works in
			// many of the cases.
			return Optional.of(clazz.getConstructor(parameterTypes));
		} catch (Exception e) {
			//NOOP
		}

		// Aww, snap. Now we have to do it the hard way...
		try {
			return Arrays.stream(clazz.getConstructors())
				.filter(cons -> isAssignmentCompatible(cons, parameterTypes))
				.sorted((consA, consB) -> {
					int comp = compareDistance(consA, consB, parameterTypes);
					if (comp == 0) {
						// Found an ambiguous constructor
						throw new ReflectionException("Found an ambigious constructor.");
					}
					return comp;
				})
				.findFirst()
				.map(constr2 -> (Constructor<T>) constr2);
		} catch (NovaException e) {
			throw e;
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	static <T> T newInstanceMatching(Constructor<T> constr, Object... args) {
		try {
			if (args == null || args.length == 0) {
				// 0 parameter case
				if (constr.isVarArgs()) {
					return constr.newInstance(new Object[] { args });
				} else {
					return constr.newInstance();
				}
			} else {
				if (constr.isVarArgs()) {
					int last = constr.getParameterCount() - 1;

					if (last < args.length) {
						if (args.length == constr.getParameterCount()) {
							// Check for array type
							Class<?> append = args[last].getClass();
							if (append.isArray() && constr.getParameterTypes()[last].isAssignableFrom(append)) {
								// We have a matching array type as last
								// argument, so use that one
								return constr.newInstance(args);
							}
						}

						// Append arguments as new array.
						return constr.newInstance(ObjectArrays.concat(
							Arrays.copyOfRange(args, 0, last),
							(Object) Arrays.copyOfRange(args, last, args.length)));
					} else {
						// Empty parameter -> pass empty array for var-args
						return constr.newInstance(ObjectArrays.concat(args, (Object) new Object[0]));
					}
				} else {
					// No var-args
					return constr.newInstance(args);
				}
			}
		} catch (Exception e) {
			throw new ReflectionException(e);
		}
	}

	public static <T> T newInstanceMatching(Class<T> clazz, Object... args) {
		try {
			if (args != null && args.length > 0) {
				return newInstanceMatching(findMatchingConstructor(clazz, types(args)).get(), args);
			} else {
				return clazz.newInstance();
			}
		} catch (Exception e) {
			throw new ReflectionException(e);
		}
	}

	public static <T> T newInstance(Class<T> clazz, Object... args) {
		try {
			if (args != null && args.length > 0) {
				return clazz.getConstructor(types(args)).newInstance(args);
			}
			return clazz.newInstance();
		} catch (Exception e) {
			throw new ReflectionException(e);
		}
	}

	/**
	 * Invokes an action on each field annotated withPriority specified annotation of
	 * given object
	 *
	 * @param <T> Annotation type
	 * @param annotation Annotation type
	 * @param clazz Class to scan
	 * @param action Action to invoke
	 */
	public static <T extends Annotation> void forEachAnnotatedField(Class<? extends T> annotation, Class<?> clazz, BiConsumer<Field, T> action) {
		Arrays.stream(clazz.getDeclaredFields())
			.filter(f -> f.isAnnotationPresent(annotation) && !f.isSynthetic())
			.forEachOrdered(f -> action.accept(f, f.getAnnotation(annotation)));
	}

	/**
	 * Gets all the annotated fields of this class, including all the parents
	 * classes in the order of hierarchy.
	 *
	 * @param annotation Your annotation class.
	 * @param clazz Class to search through.
	 * @return An ordered map of annotated fields and their annotations from the
	 * order of the most sub class to the most super class.
	 */
	public static <T extends Annotation> Map<Field, T> getAnnotatedFields(Class<T> annotation, Class<?> clazz) {
		Map<Field, T> fields = new LinkedHashMap<>();
		forEachRecursiveAnnotatedField(annotation, clazz, fields::put);
		return fields;
	}

	// TODO: Cache this?
	public static <T extends Annotation> void forEachRecursiveAnnotatedField(Class<T> annotation, Class<?> clazz, BiConsumer<Field, T> action) {
		Arrays.stream(clazz.getDeclaredFields())
			.filter(f -> f.isAnnotationPresent(annotation) && !f.isSynthetic())
			.forEachOrdered(f -> action.accept(f, f.getAnnotation(annotation)));

		Class<?> superClass = clazz.getSuperclass();

		if (superClass != null) {
			forEachRecursiveAnnotatedField(annotation, superClass, action);
		}
	}

	/**
	 * Calls consumer for each super class of fromClass staring on fromClass ending on toClass.
	 * @param fromClass starting class.
	 * @param toClass ending class.
	 * @param consumer executed on each superclass of fromClass upto toClass
	 * @param <B> ending class.
	 */
	public static <B> void forEachSuperClassUpTo(Class<? extends B> fromClass, Class<B> toClass, Consumer<Class<? extends B>> consumer) {
		if (!toClass.isAssignableFrom(fromClass)) {
			throw new IllegalArgumentException(String.format("Class %s is not extending %s.", fromClass, toClass));
		}
		Class<? extends B> clazz = fromClass;

		consumer.accept(clazz);
		while (!clazz.equals(toClass)) {
			clazz = clazz.getSuperclass().asSubclass(toClass);
			consumer.accept(clazz);
		}
	}

	/**
	 * Calls consumer on each superclass of clazz strarting from clazz ending at {@link Object}.
	 * @param clazz starting class
	 * @param consumer to be executed on each superclass of clazz.
	 */
	public static void forEachSuperClass(Class<?> clazz, Consumer<Class<?>> consumer) {
		forEachSuperClassUpTo(clazz, Object.class, consumer);
	}

	private static Cache<Class<?>, Map<String, Optional<Field>>> classFieldCache = CacheBuilder.newBuilder().weakKeys().build();

	/**
	 * Finds given field by name in this class or its superclasses. Performs cacheing.
	 *
	 * @param fieldName which it will search for.
	 * @param clazz which the search starts from.
	 * @return Optiona.of(field) is field was found and Optional.empty() in case not.
	 */
	public static Optional<Field> findField(String fieldName, Class<?> clazz) {
		try {
			Map<String, Optional<Field>> stringFieldMap = classFieldCache.get(clazz, Maps::newHashMap);
			Optional<Field> fromCache = stringFieldMap.get(fieldName);
			if (fromCache == null) {
				for (Field f : clazz.getDeclaredFields()) {
					if (f.getName().equals(fieldName)) {
						fromCache = Optional.of(f);
						break;
					}
				}
				if (fromCache == null) {
					if (!clazz.equals(Object.class)) {
						fromCache = findField(fieldName, clazz.getSuperclass());
					} else {
						fromCache = Optional.empty();
					}
				}

				stringFieldMap.put(fieldName, fromCache);
			}

			return fromCache;
		} catch (Exception e) {
			// Impossible. Would only heppen if Maps.newHashMap() trows an exception.
			e.printStackTrace();
			return Optional.empty();
		}
	}

	/**
	 * Injects value into field.
	 *
	 * @param fieldName to search for in object.
	 * @param object object to be injected.
	 * @param value to be injected.
	 * @return {@code true} if injection was sucessful, {@code false} if not.
	 */
	public static boolean injectField(String fieldName, Object object, Object value) {
		Optional<Field> oField = findField(fieldName, object.getClass());
		oField.filter(field -> !field.isAccessible()).ifPresent(field -> field.setAccessible(true));
		oField.ifPresent(field -> {
			try {
				field.set(object, value);
			} catch (IllegalAccessException e) {
				throw Throwables.propagate(e);
			}
		});
		return oField.isPresent();

	}

	public static class ReflectionException extends NovaException {
		public ReflectionException() {
			super();
		}

		public ReflectionException(String message, Object... parameters) {
			super(message, parameters);
		}

		public ReflectionException(String message) {
			super(message);
		}

		public ReflectionException(String message, Throwable cause) {
			super(message, cause);
		}

		public ReflectionException(Throwable cause) {
			super(cause);
		}
	}
}
