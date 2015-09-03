package nova.core.wrapper.mc18.wrapper.entity.forward;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import nova.core.block.Stateful;
import nova.core.component.Updater;
import nova.core.component.misc.Collider;
import nova.core.component.transform.EntityTransform;
import nova.core.entity.Entity;
import nova.core.entity.EntityFactory;
import nova.core.retention.Data;
import nova.core.retention.Storable;
import nova.core.util.shape.Cuboid;
import nova.core.wrapper.mc18.wrapper.data.DataWrapper;
import nova.internal.core.Game;

/**
 * Entity wrapper
 * @author Calclavia
 */
public class FWEntity extends net.minecraft.entity.Entity implements IEntityAdditionalSpawnData {

	protected Entity wrapped;
	protected final EntityTransform transform;
	boolean firstTick = true;

	public FWEntity(World worldIn) {
		super(worldIn);
		this.transform = new MCEntityTransform(this);
	}

	public FWEntity(World world, EntityFactory factory, Object... args) {
		this(world);
		setWrapped(factory.make(args));
		entityInit();
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		if (wrapped instanceof Storable) {
			((Storable) wrapped).load(Game.natives().toNova(nbt));
		}
		if (wrapped == null) {
			//This entity was saved to disk.
			setWrapped(Game.entities().getFactory(nbt.getString("novaID")).get().make());
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
		if (wrapped instanceof Storable) {
			Data data = new Data();
			((Storable) wrapped).save(data);
			DataWrapper.instance().toNative(nbt, data);
		}
		nbt.setString("novaID", wrapped.getID());
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		//Write the ID of the entity to client
		String id = wrapped.getID();
		char[] chars = id.toCharArray();
		buffer.writeInt(chars.length);

		for (char c : chars)
			buffer.writeChar(c);
	}

	@Override
	public void readSpawnData(ByteBuf buffer) {
		//Load the client ID
		String id = "";
		int length = buffer.readInt();
		for (int i = 0; i < length; i++)
			id += buffer.readChar();

		setWrapped(Game.entities().getFactory(id).get().make());
	}

	private void setWrapped(Entity wrapped) {
		this.wrapped = wrapped;
		wrapped.add(transform);
	}

	public Entity getWrapped() {
		return wrapped;
	}

	public EntityTransform getTransform() {
		return transform;
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
		if (wrapped != null) {
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
		} else {
			Game.logger().error("Ticking entity without wrapped entity object.");
		}
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
		setEntityBoundingBox(Game.natives().toNative(transform != null ? bounds.add(transform.position()) : bounds));
	}

	@Override
	public void setDead() {
		wrapped.events.publish(new Stateful.UnloadEvent());
		super.setDead();
	}

}
