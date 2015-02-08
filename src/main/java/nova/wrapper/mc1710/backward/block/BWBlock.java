package nova.wrapper.mc1710.backward.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import nova.core.block.Block;
import nova.core.block.components.LightEmitter;
import nova.wrapper.mc1710.backward.world.BWBlockAccess;

public class BWBlock extends Block implements LightEmitter {
	private final net.minecraft.block.Block mcBlock;
	private TileEntity mcTileEntity;

	public BWBlock(net.minecraft.block.Block block) {
		this.mcBlock = block;
	}

	private IBlockAccess getMcBlockAccess() {
		return ((BWBlockAccess) blockAccess()).access;
	}

	private int getMetadata() {
		return getMcBlockAccess().getBlockMetadata(position().x, position().y, position().z);
	}

	private TileEntity getTileEntity() {
		if (mcTileEntity == null && mcBlock.hasTileEntity(getMetadata())) {
			mcTileEntity = getMcBlockAccess().getTileEntity(position().x, position().y, position().z);
		}
		return mcTileEntity;
	}

	@Override
	public String getID() {
		return net.minecraft.block.Block.blockRegistry.getNameForObject(mcBlock);
	}

	@Override
	public float getEmittedLightLevel() {
		return mcBlock.getLightValue(getMcBlockAccess(), position().x, position().y, position().z) / 15.0F;
	}
}
