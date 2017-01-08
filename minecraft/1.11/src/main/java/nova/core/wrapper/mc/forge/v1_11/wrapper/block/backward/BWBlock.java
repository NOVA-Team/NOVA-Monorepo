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

package nova.core.wrapper.mc.forge.v1_11.wrapper.block.backward;

import net.minecraft.block.BlockSnow;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import nova.core.block.Block;
import nova.core.block.component.BlockProperty;
import nova.core.block.component.LightEmitter;
import nova.core.component.misc.Collider;
import nova.core.component.transform.BlockTransform;
import nova.core.item.ItemFactory;
import nova.core.retention.Data;
import nova.core.retention.Storable;
import nova.core.retention.Store;
import nova.core.sound.Sound;
import nova.core.util.shape.Cuboid;
import nova.core.world.World;
import nova.core.wrapper.mc.forge.v1_11.util.WrapperEvent;
import nova.core.wrapper.mc.forge.v1_11.wrapper.block.world.BWWorld;
import nova.internal.core.Game;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BWBlock extends Block implements Storable {
	public final net.minecraft.block.Block mcBlock;
	@Store
	public int metadata;
	private TileEntity mcTileEntity;

	public BWBlock(net.minecraft.block.Block block) {
		this.mcBlock = block;
	}

	public BWBlock(net.minecraft.block.Block block, World world, Vector3D pos) {
		this.mcBlock = block;

		BlockTransform transform = components.add(new BlockTransform());
		transform.setWorld(world);
		transform.setPosition(pos);

		components.add(new BlockProperty.Opacity().setLightTransmission(!blockState().getMaterial().blocksLight()));

		BlockProperty.BlockSound blockSound = components.add(new BlockProperty.BlockSound());
		SoundType soundType;
		if (getMcBlockAccess() instanceof net.minecraft.world.World)
			soundType = mcBlock.getSoundType(blockState(), (net.minecraft.world.World)getMcBlockAccess(), new BlockPos(x(), y(), z()), null);
		else
			soundType = mcBlock.getSoundType();
		blockSound.setBlockSound(BlockProperty.BlockSound.BlockSoundTrigger.PLACE, new Sound("", soundType.getPlaceSound().getSoundName().getResourcePath()));
		blockSound.setBlockSound(BlockProperty.BlockSound.BlockSoundTrigger.BREAK, new Sound("", soundType.getBreakSound().getSoundName().getResourcePath()));
		blockSound.setBlockSound(BlockProperty.BlockSound.BlockSoundTrigger.WALK, new Sound("",soundType.getStepSound().getSoundName().getResourcePath()));

		components.add(new LightEmitter()).setEmittedLevel(() -> blockState().getLightValue(getMcBlockAccess(), new BlockPos(x(), y(), z())) / 15.0F);
		components.add(new Collider(this))
			.setBoundingBox(() -> {
				AxisAlignedBB aabb = blockState().getBoundingBox(getMcBlockAccess(), new BlockPos(x(), y(), z()));
				return new Cuboid(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
			}).setOcclusionBoxes(entity -> {
				List<AxisAlignedBB> aabbs = new ArrayList<>();
				blockState().addCollisionBoxToList(
					Game.natives().toNative(world()),
					new BlockPos(x(), y(), z()),
					Game.natives().toNative(entity.isPresent() ? entity.get().components.get(Collider.class).boundingBox.get() : Cuboid.ONE.add(pos)),
					aabbs,
					entity.isPresent() ? Game.natives().toNative(entity.get()) : null
				);
				return aabbs.stream()
					.map(aabb -> (Cuboid) Game.natives().toNova(aabb))
					.map(cuboid -> cuboid.subtract(pos))
					.collect(Collectors.toSet());
			});
		WrapperEvent.BWBlockCreate event = new WrapperEvent.BWBlockCreate(world, pos, this, mcBlock);
		Game.events().publish(event);
		//TODO: Set selection bounds
	}

	@Override
	public ItemFactory getItemFactory() {
		return Game.natives().toNova(new ItemStack(Item.getItemFromBlock(mcBlock)));
	}

	public IBlockAccess getMcBlockAccess() {
		return ((BWWorld) world()).access;
	}

	public IBlockState blockState() {
		return getMcBlockAccess().getBlockState(new BlockPos(x(), y(), z()));
	}

	public TileEntity getTileEntity() {
		if (mcTileEntity == null && mcBlock.hasTileEntity(blockState())) {
			mcTileEntity = getMcBlockAccess().getTileEntity(new BlockPos(x(), y(), z()));
		}
		return mcTileEntity;
	}

	@Override
	public boolean canReplace() {
		return mcBlock.canPlaceBlockAt((net.minecraft.world.World) getMcBlockAccess(), new BlockPos(x(), y(), z()));
	}

	@Override
	public boolean shouldDisplacePlacement() {
		if (mcBlock == Blocks.SNOW_LAYER && ((int) blockState().getValue(BlockSnow.LAYERS) < 1)) {
			return false;
		}

		if (mcBlock == Blocks.VINE || mcBlock == Blocks.TALLGRASS || mcBlock == Blocks.DEADBUSH || mcBlock.isReplaceable(Game.natives().toNative(world()), new BlockPos(x(), y(), z()))) {
			return false;
		}
		return super.shouldDisplacePlacement();
	}

	@Override
	public void save(Data data) {
		Storable.super.save(data);

		TileEntity tileEntity = getTileEntity();
		if (tileEntity != null) {
			NBTTagCompound nbt = new NBTTagCompound();
			tileEntity.writeToNBT(nbt);
			data.putAll(Game.natives().toNova(nbt));
		}
	}

	@Override
	public void load(Data data) {
		Storable.super.load(data);

		TileEntity tileEntity = getTileEntity();
		if (tileEntity != null) {
			tileEntity.writeToNBT(Game.natives().toNative(data));
		}
	}
}
