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

package nova.core.wrapper.mc.forge.v1_8.wrapper.block.backward;

import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import nova.core.block.Block;
import nova.core.block.component.BlockProperty;
import nova.core.block.component.LightEmitter;
import nova.core.component.misc.Collider;
import nova.core.component.renderer.StaticRenderer;
import nova.core.item.ItemFactory;
import nova.core.retention.Data;
import nova.core.retention.Storable;
import nova.core.retention.Store;
import nova.core.sound.Sound;
import nova.core.util.shape.Cuboid;
import nova.core.world.World;
import nova.core.wrapper.mc.forge.v1_8.util.WrapperEvent;
import nova.core.wrapper.mc.forge.v1_8.wrapper.VectorConverter;
import nova.core.wrapper.mc.forge.v1_8.wrapper.block.world.WorldConverter;
import nova.core.wrapper.mc.forge.v1_8.wrapper.cuboid.CuboidConverter;
import nova.core.wrapper.mc.forge.v1_8.wrapper.data.DataConverter;
import nova.core.wrapper.mc.forge.v1_8.wrapper.entity.EntityConverter;
import nova.core.wrapper.mc.forge.v1_8.wrapper.item.ItemConverter;
import nova.core.wrapper.mc.forge.v1_8.wrapper.render.backward.BWBakedModel;
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
			replaceable.setReplaceable(() -> mcBlock.canPlaceBlockAt((net.minecraft.world.World) blockAccess(), blockPos()));
		}

		BlockProperty.BlockSound blockSound = components.add(new BlockProperty.BlockSound());
		blockSound.setBlockSound(BlockProperty.BlockSound.BlockSoundTrigger.PLACE, new Sound("", mcBlock.stepSound.getPlaceSound()));
		blockSound.setBlockSound(BlockProperty.BlockSound.BlockSoundTrigger.BREAK, new Sound("", mcBlock.stepSound.getBreakSound()));
		blockSound.setBlockSound(BlockProperty.BlockSound.BlockSoundTrigger.WALK, new Sound("", mcBlock.stepSound.getStepSound()));

		components.add(new LightEmitter()).setEmittedLevel(() -> mcBlock.getLightValue(blockAccess(), new BlockPos(x(), y(), z())) / 15.0);
		components.add(new Collider(this))
			.setBoundingBox(() -> new Cuboid(mcBlock.getBlockBoundsMinX(), mcBlock.getBlockBoundsMinY(), mcBlock.getBlockBoundsMinZ(), mcBlock.getBlockBoundsMaxX(), mcBlock.getBlockBoundsMaxY(), mcBlock.getBlockBoundsMaxZ()))
			.setOcclusionBoxes(entity -> {
				List<AxisAlignedBB> aabbs = new ArrayList<>();
				if (blockAccess() instanceof net.minecraft.world.World)
					mcBlock.addCollisionBoxesToList(
						(net.minecraft.world.World) blockAccess(),
						blockPos(),
						blockState(),
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
				final AxisAlignedBB bb = mcBlock.getSelectedBoundingBox(((net.minecraft.world.World) blockAccess()), blockPos());
				Cuboid cuboid = CuboidConverter.instance().toNova(bb);
				return Collections.singleton(cuboid.subtract(position()));
			});
		components.add(new StaticRenderer())
			.onRender(model -> {
				switch (block.getRenderType()) {
					default:
						// rendering of other type
						//  TODO
					case 1:
						// fluid rendering
						//  TODO
						break;
					case 2:
						// chest rendering
						//  Handled by DynamicRenderer
						break;
					case 3:
						// model rendering
						model.addChild(new BWBakedModel(Minecraft.getMinecraft().getBlockRendererDispatcher()
							.getModelFromBlockState(blockState(), blockAccess(), blockPos()), DefaultVertexFormats.BLOCK));
						break;
				}
			});
		// TODO: TileEntity rendering using DynamicRenderer

		WrapperEvent.BWBlockCreate event = new WrapperEvent.BWBlockCreate(world, pos, this, mcBlock);
		Game.events().publish(event);
	}

	@Override
	public ItemFactory getItemFactory() {
		return ItemConverter.instance().toNova(new ItemStack(Item.getItemFromBlock(mcBlock))).getFactory();
	}

	public net.minecraft.block.Block block() {
		return mcBlock;
	}

	public BlockPos blockPos() {
		return VectorConverter.instance().toNative(position());
	}

	public IBlockAccess blockAccess() {
		return WorldConverter.instance().toNative(world());
	}

	public IBlockState blockState() {
		return blockAccess().getBlockState(new BlockPos(x(), y(), z()));
	}

	public Optional<TileEntity> getTileEntity() {
		if (mcTileEntity == null && mcBlock.hasTileEntity(blockState())) {
			mcTileEntity = blockAccess().getTileEntity(blockPos());
		}
		return Optional.ofNullable(mcTileEntity);
	}

	@Override
	public boolean shouldDisplacePlacement() {
		if (mcBlock == Blocks.snow_layer && ((int) blockState().getValue(BlockSnow.LAYERS) < 1)) {
			return false;
		}

		if (mcBlock == Blocks.vine || mcBlock == Blocks.tallgrass || mcBlock == Blocks.deadbush || mcBlock.isReplaceable((net.minecraft.world.World) blockAccess(), blockPos())) {
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
