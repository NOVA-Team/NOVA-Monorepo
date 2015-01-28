package nova.wrapper.mc1710.backward.entity;

import nova.core.entity.Entity;
import nova.core.util.transform.Vector3d;
import nova.core.world.World;
import nova.wrapper.mc1710.backward.world.WorldWrapper;

/**
 * A Nova to Minecraft entity wrapper
 * @author Calclavia
 */
//TODO: Incomplete. All methods should be fully implemented.
public class EntityBackwardWrapper extends Entity {

	public net.minecraft.entity.Entity entity;

	public EntityBackwardWrapper(net.minecraft.entity.Entity entity) {
		//TODO: Should this be entity ID?
		super(entity.getEntityId(), new WorldWrapper(entity.worldObj), new Vector3d(entity.posX, entity.posY, entity.posZ));
		this.entity = entity;
	}

	@Override
	public String getID() {
		return null;
	}

	@Override
	public World getWorld() {
		return new WorldWrapper(entity.worldObj);
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
