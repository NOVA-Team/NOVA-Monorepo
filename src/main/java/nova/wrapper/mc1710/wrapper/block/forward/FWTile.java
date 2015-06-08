package nova.wrapper.mc1710.wrapper.block.forward;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import nova.core.block.Block;
import nova.core.block.Stateful;
import nova.core.component.Updater;
import nova.internal.core.Game;
import nova.core.network.Syncable;
import nova.core.retention.Data;
import nova.core.retention.Storable;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import nova.wrapper.mc1710.network.netty.MCNetworkManager;
import nova.wrapper.mc1710.wrapper.block.world.BWWorld;

/**
 * A Minecraft TileEntity to Nova block wrapper
 * @author Calclavia
 */
public class FWTile extends TileEntity {

	private String blockID;
	private Block block;
	private Data cacheData = null;

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

		block.add(new MCBlockTransform(block, new BWWorld(getWorldObj()), new Vector3D(xCoord, yCoord, zCoord)));

		if (cacheData != null && block instanceof Storable) {
			((Storable) block).load(cacheData);
			cacheData = null;
		}

		block.loadEvent.publish(new Stateful.LoadEvent());
	}

	@Override
	public void invalidate() {
		block.unloadEvent.publish(new Stateful.UnloadEvent());
		super.invalidate();
	}

	/**
	 * Updates the block.
	 */
	@Override
	public void updateEntity() {
		((Updater) block).update(0.05);
	}

	/**
	 * Only register tile updates if the block is an instance of Updater.
	 * @return Whether can update
	 */
	@Override
	public boolean canUpdate() {
		return block instanceof Updater;
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

		/**
		 * Because World and Position do not exist during NBT read time, we must
		 * wait until the block is injected with World and Position data using
		 * Future.
		 */
		blockID = nbt.getString("novaID");
		cacheData = Game.natives().toNova(nbt.getCompoundTag("nova"));
	}
}
