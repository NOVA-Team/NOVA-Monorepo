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

package nova.core.wrapper.mc.forge.v1_11_2.wrapper.block.forward;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import nova.core.block.Block;
import nova.core.block.Stateful;
import nova.core.network.Syncable;
import nova.core.retention.Data;
import nova.core.retention.Storable;
import nova.core.util.Direction;
import nova.core.wrapper.mc.forge.v1_11_2.network.netty.MCNetworkManager;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.DirectionConverter;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.capability.forward.NovaCapabilityProvider;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.data.DataConverter;
import nova.internal.core.Game;

import java.io.IOException;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A Minecraft TileEntity to Nova block wrapper
 * @author Calclavia
 */
public class FWTile extends TileEntity implements NovaCapabilityProvider {

	protected String blockID;
	protected Block block;
	protected Data cacheData = null;

	public FWTile() {}

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
	public SPacketUpdateTileEntity getUpdatePacket() {
		if (block instanceof Syncable) {
			return new FWPacketUpdateTileEntity<>(((MCNetworkManager) Game.network()).toMCPacket(((MCNetworkManager) Game.network()).writePacket(0, (Syncable) block)),
			this.getPos(), this.getBlockMetadata(), this.getTileData());
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

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		nbt.setString("novaID", blockID);

		if (block != null) {
			if (block instanceof Storable) {
			Data data = new Data();
			((Storable) block).save(data);
			nbt.setTag("nova", DataConverter.instance().toNative(data));
			}
		}

		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		blockID = nbt.getString("novaID");
		cacheData = DataConverter.instance().toNova(nbt.getCompoundTag("nova"));
	}

	@Override
	public boolean hasCapability(Capability<?> capability, Direction direction) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
//			return
//				block.components.has(FluidProvider.class, direction) ||
//				block.components.has(FluidConsumer.class, direction) ||
//				block.components.has(FluidHandler.class, direction) ||
//				block instanceof SidedTankProvider;
		} else if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return false; // TODO: implement
		}

		return false;
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return hasCapability(capability, DirectionConverter.instance().toNova(facing)) || super.hasCapability(capability, facing);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> Optional<T> getCapability(Capability<T> capability, Direction direction) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
//			return (Optional<T>) Optional.of(new FWFluidHandler(
//				block.components.getOp(FluidProvider.class, direction),
//				block.components.getOp(FluidConsumer.class, direction),
//				block.components.getOp(FluidHandler.class, direction),
//				Optional.of(block)
//					.filter(b -> b instanceof SidedTankProvider)
//					.map(b -> (SidedTankProvider) b), direction)).filter(FWFluidHandler::isPresent);
		} else if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return Optional.empty(); // TODO: implement
		}

		return Optional.empty();
	}

	@Override
	@Nullable
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		return getCapability(capability, DirectionConverter.instance().toNova(facing))
			.orElseGet(() -> super.getCapability(capability, facing));
	}

	private static class FWPacketUpdateTileEntity<T extends INetHandler> extends SPacketUpdateTileEntity {
		private final Packet<T> packet;

		private FWPacketUpdateTileEntity(Packet<T> packet, BlockPos blockPosIn, int metadataIn, NBTTagCompound compoundIn) {
			super(blockPosIn, metadataIn, compoundIn);
			this.packet = packet;
		}

		@Override
		@SuppressWarnings("unchecked")
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
