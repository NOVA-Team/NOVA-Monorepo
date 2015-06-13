package nova.testutils;

import nova.core.block.Block;
import nova.core.component.misc.Collider;
import nova.core.util.Buildable.ID;


@ID("solid")
@ID("air")
public class FakeBlock extends Block {

	final Collider collider;
	FakeBlock(Collider collider) {
		this.collider = collider;
	}

	public void afterConstruction(Object[] typeArguments, Object[] instanceArguments) {
		if ("solid".equals(getID())) {
			add(collider);
		}
	}

}
