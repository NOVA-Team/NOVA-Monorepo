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

package nova.core.wrapper.mc.forge.v1_11_2.wrapper.entity.backward;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import nova.core.component.inventory.InventoryPlayer;
import nova.core.component.misc.Damageable;
import nova.core.entity.Entity;
import nova.core.entity.component.Living;
import nova.core.entity.component.Player;
import nova.core.wrapper.mc.forge.v1_11_2.util.WrapperEvent;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.entity.forward.MCEntityTransform;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.inventory.BWInventory;
import nova.internal.core.Game;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * A Minecraft to NOVA Entity wrapper
 * @author Calclavia
 */
//TODO: Incomplete. Add more components!
public class BWEntity extends Entity {

	public net.minecraft.entity.Entity entity;

	public BWEntity(net.minecraft.entity.Entity entity) {
		this.entity = entity;

		components.add(new MCEntityTransform(entity));
		components.add(new Damageable() {
			@Override
			public void damage(double amount, DamageType type) {
				if (type == DamageType.generic) {
					entity.attackEntityFrom(DamageSource.GENERIC, (float) amount);
				}
				// TODO: Apply other damage source wrappers?
			}
		});

		if (entity instanceof EntityLivingBase) {
			if (entity instanceof EntityPlayer) {
				MCPlayer player = components.add(new MCPlayer(this));
				player.faceDisplacement = () -> Vector3D.PLUS_J.scalarMultiply(entity.getEyeHeight());
			} else {
				Living living = components.add(new Living());
				living.faceDisplacement = () -> Vector3D.PLUS_J.scalarMultiply(entity.getEyeHeight());
			}
		}

		WrapperEvent.BWEntityCreate event = new WrapperEvent.BWEntityCreate(this, entity);
		Game.events().publish(event);
	}

	public static class MCPlayer extends Player {
		public final BWEntity bwEntity;
		public final EntityPlayer entity;
		public final BWInventoryPlayer inventory;

		public MCPlayer(BWEntity bwEntity) {
			this.bwEntity = bwEntity;
			this.entity = (EntityPlayer) bwEntity.entity;
			this.inventory = new BWInventoryPlayer(entity);
		}

		@Override
		public Entity entity() {
			return bwEntity;
		}

		@Override
		public String getUniqueID() {
			return entity.getGameProfile().getId().toString();
		}

		@Override
		public InventoryPlayer getInventory() {
			return inventory;
		}

		@Override
		public String getUsername() {
			return entity.getGameProfile().getName();
		}

		@Override
		public String getDisplayName() {
			return entity.getDisplayName().getUnformattedText();
		}
	}

	public static class BWInventoryPlayer extends BWInventory implements InventoryPlayer {
		public final EntityPlayer entity;

		public BWInventoryPlayer(EntityPlayer entity) {
			super(entity.inventory);
			this.entity = entity;
		}

		@Override
		public int getHeldSlot() {
			return entity.inventory.currentItem;
		}
	}
}
