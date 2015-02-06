package nova.wrapper.mc1710.launcher;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import nova.core.loader.Loadable;
import nova.wrapper.mc1710.forward.block.BlockWrapper;
import nova.wrapper.mc1710.forward.block.TileWrapper;
import nova.wrapper.mc1710.item.ItemWrapper;

import java.util.Set;

/**
 * @author Calclavia
 */
public class CommonProxy implements Loadable {
	@Override
	public void preInit() {
		GameRegistry.registerTileEntity(TileWrapper.class, "novaTile");
	}

	public void registerResourcePacks(Set<Class<?>> modClasses) {

	}

	public void registerItem(ItemWrapper item) {

	}

	public void registerBlock(BlockWrapper block) {

	}

	public boolean isPaused() {
		return false;
	}

	public EntityPlayer getClientPlayer() {
		return null;
	}
}
