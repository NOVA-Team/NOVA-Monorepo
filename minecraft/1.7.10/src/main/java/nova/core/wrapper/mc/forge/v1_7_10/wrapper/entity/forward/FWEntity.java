/*
 * Copyright (c) 2015 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License version 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
 */

package nova.core.wrapper.mc.forge.v1_7_10.wrapper.entity.forward;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
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
import nova.core.wrapper.mc.forge.v1_7_10.util.WrapperEvent;
import nova.core.wrapper.mc.forge.v1_7_10.wrapper.cuboid.CuboidConverter;
import nova.core.wrapper.mc.forge.v1_7_10.wrapper.data.DataConverter;
import nova.internal.core.Game;

/**
 * Entity wrapper
 * @author Calclavia
 */
public class FWEntity extends net.minecraft.entity.Entity implements IEntityAdditionalSpawnData {

	protected final EntityTransform transform;
	protected Entity wrapped;
	boolean firstTick = true;

	public FWEntity(World worldIn) {
		super(worldIn);
		this.transform = new MCEntityTransform(this);
	}

	public FWEntity(World world, EntityFactory factory, Object... args) {
		this(world);
		setWrapped(factory.build());
		entityInit();
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		if (wrapped instanceof Storable && nbt.hasKey("nova")) {
			((Storable) wrapped).load(DataConverter.instance().toNova(nbt.getCompoundTag("nova")));
		}
		if (wrapped == null) {
			//This entity was saved to disk.
			setWrapped(Game.entities().get(nbt.getString("novaID")).get().build());
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
		if (wrapped instanceof Storable) {
			Data data = new Data();
			((Storable) wrapped).save(data);
			nbt.setTag("nova", DataConverter.instance().toNative(data));
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

		setWrapped(Game.entities().get(id).get().build());
	}

	public Entity getWrapped() {
		return wrapped;
	}

	private void setWrapped(Entity wrapped) {
		this.wrapped = wrapped;
		wrapped.components.add(transform);
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
			WrapperEvent.FWEntityCreate event = new WrapperEvent.FWEntityCreate(wrapped, this);
			Game.events().publish(event);
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
		if (wrapped.components.has(Collider.class)) {
			Collider collider = wrapped.components.get(Collider.class);

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
		if (getBoundingBox() != null) {
			setBounds(CuboidConverter.instance().toNova(getBoundingBox()));
		}
	}

	/**
	 * Sets the bounding box of the entity based on NOVA cuboid bounds
	 * @param bounds NOVA Cuboid bounds
	 */
	public void setBounds(Cuboid bounds) {
		//TODO: Fix moveEntity auto-centering
		Cuboid translated = transform != null ? bounds.add(transform.position()) : bounds;
		boundingBox.setBounds(translated.min.getX(), translated.min.getY(), translated.min.getZ(), translated.max.getX(), translated.max.getY(), translated.max.getZ());
	}

	@Override
	public void setDead() {
		wrapped.events.publish(new Stateful.UnloadEvent());
		super.setDead();
	}
}
