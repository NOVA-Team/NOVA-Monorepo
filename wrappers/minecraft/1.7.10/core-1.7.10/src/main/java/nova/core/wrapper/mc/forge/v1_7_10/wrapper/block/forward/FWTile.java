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

package nova.core.wrapper.mc.forge.v1_7_10.wrapper.block.forward;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import nova.core.block.Block;
import nova.core.block.Stateful;
import nova.core.component.Updater;
import nova.core.network.Syncable;
import nova.core.retention.Data;
import nova.core.retention.Storable;
import nova.core.wrapper.mc.forge.v1_7_10.network.netty.MCNetworkManager;
import nova.core.wrapper.mc.forge.v1_7_10.wrapper.data.DataConverter;
import nova.internal.core.Game;

/**
 * A Minecraft TileEntity to Nova block wrapper
 * @author Calclavia
 */
public class FWTile extends TileEntity {

	protected String blockID;
	protected Block block;
	protected Data cacheData = null;

	public FWTile() {

	}

	public FWTile(String blockID) {
		this.blockID = blockID;
	}

	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		if (block.components.has(TEBlockTransform.class))
			block.components.remove(TEBlockTransform.class);
		block.components.getOrAdd(new TEBlockTransform(this));
		this.block = block;
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
		if (block.components.has(TEBlockTransform.class))
			block.components.remove(TEBlockTransform.class);
		block.components.getOrAdd(new TEBlockTransform(this));

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

		nbt.setString("novaID", blockID);

		if (block != null) {
			if (block instanceof Storable) {
				Data data = new Data();
				((Storable) block).save(data);
				nbt.setTag("nova", DataConverter.instance().toNative(data));
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
		blockID = nbt.getString("novaID");
		cacheData = DataConverter.instance().toNova(nbt.getCompoundTag("nova"));
	}
}
