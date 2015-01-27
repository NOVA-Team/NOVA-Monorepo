package nova.core.player;

import nova.core.util.vector.Vector3d;
import nova.core.world.World;

public interface Player {
	Vector3d getPosition();

	void setPosition(Vector3d position);

	World getWorld();

	int getField(String name);

	String getUserName();
	default String getDisplayName() {
		return getUserName();
	}
}
