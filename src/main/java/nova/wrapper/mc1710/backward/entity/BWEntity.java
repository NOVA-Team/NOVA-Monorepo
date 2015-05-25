package nova.wrapper.mc1710.backward.entity;

import net.minecraftforge.common.DimensionManager;
import nova.core.entity.Entity;
import nova.core.util.transform.vector.Vector3d;
import nova.core.world.World;
import nova.wrapper.mc1710.backward.world.BWWorld;

import java.util.Arrays;

/**
 * A Minecraft to NOVA Entity wrapper
 * @author Calclavia
 */
//TODO: Incomplete. All methods should be fully implemented.
public class BWEntity extends Entity {

	public net.minecraft.entity.Entity entity;

	public BWEntity(net.minecraft.entity.Entity entity) {
		this.entity = entity;
	}

	@Override
	public String getID() {
		return entity.getClass().getName();
	}

	@Override
	public World world() {
		return new BWWorld(entity.worldObj);
	}

	@Override
	public Vector3d position() {
		return new Vector3d(entity.posX, entity.posY, entity.posZ);
	}

	@Override
	public void setWorld(World world) {
		entity.travelToDimension(
			Arrays
				.stream(DimensionManager.getWorlds())
				.filter(w -> w.getProviderName().equals(world.getID()))
				.findAny()
				.get()
				.provider
				.dimensionId
		);
	}

	@Override
	public void setPosition(Vector3d position) {
		entity.setPosition(position.x, position.y, position.z);
	}
}
