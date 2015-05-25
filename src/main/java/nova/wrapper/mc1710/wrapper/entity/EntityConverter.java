package nova.wrapper.mc1710.wrapper.entity;

import nova.core.entity.Entity;
import nova.core.nativewrapper.NativeConverter;
import nova.wrapper.mc1710.backward.BackwardProxyUtil;
import nova.wrapper.mc1710.backward.entity.BWEntity;
import nova.wrapper.mc1710.forward.entity.FWEntity;
import nova.wrapper.mc1710.forward.entity.MCEntityWrapper;

import java.util.Optional;

public class EntityConverter implements NativeConverter {

	@Override
	public Class<?> getNovaSide() {
		return Entity.class;
	}

	@Override
	public Class<?> getNativeSide() {
		return net.minecraft.entity.Entity.class;
	}

	@Override
	public Object toNova(Object nativeObj) {
		if (nativeObj instanceof FWEntity) {
			return ((FWEntity) nativeObj).wrapped;
		}
		return BackwardProxyUtil.getEntityWrapper((net.minecraft.entity.Entity) nativeObj);
	}

	@Override
	public Object toNative(Object novaObj) {
		Optional<MCEntityWrapper> opWrapper = ((Entity) novaObj).getOp(MCEntityWrapper.class);
		if (opWrapper.isPresent()) {
			if (opWrapper.get().wrapper instanceof FWEntity) {
				return opWrapper.get().wrapper;
			} else {
				throw new IllegalArgumentException("Entity wrapper is invalid(where did this object come from?)");
			}
		} else {
			if (novaObj instanceof BWEntity) {
				return ((BWEntity) novaObj).entity;
			} else {
				// *Shouldn't* happen, but inevitably will if someone tries implementing stuff they shouldn't.
				throw new IllegalArgumentException("Entity doesn't have a wrapper, and isn't a BWEntity, conversion not possible.");
			}
		}
	}
}