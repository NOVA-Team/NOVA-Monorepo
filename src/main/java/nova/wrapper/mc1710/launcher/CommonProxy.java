package nova.wrapper.mc1710.launcher;

import net.minecraft.entity.player.EntityPlayer;
import nova.core.loader.Loadable;
import nova.wrapper.mc1710.forward.block.BlockWrapper;
import nova.wrapper.mc1710.forward.item.ItemWrapper;

import java.util.Set;

/**
 * @author Calclavia
 */
public class CommonProxy implements Loadable {
	public void registerResourcePacks(Set<Class<?>> modClasses) {

	}

	public void registerItem(ItemWrapper item) {

	}

	public void registerBlock(BlockWrapper block) {

	}

	public EntityPlayer getClientPlayer() {
		return null;
	}
}
