package nova.wrapper.mc1710.backward.entity;

import nova.core.entity.Entity;
import nova.wrapper.mc1710.forward.entity.MCEntityTransform;
import nova.wrapper.mc1710.forward.entity.MCEntityWrapper;

/**
 * A Minecraft to NOVA Entity wrapper
 * @author Calclavia
 */
//TODO: Incomplete. Add more components!
public class BWEntity extends Entity {

	public net.minecraft.entity.Entity entity;

	public BWEntity(net.minecraft.entity.Entity entity) {
		this.entity = entity;
		add(new MCEntityWrapper(entity));
		add(new MCEntityTransform(this));
	}

	@Override
	public String getID() {
		return entity.getClass().getName();
	}
}
