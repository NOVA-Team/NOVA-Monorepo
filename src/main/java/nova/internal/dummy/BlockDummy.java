package nova.internal.dummy;

import nova.core.block.Block;

/**
 * A multi-purpose dummy block.
 *
 * @author Calclavia
 */
public class BlockDummy extends Block {
	public BlockDummy() {
		super();
	}

	@Override
	public String getID() {
		return "dummy";
	}
}