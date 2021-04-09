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

package nova.core.wrapper.mc.forge.v1_7_10.wrapper.block.backward;

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
import nova.core.item.ItemFactory;
import nova.core.render.model.CustomModel;
import nova.core.retention.Data;
import nova.core.retention.Storable;
import nova.core.retention.Store;
import nova.core.sound.Sound;
import nova.core.util.shape.Cuboid;
import nova.core.world.World;
import nova.core.wrapper.mc.forge.v1_7_10.util.WrapperEvent;
import nova.core.wrapper.mc.forge.v1_7_10.wrapper.block.world.WorldConverter;
import nova.core.wrapper.mc.forge.v1_7_10.wrapper.cuboid.CuboidConverter;
import nova.core.wrapper.mc.forge.v1_7_10.wrapper.data.DataConverter;
import nova.core.wrapper.mc.forge.v1_7_10.wrapper.entity.EntityConverter;
import nova.core.wrapper.mc.forge.v1_7_10.wrapper.item.ItemConverter;
import nova.internal.core.Game;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.ArrayList;
import java.util.Collections;
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
		components.add(new BWBlockTransform(this, world, pos));
		components.add(new BlockProperty.Opacity()).setOpacity(() -> mcBlock.getMaterial().isOpaque() ? 1 : 0);
		BlockProperty.Replaceable replaceable = components.add(new BlockProperty.Replaceable());
		if (block != Blocks.air) {
			replaceable.setReplaceable(() -> mcBlock.canPlaceBlockAt((net.minecraft.world.World) blockAccess(), xi(), yi(), zi()));
		}

		BlockProperty.BlockSound blockSound = components.add(new BlockProperty.BlockSound());
		blockSound.setBlockSound(BlockProperty.BlockSound.BlockSoundTrigger.PLACE, new Sound("", mcBlock.stepSound.func_150496_b()));
		blockSound.setBlockSound(BlockProperty.BlockSound.BlockSoundTrigger.BREAK, new Sound("", mcBlock.stepSound.getBreakSound()));
		blockSound.setBlockSound(BlockProperty.BlockSound.BlockSoundTrigger.WALK, new Sound("", mcBlock.stepSound.getStepResourcePath()));

		components.add(new LightEmitter()).setEmittedLevel(() -> mcBlock.getLightValue(blockAccess(), xi(), yi(), zi()) / 15.0);
		components.add(new Collider(this))
			.setBoundingBox(() -> new Cuboid(mcBlock.getBlockBoundsMinX(), mcBlock.getBlockBoundsMinY(), mcBlock.getBlockBoundsMinZ(), mcBlock.getBlockBoundsMaxX(), mcBlock.getBlockBoundsMaxY(), mcBlock.getBlockBoundsMaxZ()))
			.setOcclusionBoxes(entity -> {
				List<AxisAlignedBB> aabbs = new ArrayList<>();
				if (blockAccess() instanceof net.minecraft.world.World)
					mcBlock.addCollisionBoxesToList(
						(net.minecraft.world.World) blockAccess(),
						(int) position().getX(),
						(int) position().getY(),
						(int) position().getZ(),
						CuboidConverter.instance().toNative(entity
							.flatMap(e -> e.components.getOp(Collider.class))
							.map(c -> c.boundingBox.get())
							.orElseGet(() -> Cuboid.ONE.add(pos))),
						aabbs,
						entity.map(EntityConverter.instance()::toNative).orElse(null)
					);
				return aabbs.stream()
					.map(CuboidConverter.instance()::toNova)
					.map(cuboid -> cuboid.subtract(pos))
					.collect(Collectors.toSet());
			}).setSelectionBoxes(entity -> {
				final AxisAlignedBB bb = mcBlock.getSelectedBoundingBoxFromPool(((net.minecraft.world.World) blockAccess()), xi(), yi(), zi());
				Cuboid cuboid = CuboidConverter.instance().toNova(bb);
				return Collections.singleton(cuboid.subtract(position()));
			});
		components.add(new StaticRenderer())
			.onRender(model -> model.addChild(new CustomModel(self -> RenderBlocks.getInstance().renderStandardBlock(mcBlock, xi(), yi(), zi()))));
		WrapperEvent.BWBlockCreate event = new WrapperEvent.BWBlockCreate(world, pos, this, mcBlock);
		Game.events().publish(event);
	}

	@Override
	public ItemFactory getItemFactory() {
		return ItemConverter.instance().toNova(new ItemStack(Item.getItemFromBlock(mcBlock))).getFactory();
	}

	public int xi() {
		return (int) x();
	}

	public int yi() {
		return (int) y();
	}

	public int zi() {
		return (int) z();
	}

	public net.minecraft.block.Block block() {
		return mcBlock;
	}

	public IBlockAccess blockAccess() {
		return WorldConverter.instance().toNative(world());
	}

	public int getMetadata() {
		return blockAccess().getBlockMetadata(xi(), yi(), zi());
	}

	public Optional<TileEntity> getTileEntity() {
		if (mcTileEntity == null && mcBlock.hasTileEntity(getMetadata())) {
			mcTileEntity = blockAccess().getTileEntity(xi(), yi(), zi());
		}
		return  Optional.ofNullable(mcTileEntity);
	}

	@Override
	public boolean shouldDisplacePlacement() {
		if (mcBlock == Blocks.snow_layer && (blockAccess().getBlockMetadata(xi(), yi(), zi()) & 7) < 1) {
			return false;
		}

		if (mcBlock == Blocks.vine || mcBlock == Blocks.tallgrass || mcBlock == Blocks.deadbush || mcBlock.isReplaceable(blockAccess(), xi(), yi(), zi())) {
			return false;
		}
		return super.shouldDisplacePlacement();
	}

	@Override
	public void save(Data data) {
		Storable.super.save(data);
		getTileEntity().ifPresent(tile -> {
			NBTTagCompound nbt = new NBTTagCompound();
			tile.writeToNBT(nbt);
			data.putAll(DataConverter.instance().toNova(nbt));
		});
	}

	@Override
	public void load(Data data) {
		Storable.super.load(data);
		getTileEntity().ifPresent(tile -> tile.readFromNBT(DataConverter.instance().toNative(data)));
	}

	@Override
	public String getLocalizedName() {
		return mcBlock.getLocalizedName();
	}

	@Override
	public String getUnlocalizedName() {
		return mcBlock.getUnlocalizedName();
	}

	@Override
	public String toString() {
		return "BWBlock{" + mcBlock + ", " + getTileEntity() + "}";
	}
}
