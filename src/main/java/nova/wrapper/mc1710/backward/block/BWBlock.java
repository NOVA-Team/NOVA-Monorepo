package nova.wrapper.mc1710.backward.block;

import nova.core.block.Block;
import nova.core.block.BlockAccess;
import nova.core.util.transform.Vector3i;

public class BWBlock extends Block {
	private final net.minecraft.block.Block mcBlock;

	public BWBlock(BlockAccess world, Vector3i position, net.minecraft.block.Block block) {
		super(world, position);
		this.mcBlock = block;
	}

	@Override
	public String getID() {
		return net.minecraft.block.Block.blockRegistry.getNameForObject(mcBlock);
	}
}
