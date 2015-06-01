package nova.wrapper.mc1710.wrapper.entity.forward;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import nova.core.block.Stateful;
import nova.core.component.Updater;
import nova.core.component.transform.EntityTransform;
import nova.core.entity.Entity;
import nova.core.entity.EntityFactory;
import nova.core.game.Game;
import nova.core.retention.Data;
import nova.core.retention.Storable;
import nova.wrapper.mc1710.wrapper.data.DataWrapper;

/**
 * Entity wrapper
 * @author Calclavia
 */
public class FWEntity extends net.minecraft.entity.Entity {

	public final Entity wrapped;
	public final EntityTransform transform;

	public FWEntity(World world, EntityFactory factory, Object... args) {
		super(world);
		this.wrapped = factory.make(args);
		wrapped.add(new MCEntityWrapper(this));
		this.transform = new MCEntityTransform(wrapped);
		wrapped.components().add(transform);
		entityInit();
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		if (wrapped instanceof Storable) {
			((Storable) wrapped).load(Game.natives().toNova(nbt));
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
		if (wrapped instanceof Storable) {
			Data data = new Data();
			((Storable) wrapped).save(data);
			DataWrapper.instance().toNative(nbt, data);
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
			wrapped.loadEvent.publish(new Stateful.LoadEvent());
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

	@Override
	public void setDead() {
		wrapped.unloadEvent.publish(new Stateful.UnloadEvent());
		super.setDead();
	}
}
