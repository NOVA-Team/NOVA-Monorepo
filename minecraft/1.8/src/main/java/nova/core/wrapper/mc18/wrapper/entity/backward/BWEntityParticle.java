package nova.core.wrapper.mc18.wrapper.entity.backward;

import nova.core.entity.Entity;
import nova.internal.core.Game;

/**
 * @author Calclavia
 */
public class BWEntityParticle extends Entity {

	public final String particleID;

	public BWEntityParticle(String particleID) {
		this.particleID = particleID;
	}

	@Override
	public String getID() {
		return Game.info().name + particleID;
	}
}
