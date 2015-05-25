package nova.wrapper.mc1710.backward;

import net.minecraft.entity.player.EntityPlayer;
import nova.core.entity.Entity;
import nova.wrapper.mc1710.backward.entity.BWEntity;
import nova.wrapper.mc1710.backward.entity.BWEntityPlayer;

public class BackwardProxyUtil {
	private BackwardProxyUtil() {

	}

	public static Entity getEntityWrapper(net.minecraft.entity.Entity entity) {
		if (entity instanceof EntityPlayer) {
			return new BWEntityPlayer((EntityPlayer) entity);
		} else {
			return new BWEntity(entity);
		}
	}
}
