package nova.core.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ObjectArrays;
import com.google.common.primitives.Primitives;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiConsumer;

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
	 * Invokes an action on each field annotated with specified annotation of
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
	 *         order of the most sub class to the most super class.
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

	private static Cache<ClassStringPair, Field> classFieldCache = CacheBuilder.newBuilder().weakKeys().build();

	public static <T> boolean injectField(String fieldName, T object, Object value) {
		Class<?> clazz = object.getClass();
		try {
			Field field = classFieldCache.get(new ClassStringPair(clazz, fieldName), () -> {
				while (!Object.class.equals(clazz)) {
					Field f = clazz.getDeclaredField(fieldName);
					if (f != null) {
						f.setAccessible(true);
						return f;
					}
				}
				return null;
			});
			if (field != null) {
				field.set(object, value);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	private static class ClassStringPair {
		public final Class<?> clazz;
		public final String string;

		ClassStringPair(Class<?> clazz, String string) {
			this.clazz = clazz;
			this.string = string;
		}

		@Override
		public int hashCode() {
			return 37 * clazz.hashCode() + string.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			return obj == this || obj instanceof ClassStringPair && string.equals(((ClassStringPair) obj).string) && clazz.equals(((ClassStringPair) obj).clazz);
		}
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
