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

package nova.core.wrapper.mc.forge.v1_11_2.network.discriminator;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import nova.core.network.handler.PacketHandler;
import nova.core.wrapper.mc.forge.v1_11_2.network.MCPacket;
import nova.core.wrapper.mc.forge.v1_11_2.network.netty.MCNetworkManager;
import nova.core.wrapper.mc.forge.v1_11_2.util.WrapUtility;
import nova.internal.core.Game;

/**
 * NOVA Packet Structure:
 *
 * 1. Packet Type ID
 * 2. Packet Sub ID
 * 3. Data
 * @author Calclavia
 */
public class NovaPacket extends PacketAbstract {

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeBytes(data);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		data = buffer.slice();
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		handle(player);
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		handle(player);
	}

	public void handle(EntityPlayer player) {
		try {
			MCNetworkManager network = (MCNetworkManager) Game.network();
			PacketHandler<?> packetHandler = network.getPacketType(data.readInt());
			int subId = data.readInt();
			MCPacket packet = new MCPacket(data.slice(), WrapUtility.getNovaPlayer(player).get());
			//Set the ID of the packet
			packet.setID(subId);
			packetHandler.read(packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
