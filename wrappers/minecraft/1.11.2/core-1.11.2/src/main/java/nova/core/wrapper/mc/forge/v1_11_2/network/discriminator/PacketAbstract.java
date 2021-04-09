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
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import nova.core.wrapper.mc.forge.v1_11_2.network.netty.MCNetworkManager;

/**
 * For custom packets extend this Class and register on Mod loading phase
 * <p>
 * Without registering a NPE will be thrown as the {@link MCNetworkManager} won't know how to handle it
 * </p>
 * To send this packet also look at {@link MCNetworkManager#sendToAll(PacketAbstract)}
 * And other implementations there.
 * @author tgame14, Calclavia
 * @since 26/05/14
 */
public abstract class PacketAbstract {
	public ByteBuf data = Unpooled.buffer();
	EntityPlayer sender = null;

	/**
	 * Encode the packet data into the ByteBuf stream. Complex data sets may need specific data handlers
	 * @param ctx channel context
	 * @param buffer the buffer to encode into
	 * @see net.minecraftforge.fml.common.network.ByteBufUtils
	 */
	public abstract void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer);

	/**
	 * Decode the packet data from the ByteBuf stream. Complex data sets may need specific data handlers
	 * @param ctx channel context
	 * @param buffer the buffer to decode from
	 * @see net.minecraftforge.fml.common.network.ByteBufUtils
	 */
	public abstract void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer);

	/**
	 * Handle a packet on the client side. Note this occurs after decoding has completed.
	 * @param player the player reference
	 */
	public void handleClientSide(EntityPlayer player) {
		throw new UnsupportedOperationException("Unsupported operation for Packet: " + getClass().getSimpleName());
	}

	/**
	 * Handle a packet on the server side. Note this occurs after decoding has completed.
	 * @param player the player reference
	 */
	public void handleServerSide(EntityPlayer player) {
		throw new UnsupportedOperationException("Unsupported operation for Packet: " + getClass().getSimpleName());
	}
}
