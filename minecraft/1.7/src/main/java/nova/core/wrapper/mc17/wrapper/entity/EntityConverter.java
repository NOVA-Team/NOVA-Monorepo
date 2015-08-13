package nova.core.wrapper.mc17.wrapper.entity;

import net.minecraft.client.particle.EntityFX;
import nova.core.entity.Entity;
import nova.core.loader.Loadable;
import nova.core.nativewrapper.NativeConverter;
import nova.core.wrapper.mc17.wrapper.entity.backward.BWEntity;
import nova.core.wrapper.mc17.wrapper.entity.backward.BWEntityFX;
import nova.core.wrapper.mc17.wrapper.entity.forward.FWEntity;
import nova.core.wrapper.mc17.wrapper.entity.forward.MCEntityTransform;

public class EntityConverter implements NativeConverter<Entity, net.minecraft.entity.Entity>, Loadable {

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
		MCEntityTransform transform = novaObj.get(MCEntityTransform.class);

		if (transform.wrapper instanceof FWEntity) {
			return transform.wrapper;
		}

		throw new IllegalArgumentException("Entity wrapper is invalid (where did this object come from?)");
	}

	@Override
	public void preInit() {
		//TODO: Register particle fxs
		/**
		 * Backward register all particle effects
		 */
	}
}