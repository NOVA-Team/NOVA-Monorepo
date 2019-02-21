/*
 * Copyright (c) 2015 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License version 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
 */

package nova.core.wrapper.mc.forge.v1_11_2.wrapper.entity;

import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nova.core.entity.Entity;
import nova.core.entity.EntityFactory;
import nova.core.nativewrapper.NativeConverter;
import nova.core.wrapper.mc.forge.v1_11_2.launcher.ForgeLoadable;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.entity.backward.BWEntity;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.entity.forward.FWEntity;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.entity.forward.MCEntityTransform;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.particle.backward.BWParticle;
import nova.internal.core.Game;

import java.util.Optional;

public class EntityConverter implements NativeConverter<Entity, net.minecraft.entity.Entity>, ForgeLoadable {

	public static EntityConverter instance() {
		return Game.natives().getNative(Entity.class, net.minecraft.entity.Entity.class);
	}

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
			return ((FWEntity) mcEntity).getWrapped();
		}

		//TODO: Make this BWRegistry non-lazy
		//Lazy registry
		String id = mcEntity.getClass().getName();
		Optional<EntityFactory> entityFactory = Game.entities().get(id);

		if (entityFactory.isPresent()) {
			return entityFactory.get().build();
		} else {
			return Game.entities().register(id, () -> new BWEntity(mcEntity)).build();
		}
	}

	@Override
	public net.minecraft.entity.Entity toNative(Entity novaObj) {
		MCEntityTransform transform = novaObj.components.get(MCEntityTransform.class);

		if (transform.wrapper instanceof FWEntity) {
			return transform.wrapper;
		}

		throw new IllegalArgumentException("Entity wrapper is invalid (where did this object come from?)");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void preInit(FMLPreInitializationEvent evt) {
		/**
		 * Backward register all particle effects
		 */
		//Look up for particle factory and pass it into BWParticle
		for (EnumParticleTypes type : EnumParticleTypes.values()) {
			Game.entities().register(Game.info().name + ":" + type.getParticleName(), () -> new BWParticle(type.getParticleID()));
		}
	}
}