package nova.wrapper.mc18.wrapper.entity.forward;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.DimensionManager;
import nova.core.component.transform.EntityTransform;
import nova.core.util.math.RotationUtil;
import nova.core.util.math.Vector3DUtil;
import nova.core.world.World;
import nova.wrapper.mc18.wrapper.block.world.BWWorld;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.Arrays;

/**
 * Wraps Transform3d used in entity
 * @author Calclavia
 */
public class MCEntityTransform extends EntityTransform {
	public final net.minecraft.entity.Entity wrapper;

	public MCEntityTransform(net.minecraft.entity.Entity wrapper) {
		this.wrapper = wrapper;
		this.setPivot(Vector3D.ZERO);
		this.setScale(Vector3DUtil.ONE);
	}

	@Override
	public World world() {
		return new BWWorld(wrapper.worldObj);
	}

	@Override
	public void setWorld(World world) {
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
	public Vector3D position() {
		return new Vector3D(wrapper.posX, wrapper.posY, wrapper.posZ);
	}

	@Override
	public void setPosition(Vector3D position) {
		if (wrapper instanceof EntityPlayerMP) {
			((EntityPlayerMP) wrapper).playerNetServerHandler.setPlayerLocation(position.getX(), position.getY(), position.getZ(), wrapper.rotationYaw, wrapper.rotationPitch);
		} else {
			wrapper.posX = position.getX();
			wrapper.posY = position.getY();
			wrapper.posZ = position.getZ();
		}
	}

	@Override
	public Rotation rotation() {
		return new Rotation(RotationUtil.DEFAULT_ORDER, -Math.toRadians(wrapper.rotationYaw) - Math.PI, -Math.toRadians(wrapper.rotationPitch), 0);
	}

	@Override
	public void setRotation(Rotation rotation) {
		double[] euler = rotation.getAngles(RotationUtil.DEFAULT_ORDER);
		wrapper.rotationYaw = (float) Math.toDegrees(euler[0]);
		wrapper.rotationPitch = (float) Math.toDegrees(euler[1]);
	}
}
