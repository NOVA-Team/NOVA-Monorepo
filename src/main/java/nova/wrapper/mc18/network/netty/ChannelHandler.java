package nova.wrapper.mc18.network.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraftforge.fml.common.network.FMLIndexedMessageToMessageCodec;
import nova.wrapper.mc18.network.discriminator.NovaPacket;
import nova.wrapper.mc18.network.discriminator.PacketAbstract;
import nova.wrapper.mc18.network.discriminator.PacketGui;
import nova.wrapper.mc18.network.discriminator.PacketPlayerItem;

/**
 * Handles the channel and discriminators.
 * @author Calclavia
 */
public class ChannelHandler extends FMLIndexedMessageToMessageCodec<PacketAbstract> {
	public ChannelHandler() {
		addDiscriminator(0, NovaPacket.class);
		addDiscriminator(1, PacketPlayerItem.class);
		addDiscriminator(2, PacketGui.class);
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
