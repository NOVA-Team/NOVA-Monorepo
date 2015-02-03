package nova.wrapper.mc1710.forward.block;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import nova.core.block.Block;
import nova.core.block.BlockFactory;
import nova.core.block.components.Stateful;
import nova.core.util.components.Storable;
import nova.core.util.components.Updater;
import nova.core.util.transform.Vector3i;
import nova.wrapper.mc1710.backward.world.BWWorld;
import nova.wrapper.mc1710.util.NBTUtility;

import java.util.HashMap;
import java.util.Map;

/**
 * A Minecraft TileEntity to Nova block wrapper
 *
 * @author Calclavia
 */
public class TileWrapper extends TileEntity {
	private Block block;

	public TileWrapper() {

	}

	public Block getBlock() {
		return block;
	}

	public void setBlock(BlockFactory factory) {
		this.block = factory.makeBlock(new BWWorld(getWorldObj()), new Vector3i(xCoord, yCoord, zCoord));
	}

	@Override
	public void validate() {
		super.validate();/*
		block = ((BlockWrapper) worldObj.getChunkFromBlockCoords(xCoord, zCoord).getBlock(xCoord, yCoord, zCoord)).getBlockInstance(new BWWorld(worldObj), new Vector3i(xCoord, yCoord, zCoord));
		if (block instanceof Stateful)
		{
			// TODO: Initialize, by spec, is only called the first time a block is placed.
			// Perhaps the spec should be changed?
			((Stateful) block).initialize();
			((Stateful) block).load();
		}*/
	}

	@Override
	public void invalidate() {
		super.invalidate();
		if (block instanceof Stateful) {
			((Stateful) block).unload();
		}
	}

	@Override
	public boolean isInvalid() {
		if (block instanceof Stateful && !((Stateful) block).isValid()) {
			return true;
		}
		return super.isInvalid();
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
	 *
	 * @return Whether can update
	 */
	@Override
	public boolean canUpdate() {
		return block instanceof Updater;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		if (block instanceof Storable) {
			Map<String, Object> data = new HashMap<>();
			((Storable) block).save(data);
			nbt.setTag("nova", NBTUtility.mapToNBT(data));
		}

		if (block != null) {
			nbt.setString("novaID", block.getID());
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		if (block instanceof Storable) {
			((Storable) block).load(NBTUtility.nbtToMap(nbt.getCompoundTag("nova")));
		}
		//TODO: Retrieving block doesn't seem to really work.
		/*
		Optional<BlockFactory> factory = Game.instance.get().blockManager.getBlockFactory(nbt.getString("novaID"));
		if(factory.isPresent()) {
			block = factory.get().makeBlock()
		}*/
	}
}
