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

package nova.core.wrapper.mc.forge.v1_11.wrapper.block.forward;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import nova.core.block.Block;
import nova.core.block.Stateful;
import nova.core.network.Syncable;
import nova.core.retention.Data;
import nova.core.retention.Storable;
import nova.core.wrapper.mc.forge.v1_11.network.netty.MCNetworkManager;
import nova.internal.core.Game;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.io.IOException;

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
		this.block = block;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		if (block instanceof Syncable) {
			return new FWPacketUpdateTileEntity(((MCNetworkManager) Game.network()).toMCPacket(((MCNetworkManager) Game.network()).writePacket(0, (Syncable) block)),
			this.getPos(), this.getBlockMetadata(), this.getTileData());
		}
		return null;
	}

	@Override
	public void validate() {
		super.validate();
		block.components.getOrAdd(new MCBlockTransform(block, Game.natives().toNova(getWorld()), new Vector3D(pos.getX(), pos.getY(), pos.getZ())));

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

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		nbt.setString("novaID", blockID);

		if (block != null) {
			if (block instanceof Storable) {
				Data data = new Data();
				((Storable) block).save(data);
				nbt.setTag("nova", Game.natives().toNative(data));
			}
		}

		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		blockID = nbt.getString("novaID");
		cacheData = Game.natives().toNova(nbt.getCompoundTag("nova"));
	}

	private static class FWPacketUpdateTileEntity<T extends INetHandler> extends SPacketUpdateTileEntity {
		private final Packet<T> packet;

		public FWPacketUpdateTileEntity(Packet<T> packet, BlockPos blockPosIn, int metadataIn, NBTTagCompound compoundIn) {
			super(blockPosIn, metadataIn, compoundIn);
			this.packet = packet;
		}

		@Override
		public void processPacket(INetHandlerPlayClient handler) {
			super.processPacket(handler);
			try {
				this.packet.processPacket((T) handler);
			} catch (ClassCastException | NoSuchMethodError e) {
				// Why did Mojang incompatibly replace getDescriptionPacket() with getUpdatePacket().
			}
		}

		@Override
		public void writePacketData(PacketBuffer buf) throws IOException {
			super.writePacketData(buf);
			this.packet.writePacketData(buf);
		}

		@Override
		public void readPacketData(PacketBuffer buf) throws IOException {
			super.readPacketData(buf);
			this.packet.readPacketData(buf);
		}
	}
}
