package nova.testutils;

import nova.core.block.Block;

/**
 * @author Calclavia
 */
public class FakeBlock extends Block {

	private final String id;

	public FakeBlock(String id) {
		this.id = id;
	}

	@Override
	public String getID() {
		return id;
	}
}
