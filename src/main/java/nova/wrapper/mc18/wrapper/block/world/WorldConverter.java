package nova.wrapper.mc18.wrapper.block.world;

import nova.core.nativewrapper.NativeConverter;
import nova.core.world.World;
import nova.internal.core.Game;

import java.util.Optional;

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
		Optional<World> opWorld = Game.worlds().findWorld(nativeObj.provider.getDimensionName());
		if (opWorld.isPresent()) {
			return opWorld.get();
		}

		return new BWWorld(nativeObj);
	}

	@Override
	public net.minecraft.world.World toNative(World novaObj) {
		if (novaObj instanceof BWWorld) {
			return ((BWWorld) novaObj).world();
		}

		//TODO: Right exception?
		throw new RuntimeException("Attempt to convert a world that is not a BWWorld!");
	}
}
