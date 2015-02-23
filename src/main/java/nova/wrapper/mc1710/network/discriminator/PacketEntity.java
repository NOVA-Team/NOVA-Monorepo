package nova.wrapper.mc1710.network.discriminator;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import nova.core.network.PacketHandler;
import nova.wrapper.mc1710.network.MCPacket;

/**
 * @author tgame14
 * @since 26/05/14
 */
public class PacketEntity extends PacketAbstract {
	protected int entityId;

	public PacketEntity(Entity entity) {
		entityId = entity.getEntityId();
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeInt(entityId);
		buffer.writeBytes(data);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		entityId = buffer.readInt();
		data = buffer.slice();
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		Entity entity = player.getEntityWorld().getEntityByID(entityId);

		if (entity instanceof PacketHandler) {
			((PacketHandler) entity).read(data.readInt(), new MCPacket(data));
		}
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		Entity entity = player.getEntityWorld().getEntityByID(entityId);

		if (entity instanceof PacketHandler) {
			((PacketHandler) entity).read(data.readInt(), new MCPacket(data));
		}
	}
}
