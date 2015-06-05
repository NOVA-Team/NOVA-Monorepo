package nova.wrapper.mc1710.wrapper.entity.forward;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.DimensionManager;
import nova.core.component.ComponentProvider;
import nova.core.component.transform.EntityTransform;
import nova.core.util.transform.matrix.Quaternion;
import nova.core.util.transform.vector.Vector3d;
import nova.core.world.World;
import nova.wrapper.mc1710.wrapper.block.world.BWWorld;

import java.util.Arrays;

/**
 * Wraps Transform3d used in entity
 * @author Calclavia
 */
public class MCEntityTransform extends EntityTransform {
	public final net.minecraft.entity.Entity wrapper;

	public MCEntityTransform(net.minecraft.entity.Entity wrapper) {
		this.wrapper = wrapper;
		this.setPivot(Vector3d.zero);
		this.setScale(Vector3d.one);
	}

	@Override
	public World world() {
		return new BWWorld(wrapper.worldObj);
	}

	@Override
	public void setWorld(nova.core.world.World world) {
		wrapper.travelToDimension(Arrays
				.stream(DimensionManager.getWorlds())
				.filter(w -> w.getProviderName().equals(world.getID()))
				.findAny()
				.get()
				.provider
				.dimensionId
		);
	}

	@Override
	public Vector3d position() {
		return new Vector3d(wrapper.posX, wrapper.posY, wrapper.posZ);
	}

	@Override
	public void setPosition(Vector3d position) {
		if (wrapper instanceof EntityPlayerMP) {
			((EntityPlayerMP) wrapper).playerNetServerHandler.setPlayerLocation(position.x, position.y, position.z, wrapper.rotationYaw, wrapper.rotationPitch);
		} else {
			wrapper.posX = position.x;
			wrapper.posY = position.y;
			wrapper.posZ = position.z;
		}
	}

	@Override
	public Quaternion rotation() {
		return Quaternion.fromEuler(-Math.toRadians(wrapper.rotationYaw) - Math.PI, -Math.toRadians(wrapper.rotationPitch));
	}

	@Override
	public void setRotation(Quaternion rotation) {
		Vector3d euler = rotation.toEuler();
		wrapper.rotationYaw = (float) Math.toDegrees(euler.x);
		wrapper.rotationPitch = (float) Math.toDegrees(euler.y);
	}
}
