package nova.wrapper.mc1710.network.netty;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import nova.wrapper.mc1710.launcher.NovaMinecraft;
import nova.wrapper.mc1710.network.discriminator.PacketAbstract;

/**
 * @author tgame14
 * @since 31/05/14
 */
@ChannelHandler.Sharable
public class PacketHandler extends SimpleChannelInboundHandler<PacketAbstract> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, PacketAbstract packet) throws Exception {
		INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();

		switch (FMLCommonHandler.instance().getEffectiveSide()) {
			case CLIENT:
				packet.handleClientSide(NovaMinecraft.proxy.getClientPlayer());
				break;
			case SERVER:
				packet.handleServerSide(((NetHandlerPlayServer) netHandler).playerEntity);
				break;
			default:
				break;
		}

	}

}
