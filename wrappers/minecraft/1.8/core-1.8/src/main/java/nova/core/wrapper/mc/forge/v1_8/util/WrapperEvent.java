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

package nova.core.wrapper.mc.forge.v1_8.util;

import net.minecraft.item.ItemStack;
import nova.core.block.Block;
import nova.core.entity.Entity;
import nova.core.event.BlockEvent;
import nova.core.event.bus.CancelableEvent;
import nova.core.event.bus.Event;
import nova.core.item.Item;
import nova.core.util.Direction;
import nova.core.world.World;
import nova.core.wrapper.mc.forge.v1_8.wrapper.block.backward.BWBlock;
import nova.core.wrapper.mc.forge.v1_8.wrapper.block.forward.FWTile;
import nova.core.wrapper.mc.forge.v1_8.wrapper.entity.backward.BWEntity;
import nova.core.wrapper.mc.forge.v1_8.wrapper.entity.forward.FWEntity;
import nova.core.wrapper.mc.forge.v1_8.wrapper.entity.forward.FWEntityFX;
import nova.core.wrapper.mc.forge.v1_8.wrapper.item.BWItem;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.Optional;

/**
 * Events for wrappers to hook into
 * @author Calclavia
 */
public class WrapperEvent {

	public static class RedstoneConnect extends BlockEvent {
		public final Direction direction;
		public boolean canConnect;

		public RedstoneConnect(World world, Vector3D position, Direction direction) {
			super(world, position);
			this.direction = direction;
		}
	}

	public static class StrongRedstone extends BlockEvent {
		public final Direction direction;
		public int power;

		public StrongRedstone(World world, Vector3D position, Direction direction) {
			super(world, position);
			this.direction = direction;
		}
	}

	public static class WeakRedstone extends BlockEvent {
		public final Direction direction;
		public int power;

		public WeakRedstone(World world, Vector3D position, Direction direction) {
			super(world, position);
			this.direction = direction;
		}
	}

	public static class BWBlockCreate extends BlockEvent {
		public final net.minecraft.block.Block mcBlock;
		public final BWBlock novaBlock;

		public BWBlockCreate(World world, Vector3D position, BWBlock novaBlock, net.minecraft.block.Block mcBlock) {
			super(world, position);
			this.novaBlock = novaBlock;
			this.mcBlock = mcBlock;
		}

		@Override
		public boolean isCanceled() {
			return false;
		}
	}

	public static class BWItemCreate extends Event {
		public final net.minecraft.item.Item mcItem;
		public final BWItem novaItem;
		public final Optional<ItemStack> itemStack;

		public BWItemCreate(BWItem novaItem, net.minecraft.item.Item mcItem) {
			this.novaItem = novaItem;
			this.mcItem = mcItem;
			this.itemStack = Optional.empty();
		}

		public BWItemCreate(BWItem novaItem, net.minecraft.item.Item mcItem, ItemStack itemStack) {
			this.novaItem = novaItem;
			this.mcItem = mcItem;
			this.itemStack = Optional.of(itemStack);
		}
	}

	public static class BWEntityCreate extends Event {
		public final net.minecraft.entity.Entity mcEntity;
		public final BWEntity novaEntity;

		public BWEntityCreate(BWEntity novaEntity, net.minecraft.entity.Entity mcEntity) {
			this.novaEntity = novaEntity;
			this.mcEntity = mcEntity;
		}
	}

	public static class BWEntityFXCreate extends Event {
		public final net.minecraft.client.particle.EntityFX mcEntityFX;
		public final BWEntity novaEntity;

		public BWEntityFXCreate(BWEntity novaEntity, net.minecraft.client.particle.EntityFX mcEntityFX) {
			this.novaEntity = novaEntity;
			this.mcEntityFX = mcEntityFX;
		}
	}

	public static class UpdateItemEvent extends Event {
		public final ItemStack mcItem;
		public final Item novaItem;

		public UpdateItemEvent(Item novaItem, ItemStack mcItem) {
			this.novaItem = novaItem;
			this.mcItem = mcItem;
		}
	}

	public static class FWTileCreate extends Event {
		public final Block novaBlock;
		public final FWTile tileEntity;

		public FWTileCreate(Block novaBlock, FWTile tileEntity) {
			this.novaBlock = novaBlock;
			this.tileEntity = tileEntity;
		}
	}

	public static class FWEntityCreate extends Event {
		public final Entity novaEntity;
		public final FWEntity mcEntity;

		public FWEntityCreate(Entity novaEntity, FWEntity mcEntity) {
			this.novaEntity = novaEntity;
			this.mcEntity = mcEntity;
		}
	}

	public static class FWEntityFXCreate extends Event {
		public final Entity novaEntity;
		public final FWEntityFX mcEntity;

		public FWEntityFXCreate(Entity novaEntity, FWEntityFX mcEntity) {
			this.novaEntity = novaEntity;
			this.mcEntity = mcEntity;
		}
	}
}
