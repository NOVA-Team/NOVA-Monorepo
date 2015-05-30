package nova.wrapper.mc1710.wrapper.entity;

import nova.core.entity.Entity;
import nova.core.nativewrapper.NativeConverter;
import nova.wrapper.mc1710.wrapper.entity.forward.FWEntity;
import nova.wrapper.mc1710.wrapper.entity.forward.MCEntityWrapper;

public class EntityConverter implements NativeConverter<Entity, net.minecraft.entity.Entity> {

	@Override
	public Class<Entity> getNovaSide() {
		return Entity.class;
	}

	@Override
	public Class<net.minecraft.entity.Entity> getNativeSide() {
		return net.minecraft.entity.Entity.class;
	}

	@Override
	public Entity toNova(net.minecraft.entity.Entity mcEntity) {
		//Prevents dual wrapping
		if (mcEntity instanceof FWEntity) {
			return ((FWEntity) mcEntity).wrapped;
		}

		return new BWEntity(mcEntity);
	}

	@Override
	public net.minecraft.entity.Entity toNative(Entity novaObj) {
		MCEntityWrapper wrapper = novaObj.get(MCEntityWrapper.class);

		if (wrapper.wrapper instanceof FWEntity) {
			return wrapper.wrapper;
		}

		throw new IllegalArgumentException("Entity wrapper is invalid (where did this object come from?)");

	}
}