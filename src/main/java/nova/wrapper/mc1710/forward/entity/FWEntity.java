package nova.wrapper.mc1710.forward.entity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import nova.core.entity.Entity;
import nova.core.entity.EntityWrapper;
import nova.core.util.components.Storable;
import nova.core.util.components.Updater;
import nova.core.util.transform.Quaternion;
import nova.core.util.transform.Vector3d;
import nova.wrapper.mc1710.backward.world.BWWorld;
import nova.wrapper.mc1710.util.NBTUtility;

import java.util.HashMap;

/**
 * @author Calclavia
 */
public class FWEntity extends net.minecraft.entity.Entity implements EntityWrapper {
	private final Entity wrapped;

	public FWEntity(World world, Entity entity) {
		super(world);
		this.wrapped = entity;
	}

	@Override
	protected void entityInit() {
		wrapped.awake();
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		if (wrapped instanceof Updater) {
			((Updater) wrapped).update(0.05);
		}
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		if (wrapped instanceof Storable) {
			((Storable) wrapped).load(NBTUtility.nbtToMap(nbt));
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {

		if (wrapped instanceof Storable) {
			HashMap<String, Object> data = new HashMap<>();
			((Storable) wrapped).save(data);
			NBTUtility.mapToNBT(nbt, data);
		}
	}

	@Override
	public boolean isValid() {
		return !isDead;
	}

	@Override
	public nova.core.world.World world() {
		return new BWWorld(worldObj);
	}

	@Override
	public Vector3d position() {
		return new Vector3d(posX, posY, posZ);
	}

	@Override
	public Quaternion rotation() {
		return Quaternion.fromEuler(Math.toRadians(rotationYaw), Math.toRadians(rotationPitch), 0);
	}

	@Override
	public void setWorld(nova.core.world.World world) {
		//TODO: Change entity's world
	}

	@Override
	public void setPosition(Vector3d position) {
		setPosition(position.x, position.y, position.z);
	}

	@Override
	public void setRotation(Quaternion rotation) {
		Vector3d euler = rotation.toEuler();
		setRotation((float) Math.toDegrees(euler.x), (float) Math.toDegrees(euler.y));
	}
}
