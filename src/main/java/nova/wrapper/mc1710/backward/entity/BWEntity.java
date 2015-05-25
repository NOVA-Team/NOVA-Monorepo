package nova.wrapper.mc1710.backward.entity;

import nova.core.entity.Entity;

/**
 * A Minecraft to NOVA Entity wrapper
 * @author Calclavia
 */
//TODO: Incomplete. All methods should be fully implemented.
public class BWEntity extends Entity {

	public net.minecraft.entity.Entity entity;

	public BWEntity(net.minecraft.entity.Entity entity) {
		this.entity = entity;
	}

	@Override
	public String getID() {
		return entity.getClass().getName();
	}
}
