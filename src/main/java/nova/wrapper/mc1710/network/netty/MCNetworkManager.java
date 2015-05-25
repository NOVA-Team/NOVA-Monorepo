package nova.wrapper.mc1710.network.netty;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import nova.core.block.Block;
import nova.core.entity.Entity;
import nova.core.entity.component.Player;
import nova.core.network.NetworkManager;
import nova.core.network.PacketHandler;
import nova.core.util.exception.NovaException;
import nova.core.util.transform.vector.Vector3d;
import nova.core.util.transform.vector.Vector3i;
import nova.wrapper.mc1710.backward.entity.BWEntityPlayer;
import nova.wrapper.mc1710.forward.entity.MCEntityWrapper;
import nova.wrapper.mc1710.launcher.NovaMinecraft;
import nova.wrapper.mc1710.network.MCPacket;
import nova.wrapper.mc1710.network.discriminator.PacketAbstract;
import nova.wrapper.mc1710.network.discriminator.PacketBlock;
import nova.wrapper.mc1710.network.discriminator.PacketEntity;

import java.util.EnumMap;

/**
 * The implementation of NetworkManager that will be injected.
 * @author Calclavia
 * @since 26/05/14
 */
public class MCNetworkManager extends NetworkManager {
	public final String channel = NovaMinecraft.id;
	public final EnumMap<Side, FMLEmbeddedChannel> channelEnumMap = NetworkRegistry.INSTANCE.newChannel(channel, new ChannelHandler(), new MCPacketHandler());

	public Packet toMCPacket(PacketAbstract packet) {
		return channelEnumMap.get(FMLCommonHandler.instance().getEffectiveSide()).generatePacketFrom(packet);
	}

	@Override
	public nova.core.network.Packet newPacket() {
		return new MCPacket(Unpooled.buffer());
	}

	@Override
	public void sendPacket(PacketHandler sender, nova.core.network.Packet packet) {
		PacketAbstract discriminator;

		if (sender instanceof Block) {
			Vector3i position = ((Block) sender).position();
			discriminator = new PacketBlock(position.xi(), position.yi(), position.zi());
		} else if (sender instanceof Entity) {
			Entity entity = (Entity) sender;
			discriminator = new PacketEntity(entity.get(MCEntityWrapper.class).get().wrapper);

		} else {
			throw new NovaException("Fail to send packet as the PacketHandler is of invalid type.");
		}
		//Write packet ID
		discriminator.data.writeInt(packet.getID());
		discriminator.data.writeBytes(((MCPacket) packet).buf);

		if (isServer()) {
			sendToAll(discriminator);
		} else {
			sendToServer(discriminator);
		}
	}

	@Override
	public void sync(int id, PacketHandler sender) {
		nova.core.network.Packet packet = newPacket();
		packet.setID(id);
		sender.write(packet);
		sendPacket(sender, packet);
	}

	public PacketBlock getBlockPacket(int id, PacketHandler sender) {
		Vector3i position = ((Block) sender).position();
		PacketBlock discriminator = new PacketBlock(position.xi(), position.yi(), position.zi());
		MCPacket mcPacket = new MCPacket(discriminator.data);
		mcPacket.setID(id);
		discriminator.data.writeInt(id);
		sender.write(mcPacket);
		return discriminator;
	}

	@Override
	public void sendChat(Player player, String message) {
		if (player instanceof BWEntityPlayer.MCPlayer) {
			((BWEntityPlayer.MCPlayer) player).entity.addChatMessage(new ChatComponentText(message));
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
	 * @param dimId the dimension id to send to.
	 */
	public void sendToAllInDimension(PacketAbstract packet, int dimId) {
		this.channelEnumMap.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
		this.channelEnumMap.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimId);
		this.channelEnumMap.get(Side.SERVER).writeAndFlush(packet);
	}

	public void sendToAllInDimension(PacketAbstract packet, World world) {
		sendToAllInDimension(packet, world.provider.dimensionId);
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

	public void sendToAllAround(PacketAbstract message, World world, Vector3d point, double range) {
		sendToAllAround(message, world, point.xd(), point.yd(), point.zd(), range);
	}

	public void sendToAllAround(PacketAbstract message, TileEntity tile) {
		sendToAllAround(message, tile, 64);
	}

	public void sendToAllAround(PacketAbstract message, TileEntity tile, double range) {
		sendToAllAround(message, tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord, range);
	}

	public void sendToAllAround(PacketAbstract message, World world, double x, double y, double z, double range) {
		sendToAllAround(message, new NetworkRegistry.TargetPoint(world.provider.dimensionId, x, y, z, range));
	}

	@SideOnly(Side.CLIENT)
	public void sendToServer(PacketAbstract packet) {
		this.channelEnumMap.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
		this.channelEnumMap.get(Side.CLIENT).writeAndFlush(packet);
	}
}


