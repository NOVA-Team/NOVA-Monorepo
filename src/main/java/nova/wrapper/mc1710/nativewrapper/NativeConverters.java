package nova.wrapper.mc1710.nativewrapper;

import nova.core.block.Block;
import nova.core.entity.Entity;
import nova.core.nativewrapper.NativeConverter;
import nova.core.nativewrapper.NativeManager;
import nova.wrapper.mc1710.backward.BackwardProxyUtil;
import nova.wrapper.mc1710.backward.block.BWBlock;
import nova.wrapper.mc1710.backward.entity.BWEntity;
import nova.wrapper.mc1710.forward.block.BlockWrapperRegistry;
import nova.wrapper.mc1710.forward.entity.FWEntity;
import nova.wrapper.mc1710.forward.entity.MCEntityWrapper;

import java.util.Optional;

public class NativeConverters {

	public static void registerConverters(NativeManager nativeManager) {
		nativeManager.registerNativeConverter(new EntityNativeConverter());
		nativeManager.registerNativeConverter(new BlockNativeConverter());
	}

	public static class EntityNativeConverter implements NativeConverter {

		@Override
		public Class<?> getNovaSide() {
			return Entity.class;
		}

		@Override
		public Class<?> getNativeSide() {
			return net.minecraft.entity.Entity.class;
		}

		@Override
		public Object convertToNova(Object nativeObj) {
			if (nativeObj instanceof FWEntity)
			{
				return ((FWEntity) nativeObj).wrapped;
			}
			return BackwardProxyUtil.getEntityWrapper((net.minecraft.entity.Entity) nativeObj);
		}

		@Override
		public Object convertToNative(Object novaObj) {
			Optional<MCEntityWrapper> opWrapper = ((Entity) novaObj).getOp(MCEntityWrapper.class);
			if (opWrapper.isPresent())
			{
				if (opWrapper.get().wrapper instanceof FWEntity)
				{
					return opWrapper.get().wrapper;
				} else {
					throw new IllegalArgumentException("Entity wrapper is invalid(where did this object come from?)");
				}
			} else {
				if (novaObj instanceof BWEntity)
				{
					return ((BWEntity)novaObj).entity;
				} else {
					// *Shouldn't* happen, but inevitably will if someone tries implementing stuff they shouldn't.
					throw new IllegalArgumentException("Entity doesn't have a wrapper, and isn't a BWEntity, conversion not possible.");
				}
			}
		}
	}

	public static class BlockNativeConverter implements NativeConverter {

		@Override
		public Class<?> getNovaSide() {
			return Block.class;
		}

		@Override
		public Class<?> getNativeSide() {
			return net.minecraft.block.Block.class;
		}

		@Override
		public Object convertToNova(Object nativeObj) {
			return new BWBlock((net.minecraft.block.Block)nativeObj);
		}

		@Override
		public Object convertToNative(Object novaObj) {
			return BlockWrapperRegistry.instance.getMCBlock((Block)novaObj);
		}

	}

}
