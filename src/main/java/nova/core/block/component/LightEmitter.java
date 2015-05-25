package nova.core.block.component;

import nova.core.component.Component;

public class LightEmitter extends Component {
	public float emittedLevel = 0;

	/**
	 * Called to get the amount of light emitted from a block.
	 * @return The level of light that is emitted by the block.
	 */
	public float getEmittedLightLevel() {
		return emittedLevel;
	}
}
