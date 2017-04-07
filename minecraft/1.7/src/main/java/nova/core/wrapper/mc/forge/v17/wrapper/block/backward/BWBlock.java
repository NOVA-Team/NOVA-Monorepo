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

package nova.core.wrapper.mc.forge.v17.wrapper.block.backward;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import nova.core.block.Block;
import nova.core.block.component.BlockProperty;
import nova.core.block.component.LightEmitter;
import nova.core.component.misc.Collider;
import nova.core.component.renderer.StaticRenderer;
import nova.core.component.transform.BlockTransform;
import nova.core.item.ItemFactory;
import nova.core.render.model.CustomModel;
import nova.core.retention.Data;
import nova.core.retention.Storable;
import nova.core.retention.Store;
import nova.core.sound.Sound;
import nova.core.util.shape.Cuboid;
import nova.core.world.World;
import nova.core.wrapper.mc.forge.v17.util.WrapperEvent;
import nova.core.wrapper.mc.forge.v17.wrapper.block.world.BWWorld;
import nova.internal.core.Game;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

		components.add(new BlockProperty.Opacity().setOpacity(mcBlock.getMaterial().isOpaque() ? 1 : 0));
		if (mcBlock.isReplaceable(getMcBlockAccess(), x(), y(), z()))
			components.add(BlockProperty.Replaceable.instance());

		BlockProperty.BlockSound blockSound = components.add(new BlockProperty.BlockSound());
		blockSound.setBlockSound(BlockProperty.BlockSound.BlockSoundTrigger.PLACE, new Sound("", mcBlock.stepSound.func_150496_b()));
		blockSound.setBlockSound(BlockProperty.BlockSound.BlockSoundTrigger.BREAK, new Sound("", mcBlock.stepSound.getBreakSound()));
		blockSound.setBlockSound(BlockProperty.BlockSound.BlockSoundTrigger.WALK, new Sound("", mcBlock.stepSound.getStepResourcePath()));

		components.add(new LightEmitter()).setEmittedLevel(() -> mcBlock.getLightValue(getMcBlockAccess(), x(), y(), z()) / 15.0);
		components.add(new Collider(this))
			.setBoundingBox(() -> new Cuboid(mcBlock.getBlockBoundsMinX(), mcBlock.getBlockBoundsMinY(), mcBlock.getBlockBoundsMinZ(), mcBlock.getBlockBoundsMaxX(), mcBlock.getBlockBoundsMaxY(), mcBlock.getBlockBoundsMaxZ()))
			.setOcclusionBoxes(entity -> {
				List<AxisAlignedBB> aabbs = new ArrayList<>();
				mcBlock.addCollisionBoxesToList(
					Game.natives().toNative(world()),
					(int) position().getX(),
					(int) position().getY(),
					(int) position().getZ(),
					Game.natives().toNative(entity.isPresent() ? entity.get().components.get(Collider.class).boundingBox.get() : Cuboid.ONE.add(pos)),
					aabbs,
					entity.isPresent() ? Game.natives().toNative(entity.get()) : null
				);

				return aabbs.stream()
					.map(aabb -> (Cuboid) Game.natives().toNova(aabb))
					.map(cuboid -> cuboid.subtract(pos))
					.collect(Collectors.toSet());
			});
		//TODO: Set selection bounds
		components.add(new StaticRenderer())
			.onRender(model -> model.addChild(new CustomModel(self -> RenderBlocks.getInstance().renderStandardBlock(mcBlock, x(), y(), z()))));
		WrapperEvent.BWBlockCreate event = new WrapperEvent.BWBlockCreate(world, pos, this, mcBlock);
		Game.events().publish(event);
	}

	@Override
	public ItemFactory getItemFactory() {
		return Game.natives().toNova(new ItemStack(Item.getItemFromBlock(mcBlock)));
	}

	public IBlockAccess getMcBlockAccess() {
		return ((BWWorld) world()).access;
	}

	public int getMetadata() {
		return getMcBlockAccess().getBlockMetadata(x(), y(), z());
	}

	public Optional<TileEntity> getTileEntity() {
		return Optional.ofNullable(getTileEntityImpl());
	}

	private TileEntity getTileEntityImpl() {
		if (mcTileEntity == null && mcBlock.hasTileEntity(getMetadata())) {
			mcTileEntity = getMcBlockAccess().getTileEntity(x(), y(), z());
		}
		return mcTileEntity;
	}

	@Override
	public boolean canReplace() {
		return mcBlock.canPlaceBlockAt((net.minecraft.world.World) getMcBlockAccess(), x(), y(), z());
	}

	@Override
	public boolean shouldDisplacePlacement() {
		if (mcBlock == Blocks.snow_layer && (getMcBlockAccess().getBlockMetadata(x(), y(), z()) & 7) < 1) {
			return false;
		}

		if (mcBlock == Blocks.vine || mcBlock == Blocks.tallgrass || mcBlock == Blocks.deadbush || mcBlock.isReplaceable(getMcBlockAccess(), x(), y(), z())) {
			return false;
		}
		return super.shouldDisplacePlacement();
	}

	@Override
	public void save(Data data) {
		Storable.super.save(data);

		TileEntity tileEntity = getTileEntityImpl();
		if (tileEntity != null) {
			NBTTagCompound nbt = new NBTTagCompound();
			tileEntity.writeToNBT(nbt);
			data.putAll(Game.natives().toNova(nbt));
		}
	}

	@Override
	public void load(Data data) {
		Storable.super.load(data);

		TileEntity tileEntity = getTileEntityImpl();
		if (tileEntity != null) {
			tileEntity.writeToNBT(Game.natives().toNative(data));
		}
	}

	@Override
	public String getLocalizedName() {
		return mcBlock.getLocalizedName();
	}

	@Override
	public String getUnlocalizedName() {
		return mcBlock.getUnlocalizedName();
	}
}
