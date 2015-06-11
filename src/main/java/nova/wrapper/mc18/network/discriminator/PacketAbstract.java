package nova.wrapper.mc18.network.discriminator;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

/**
 * For custom packets extend this Class and register on Mod loading phase
 * <p>
 * Without registering a NPE will be thrown as the {@link nova.wrapper.mc18.network.netty.MCNetworkManager} won't know how to handle it
 * </p>
 * To send this packet also look at {@link nova.wrapper.mc18.network.netty.MCNetworkManager#sendToAll(PacketAbstract)}
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
