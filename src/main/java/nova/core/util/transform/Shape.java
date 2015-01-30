package nova.core.util.transform;

/**
 * Represents a 3-dimensional shape.
 *
 * @param <I> -describeme-
 * @param <O> -describeme-
 * @author Calclavia
 */
public abstract class Shape<I extends Shape<I, O>, O extends I> extends Operator<I, O> {

}
