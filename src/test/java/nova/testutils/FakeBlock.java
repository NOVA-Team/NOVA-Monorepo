package nova.testutils;

import nova.core.block.Block;
import nova.core.component.ComponentManager;
import nova.core.component.misc.Collider;
import nova.core.util.Buildable.ID;

import java.util.Optional;

@ID("solid")
@ID("air")
public class FakeBlock extends Block {

	final ComponentManager cm;
	FakeBlock(ComponentManager cm) {

		this.cm = cm;
	}
	@Override
	public void afterConstruction(Optional<Object[]> typeArguments, Optional<Object[]> instanceArguments) {
		if ("solid".equals(getID())) {
			add(Collider.class);
		}
	}

}
