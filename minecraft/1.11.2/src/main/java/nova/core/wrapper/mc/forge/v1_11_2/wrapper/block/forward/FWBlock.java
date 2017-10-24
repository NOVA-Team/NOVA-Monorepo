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
import nova.core.util.math.Vector3DUtil;
import nova.core.util.shape.Cuboid;
import nova.core.wrapper.mc.forge.v1_11_2.util.WrapperEvent;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.DirectionConverter;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.VectorConverter;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.block.world.WorldConverter;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.cuboid.CuboidConverter;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.entity.EntityConverter;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.item.ItemConverter;
import nova.internal.core.Game;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

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
	//TODO: Hack. Bad practice.
	public IBlockAccess lastExtendedWorld;
	public BlockPos lastExtendedStatePos;
	private Map<BlockPosition, Block> harvestedBlocks = new HashMap<>();

	private static Material getMcMaterial(Block dummy) {
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
		this(factory, factory.build());
	}

	private FWBlock(BlockFactory factory, Block dummy) {
		super(getMcMaterial(dummy));
		this.factory = factory;
		this.dummy = dummy;
		if (dummy.components.has(BlockProperty.BlockSound.class)) {
			this.blockSoundType = new FWBlockSound(dummy.components.get(BlockProperty.BlockSound.class));
		} else {
			BlockProperty.BlockSound properties = dummy.components.add(new BlockProperty.BlockSound());
			properties.setBlockSound(BlockProperty.BlockSound.BlockSoundTrigger.BREAK, new Sound(SoundType.STONE.getBreakSound().getSoundName().getResourceDomain(), SoundType.STONE.getBreakSound().getSoundName().getResourcePath()));
			properties.setBlockSound(BlockProperty.BlockSound.BlockSoundTrigger.PLACE, new Sound(SoundType.STONE.getPlaceSound().getSoundName().getResourceDomain(), SoundType.STONE.getPlaceSound().getSoundName().getResourcePath()));
			properties.setBlockSound(BlockProperty.BlockSound.BlockSoundTrigger.WALK, new Sound(SoundType.STONE.getStepSound().getSoundName().getResourceDomain(), SoundType.STONE.getStepSound().getSoundName().getResourcePath()));
			this.blockSoundType = SoundType.STONE;
		}
		this.blockClass = dummy.getClass();
		this.setUnlocalizedName(dummy.getID());

		// Recalculate super constructor things after loading the block properly
		this.fullBlock = getDefaultState().isOpaqueCube();
		this.lightOpacity = dummy.components
			.getOp(BlockProperty.Opacity.class)
			.map(o -> MathUtil.clamp((int) Math.round(o.getOpacity() * 255), 0, 255))
			.orElse(this.fullBlock ? 255 : 0);
		this.translucent = !this.fullBlock;
	}

	public BlockFactory getFactory() {
		return this.factory;
	}

	public Block getBlockInstance(IBlockAccess access, BlockPos position) {
		return getBlockInstance(access, VectorConverter.instance().toNova(position));
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
		return getBlockInstance(WorldConverter.instance().toNova(access), position);
	}

	private Block getBlockInstance(nova.core.world.World world, Vector3D position) {
		// TODO: Implement obj args
		Block block = factory.build();
		block.components.add(new FWBlockTransform(block, world, position));
		if (!block.components.has(BlockProperty.BlockSound.class)) {
			BlockProperty.BlockSound properties = block.components.add(new BlockProperty.BlockSound());
			properties.setBlockSound(BlockProperty.BlockSound.BlockSoundTrigger.BREAK, new Sound(SoundType.STONE.getBreakSound().getSoundName().getResourceDomain(), SoundType.STONE.getBreakSound().getSoundName().getResourcePath()));
			properties.setBlockSound(BlockProperty.BlockSound.BlockSoundTrigger.PLACE, new Sound(SoundType.STONE.getPlaceSound().getSoundName().getResourceDomain(), SoundType.STONE.getPlaceSound().getSoundName().getResourcePath()));
			properties.setBlockSound(BlockProperty.BlockSound.BlockSoundTrigger.WALK, new Sound(SoundType.STONE.getStepSound().getSoundName().getResourceDomain(), SoundType.STONE.getStepSound().getSoundName().getResourcePath()));
		}
		return block;
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		// HACK: called before block is destroyed by the player prior to the
		// player getting the drops. Determine drops here.
		// hack is needed because the player sets the block to air *before*
		// getting the drops. woo good logic from mojang.
		if (!player.capabilities.isCreativeMode) {
			harvestedBlocks.put(new BlockPosition(world, pos.getX(), pos.getY(), pos.getZ()), getBlockInstance(world, pos));
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
			blockInstance = getBlockInstance(world, pos);
		}

		Block.DropEvent event = new Block.DropEvent(blockInstance);
		blockInstance.events.publish(event);

		return event.drops
			.stream()
			.map(ItemConverter.instance()::toNative)
			.collect(Collectors.toList());
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		// A block requires a TileEntity if it stores data or if it ticks.
		return Storable.class.isAssignableFrom(blockClass) ||
		       Stateful.class.isAssignableFrom(blockClass) ||
		       Updater.class.isAssignableFrom(blockClass);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		FWTile fwTile = FWTileLoader.loadTile(dummy.getID());
		fwTile.getBlock().components.getOrAdd(new TEBlockTransform(fwTile));
		if (!fwTile.getBlock().components.has(BlockProperty.BlockSound.class)) {
			BlockProperty.BlockSound properties = fwTile.getBlock().components.add(new BlockProperty.BlockSound());
			properties.setBlockSound(BlockProperty.BlockSound.BlockSoundTrigger.BREAK, new Sound(SoundType.STONE.getBreakSound().getSoundName().getResourceDomain(), SoundType.STONE.getBreakSound().getSoundName().getResourcePath()));
			properties.setBlockSound(BlockProperty.BlockSound.BlockSoundTrigger.PLACE, new Sound(SoundType.STONE.getPlaceSound().getSoundName().getResourceDomain(), SoundType.STONE.getPlaceSound().getSoundName().getResourcePath()));
			properties.setBlockSound(BlockProperty.BlockSound.BlockSoundTrigger.WALK, new Sound(SoundType.STONE.getStepSound().getSoundName().getResourceDomain(), SoundType.STONE.getStepSound().getSoundName().getResourcePath()));
		}
		return fwTile;
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		lastExtendedWorld = world;
		lastExtendedStatePos = pos;
		return super.getExtendedState(state, world, pos);
	}

	@Override
	@Deprecated
	public void neighborChanged(IBlockState state, World world, BlockPos pos, net.minecraft.block.Block other, BlockPos neighbor) {
		onNeighborChange(world, pos, neighbor);
	}

	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
		Block blockInstance = getBlockInstance(world, pos);
		Block.NeighborChangeEvent evt = new Block.NeighborChangeEvent(Optional.of(VectorConverter.instance().toNova(neighbor)));
		blockInstance.events.publish(evt);
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
		Block blockInstance = getBlockInstance(world, pos);
		Block.RemoveEvent evt = new Block.RemoveEvent(Optional.of(EntityConverter.instance().toNova(player)));
		blockInstance.events.publish(evt);
		if (evt.result) {
			return super.removedByPlayer(state, world, pos, player, willHarvest);
		}
		return false;
	}

	@Override
	public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {
		Block blockInstance = getBlockInstance(world, pos);
		Optional<RayTraceResult> mop = Optional.ofNullable(player.rayTrace(10, 1));
		Block.LeftClickEvent evt = new Block.LeftClickEvent(EntityConverter.instance().toNova(player),
			DirectionConverter.instance().toNova(mop.map(m -> m.sideHit).orElse(null)),
			mop.map(m -> m.hitVec).map(VectorConverter.instance()::toNova).orElseGet(() -> VectorConverter.instance().toNova(pos)));
		blockInstance.events.publish(evt);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		Block blockInstance = getBlockInstance(world, pos);
		Block.RightClickEvent evt = new Block.RightClickEvent(EntityConverter.instance().toNova(player), DirectionConverter.instance().toNova(side), new Vector3D(hitX, hitY, hitZ));
		blockInstance.events.publish(evt);
		return evt.result;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
		Block blockInstance = getBlockInstance(world, pos);
		blockInstance.components.getOp(Collider.class).ifPresent(collider -> blockInstance.events.publish(new Collider.CollideEvent(EntityConverter.instance().toNova(entity))));
	}

	@Override
	@Deprecated
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess access, BlockPos pos) {
		Block blockInstance = getBlockInstance(access, pos);
		if (blockInstance.components.has(Collider.class)) {
			Cuboid cuboid = blockInstance.components.get(Collider.class).boundingBox.get();
			return new AxisAlignedBB(cuboid.min.getX(), cuboid.min.getY(), cuboid.min.getZ(), cuboid.max.getX(), cuboid.max.getY(), cuboid.max.getZ());
		}
		return super.getBoundingBox(state, access, pos);
	}

	@Override
	@Deprecated
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World world, BlockPos pos) {
		Block blockInstance = getBlockInstance(world, pos);

		if (blockInstance.components.has(Collider.class)) {
			Collider collider = blockInstance.components.get(Collider.class);
			Set<Cuboid> cuboids = collider.selectionBoxes.apply(Optional.empty());
			Cuboid cuboid = cuboids.stream().reduce(collider.boundingBox.get(),
				(c1, c2) -> new Cuboid(Vector3DUtil.min(c1.min, c2.min), Vector3DUtil.max(c1.max, c2.max)));
			return CuboidConverter.instance().toNative(cuboid.add(VectorConverter.instance().toNova(pos)));
		}
		return super.getSelectedBoundingBox(state, world, pos);
	}

	@Override
	@Deprecated
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB boundingBox, List<AxisAlignedBB> list, Entity entity, boolean b) {
		Block blockInstance = getBlockInstance(world, pos);
		blockInstance.components.getOp(Collider.class).ifPresent(
			collider -> {
				Set<Cuboid> boxes = collider.occlusionBoxes.apply(Optional.ofNullable(entity != null ? EntityConverter.instance().toNova(entity) : null));

				list.addAll(
					boxes
						.stream()
						.map(c -> c.add(VectorConverter.instance().toNova(pos)))
						.filter(c -> c.intersects(CuboidConverter.instance().toNova(boundingBox)))
						.map(CuboidConverter.instance()::toNative)
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
		Optional<Collider> blockCollider = getBlockInstance(world, pos).components.getOp(Collider.class);

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
		Block blockInstance = getBlockInstance(access, pos);
		Optional<LightEmitter> opEmitter = blockInstance.components.getOp(LightEmitter.class);

		if (opEmitter.isPresent()) {
			return (int) MathUtil.clamp(Math.round(opEmitter.get().emittedLevel.getAsDouble() * 15), 0, 15);
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
		Block blockInstance = getBlockInstance(access, pos);
		WrapperEvent.RedstoneConnect event = new WrapperEvent.RedstoneConnect(blockInstance.world(), blockInstance.position(), Direction.fromOrdinal(side.ordinal()));
		Game.events().publish(event);
		return event.canConnect;
	}

	@Override
	@Deprecated
	public int getWeakPower(IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing side) {
		Block blockInstance = getBlockInstance(access, pos);
		WrapperEvent.WeakRedstone event = new WrapperEvent.WeakRedstone(blockInstance.world(), blockInstance.position(), Direction.fromOrdinal(side.ordinal()));
		Game.events().publish(event);
		return event.power;
	}

	@Override
	@Deprecated
	public int getStrongPower(IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing side) {
		Block blockInstance = getBlockInstance(access, pos);
		WrapperEvent.StrongRedstone event = new WrapperEvent.StrongRedstone(blockInstance.world(), blockInstance.position(), Direction.fromOrdinal(side.ordinal()));
		Game.events().publish(event);
		return event.power;
	}

	@Override
	public String getUnlocalizedName() {
		return factory.getUnlocalizedName();
	}

	@Override
	public String getLocalizedName() {
		return factory.getLocalizedName();
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
		// TODO: Maybe do something withPriority these parameters.

		// This number was calculated from the blast resistance of Stone,
		// which requires exactly one cubic meter of TNT to get blown up.
		//
		//   1. During construction, the setResistance method is called
		//     on minecraft:stone with a value of 10.
		//
		//   2. The setResistance method multiplies that by 3 and assigns
		//      the result to the blockResistance instance variable.
		//
		//   3. Finally, the getExplosionResistance method divides the
		//      blockResistance instance variable by 5 and returns the result.
		//
		// From this we see that minecraft:stone’s final blast resistance is 6.

		return (float) getBlockInstance(world, pos).getResistance() * 6;
	}

	@Override
	@Deprecated
	public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
		return (float) getBlockInstance(world, pos).getHardness() * 2;
	}

	@Override
	public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
		return getBlockInstance(world, pos)
			.components.getOp(BlockProperty.Opacity.class)
			.map(o -> MathUtil.clamp((int) Math.round(o.getOpacity() * 255), 0, 255))
			.orElse(this.fullBlock ? 255 : 0);
	}

	@Override
	public boolean isReplaceable(IBlockAccess world, BlockPos pos) {
		return getBlockInstance(world, pos)
			.components.getOp(BlockProperty.Replaceable.class)
			.filter(BlockProperty.Replaceable::isReplaceable)
			.isPresent();
	}
}
