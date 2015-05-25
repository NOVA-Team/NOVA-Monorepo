package nova.core.block.component;

import nova.core.component.Component;

import java.util.function.Supplier;

public class LightEmitter extends Component {

	/**
	 * Called to get the amount of light emitted from a block.
	 * @return The level of light that is emitted by the block.
	 */
	public Supplier<Float> emittedLevel = () -> 0f;

	public LightEmitter setEmittedLevel(Supplier<Float> emittedLevel) {
		this.emittedLevel = emittedLevel;
		return this;
	}
}
