package nova.core.util.collection;

/**
 * A 2-Tuple
 *
 * @param <L> First type
 * @param <R> Second type
 * @author Calclavia
 */
public class Tuple2<L, R> {
	public L _1;
	public R _2;

	public Tuple2(L left, R right) {
		this._1 = left;
		this._2 = right;
	}
}