package nova.wrapper.mc1710.backward.entity;

import net.minecraft.entity.Entity;
import nova.core.util.transform.Vector3d;
import nova.core.world.World;

/**
 * A Nova to Minecraft entity wrapper
 * @author Calclavia
 */
//TODO: Incomplete. All methods should be fully implemented.
public class EntityWrapper implements nova.core.entity.Entity {

	public Entity entity;

	public EntityWrapper(Entity entity) {
		this.entity = entity;
	}

	@Override
	public World getWorld() {
		//		return entity.worldObj;
		return null;
	}

	@Override
	public Vector3d getPosition() {
		return new Vector3d(entity.posX, entity.posY, entity.posZ);
	}

	@Override
	public boolean setWorld(World world) {
		return false;
	}

	@Override
	public boolean setPosition(Vector3d position) {
		return false;
	}
}
