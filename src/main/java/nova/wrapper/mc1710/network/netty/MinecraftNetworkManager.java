package nova.wrapper.mc1710.network.netty;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import nova.core.block.Block;
import nova.core.network.NetworkManager;
import nova.core.network.PacketSender;
import nova.core.util.transform.Vector3d;
import nova.core.util.transform.Vector3i;
import nova.wrapper.mc1710.network.PacketWrapper;
import nova.wrapper.mc1710.network.discriminator.PacketAbstract;
import nova.wrapper.mc1710.network.discriminator.PacketBlock;

import java.util.EnumMap;

/**
 * @author Calclavia
 * @since 26/05/14
 */
public class MinecraftNetworkManager extends NetworkManager {
	public final String channel;
	protected final EnumMap<Side, FMLEmbeddedChannel> channelEnumMap;

	public MinecraftNetworkManager(String channel, EnumMap<Side, FMLEmbeddedChannel> channelEnumMap) {
		this.channel = channel;
		this.channelEnumMap = channelEnumMap;
	}

	public Packet toMCPacket(PacketAbstract packet) {
		return channelEnumMap.get(FMLCommonHandler.instance().getEffectiveSide()).generatePacketFrom(packet);
	}

	/**
	 * Syncs a block's packet data.
	 */
	@Override
	protected void syncBlock(int id, PacketSender sender) {
		sendToAll(getBlockPacket(id, sender));
	}

	public PacketBlock getBlockPacket(int id, PacketSender sender) {
		Vector3i position = ((Block) sender).getPosition();
		PacketBlock discriminator = new PacketBlock(position.xi(), position.yi(), position.zi());
		PacketWrapper wrappedPacket = new PacketWrapper(discriminator.data);
		sender.write(id, wrappedPacket);
		return discriminator;
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
	 *
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


