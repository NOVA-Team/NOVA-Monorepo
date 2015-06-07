package nova.core.factory;

import com.google.common.collect.ImmutableList;
import nova.core.util.exception.WrapperBrokenException;
import nova.internal.Game;
import se.jbee.inject.Dependency;
import se.jbee.inject.Injector;

import java.util.Arrays;
import java.util.function.Consumer;

public class Factory<T extends Buildable> implements Cloneable{

    private final ImmutableList<Consumer<T>> finalizers;
    private final Class<T> clazz;

    private Factory(Class<T> clazz) {
        this.clazz = clazz;
        finalizers = ImmutableList.<Consumer<T>>builder().build();
    }
    private Factory(Class<T> clazz, ImmutableList<Consumer<T>> finalizers) {
        this.clazz = clazz;
        this.finalizers = finalizers;
    }

    public static <T extends Buildable> Factory of(Class<T> clazz) {
        return new Factory(clazz);
    }
    public static <T extends Buildable> Factory of(T obj) {
        return obj.factory();
    }

    public Factory<T> arguments(Object ... args) {
        Object argsCopy = Arrays.copyOf(args, args.length);
        return this.withFinalizer((buildable) -> buildable.arguments(argsCopy));
    }

    public Factory<T> withFinalizer(Consumer<T> finalizer){
        return new Factory<>(this.clazz, ImmutableList.<Consumer<T>>builder().addAll(this.finalizers).add(finalizer).build());
    }

    public T resolve(Injector injector) {
        T obj = injector.resolve(Dependency.dependency(clazz));
        finalizers.stream().forEach((cons) -> cons.accept(obj));
        return obj;
    }

    public T resolve() {
        Injector injector = Game.injector().orElseThrow(() -> new WrapperBrokenException("Game.injector is not available"));
        T obj = injector.resolve(Dependency.dependency(clazz));
        finalizers.stream().forEach((cons) -> cons.accept(obj));
        return obj;
    }







}
