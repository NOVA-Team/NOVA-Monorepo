package nova.wrapper.mc1710.backward.block;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import nova.core.block.Block;
import nova.core.block.component.LightEmitter;
import nova.core.retention.Data;
import nova.core.retention.Storable;
import nova.core.retention.Stored;
import nova.wrapper.mc1710.backward.world.BWWorld;
import nova.wrapper.mc1710.util.DataUtility;

public class BWBlock extends Block implements Storable {
	private final net.minecraft.block.Block mcBlock;
	@Stored
	public int metadata;
	private TileEntity mcTileEntity;

	public BWBlock(net.minecraft.block.Block block) {
		this.mcBlock = block;

		add(
			new LightEmitter()
			.setEmittedLevel(() -> mcBlock.getLightValue(getMcBlockAccess(), x(), y(), z()) / 15.0F)
		);
	}

	private IBlockAccess getMcBlockAccess() {
		return ((BWWorld) transform.world()).access;
	}

	private int getMetadata() {
		return getMcBlockAccess().getBlockMetadata(x(), y(), z());
	}

	private TileEntity getTileEntity() {
		if (mcTileEntity == null && mcBlock.hasTileEntity(getMetadata())) {
			mcTileEntity = getMcBlockAccess().getTileEntity(x(), y(), z());
		}
		return mcTileEntity;
	}

	@Override
	public String getID() {
		return net.minecraft.block.Block.blockRegistry.getNameForObject(mcBlock);
	}

	@Override
	public void save(Data data) {
		Storable.super.save(data);

		TileEntity tileEntity = getTileEntity();
		if (tileEntity != null) {
			NBTTagCompound nbt = new NBTTagCompound();
			tileEntity.writeToNBT(nbt);
			data.putAll(DataUtility.nbtToData(nbt));
		}
	}

	@Override
	public void load(Data data) {
		Storable.super.load(data);

		TileEntity tileEntity = getTileEntity();
		if (tileEntity != null) {
			tileEntity.writeToNBT(DataUtility.dataToNBT(data));
		}
	}
}
