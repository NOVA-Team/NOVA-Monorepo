package nova.core.util.collection;

/**
 * A 2-Tuple
 * @author Calclavia
 */
public class Pair<L, R> {
	public L _1;
	public R _2;

	public Pair(L left, R right) {
		this._1 = left;
		this._2 = right;
	}

	@Override
	public int hashCode() {
		if (_1 == null || _2 == null) {
			super.hashCode();
		}
		return _1.hashCode() ^ _2.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof Pair)) {
			return false;
		}
		Pair pairo = (Pair) o;
		return this._1.equals(pairo._1) && this._2.equals(pairo._2);
	}
}