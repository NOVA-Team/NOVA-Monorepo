package nova.wrapper.mc1710.backward.entity;

import nova.core.entity.Entity;
import nova.core.util.transform.Vector3d;
import nova.core.world.World;
import nova.wrapper.mc1710.backward.world.BWWorld;

/**
 * A Nova to Minecraft entity wrapper
 * @author Calclavia
 */
//TODO: Incomplete. All methods should be fully implemented.
public class BWEntity extends Entity {

	public net.minecraft.entity.Entity entity;

	public BWEntity(net.minecraft.entity.Entity entity) {
		//TODO: Should this be entity ID?
		super(entity.getEntityId(), new BWWorld(entity.worldObj), new Vector3d(entity.posX, entity.posY, entity.posZ));
		this.entity = entity;
	}

	@Override
	public String getID() {
		return entity.getClass().getName();
	}

	@Override
	public World getWorld() {
		return new BWWorld(entity.worldObj);
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
