package nova.wrapper.mc1710.forward.entity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import nova.core.component.Updater;
import nova.core.entity.Entity;
import nova.core.entity.EntityFactory;
import nova.core.entity.EntityWrapper;
import nova.core.retention.Data;
import nova.core.retention.Storable;
import nova.core.util.transform.Transform3d;
import nova.core.util.transform.vector.Vector3d;
import nova.wrapper.mc1710.backward.world.BWWorld;
import nova.wrapper.mc1710.util.DataUtility;

import java.util.Arrays;

/**
 * Entity wrapper
 * @author Calclavia
 */
public class FWEntity extends net.minecraft.entity.Entity implements EntityWrapper {

	public final Entity wrapped;
	public final Transform3d transform;

	public FWEntity(World world, EntityFactory factory, Object... args) {
		super(world);
		this.wrapped = factory.makeEntity(this, args);
		this.transform = wrapped.transform;
		entityInit();
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		if (wrapped instanceof Storable) {
			((Storable) wrapped).load(DataUtility.nbtToData(nbt));
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {

		if (wrapped instanceof Storable) {
			Data data = new Data();
			((Storable) wrapped).save(data);
			DataUtility.dataToNBT(nbt, data);
		}
	}

	/**
	 * All methods below here are exactly the same between FWEntity and FWEntityFX.
	 * *****************************************************************************
	 */
	@Override
	protected void entityInit() {
		//MC calls entityInit() before we finish wrapping, so this variable is required to check if wrapped exists.
		if (wrapped != null) {
			wrapped.awake();
		}
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		double deltaTime = 0.05;

		if (wrapped instanceof Updater) {
			((Updater) wrapped).update(deltaTime);
		}

		/**
		 * Update all components in the entity.
		 */
		wrapped.components()
			.stream()
			.filter(component -> component instanceof Updater)
			.forEach(component -> ((Updater) component).update(deltaTime));
	}

	/**
	 * Entity Wrapper Methods
	 * @return
	 */
	@Override
	public nova.core.world.World world() {
		return new BWWorld(worldObj);
	}

	@Override
	public Vector3d position() {
		return new Vector3d(posX, posY, posZ);
	}

	@Override
	public void setWorld(nova.core.world.World world) {
		travelToDimension(Arrays
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
		setPosition(position.x, position.y, position.z);
	}
}
