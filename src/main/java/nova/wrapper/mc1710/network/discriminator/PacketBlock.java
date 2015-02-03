package nova.wrapper.mc1710.network.discriminator;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import nova.core.block.Block;
import nova.core.network.PacketReceiver;
import nova.core.util.transform.Vector3d;
import nova.core.util.transform.Vector3i;
import nova.wrapper.mc1710.forward.block.BlockWrapper;
import nova.wrapper.mc1710.forward.block.TileWrapper;
import nova.wrapper.mc1710.network.PacketWrapper;

/**
 * Packet type designed to be used with Blocks
 * @author Calclavia
 */
public class PacketBlock extends PacketAbstract {
	public int x;
	public int y;
	public int z;

	public PacketBlock() {

	}

	public PacketBlock(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		buffer.writeBytes(data);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		x = buffer.readInt();
		y = buffer.readInt();
		z = buffer.readInt();
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
		sender = player;

		Block block = null;

		TileEntity tile = player.getEntityWorld().getTileEntity(x, y, z);

		if (tile instanceof TileWrapper) {
			block = ((TileWrapper) tile).getBlock();
		}

		if (block == null) {
			net.minecraft.block.Block wrappedBlock = player.getEntityWorld().getBlock(x, y, z);

			if (wrappedBlock instanceof BlockWrapper) {
				block = ((BlockWrapper) wrappedBlock).getBlockInstance(player.getEntityWorld(), new Vector3i(x, y, z));
			}
		}

		if (block instanceof PacketReceiver) {
			try {
				PacketReceiver receiver = (PacketReceiver) block;
				receiver.read(new PacketWrapper(data.slice()));
			} catch (IndexOutOfBoundsException e) {
				System.out.println("Packet sent to a block and received IndexOutOfBoundsException: [" + tile + "] in " + new Vector3d(x, y, z));
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println("Packet sent to a TileEntity failed to be received [" + tile + "] in " + new Vector3d(x, y, z));
				e.printStackTrace();
			}
		} else {
			System.out.println("Packet was sent to a block not implementing IPacketReceiver, this is a coding error [" + tile + "] in " + new Vector3d(x, y, z));
		}
	}
}
