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

import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nova.core.entity.component.Player;
import nova.core.network.NetworkManager;
import nova.core.network.Syncable;
import nova.core.wrapper.mc.forge.v1_11_2.launcher.NovaMinecraft;
import nova.core.wrapper.mc.forge.v1_11_2.network.MCPacket;
import nova.core.wrapper.mc.forge.v1_11_2.network.discriminator.NovaPacket;
import nova.core.wrapper.mc.forge.v1_11_2.network.discriminator.PacketAbstract;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.entity.backward.BWEntity;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.EnumMap;

/**
 * The implementation of NetworkManager that will be injected.
 * @author Calclavia
 * @since 26/05/14
 */
public class MCNetworkManager extends NetworkManager {
	public final String channel = NovaMinecraft.MOD_ID;
	public final EnumMap<Side, FMLEmbeddedChannel> channelEnumMap = NetworkRegistry.INSTANCE.newChannel(channel, new ChannelHandler(), new MCPacketHandler());

	public Packet<?> toMCPacket(PacketAbstract packet) {
		return channelEnumMap.get(FMLCommonHandler.instance().getEffectiveSide()).generatePacketFrom(packet);
	}

	@Override
	public nova.core.network.Packet newPacket() {
		return new MCPacket(Unpooled.buffer());
	}

	@Override
	public void sendPacket(nova.core.network.Packet packet) {
		//Wrap the packet in NOVA's discriminator
		PacketAbstract discriminator = new NovaPacket();
		//Write packet
		discriminator.data.writeBytes(((MCPacket) packet).buf);

		if (isServer()) {
			sendToAll(discriminator);
		} else {
			sendToServer(discriminator);
		}
	}

	public PacketAbstract writePacket(int id, Syncable sender) {
		PacketAbstract discriminator = new NovaPacket();
		nova.core.network.Packet packet = newPacket();
		packet.setID(id);

		//Write packet
		writePacket(sender, packet);
		discriminator.data.writeBytes(((MCPacket) packet).buf);
		return discriminator;
	}

	@Override
	public void sendChat(Player player, String message) {
		if (player instanceof BWEntity.MCPlayer) {
			((BWEntity.MCPlayer) player).entity.sendMessage(new TextComponentString(message));
		}
	}

	@Override
	public boolean isServer() {
		return FMLCommonHandler.instance().getEffectiveSide().isServer();
	}

	/**
	 * @param packet the packet to send to the player
	 * @param player the player MP object
	 */
	public void sendToPlayer(PacketAbstract packet, EntityPlayerMP player) {
		this.channelEnumMap.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
		this.channelEnumMap.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
		this.channelEnumMap.get(Side.SERVER).writeAndFlush(packet);
	}

	/**
	 * @param packet the packet to send to the players in the dimension
	 * @param dimId the dimension MOD_ID to send to.
	 */
	public void sendToAllInDimension(PacketAbstract packet, int dimId) {
		this.channelEnumMap.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
		this.channelEnumMap.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimId);
		this.channelEnumMap.get(Side.SERVER).writeAndFlush(packet);
	}

	public void sendToAllInDimension(PacketAbstract packet, World world) {
		sendToAllInDimension(packet, world.provider.getDimension());
	}

	/**
	 * sends to all clients connected to the server
	 * @param packet the packet to send.
	 */
	public void sendToAll(PacketAbstract packet) {
		this.channelEnumMap.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
		this.channelEnumMap.get(Side.SERVER).writeAndFlush(packet);
	}

	public void sendToAllAround(PacketAbstract message, NetworkRegistry.TargetPoint point) {
		this.channelEnumMap.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
		this.channelEnumMap.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
		this.channelEnumMap.get(Side.SERVER).writeAndFlush(message);
	}

	public void sendToAllAround(PacketAbstract message, World world, Vector3D point, double range) {
		sendToAllAround(message, world, point.getX(), point.getY(), point.getZ(), range);
	}

	public void sendToAllAround(PacketAbstract message, TileEntity tile) {
		sendToAllAround(message, tile, 64);
	}

	public void sendToAllAround(PacketAbstract message, TileEntity tile, double range) {
		sendToAllAround(message, tile.getWorld(), tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), range);
	}

	public void sendToAllAround(PacketAbstract message, World world, double x, double y, double z, double range) {
		sendToAllAround(message, new NetworkRegistry.TargetPoint(world.provider.getDimension(), x, y, z, range));
	}

	@SideOnly(Side.CLIENT)
	public void sendToServer(PacketAbstract packet) {
		this.channelEnumMap.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
		this.channelEnumMap.get(Side.CLIENT).writeAndFlush(packet);
	}
}


