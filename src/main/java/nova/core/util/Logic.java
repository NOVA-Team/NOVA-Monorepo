package nova.core.util;

public abstract class Logic
{
	private boolean wasValidated;

	public void validate()
	{
		wasValidated = true;
	}

	public void invalidate()
	{
		wasValidated = false;
	}

	public boolean isValid()
	{
		return wasValidated;
	}

	/**
	 * This decides whether the block will be added to the list of updatable
	 * blocks by default. You can later change this by using
	 * startRequestingUpdates and stopRequestingUpdates in World.
	 *
	 * @return Whether the block wants to be updated by default.
	 */
	public abstract boolean canUpdate();

	/**
	 * Called in the first update of the Logic.
	 */
	public void start()
	{

	}

	/**
	 * Performs and update call on this Logic.
	 *
	 * @param deltaTime - The time (in seconds) that has passed between this update and the last update.
	 */
	public void update(double deltaTime)
	{
	}
}

