package nova.core.component.transform;

import nova.core.component.Component;

/**
 * A tansform that is associates withPriority a position.
 * @author Calclavia
 */
public class PositionTransform<P> extends Component {

	private P position;

	public P position() {
		return position;
	}

	public void setPosition(P position) {
		this.position = position;
	}
}
