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

package nova.core.wrapper.mc.forge.v1_11_2.network.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraftforge.fml.common.network.FMLIndexedMessageToMessageCodec;
import nova.core.wrapper.mc.forge.v1_11_2.network.discriminator.NovaPacket;
import nova.core.wrapper.mc.forge.v1_11_2.network.discriminator.PacketAbstract;
import nova.core.wrapper.mc.forge.v1_11_2.network.discriminator.PacketPlayerItem;

/**
 * Handles the channel and discriminators.
 * @author Calclavia
 */
public class ChannelHandler extends FMLIndexedMessageToMessageCodec<PacketAbstract> {
	public ChannelHandler() {
		addDiscriminator(0, NovaPacket.class);
		addDiscriminator(1, PacketPlayerItem.class);
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, PacketAbstract packet, ByteBuf target) throws Exception {
		packet.encodeInto(ctx, target);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf source, PacketAbstract packet) {
		packet.decodeInto(ctx, source);
	}
}
