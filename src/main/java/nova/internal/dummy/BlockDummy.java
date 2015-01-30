package nova.internal.dummy;

import nova.core.block.Block;
import nova.core.block.BlockAccess;
import nova.core.util.transform.Vector3i;

/**
 * A multi-purpose dummy block.
 * @author Calclavia
 */
public class BlockDummy extends Block {
	public BlockDummy(BlockAccess blockAccess, Vector3i position) {
		super(blockAccess, position);
	}

	@Override
	public String getID() {
		return "dummy";
	}
}