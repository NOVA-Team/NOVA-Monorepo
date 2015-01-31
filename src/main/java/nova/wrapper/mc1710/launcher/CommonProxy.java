package nova.wrapper.mc1710.launcher;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourcePack;
import nova.core.loader.Loadable;
import nova.wrapper.mc1710.forward.block.BlockWrapper;
import nova.wrapper.mc1710.forward.item.ItemWrapper;
import nova.wrapper.mc1710.util.NovaResourcePack;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
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
}
