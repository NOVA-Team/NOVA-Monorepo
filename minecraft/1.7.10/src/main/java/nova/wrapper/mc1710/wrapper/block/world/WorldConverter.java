package nova.wrapper.mc1710.wrapper.block.world;

import net.minecraft.world.IBlockAccess;
import nova.core.nativewrapper.NativeConverter;
import nova.core.world.World;
import nova.internal.core.Game;

import java.util.Optional;

/**
 * @author Calclavia
 */
public class WorldConverter implements NativeConverter<World, IBlockAccess> {
	@Override
	public Class<World> getNovaSide() {
		return World.class;
	}

	@Override
	public Class<IBlockAccess> getNativeSide() {
		return IBlockAccess.class;
	}

	@Override
	public World toNova(IBlockAccess nativeObj) {
		if (nativeObj instanceof net.minecraft.world.World) {
			Optional<World> opWorld = Game.worlds().findWorld(((net.minecraft.world.World) nativeObj).provider.getDimensionName());
			if (opWorld.isPresent()) {
				return opWorld.get();
			}
		}

		return new BWWorld(nativeObj);
	}

	@Override
	public IBlockAccess toNative(World novaObj) {
		if (novaObj instanceof BWWorld) {
			return ((BWWorld) novaObj).world();
		}

		//TODO: Right exception?
		throw new RuntimeException("Attempt to convert a world that is not a BWWorld!");
	}
}
