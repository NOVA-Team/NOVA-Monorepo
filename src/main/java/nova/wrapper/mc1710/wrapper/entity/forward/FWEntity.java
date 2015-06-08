package nova.wrapper.mc1710.wrapper.entity.forward;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import nova.core.block.Stateful;
import nova.core.component.Updater;
import nova.core.component.misc.Collider;
import nova.core.component.transform.EntityTransform;
import nova.core.entity.Entity;
import nova.core.entity.EntityFactory;
import nova.core.retention.Data;
import nova.core.retention.Storable;
import nova.core.util.shape.Cuboid;
import nova.internal.core.Game;
import nova.wrapper.mc1710.wrapper.data.DataWrapper;

/**
 * Entity wrapper
 * @author Calclavia
 */
public class FWEntity extends net.minecraft.entity.Entity {

	public final Entity wrapped;
	public final EntityTransform transform;
	boolean firstTick = true;

	public FWEntity(World world, EntityFactory factory, Object... args) {
		super(world);
		this.wrapped = factory.make(args);
		this.transform = new MCEntityTransform(this);
		wrapped.add(transform);
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
		if (firstTick) {
			prevPosX = posX;
			prevPosY = posY;
			prevPosZ = posZ;
			setPosition(posX, posY, posZ);
			firstTick = false;
		}

		super.onUpdate();
		double deltaTime = 0.05;

		if (wrapped instanceof Updater) {
			((Updater) wrapped).update(deltaTime);
		}

		//Wrap entity collider
		if (wrapped.has(Collider.class)) {
			Collider collider = wrapped.get(Collider.class);

			//Transform cuboid based on entity.
			Cuboid size = collider
				.boundingBox
				.get();
			///.scalarMultiply(transform.scale());

			//Sadly Minecraft doesn't support rotated cuboids. And fixed x-z sizes. We take average..
			float width = (float) ((size.max.getX() - size.min.getX()) + (size.max.getZ() - size.min.getZ())) / 2;
			float height = (float) (size.max.getY() - size.min.getY());
			setSize(width, height);
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
