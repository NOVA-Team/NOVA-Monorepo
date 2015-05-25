package nova.wrapper.mc1710.forward.entity;

import net.minecraft.entity.Entity;
import nova.internal.dummy.Wrapper;

/**
 * @author Calclavia
 */
public class MCEntityWrapper extends Wrapper {

	public final net.minecraft.entity.Entity wrapper;

	public MCEntityWrapper(Entity wrapper) {
		this.wrapper = wrapper;
	}
}
