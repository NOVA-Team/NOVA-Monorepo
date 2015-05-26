package nova.wrapper.mc1710.wrapper.block.world;

import nova.core.nativewrapper.NativeConverter;
import nova.core.util.exception.NovaException;
import nova.core.world.World;

/**
 * @author Calclavia
 */
public class WorldConverter implements NativeConverter<World, net.minecraft.world.World> {
	@Override
	public Class<World> getNovaSide() {
		return World.class;
	}

	@Override
	public Class<net.minecraft.world.World> getNativeSide() {
		return net.minecraft.world.World.class;
	}

	@Override
	public World toNova(net.minecraft.world.World nativeObj) {
		return new BWWorld(nativeObj);
	}

	@Override
	public net.minecraft.world.World toNative(World novaObj) {
		if (novaObj instanceof BWWorld) {
			return ((BWWorld) novaObj).world();
		}

		throw new NovaException("Attempt to convert a world that is not a BWWorld!");
	}
}
