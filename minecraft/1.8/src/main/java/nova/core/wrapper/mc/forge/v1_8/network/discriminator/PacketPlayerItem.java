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

package nova.core.wrapper.mc.forge.v1_8.network.discriminator;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import nova.core.network.Syncable;
import nova.core.wrapper.mc.forge.v1_8.network.MCPacket;

/**
 * A packet handler for players who are currently holding their item.
 * @author Calclavia
 */
//TODO: Move to NOVA Core
public class PacketPlayerItem extends PacketAbstract {
	public int slotId;

	public PacketPlayerItem() {

	}

	public PacketPlayerItem(int slotId) {
		this.slotId = slotId;
	}

	public PacketPlayerItem(EntityPlayer player) {
		this(player.inventory.currentItem);
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeInt(slotId);
		buffer.writeBytes(data);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		slotId = buffer.readInt();
		data = buffer.slice();
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		ItemStack stack = player.inventory.getStackInSlot(this.slotId);

		if (stack != null && stack.getItem() instanceof Syncable) {
			MCPacket mcPacket = new MCPacket(data);
			mcPacket.setID(data.readInt());
			((Syncable) stack.getItem()).read(mcPacket);
		}
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		ItemStack stack = player.inventory.getStackInSlot(this.slotId);

		if (stack != null && stack.getItem() instanceof Syncable) {
			MCPacket mcPacket = new MCPacket(data);
			mcPacket.setID(data.readInt());
			((Syncable) stack.getItem()).read(mcPacket);
		}
	}
}
