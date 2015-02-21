package nova.wrapper.mc1710.launcher;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import nova.core.entity.Entity;
import nova.core.entity.EntityFactory;
import nova.core.loader.Loadable;
import nova.wrapper.mc1710.forward.block.FWBlock;
import nova.wrapper.mc1710.forward.block.FWTile;
import nova.wrapper.mc1710.item.FWItem;

import java.util.Set;

/**
 * @author Calclavia
 */
public class CommonProxy implements Loadable {
	@Override
	public void preInit() {
		GameRegistry.registerTileEntity(FWTile.class, "novaTile");
	}

	public void registerResourcePacks(Set<Class<?>> modClasses) {

	}

	public void registerItem(FWItem item) {

	}

	public void registerBlock(FWBlock block) {

	}

	public Entity spawnParticle(net.minecraft.world.World world, EntityFactory factory) {
		return null;
	}

	public boolean isPaused() {
		return false;
	}

	public EntityPlayer getClientPlayer() {
		return null;
	}
}
