package nova.wrapper.mc1710.forward.entity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import nova.core.entity.Entity;
import nova.core.util.components.Storable;
import nova.wrapper.mc1710.util.NBTUtility;

import java.util.HashMap;

/**
 * @author Calclavia
 */
public class FWEntity extends net.minecraft.entity.Entity {
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
}
