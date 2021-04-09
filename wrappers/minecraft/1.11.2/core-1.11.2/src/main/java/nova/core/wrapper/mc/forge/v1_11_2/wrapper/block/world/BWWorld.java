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

package nova.core.wrapper.mc.forge.v1_11_2.wrapper.block.world;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import nova.core.block.Block;
import nova.core.block.BlockFactory;
import nova.core.component.misc.FactoryProvider;
import nova.core.entity.Entity;
import nova.core.entity.EntityFactory;
import nova.core.item.Item;
import nova.core.sound.Sound;
import nova.core.util.shape.Cuboid;
import nova.core.world.World;
import nova.core.wrapper.mc.forge.v1_11_2.launcher.NovaMinecraft;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.VectorConverter;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.block.BlockConverter;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.block.backward.BWBlock;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.block.forward.FWBlock;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.block.forward.FWBlockTransform;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.cuboid.CuboidConverter;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.entity.EntityConverter;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.entity.forward.FWEntity;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.entity.forward.MCEntityTransform;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.item.ItemConverter;
import nova.internal.core.Game;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The backwards world wrapper.
 * @author Calclavia
 */
public class BWWorld extends World {
	public final net.minecraft.world.IBlockAccess access;

	public BWWorld(net.minecraft.world.IBlockAccess blockAccess) {
		this.access = blockAccess;
	}

	public net.minecraft.world.World world() {
		// Trying to access world from a IBlockAccess object!
		// TODO: Return an optional
		assert access instanceof World;
		return (net.minecraft.world.World) access;
	}

	@Override
	public void markStaticRender(Vector3D position) {
		BlockPos pos = new BlockPos((int) position.getX(), (int) position.getY(), (int) position.getZ());
		world().markBlockRangeForRenderUpdate(pos, pos);
	}

	@Override
	public void markChange(Vector3D position) {
		world().notifyNeighborsOfStateChange(
			new BlockPos((int) position.getX(), (int) position.getY(), (int) position.getZ()),
			access.getBlockState(new BlockPos((int) position.getX(), (int) position.getY(), (int) position.getZ())).getBlock(),
			true
		);
	}

	@Override
	public Optional<Block> getBlock(Vector3D position) {
		IBlockState blockState = access.getBlockState(new BlockPos((int) position.getX(), (int) position.getY(), (int) position.getZ()));
		net.minecraft.block.Block block = blockState == null ? null : blockState.getBlock();
		if (blockState == null || block == null || block == Blocks.AIR) {
			Block airBlock = Game.blocks().getAirBlock().build();
			airBlock.components.add(new FWBlockTransform(airBlock, this, position));
			return Optional.of(airBlock);
		}
		if (block instanceof FWBlock) {
			return Optional.of(((FWBlock) block).getBlockInstance(access, position));
		} else {
			BWBlock wrappedBlock = new BWBlock(blockState, this, position);
			Game.blocks().get(Objects.toString(net.minecraft.block.Block.REGISTRY.getNameForObject(block)))
				.ifPresent(blockFactory -> wrappedBlock.components.getOrAdd(new FactoryProvider(blockFactory)));
			return Optional.of(wrappedBlock);
		}
	}

	@Override
	public boolean setBlock(Vector3D position, BlockFactory blockFactory) {
		net.minecraft.block.Block mcBlock = BlockConverter.instance().toNative(blockFactory);
		BlockPos pos = VectorConverter.instance().toNative(position);
		net.minecraft.block.Block actualBlock = mcBlock != null ? mcBlock : Blocks.AIR;
		IBlockState defaultState = actualBlock.getDefaultState();
		IBlockState extendedState = actualBlock.getExtendedState(defaultState, world(), pos);
		return world().setBlockState(pos, extendedState);
	}

	@Override
	public boolean removeBlock(Vector3D position) {
		return world().setBlockToAir(new BlockPos((int) position.getX(), (int) position.getY(), (int) position.getZ()));
	}

	@Override
	public Entity addEntity(EntityFactory factory) {
		FWEntity bwEntity = new FWEntity(world(), factory);
		bwEntity.forceSpawn = true;
		world().spawnEntity(bwEntity);
		return bwEntity.getWrapped();
	}

	@Override
	public Entity addClientEntity(EntityFactory factory) {
		return NovaMinecraft.proxy.spawnParticle(world(), factory);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends Entity> T addClientEntity(T entity) {
		return (T) NovaMinecraft.proxy.spawnParticle(world(), entity);
	}

	@Override
	public void removeEntity(Entity entity) {
		if (access instanceof net.minecraft.world.World) {
			net.minecraft.entity.Entity wrapper = entity.components.get(MCEntityTransform.class).wrapper;
			wrapper.setDead();
			world().removeEntity(wrapper);
		}
	}

	@Override
	public Set<Entity> getEntities(Cuboid bound) {
		return Optional.of(access)
			.filter(access -> access instanceof net.minecraft.world.World)
			.map(access -> world().getEntitiesWithinAABB(net.minecraft.entity.Entity.class, CuboidConverter.instance().toNative(bound)))
			.orElseGet(Collections::emptyList)
			.stream()
			.map(EntityConverter.instance()::toNova)
			.collect(Collectors.toSet());
	}

	@Override
	public Entity addEntity(Vector3D position, Item item) {
		EntityItem entityItem = new EntityItem(world(), position.getX(), position.getY(), position.getZ(), ItemConverter.instance().toNative(item));
		world().spawnEntity(entityItem);
		return EntityConverter.instance().toNova(entityItem);
	}

	@Override
	public Optional<Entity> getEntity(String uniqueID) {
		return Optional.ofNullable(EntityConverter.instance().toNova(world().getEntityByID(Integer.parseInt(uniqueID))));
	}

	@Override
	public String getID() {
		return world().provider.getDimensionType().getName();
	}

	@Override
	public void playSoundAtPosition(Vector3D position, Sound sound) {
		world().playSound(position.getX(), position.getY(), position.getZ(),
			SoundEvent.REGISTRY.getObject(new ResourceLocation(sound.domain.isEmpty() ? sound.name : sound.getID())),
			SoundCategory.BLOCKS, sound.volume, sound.pitch, true);
	}
}
