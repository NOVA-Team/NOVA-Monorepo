package nova.sample;

import nova.core.block.Block;
import nova.core.block.BlockAccess;
import nova.core.entity.Entity;
import nova.core.util.transform.Vector3d;
import nova.core.util.transform.Vector3i;

/**
 * Literarlly, this is a test block.
 * @author Calclavia
 */
public class BlockTest extends Block {
	public BlockTest(BlockAccess access, Vector3i position) {
		super(access, position);
	}

	@Override
	public boolean onRightClick(Entity entity, int side, Vector3d hit) {
		System.out.println("I'm being right clicked!");
		return true;
	}

	@Override
	public String getID() {
		return "test";
	}
}
