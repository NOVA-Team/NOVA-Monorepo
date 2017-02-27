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

package nova.core.wrapper.mc.forge.v1_11_2.wrapper.block.forward;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import nova.core.block.Block;
import nova.core.block.BlockFactory;
import nova.core.block.Stateful;
import nova.core.block.component.BlockProperty;
import nova.core.block.component.LightEmitter;
import nova.core.component.Updater;
import nova.core.component.misc.Collider;
import nova.core.retention.Storable;
import nova.core.sound.Sound;
import nova.core.util.Direction;
import nova.core.util.math.MathUtil;
import nova.core.util.shape.Cuboid;
import nova.core.wrapper.mc.forge.v1_11_2.util.WrapperEvent;
import nova.internal.core.Game;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A Minecraft to Nova block wrapper
 * @author Calclavia
 */
public class FWBlock extends net.minecraft.block.Block {
	public final Block dummy;
	/**
	 * Reference to the wrapper Nova block
	 */
	private final BlockFactory factory;
	private final Class<? extends Block> blockClass;
	private Map<BlockPosition, Block> harvestedBlocks = new HashMap<>();

	private static Material getMcMaterial(BlockFactory factory) {
		Block dummy = factory.build();
		if (dummy.components.has(BlockProperty.Opacity.class) || dummy.components.has(BlockProperty.Replaceable.class)) {
			// TODO allow color selection
			return new ProxyMaterial(MapColor.GRAY,
				dummy.components.getOp(BlockProperty.Opacity.class),
				dummy.components.getOp(BlockProperty.Replaceable.class));
		} else {
			return Material.PISTON;
		}
	}

	public FWBlock(BlockFactory factory) {
		//TODO: Hack build() method
		super(getMcMaterial(factory));
		this.factory = factory;
		this.dummy = factory.build();
		if (dummy.components.has(BlockProperty.BlockSound.class)) {
			this.blockSoundType = new FWBlockSound(dummy.components.get(BlockProperty.BlockSound.class));
		} else {
			BlockProperty.BlockSound properties = dummy.components.add(new BlockProperty.BlockSound());
			properties.setBlockSound(BlockProperty.BlockSound.BlockSoundTrigger.BREAK, new Sound("", SoundType.STONE.getBreakSound().getSoundName().getResourcePath()));
			properties.setBlockSound(BlockProperty.BlockSound.BlockSoundTrigger.PLACE, new Sound("", SoundType.STONE.getPlaceSound().getSoundName().getResourcePath()));
			properties.setBlockSound(BlockProperty.BlockSound.BlockSoundTrigger.WALK, new Sound("", SoundType.STONE.getStepSound().getSoundName().getResourcePath()));
			this.blockSoundType = SoundType.STONE;
		}
		this.blockClass = dummy.getClass();
		this.setUnlocalizedName(dummy.getID());

		// Recalculate super constructor things after loading the block properly
		this.fullBlock = getDefaultState().isOpaqueCube();
		this.lightOpacity = this.fullBlock ? 255 : 0;
		this.translucent = !this.fullBlock;
	}

	public BlockFactory getFactory() {
		return this.factory;
	}

	public Block getBlockInstance(IBlockAccess access, Vector3D position) {
		/**
		 * If this block has a TileEntity, forward the method into the Stateful
		 * block. Otherwise, create a new instance of the block and forward the
		 * methods over.
		 */
		if (hasTileEntity(null)) {
			FWTile tileWrapper = (FWTile) access.getTileEntity(new BlockPos((int) position.getX(), (int) position.getY(), (int) position.getZ()));
			if (tileWrapper != null && tileWrapper.getBlock() != null) {
				return tileWrapper.getBlock();
			}

			try {
				throw new RuntimeException("Error: Block in TileWrapper is null for " + dummy);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return getBlockInstance((nova.core.world.World) Game.natives().toNova(access), position);

	}

	private Block getBlockInstance(nova.core.world.World world, Vector3D position) {
		// TODO: Implement obj args
		Block block = factory.build();
		block.components.add(new MCBlockTransform(block, world, position));
		return block;
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		// HACK: called before block is destroyed by the player prior to the
		// player getting the drops. Determine drops here.
		// hack is needed because the player sets the block to air *before*
		// getting the drops. woo good logic from mojang.
		if (!player.capabilities.isCreativeMode) {
			harvestedBlocks.put(new BlockPosition(world, pos.getX(), pos.getY(), pos.getZ()), getBlockInstance(world, new Vector3D(pos.getX(), pos.getY(), pos.getZ())));
		}
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		Block blockInstance;

		// see onBlockHarvested for why the harvestedBlocks hack exists
		// this method will be called exactly once after destroying the block
		BlockPosition position = new BlockPosition((World) world, pos.getX(), pos.getY(), pos.getZ());
		if (harvestedBlocks.containsKey(position)) {
			blockInstance = harvestedBlocks.remove(position);
		} else {
			blockInstance = getBlockInstance(world, new Vector3D(pos.getX(), pos.getY(), pos.getZ()));
		}

		Block.DropEvent event = new Block.DropEvent(blockInstance);
		blockInstance.events.publish(event);

		return new ArrayList<>(
			event.drops
				.stream()
				.map(item -> (ItemStack) Game.natives().toNative(item))
				.collect(Collectors.toCollection(ArrayList::new))
		);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		// A block requires a TileEntity if it stores data or if it ticks.
		return Storable.class.isAssignableFrom(blockClass) || Stateful.class.isAssignableFrom(blockClass) || Updater.class.isAssignableFrom(blockClass);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return FWTileLoader.loadTile(dummy.getID());
	}

	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighborBlock) {
		Block blockInstance = getBlockInstance(world, new Vector3D(pos.getX(), pos.getY(), pos.getZ()));
		// Minecraft does not provide the neighbor :(
		Block.NeighborChangeEvent evt = new Block.NeighborChangeEvent(Optional.empty());
		blockInstance.events.publish(evt);
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
		Block blockInstance = getBlockInstance(world, new Vector3D(pos.getX(), pos.getY(), pos.getZ()));
		Block.RemoveEvent evt = new Block.RemoveEvent(Optional.of(Game.natives().toNova(player)));
		blockInstance.events.publish(evt);
		if (evt.result) {
			return super.removedByPlayer(state, world, pos, player, willHarvest);
		}
		return false;
	}

	@Override
	public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {
		Block blockInstance = getBlockInstance(world, new Vector3D(pos.getX(), pos.getY(), pos.getZ()));
		RayTraceResult mop = player.rayTrace(10, 1);
		Block.LeftClickEvent evt = new Block.LeftClickEvent(Game.natives().toNova(player), Direction.fromOrdinal(mop.sideHit.ordinal()), new Vector3D(mop.hitVec.xCoord, mop.hitVec.yCoord, mop.hitVec.zCoord));
		blockInstance.events.publish(evt);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		Block blockInstance = getBlockInstance(world, new Vector3D(pos.getX(), pos.getY(), pos.getZ()));
		Block.RightClickEvent evt = new Block.RightClickEvent(Game.natives().toNova(player), Direction.fromOrdinal(side.ordinal()), new Vector3D(hitX, hitY, hitZ));
		blockInstance.events.publish(evt);
		return evt.result;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
		Block blockInstance = getBlockInstance(world, new Vector3D(pos.getX(), pos.getY(), pos.getZ()));
		blockInstance.components.getOp(Collider.class).ifPresent(collider -> blockInstance.events.publish(new Collider.CollideEvent(Game.natives().toNova(entity))));
	}

	@Override
	@Deprecated
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess access, BlockPos pos) {
		Block blockInstance = getBlockInstance(access, new Vector3D(pos.getX(), pos.getY(), pos.getZ()));
		if (blockInstance.components.has(Collider.class)) {
			Cuboid cuboid = blockInstance.components.get(Collider.class).boundingBox.get();
			return new AxisAlignedBB(cuboid.min.getX(), cuboid.min.getY(), cuboid.min.getZ(), cuboid.max.getX(), cuboid.max.getY(), cuboid.max.getZ());
		}
		return super.getBoundingBox(state, access, pos);
	}

	@Override
	@Deprecated
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World world, BlockPos pos) {
		Block blockInstance = getBlockInstance(world, new Vector3D(pos.getX(), pos.getY(), pos.getZ()));

		if (blockInstance.components.has(Collider.class)) {
			Cuboid cuboid = blockInstance.components.get(Collider.class).boundingBox.get();
			return Game.natives().toNative(cuboid.add(new Vector3D(pos.getX(), pos.getY(), pos.getZ())));
		}
		return super.getSelectedBoundingBox(state, world, pos);
	}

	@Override
	@Deprecated
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB boundingBox, List<AxisAlignedBB> list, Entity entity, boolean b) {
		Block blockInstance = getBlockInstance(world, new Vector3D(pos.getX(), pos.getY(), pos.getZ()));
		blockInstance.components.getOp(Collider.class).ifPresent(
			collider -> {
				Set<Cuboid> boxes = collider.occlusionBoxes.apply(Optional.ofNullable(entity != null ? Game.natives().toNova(entity) : null));

				list.addAll(
					boxes
						.stream()
						.map(c -> c.add(new Vector3D(pos.getX(), pos.getY(), pos.getZ())))
						.filter(c -> c.intersects((Cuboid) Game.natives().toNova(boundingBox)))
						.map(cuboid -> (AxisAlignedBB) Game.natives().toNative(cuboid))
						.collect(Collectors.toList())
				);
			}
		);
	}

	@Override
	@Deprecated
	public boolean isOpaqueCube(IBlockState state) {
		if (dummy == null) {
			// Superconstructor fix. -10 style points.
			return true;
		}

		Optional<Collider> blockCollider = dummy.components.getOp(Collider.class);

		if (blockCollider.isPresent()) {
			return blockCollider.get().isOpaqueCube.get();
		} else {
			return super.isOpaqueCube(state);
		}
	}

	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
		Optional<Collider> blockCollider = getBlockInstance(world, new Vector3D(pos.getX(), pos.getY(), pos.getZ())).components.getOp(Collider.class);

		if (blockCollider.isPresent()) {
			return blockCollider.get().isCube.get();
		} else {
			return super.isNormalCube(state, world, pos);
		}
	}

	@Override
	@Deprecated
	public boolean isFullCube(IBlockState state) {
		Optional<Collider> blockCollider = dummy.components.getOp(Collider.class);

		if (blockCollider.isPresent()) {
			return blockCollider.get().isCube.get();
		} else {
			return super.isFullCube(state);
		}
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess access, BlockPos pos) {
		Block blockInstance = getBlockInstance(access, new Vector3D(pos.getX(), pos.getY(), pos.getZ()));
		Optional<LightEmitter> opEmitter = blockInstance.components.getOp(LightEmitter.class);

		if (opEmitter.isPresent()) {
			return (int) MathUtil.clamp(Math.round(((java.util.function.DoubleSupplier)((Object)opEmitter.get().emittedLevel)).getAsDouble() * 15), 0, 15);
		} else {
			return 0;
		}
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing side) {
		Block blockInstance = getBlockInstance(access, new Vector3D(pos.getX(), pos.getY(), pos.getZ()));
		WrapperEvent.RedstoneConnect event = new WrapperEvent.RedstoneConnect(blockInstance.world(), blockInstance.position(), Direction.fromOrdinal(side.ordinal()));
		Game.events().publish(event);
		return event.canConnect;
	}

	@Override
	@Deprecated
	public int getWeakPower(IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing side) {
		Block blockInstance = getBlockInstance(access, new Vector3D(pos.getX(), pos.getY(), pos.getZ()));
		WrapperEvent.WeakRedstone event = new WrapperEvent.WeakRedstone(blockInstance.world(), blockInstance.position(), Direction.fromOrdinal(side.ordinal()));
		Game.events().publish(event);
		return event.power;
	}

	@Override
	@Deprecated
	public int getStrongPower(IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing side) {
		Block blockInstance = getBlockInstance(access, new Vector3D(pos.getX(), pos.getY(), pos.getZ()));
		WrapperEvent.StrongRedstone event = new WrapperEvent.StrongRedstone(blockInstance.world(), blockInstance.position(), Direction.fromOrdinal(side.ordinal()));
		Game.events().publish(event);
		return event.power;
	}

	@Override
	public String getUnlocalizedName() {
		return super.getUnlocalizedName().replaceFirst("tile", "block");
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
		// TODO: Maybe do something withPriority these parameters.
		return (float) getBlockInstance(world, new Vector3D(pos.getX(), pos.getY(), pos.getZ())).getResistance() * 30;
	}

	@Override
	@Deprecated
	public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
		return (float) getBlockInstance(world, new Vector3D(pos.getX(), pos.getY(), pos.getZ())).getHardness() * 2;
	}
}
