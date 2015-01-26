package asie.api.core;

public abstract class Logic {
	private boolean wasValidated;

	public void initialize() {

	}

	public void validate() {
		wasValidated = true;
	}

	public void invalidate() {
		wasValidated = false;
	}

	public boolean isValid() {
		return wasValidated;
	}

	/**
	 * This decides whether the block will be added to the list of updatable
	 * blocks by default. You can later change this by using
	 * startRequestingUpdates and stopRequestingUpdates in World.
	 * @return Whether the block wants to be updated by default.
	 */
	public abstract boolean canUpdate();

	public void update() {
	}
}

