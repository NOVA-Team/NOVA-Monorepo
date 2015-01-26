package nova.core.player;

import nova.core.util.Identifiable;
import nova.core.util.Vector3d;
import nova.core.world.World;

public interface Player {
	Vector3d getPosition();
	World getWorld();
	void setPosition(Vector3d position);
	int getField(String name);

	String getUserName();
	default String getDisplayName() {
		return getUserName();
	}
}
