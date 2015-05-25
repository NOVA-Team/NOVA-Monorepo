package nova.core.block.component;

public interface LightEmitter {
	/**
	 * Called to get the amount of light emitted from a block.
	 *
	 * @return The level of light that is emitted by the block.
	 */
	public float getEmittedLightLevel();
}
