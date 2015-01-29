package nova.wrapper.mc1710.backward.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import nova.core.block.Block;
import nova.core.block.BlockAccess;
import nova.core.block.components.LightEmitter;
import nova.core.util.transform.Vector3i;
import nova.core.world.World;
import nova.wrapper.mc1710.backward.world.BWBlockAccess;

public class BWBlock extends Block implements LightEmitter {
	private final net.minecraft.block.Block mcBlock;
	private TileEntity mcTileEntity;

	public BWBlock(BlockAccess world, Vector3i position, net.minecraft.block.Block block) {
		super(world, position);
		this.mcBlock = block;
	}

	private IBlockAccess getMcBlockAccess() {
		return ((BWBlockAccess) getBlockAccess()).access;
	}

	private int getMetadata() {
		return getMcBlockAccess().getBlockMetadata(getX(), getY(), getZ());
	}

	private TileEntity getTileEntity() {
		if (mcTileEntity == null && mcBlock.hasTileEntity(getMetadata())) {
			mcTileEntity = getMcBlockAccess().getTileEntity(getX(), getY(), getZ());
		}
		return mcTileEntity;
	}

	@Override
	public String getID() {
		return net.minecraft.block.Block.blockRegistry.getNameForObject(mcBlock);
	}

	@Override
	public float getEmittedLightLevel() {
		return mcBlock.getLightValue(getMcBlockAccess(), getX(), getY(), getZ()) / 15.0F;
	}
}
