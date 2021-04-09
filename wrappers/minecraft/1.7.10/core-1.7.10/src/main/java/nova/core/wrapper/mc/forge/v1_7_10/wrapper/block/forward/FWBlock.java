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

package nova.core.wrapper.mc.forge.v1_7_10.wrapper.block.forward;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import nova.core.block.Block;
import nova.core.block.BlockFactory;
import nova.core.block.Stateful;
import nova.core.block.component.BlockProperty;
import nova.core.block.component.LightEmitter;
import nova.core.component.Updater;
import nova.core.component.misc.Collider;
import nova.core.component.renderer.Renderer;
import nova.core.component.renderer.StaticRenderer;
import nova.core.retention.Storable;
import nova.core.sound.Sound;
import nova.core.util.Direction;
import nova.core.util.math.MathUtil;
import nova.core.util.math.MatrixStack;
import nova.core.util.shape.Cuboid;
import nova.core.wrapper.mc.forge.v1_7_10.util.WrapperEvent;
import nova.core.wrapper.mc.forge.v1_7_10.wrapper.block.world.WorldConverter;
import nova.core.wrapper.mc.forge.v1_7_10.wrapper.cuboid.CuboidConverter;
import nova.core.wrapper.mc.forge.v1_7_10.wrapper.entity.EntityConverter;
import nova.core.wrapper.mc.forge.v1_7_10.wrapper.item.ItemConverter;
import nova.core.wrapper.mc.forge.v1_7_10.wrapper.render.BWModel;
import nova.internal.core.Game;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_BIT;

/**
 * A Minecraft to Nova block wrapper
 * @author Calclavia
 */
public class FWBlock extends net.minecraft.block.Block implements ISimpleBlockRenderingHandler, IItemRenderer {
	public final Block dummy;
	/**
	 * Reference to the wrapper Nova block
	 */
	private final BlockFactory factory;
	private final Class<? extends Block> blockClass;

	@SideOnly(Side.CLIENT)
	private int blockRenderingID;

	private Map<BlockPosition, Block> harvestedBlocks = new HashMap<>();

	private static Material getMcMaterial(BlockFactory factory) {
		Block dummy = factory.build();
		if (dummy.components.has(BlockProperty.Opacity.class) || dummy.components.has(BlockProperty.Replaceable.class)) {
			// TODO allow color selection
			return new ProxyMaterial(MapColor.grayColor,
				dummy.components.getOp(BlockProperty.Opacity.class),
				dummy.components.getOp(BlockProperty.Replaceable.class));
		} else {
			return Material.piston;
		}
	}

	public FWBlock(BlockFactory factory) {
		//TODO: Hack build() method
		super(getMcMaterial(factory));
		this.factory = factory;
		this.dummy = factory.build();
		if (dummy.components.has(BlockProperty.BlockSound.class)) {
			this.stepSound = new FWBlockSound(dummy.components.get(BlockProperty.BlockSound.class));
		} else {
			BlockProperty.BlockSound properties = dummy.components.add(new BlockProperty.BlockSound());
			properties.setBlockSound(BlockProperty.BlockSound.BlockSoundTrigger.BREAK, new Sound("", soundTypeStone.getBreakSound()));
			properties.setBlockSound(BlockProperty.BlockSound.BlockSoundTrigger.PLACE, new Sound("", soundTypeStone.func_150496_b()));
			properties.setBlockSound(BlockProperty.BlockSound.BlockSoundTrigger.WALK, new Sound("", soundTypeStone.getStepResourcePath()));
			this.stepSound = soundTypeStone;
		}
		this.blockClass = dummy.getClass();

		// Recalculate super constructor things after loading the block properly
		this.opaque = isOpaqueCube();
		this.lightOpacity = isOpaqueCube() ? 255 : 0;

		if (FMLCommonHandler.instance().getSide().isClient()) {
			blockRenderingID = RenderingRegistry.getNextAvailableRenderId();
		}
	}

	public BlockFactory getFactory() {
		return this.factory;
	}

	public Block getBlockInstance(net.minecraft.world.IBlockAccess access, Vector3D position) {
		/**
		 * If this block has a TileEntity, forward the method into the Stateful
		 * block. Otherwise, create a new instance of the block and forward the
		 * methods over.
		 */
		if (hasTileEntity(0)) {
			try {
				FWTile tileWrapper = (FWTile) access.getTileEntity((int) position.getX(), (int) position.getY(), (int) position.getZ());
				if (tileWrapper != null && tileWrapper.getBlock() != null) {
					return tileWrapper.getBlock();
				}

				throw new IllegalStateException("Error: Block in TileWrapper is null for " + blockClass.getName());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return getBlockInstance(WorldConverter.instance().toNova(access), position);

	}

	private Block getBlockInstance(nova.core.world.World world, Vector3D position) {
		Block block = factory.build();
		block.components.add(new MCBlockTransform(block, world, position));
		if (!block.components.has(BlockProperty.BlockSound.class)) {
			BlockProperty.BlockSound properties = block.components.add(new BlockProperty.BlockSound());
			properties.setBlockSound(BlockProperty.BlockSound.BlockSoundTrigger.BREAK, new Sound("", soundTypeStone.getBreakSound()));
			properties.setBlockSound(BlockProperty.BlockSound.BlockSoundTrigger.PLACE, new Sound("", soundTypeStone.func_150496_b()));
			properties.setBlockSound(BlockProperty.BlockSound.BlockSoundTrigger.WALK, new Sound("", soundTypeStone.getStepResourcePath()));
			this.stepSound = soundTypeStone;
		}
		return block;
	}

	@Override
	public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer player) {
		// HACK: called before block is destroyed by the player prior to the
		// player getting the drops. Determine drops here.
		// hack is needed because the player sets the block to air *before*
		// getting the drops. woo good logic from mojang.
		if (!player.capabilities.isCreativeMode) {
			harvestedBlocks.put(new BlockPosition(world, x, y, z), getBlockInstance(world, new Vector3D(x, y, z)));
		}
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		Block blockInstance;

		// see onBlockHarvested for why the harvestedBlocks hack exists
		// this method will be called exactly once after destroying the block
		BlockPosition position = new BlockPosition(world, x, y, z);
		if (harvestedBlocks.containsKey(position)) {
			blockInstance = harvestedBlocks.remove(position);
		} else {
			blockInstance = getBlockInstance(world, new Vector3D(x, y, z));
		}

		Block.DropEvent event = new Block.DropEvent(blockInstance);
		blockInstance.events.publish(event);

		return event.drops
			.stream()
			.map(ItemConverter.instance()::toNative)
			.collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public boolean hasTileEntity(int metadata) {
		// A block requires a TileEntity if it stores data or if it ticks.
		return Storable.class.isAssignableFrom(blockClass)
			|| Stateful.class.isAssignableFrom(blockClass)
			|| Updater.class.isAssignableFrom(blockClass);
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		FWTile fwTile = FWTileLoader.loadTile(dummy.getID());
		fwTile.getBlock().components.getOrAdd(new TEBlockTransform(fwTile));
		if (!fwTile.block.components.has(BlockProperty.BlockSound.class)) {
			BlockProperty.BlockSound properties = fwTile.block.components.add(new BlockProperty.BlockSound());
			properties.setBlockSound(BlockProperty.BlockSound.BlockSoundTrigger.BREAK, new Sound("", soundTypeStone.getBreakSound()));
			properties.setBlockSound(BlockProperty.BlockSound.BlockSoundTrigger.PLACE, new Sound("", soundTypeStone.func_150496_b()));
			properties.setBlockSound(BlockProperty.BlockSound.BlockSoundTrigger.WALK, new Sound("", soundTypeStone.getStepResourcePath()));
			this.stepSound = soundTypeStone;
		}
		return fwTile;
	}

	@Override
	public IIcon getIcon(IBlockAccess access, int x, int y, int z, int side) {
		//TODO: Fill in something
		/*
		Block blockInstance = getBlockInstance(access, new Vector3D(x, y, z));
		Optional<StaticBlockRenderer> opRenderer = blockInstance.components.getOp(StaticBlockRenderer.class);
		if (opRenderer.isPresent()) {
			Optional<Texture> texture = opRenderer.components.get().texture.apply(Direction.values()[side]);
			if (texture.isPresent()) {
				return RenderUtility.instance.getIcon(texture.components.get());
			}
		}*/
		return null;
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		//TODO: Fill in something
		/*
		Optional<StaticBlockRenderer> opRenderer = block.components.getOp(StaticBlockRenderer.class);
		if (opRenderer.isPresent()) {
			Optional<Texture> texture = opRenderer.components.get().texture.apply(Direction.values()[side]);
			if (texture.isPresent()) {
				return RenderUtility.instance.getIcon(texture.components.get());
			}
		}*/
		return null;
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, net.minecraft.block.Block otherBlock) {
		Block blockInstance = getBlockInstance(world, new Vector3D(x, y, z));
		// Minecraft does not provide the neighbor :(
		Block.NeighborChangeEvent evt = new Block.NeighborChangeEvent(Optional.empty());
		blockInstance.events.publish(evt);
	}

	@Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest) {
		Block blockInstance = getBlockInstance(world, new Vector3D(x, y, z));
		Block.RemoveEvent evt = new Block.RemoveEvent(Optional.of(EntityConverter.instance().toNova(player)));
		blockInstance.events.publish(evt);
		if (evt.result) {
			return super.removedByPlayer(world, player, x, y, z, willHarvest);
		}
		return false;
	}

	@Override
	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
		Block blockInstance = getBlockInstance(world, new Vector3D(x, y, z));
		MovingObjectPosition mop = player.rayTrace(10, 1);
		Block.LeftClickEvent evt = new Block.LeftClickEvent(EntityConverter.instance().toNova(player), Direction.fromOrdinal(mop.sideHit), new Vector3D(mop.hitVec.xCoord, mop.hitVec.yCoord, mop.hitVec.zCoord));
		blockInstance.events.publish(evt);
	}

	@Override
	public void registerBlockIcons(IIconRegister ir) {

	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		Block blockInstance = getBlockInstance(world, new Vector3D(x, y, z));
		Block.RightClickEvent evt = new Block.RightClickEvent(EntityConverter.instance().toNova(player), Direction.fromOrdinal(side), new Vector3D(hitX, hitY, hitZ));
		blockInstance.events.publish(evt);
		return evt.result;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		Block blockInstance = getBlockInstance(world, new Vector3D(x, y, z));
		blockInstance.components.getOp(Collider.class).ifPresent(collider -> blockInstance.events.publish(new Collider.CollideEvent(EntityConverter.instance().toNova(entity))));
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess access, int x, int y, int z) {
		Block blockInstance = getBlockInstance(access, new Vector3D(x, y, z));
		if (blockInstance.components.has(Collider.class)) {
			Cuboid cuboid = blockInstance.components.get(Collider.class).boundingBox.get();
			setBlockBounds((float) cuboid.min.getX(), (float) cuboid.min.getY(), (float) cuboid.min.getZ(), (float) cuboid.max.getX(), (float) cuboid.max.getY(), (float) cuboid.max.getZ());
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
		Block blockInstance = getBlockInstance(world, new Vector3D(x, y, z));

		if (blockInstance.components.has(Collider.class)) {
			Cuboid cuboid = blockInstance.components.get(Collider.class).boundingBox.get();
			return CuboidConverter.instance().toNative(cuboid.add(new Vector3D(x, y, z)));
		}
		return super.getSelectedBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity entity) {
		Block blockInstance = getBlockInstance(world, new Vector3D(x, y, z));
		blockInstance.components.getOp(Collider.class).ifPresent(
			collider -> {
				Set<Cuboid> boxes = collider.occlusionBoxes.apply(Optional.ofNullable(entity != null ? EntityConverter.instance().toNova(entity) : null));

				list.addAll(
					boxes
						.stream()
						.map(c -> c.add(new Vector3D(x, y, z)))
						.filter(c -> c.intersects(CuboidConverter.instance().toNova(aabb)))
						.map(CuboidConverter.instance()::toNative)
						.collect(Collectors.toList())
				);
			}
		);
	}

	@Override
	public boolean isOpaqueCube() {
		if (dummy == null) {
			// Superconstructor fix. -10 style points.
			return true;
		}

		Optional<Collider> blockCollider = dummy.components.getOp(Collider.class);

		if (blockCollider.isPresent()) {
			return blockCollider.get().isOpaqueCube.get();
		} else {
			return super.isOpaqueCube();
		}
	}

	@Override
	public boolean isNormalCube() {
		Optional<Collider> blockCollider = dummy.components.getOp(Collider.class);

		if (blockCollider.isPresent()) {
			return blockCollider.get().isCube.get();
		} else {
			return super.isNormalCube();
		}
	}

	@Override
	public boolean renderAsNormalBlock() {
		return isNormalCube();
	}

	@Override
	public int getLightValue(IBlockAccess access, int x, int y, int z) {
		Block blockInstance = getBlockInstance(access, new Vector3D(x, y, z));
		Optional<LightEmitter> opEmitter = blockInstance.components.getOp(LightEmitter.class);

		if (opEmitter.isPresent()) {
			return (int) MathUtil.clamp(Math.round(opEmitter.get().emittedLevel.getAsDouble() * 15), 0, 15);
		} else {
			return 0;
		}
	}

	@Override
	public boolean canConnectRedstone(IBlockAccess access, int x, int y, int z, int side) {
		Block blockInstance = getBlockInstance(access, new Vector3D(x, y, z));
		WrapperEvent.RedstoneConnect event = new WrapperEvent.RedstoneConnect(blockInstance.world(), blockInstance.position(), Direction.fromOrdinal(side));
		Game.events().publish(event);
		return event.canConnect;
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess access, int x, int y, int z, int side) {
		Block blockInstance = getBlockInstance(access, new Vector3D(x, y, z));
		WrapperEvent.WeakRedstone event = new WrapperEvent.WeakRedstone(blockInstance.world(), blockInstance.position(), Direction.fromOrdinal(side));
		Game.events().publish(event);
		return event.power;
	}

	@Override
	public int isProvidingStrongPower(IBlockAccess access, int x, int y, int z, int side) {
		Block blockInstance = getBlockInstance(access, new Vector3D(x, y, z));
		WrapperEvent.StrongRedstone event = new WrapperEvent.StrongRedstone(blockInstance.world(), blockInstance.position(), Direction.fromOrdinal(side));
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

	/**
	 * Rendering forwarding
	 */
	@Override
	public int getRenderType() {
		return blockRenderingID;
	}

	@Override
	public int getRenderBlockPass() {
		return 1;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderInventoryBlock(net.minecraft.block.Block block, int metadata, int modelId, RenderBlocks renderBlocks) {
		if (this.dummy.components.has(Renderer.class)) {
			GL11.glPushAttrib(GL_TEXTURE_BIT);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glPushMatrix();
			Tessellator.instance.startDrawingQuads();
			BWModel model = new BWModel();
			this.dummy.components.getSet(Renderer.class).forEach(renderer -> renderer.onRender.accept(model));
			model.render();
			Tessellator.instance.draw();
			GL11.glPopMatrix();
			GL11.glPopAttrib();
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, net.minecraft.block.Block block, int modelId, RenderBlocks renderBlocks) {
		Block blockInstance = getBlockInstance(world, new Vector3D(x, y, z));
		Optional<StaticRenderer> staticRenderer = blockInstance.components.getOp(StaticRenderer.class);
		if (staticRenderer.isPresent()) {
			BWModel model = new BWModel();
			model.matrix = new MatrixStack().translate(x + 0.5, y + 0.5, z + 0.5);
			staticRenderer.get().onRender.accept(model);
			model.render(world);

			return Tessellator.instance.rawBufferIndex != 0; // Returns true if Tesselator is not empty. Avoids crash on empty Tesselator buffer.
		}
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getRenderId() {
		return blockRenderingID;
	}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		// TODO: Use this
	}

	@Override
	public float getExplosionResistance(Entity expEntity, World world, int x, int y, int z, double explosionX, double p_explosionresistance, double explosionY) {
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

		return (float) getBlockInstance(world, new Vector3D(x, y, z)).getResistance() * 6;
	}

	@Override
	public float getBlockHardness(World world, int x, int y, int z) {
		return (float) getBlockInstance(world, new Vector3D(x, y, z)).getHardness() * 2;
	}

	@Override
	public boolean isReplaceable(IBlockAccess access, int x, int y, int z) {
		return getBlockInstance(access, new Vector3D(x, y, z))
			.components.getOp(BlockProperty.Replaceable.class)
			.filter(BlockProperty.Replaceable::isReplaceable)
			.isPresent();
	}
}
