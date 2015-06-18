package nova.core.util;

import com.google.common.collect.ImmutableList;
import nova.internal.core.Game;
import se.jbee.inject.Dependency;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Instances of this calss are used for registraction of Dependency Injection schematic classes.
 * It is fully immutable, any change results in return of new Factory instance.
 * For eaze of creating factories from {@link Buildable.ID} annotated classes look at {@link Buildable#factoriesFor(Class)}.
 * @param <T>
 */
public final class Factory<T extends Buildable> implements Identifiable {
	/**
	 * Class to be factorised by this Factory.
	 */
	public final Class<? extends T> clazz;
	private final ImmutableList<Consumer<T>> finalizers;
	/**
	 * Optional ID given.
	 */
	public final Optional<String> ID;

	private final Optional<Object[]> typeArguments;

	private Factory(Class<? extends T> clazz) {
		this.clazz = clazz;
		this.finalizers = ImmutableList.<Consumer<T>>builder().build();
		this.ID = Optional.empty();
		this.typeArguments = Optional.empty();
	}

	private Factory(Class<? extends T> clazz, ImmutableList<Consumer<T>> finalizers, Optional<String> ID, Optional<Object[]> typeArguments) {
		this.clazz = clazz;
		this.finalizers = finalizers;
		this.ID = ID;
		this.typeArguments = typeArguments;
	}


	/**
	 * Method of creation of new factories. The clazz field is being initalised using provided class
	 * @param clazz for this factory to build instances from.
	 * @param <T> is type of instances that will be built.
	 * @return New Factory instance for class {@code clazz}
	 */
	public static <T extends Buildable> Factory<? extends T> of(Class<T> clazz) {
		return new Factory<>(clazz);
	}

	/**
	 * Helper method for getting factory from given Buildable instance.
	 * @param object that will be source of factory.
	 * @param <T> Type for the object.
	 * @param <S> Type of factory.
	 * @return a factory instance that was used to build this object.
	 */
	public static <T extends Buildable<S>, S extends Buildable<S>> Factory<? extends S> of(T object) {
		return object.factory();
	}

	/**
	 * Sets type arguemnts.
	 * Type arguments are then returned in @{@link Buildable#afterConstruction(Optional, Optional)}.
	 * @param args arguments to be used.
	 * @return new factory instance that will use those type arguments.
	 */
	public Factory<T> typeArguments(Object... args) {
		return new Factory<>(this.clazz, this.finalizers, this.ID, Optional.of(Arrays.copyOf(args, args.length)));
	}

	public Factory<T> finalizer(Consumer<T> finalizer) {
		return new Factory<>(this.clazz, ImmutableList.<Consumer<T>>builder().addAll(this.finalizers).add(finalizer).build(), ID, typeArguments);
	}

	/**
	 * Sets ID of this factory.
	 * @param ID to be used as ID of this factory.
	 * @return new factory instance that will use this ID.
	 */
	public Factory<T> ID(String ID) {
		return new Factory<>(this.clazz, this.finalizers, Optional.of(ID), typeArguments);
	}

	/**
	 * Creates new instance of this class using Depenency Injection.
	 * @param args to be passed as instanceArguments in {@link Buildable#afterConstruction(Optional, Optional)}
	 * @return a new instance for this factory.
	 */
	public T make(Object... args) {
		T obj = Game.resolve(Dependency.dependency(clazz).named(getID()));
		ReflectionUtil.injectField("ID", obj, getID());
		obj.afterConstruction(typeArguments, Optional.ofNullable(args));
		finalizers.stream().forEach((cons) -> cons.accept(obj));
		obj.afterFinalizers();
		return obj;
	}

	/**
	 * Creates new instance without instance arguments. {@linkplain Factory#make(Object...)}.
	 * @return a new instance for this factory.
	 */
	public T make() {
		//noinspection NullArgumentToVariableArgMethod
		return make(null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getID() {
		return ID.orElseGet(clazz::getCanonicalName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		return obj == this || obj instanceof Factory && sameType((Factory) obj);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return String.format("ID: %s, class: %s, type arguments: [%s]", getID(), clazz, typeArguments.map(Arrays::toString).orElseGet(() -> "none"));
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return getID().hashCode();
	}
}
