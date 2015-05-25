package nova.wrapper.mc1710.forward.entity;

import nova.core.component.ComponentProvider;
import nova.core.component.transform.Transform3d;
import nova.core.util.transform.matrix.Quaternion;
import nova.core.util.transform.vector.Vector3d;

/**
 * Wraps Transform3d used in entity
 * @author Calclavia
 */
public class MCTransform3d extends Transform3d {
	private net.minecraft.entity.Entity mcEntity;

	public MCTransform3d(ComponentProvider provider) {
		//TODO: This nullable provider is horrible. Change this.
		if (provider != null) {
			mcEntity = (net.minecraft.entity.Entity) ((nova.core.entity.Entity) provider).wrapper;
		}
	}

	@Override
	public Vector3d getPosition() {
		return new Vector3d(mcEntity.posX, mcEntity.posY, mcEntity.posZ);
	}

	@Override
	public void setPosition(Vector3d position) {
		mcEntity.posX = position.x;
		mcEntity.posY = position.y;
		mcEntity.posZ = position.z;
	}

	@Override
	public Quaternion getRotation() {
		return Quaternion.fromEuler(Math.toRadians(mcEntity.rotationYaw), Math.toRadians(mcEntity.rotationPitch), 0);
	}

	@Override
	public void setRotation(Quaternion rotation) {
		Vector3d euler = rotation.toEuler();
		mcEntity.rotationYaw = (float) Math.toDegrees(euler.x);
		mcEntity.rotationPitch = (float) Math.toDegrees(euler.y);
	}
}
