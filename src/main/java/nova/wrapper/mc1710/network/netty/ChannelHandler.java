package nova.wrapper.mc1710.network.netty;

import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import nova.wrapper.mc1710.network.discriminator.PacketAbstract;
import nova.wrapper.mc1710.network.discriminator.PacketBlock;
import nova.wrapper.mc1710.network.discriminator.PacketEntity;
import nova.wrapper.mc1710.network.discriminator.PacketPlayerItem;

/**
 * Handles the channel and discriminators.
 * @author Calclavia
 */
public class ChannelHandler extends FMLIndexedMessageToMessageCodec<PacketAbstract> {
	public ChannelHandler() {
		addDiscriminator(0, PacketBlock.class);
		addDiscriminator(1, PacketEntity.class);
		addDiscriminator(3, PacketPlayerItem.class);
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
