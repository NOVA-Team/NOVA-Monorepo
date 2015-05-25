package nova.wrapper.mc1710.forward.block;

import nova.core.block.Block;
import nova.core.component.transform.BlockTransform;
import nova.core.util.transform.vector.Vector3i;
import nova.core.world.World;

/**
 * @author Calclavia
 */
public class MCBlockTransform extends BlockTransform {
	public final Block provider;
	public final MCBlockWrapper wrapper;

	public MCBlockTransform(Block provider) {
		this.provider = provider;
		this.wrapper = provider.get(MCBlockWrapper.class);
	}

	@Override
	public Vector3i position() {
		return wrapper.position;
	}

	@Override
	public World world() {
		return wrapper.world;
	}
}
