package nova.sample;

import nova.core.block.Block;
import nova.core.entity.Entity;
import nova.core.render.Texture;
import nova.core.util.Direction;
import nova.core.util.transform.Vector3d;

import java.util.Optional;

/**
 * Literarlly, this is a test block.
 * @author Calclavia
 */
public class BlockTest extends Block {

	@Override
	public boolean onRightClick(Entity entity, int side, Vector3d hit) {
		System.out.println("I'm being right clicked!");
		return true;
	}

	@Override
	public Optional<Texture> getTexture(Direction side) {
		return Optional.of(NovaTest.steelTexture);
	}

	@Override
	public String getID() {
		return "test";
	}
}
