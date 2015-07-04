package nova.wrapper.mc18.wrapper.block.forward;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import nova.core.block.Block;
import nova.core.block.Stateful;
import nova.core.network.Syncable;
import nova.core.retention.Data;
import nova.core.retention.Storable;
import nova.internal.core.Game;
import nova.wrapper.mc18.network.netty.MCNetworkManager;
import nova.wrapper.mc18.wrapper.block.world.BWWorld;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * A Minecraft TileEntity to Nova block wrapper
 * @author Calclavia
 */
public class FWTile extends TileEntity {

	protected String blockID;
	protected Block block;
	protected Data cacheData = null;

	public FWTile() {

	}

	public FWTile(String blockID) {
		this.blockID = blockID;
	}

	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	@Override
	public Packet getDescriptionPacket() {
		if (block instanceof Syncable) {
			return ((MCNetworkManager) Game.network()).toMCPacket(((MCNetworkManager) Game.network()).writePacket(0, (Syncable) block));
		}
		return null;
	}

	@Override
	public void validate() {
		super.validate();
		block.getOrAdd(new MCBlockTransform(block, Game.natives().toNova(getWorld()), new Vector3D(pos.getX(), pos.getY(), pos.getZ())));

		if (cacheData != null && block instanceof Storable) {
			((Storable) block).load(cacheData);
			cacheData = null;
		}

		block.events.publish(new Stateful.LoadEvent());
	}

	@Override
	public void invalidate() {
		block.events.publish(new Stateful.UnloadEvent());
		super.invalidate();
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		nbt.setString("novaID", blockID);

		if (block != null) {
			if (block instanceof Storable) {
				Data data = new Data();
				((Storable) block).save(data);
				nbt.setTag("nova", Game.natives().toNative(data));
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		blockID = nbt.getString("novaID");
		cacheData = Game.natives().toNova(nbt.getCompoundTag("nova"));
	}
}
