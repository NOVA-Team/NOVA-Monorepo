package nova.wrapper.mc1710.network.discriminator;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import nova.core.game.Game;
import nova.core.network.handler.PacketType;
import nova.wrapper.mc1710.network.MCPacket;
import nova.wrapper.mc1710.network.netty.MCNetworkManager;

/**
 * NOVA Packet Structure:
 *
 * 1. Packet Type ID
 * 2. Packet Sub ID
 * 3. Data
 *
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
		MCNetworkManager network = (MCNetworkManager) Game.network();
		PacketType<?> packetType = network.getPacketType(data.readInt());
		MCPacket packet = new MCPacket(data);
		//Set the ID of the packet
		packet.setID(packet.readInt());
		packetType.read(packet);
	}
}
