package nova.wrapper.mc1710.wrapper.entity.forward;

import net.minecraft.entity.Entity;
import nova.core.util.UniqueIdentifiable;
import nova.internal.dummy.Wrapper;

/**
 * @author Calclavia
 */
public class MCEntityWrapper extends Wrapper implements UniqueIdentifiable {

	public final net.minecraft.entity.Entity wrapper;

	public MCEntityWrapper(Entity wrapper) {
		this.wrapper = wrapper;
	}

	@Override
	public String getUniqueID() {
		return wrapper.getEntityId() + "";
	}
}
