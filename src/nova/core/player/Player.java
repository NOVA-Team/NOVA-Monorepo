package nova.core.player;

import nova.core.util.Vector3d;
import nova.core.world.World;

public interface Player {
	Vector3d getPosition();
	World getWorld();
	void setPosition(Vector3d position);
	String getUserName();
	String getDisplayName();
	int getField(String name);
}
