package nova.core.wrapper.mc17.wrapper.block.forward;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
import nova.core.block.component.LightEmitter;
import nova.core.component.Updater;
import nova.core.component.misc.Collider;
import nova.core.component.renderer.ItemRenderer;
import nova.core.component.renderer.StaticRenderer;
import nova.core.retention.Storable;
import nova.core.util.Direction;
import nova.core.util.math.MatrixStack;
import nova.core.util.shape.Cuboid;
import nova.core.wrapper.mc17.util.WrapperEvent;
import nova.core.wrapper.mc17.wrapper.render.BWModel;
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
 *
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

	// TODO: Resolve unknown material issue
	public FWBlock(BlockFactory factory) {
		super(Material.piston);
		this.factory = factory;
		this.dummy = factory.build();
		this.blockClass = dummy.getClass();
		this.setBlockName(dummy.getID());

		// Recalculate super constructor things after loading the block properly
		this.opaque = isOpaqueCube();
		this.lightOpacity = isOpaqueCube() ? 255 : 0;

		if (FMLCommonHandler.instance().getSide().isClient()) {
			blockRenderingID = RenderingRegistry.getNextAvailableRenderId();
		}
	}

	public Block getBlockInstance(net.minecraft.world.IBlockAccess access, Vector3D position) {
		/**
		 * If this block has a TileEntity, forward the method into the Stateful
		 * block. Otherwise, create a new instance of the block and forward the
		 * methods over.
		 */
		if (hasTileEntity(0)) {
			FWTile tileWrapper = (FWTile) access.getTileEntity((int) position.getX(), (int) position.getY(), (int) position.getZ());
			if (tileWrapper != null && tileWrapper.getBlock() != null) {
				return tileWrapper.getBlock();
			}

			System.out.println("Error: Block in TileWrapper is null.");
		}
		return getBlockInstance((nova.core.world.World) Game.natives().toNova(access), position);

	}

	private Block getBlockInstance(nova.core.world.World world, Vector3D position) {
		Block block = factory.build();
		block.add(new MCBlockTransform(block, world, position));
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

		return new ArrayList<>(
			event.drops
				.stream()
				.map(item -> (ItemStack) Game.natives().toNative(item))
				.collect(Collectors.toCollection(ArrayList::new))
		);
	}

	@Override
	public boolean hasTileEntity(int metadata) {
		// A block requires a TileEntity if it stores data or if it ticks.
		return Storable.class.isAssignableFrom(blockClass) || Stateful.class.isAssignableFrom(blockClass) || Updater.class.isAssignableFrom(blockClass);
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return FWTileLoader.loadTile(dummy.getID());
	}

	@Override
	public IIcon getIcon(IBlockAccess access, int x, int y, int z, int side) {
		//TODO: Fill in something
		/*
		Block blockInstance = getBlockInstance(access, new Vector3D(x, y, z));
		Optional<StaticBlockRenderer> opRenderer = blockInstance.getOp(StaticBlockRenderer.class);
		if (opRenderer.isPresent()) {
			Optional<Texture> texture = opRenderer.get().texture.apply(Direction.values()[side]);
			if (texture.isPresent()) {
				return RenderUtility.instance.getIcon(texture.get());
			}
		}*/
		return null;
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		//TODO: Fill in something
		/*
		Optional<StaticBlockRenderer> opRenderer = block.getOp(StaticBlockRenderer.class);
		if (opRenderer.isPresent()) {
			Optional<Texture> texture = opRenderer.get().texture.apply(Direction.values()[side]);
			if (texture.isPresent()) {
				return RenderUtility.instance.getIcon(texture.get());
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
		Block.RemoveEvent evt = new Block.RemoveEvent(Optional.of(Game.natives().toNova(player)));
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
		Block.LeftClickEvent evt = new Block.LeftClickEvent(Game.natives().toNova(player), Direction.fromOrdinal(mop.sideHit), new Vector3D(mop.hitVec.xCoord, mop.hitVec.yCoord, mop.hitVec.zCoord));
		blockInstance.events.publish(evt);
	}

	@Override
	public void registerBlockIcons(IIconRegister ir) {

	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		Block blockInstance = getBlockInstance(world, new Vector3D(x, y, z));
		Block.RightClickEvent evt = new Block.RightClickEvent(Game.natives().toNova(player), Direction.fromOrdinal(side), new Vector3D(hitX, hitY, hitZ));
		blockInstance.events.publish(evt);
		return evt.result;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		Block blockInstance = getBlockInstance(world, new Vector3D(x, y, z));
		blockInstance.getOp(Collider.class).ifPresent(collider -> blockInstance.events.publish(new Collider.CollideEvent(Game.natives().toNova(entity))));
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess access, int x, int y, int z) {
		Block blockInstance = getBlockInstance(access, new Vector3D(x, y, z));
		if (blockInstance.has(Collider.class)) {
			Cuboid cuboid = blockInstance.get(Collider.class).boundingBox.get();
			setBlockBounds((float) cuboid.min.getX(), (float) cuboid.min.getY(), (float) cuboid.min.getZ(), (float) cuboid.max.getX(), (float) cuboid.max.getY(), (float) cuboid.max.getZ());
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
		Block blockInstance = getBlockInstance(world, new Vector3D(x, y, z));

		if (blockInstance.has(Collider.class)) {
			Cuboid cuboid = blockInstance.get(Collider.class).boundingBox.get();
			return Game.natives().toNative(cuboid.add(new Vector3D(x, y, z)));
		}
		return super.getSelectedBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity entity) {
		Block blockInstance = getBlockInstance(world, new Vector3D(x, y, z));
		blockInstance.getOp(Collider.class).ifPresent(
			collider -> {
				Set<Cuboid> boxes = collider.occlusionBoxes.apply(Optional.ofNullable(entity != null ? Game.natives().toNova(entity) : null));

				list.addAll(
					boxes
						.stream()
						.map(c -> c.add(new Vector3D(x, y, z)))
						.filter(c -> c.intersects((Cuboid) Game.natives().toNova(aabb)))
						.map(cuboid -> Game.natives().toNative(cuboid))
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

		Optional<Collider> blockCollider = dummy.getOp(Collider.class);

		if (blockCollider.isPresent()) {
			return blockCollider.get().isOpaqueCube.get();
		} else {
			return super.isOpaqueCube();
		}
	}

	@Override
	public boolean isNormalCube() {
		Optional<Collider> blockCollider = dummy.getOp(Collider.class);

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
		Optional<LightEmitter> opEmitter = blockInstance.getOp(LightEmitter.class);

		if (opEmitter.isPresent()) {
			return Math.round(opEmitter.get().emittedLevel.get() * 15.0F);
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
		return super.getUnlocalizedName().replaceFirst("tile", "block");
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
	public void renderInventoryBlock(net.minecraft.block.Block block, int metadata, int modelId, RenderBlocks renderer) {
		Optional<ItemRenderer> opRenderer = this.dummy.getOp(ItemRenderer.class);
		if (opRenderer.isPresent()) {
			GL11.glPushAttrib(GL_TEXTURE_BIT);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glPushMatrix();
			Tessellator.instance.startDrawingQuads();
			BWModel model = new BWModel();
			opRenderer.get().onRender.accept(model);
			model.render();
			Tessellator.instance.draw();
			GL11.glPopMatrix();
			GL11.glPopAttrib();
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, net.minecraft.block.Block block, int modelId, RenderBlocks renderer) {
		Block blockInstance = getBlockInstance(world, new Vector3D(x, y, z));
		Optional<StaticRenderer> opRenderer = blockInstance.getOp(StaticRenderer.class);
		if (opRenderer.isPresent()) {
			BWModel model = new BWModel();
			model.matrix = new MatrixStack().translate(x + 0.5, y + 0.5, z + 0.5);
			opRenderer.get().onRender.accept(model);
			model.renderWorld(world);

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
		return (float) getBlockInstance(world, new Vector3D(x, y, z)).getResistance() * 30;
	}

	@Override
	public float getBlockHardness(World world, int x, int y, int z) {
		return (float) getBlockInstance(world, new Vector3D(x, y, z)).getHardness() * 2;
	}
}
