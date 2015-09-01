package nova.core.wrapper.mc17.wrapper.entity.forward;

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
import nova.core.wrapper.mc17.wrapper.data.DataWrapper;
import nova.internal.core.Game;

/**
 * Entity wrapper
 * @author Calclavia
 */
public class FWEntity extends net.minecraft.entity.Entity {

	public final Entity wrapped;
	public final EntityTransform transform;
	boolean firstTick = true;

	//TODO: Need a less argument constructor for retention
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
			wrapped.events.publish(new Stateful.LoadEvent());
			updateCollider();
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

		updateCollider();

		/**
		 * Update all components in the entity.
		 */
		wrapped.components()
			.stream()
			.filter(component -> component instanceof Updater)
			.forEach(component -> ((Updater) component).update(deltaTime));
	}

	/**
	 * Wraps the entity collider values
	 */
	public void updateCollider() {
		//Wrap entity collider
		if (wrapped.has(Collider.class)) {
			Collider collider = wrapped.get(Collider.class);

			//Transform cuboid based on entity.
			Cuboid size = collider
				.boundingBox
				.get();
			///.scalarMultiply(transform.scale());

			setBounds(size);
		}
	}

	@Override
	protected void setSize(float width, float height) {
		if (width != this.width || height != this.height) {
			this.width = width;
			this.height = height;
			setBounds(new Cuboid(-width / 2, -height / 2, -width / 2, width / 2, height / 2, width / 2));
		}
	}

	@Override
	public void setPosition(double x, double y, double z) {
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		//Reset the bounding box
		setBounds(getBoundingBox() != null ? Game.natives().toNova(getBoundingBox()) : Cuboid.ZERO);
	}

	/**
	 * Sets the bounding box of the entity based on NOVA cuboid bounds
	 * @param bounds NOVA Cuboid bounds
	 */
	public void setBounds(Cuboid bounds) {
		Cuboid translated = transform != null ? bounds.add(transform.position()) : bounds;
		boundingBox.setBounds(translated.min.getX(), translated.min.getY(), translated.min.getZ(), translated.max.getX(), translated.max.getY(), translated.max.getZ());
	}

	@Override
	public void setDead() {
		wrapped.events.publish(new Stateful.UnloadEvent());
		super.setDead();
	}
}
