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
	private net.minecraft.entity.Entity mcEntity;

	public MCEntityTransform(ComponentProvider provider) {
		//TODO: This nullable provider is horrible. Change this.
		if (provider != null) {
			mcEntity = provider.get(MCEntityWrapper.class).wrapper;
		}
	}

	@Override
	public World world() {
		return new BWWorld(mcEntity.worldObj);
	}

	@Override
	public void setWorld(nova.core.world.World world) {
		mcEntity.travelToDimension(Arrays
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
		return new Vector3d(mcEntity.posX, mcEntity.posY, mcEntity.posZ);
	}

	@Override
	public void setPosition(Vector3d position) {
		if (mcEntity instanceof EntityPlayerMP) {
			((EntityPlayerMP) mcEntity).playerNetServerHandler.setPlayerLocation(position.x, position.y, position.z, mcEntity.rotationYaw, mcEntity.rotationPitch);
		} else {
			mcEntity.posX = position.x;
			mcEntity.posY = position.y;
			mcEntity.posZ = position.z;
		}
	}

	@Override
	public Quaternion rotation() {
		return Quaternion.fromEuler(Math.toRadians(-mcEntity.rotationYaw), -Math.toRadians(mcEntity.rotationPitch), 0);
	}

	@Override
	public void setRotation(Quaternion rotation) {
		Vector3d euler = rotation.toEuler();
		mcEntity.rotationYaw = (float) Math.toDegrees(euler.x);
		mcEntity.rotationPitch = (float) Math.toDegrees(euler.y);
	}
}
