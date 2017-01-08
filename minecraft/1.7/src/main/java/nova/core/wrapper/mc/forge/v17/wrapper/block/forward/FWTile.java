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

package nova.core.wrapper.mc.forge.v17.wrapper.block.forward;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import nova.core.block.Block;
import nova.core.block.Stateful;
import nova.core.component.Updater;
import nova.core.network.Syncable;
import nova.core.retention.Data;
import nova.core.retention.Storable;
import nova.core.util.id.Identifier;
import nova.core.util.id.IdentifierRegistry;
import nova.core.util.id.StringIdentifier;
import nova.core.wrapper.mc.forge.v17.network.netty.MCNetworkManager;
import nova.internal.core.Game;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.Objects;

/**
 * A Minecraft TileEntity to Nova block wrapper
 * @author Calclavia
 */
public class FWTile extends TileEntity {

	private Identifier blockID;
	private Block block;
	private Data cacheData = null;

	public FWTile() {

	}

	public FWTile(Identifier blockID) {
		this.blockID = blockID;
	}

	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
		this.blockID = block.getID();
	}

	@Override
	public Packet getDescriptionPacket() {
		if (block instanceof Syncable) {
			return ((MCNetworkManager) Game.network()).toMCPacket(((MCNetworkManager) Game.network()).writePacket(0, (Syncable) block));
		}
		return null;
	}

	@Override
	public void validate() {
		super.validate();

		block.components.add(new MCBlockTransform(block, Game.natives().toNova(getWorldObj()), new Vector3D(xCoord, yCoord, zCoord)));

		if (cacheData != null && block instanceof Storable) {
			((Storable) block).load(cacheData);
			cacheData = null;
		}

		block.events.publish(new Stateful.LoadEvent());
	}

	@Override
	public void invalidate() {
		block.events.publish(new Stateful.UnloadEvent());
		super.invalidate();
	}

	/**
	 * Updates the block.
	 */
	@Override
	public void updateEntity() {
		((Updater) block).update(0.05);
	}

	/**
	 * Only register tile updates if the block is an instance of Updater.
	 * @return Whether can update
	 */
	@Override
	public boolean canUpdate() {
		return block instanceof Updater;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		// If we have a block, store its id instead
		nbt.setTag("novaID", Game.natives().toNative(Data.serialize(block != null ? block.getID() : blockID)));

		if (block != null) {
			if (block instanceof Storable) {
				Data data = new Data();
				((Storable) block).save(data);
				nbt.setTag("nova", Game.natives().toNative(data));
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		/**
		 * Because World and Position do not exist during NBT read time, we must
		 * wait until the block is injected withPriority World and Position data using
		 * Future.
		 */
		blockID = ((Data)Game.natives().toNova(nbt)).getIdentifier("novaID");
		cacheData = Game.natives().toNova(nbt.getCompoundTag("nova"));
	}
}
