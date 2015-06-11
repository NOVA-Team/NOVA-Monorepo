package nova.wrapper.mc18.wrapper.block.forward;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nova.core.block.Block;
import nova.core.block.BlockFactory;
import nova.core.block.Stateful;
import nova.core.block.component.LightEmitter;
import nova.core.block.component.StaticBlockRenderer;
import nova.core.component.Updater;
import nova.core.component.misc.Collider;
import nova.core.component.renderer.ItemRenderer;
import nova.core.component.renderer.StaticRenderer;
import nova.core.render.texture.Texture;
import nova.core.retention.Storable;
import nova.core.util.Direction;
import nova.core.util.math.MatrixStack;
import nova.core.util.shape.Cuboid;
import nova.internal.core.Game;
import nova.wrapper.mc18.launcher.NovaMinecraft;
import nova.wrapper.mc18.render.RenderUtility;
import nova.wrapper.mc18.util.WrapperEventManager;
import nova.wrapper.mc18.wrapper.block.world.BWWorld;
import nova.wrapper.mc18.wrapper.render.BWModel;
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
public class FWBlock extends net.minecraft.block.Block {
	public final Block block;
	/**
	 * Reference to the wrapper Nova block
	 */
	private final BlockFactory factory;
	private final Class<? extends Block> blockClass;

	@SideOnly(Side.CLIENT)
	private int blockRenderingID;

	private Map<BlockPosition, Block> harvestedBlocks = new HashMap<>();

	public FWBlock(BlockFactory factory) {
		super(Material.piston);
		this.factory = factory;
		this.block = factory.getDummy();
		this.blockClass = block.getClass();
		this.setUnlocalizedName(block.getID());

		// Recalculate super constructor things after loading the block properly
		this.fullBlock = isOpaqueCube();
		this.lightOpacity = isOpaqueCube() ? 255 : 0;

		if (FMLCommonHandler.instance().getSide().isClient()) {
			blockRenderingID = RenderingRegistry.getNextAvailableRenderId();
		}
	}

	public Block getBlockInstance(IBlockAccess access, Vector3D position) {
		/**
		 * If this block has a TileEntity, forward the method into the Stateful
		 * block. Otherwise, create a new instance of the block and forward the
		 * methods over.
		 */
		if (hasTileEntity(0)) {
			FWTile tileWrapper = (FWTile) access.getTileEntity(new BlockPos((int) position.getX(), (int) position.getY(), (int) position.getZ()));
			if (tileWrapper != null && tileWrapper.getBlock() != null) {
				return ((FWTile) access.getTileEntity(new BlockPos((int) position.getX(), (int) position.getY(), (int) position.getZ()))).getBlock();
			}

			System.out.println("Error: Block in TileWrapper is null.");
		}
		return getBlockInstance(new BWWorld(access), position);

	}

	private Block getBlockInstance(nova.core.world.World world, Vector3D position) {
		// TODO: Implement obj args
		Block block = factory.makeBlock();
		block.add(new MCBlockTransform(block, world, position));
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
	public TileEntity createTileEntity(World world, int metadata) {
		return FWTileLoader.loadTile(block.getID());
	}

	@Override
	public IIcon getIcon(IBlockAccess access, int x, int y, int z, int side) {
		Block blockInstance = getBlockInstance(access, new Vector3D(pos.getX(), pos.getY(), pos.getZ()));
		Optional<StaticBlockRenderer> opRenderer = blockInstance.getOp(StaticBlockRenderer.class);
		if (opRenderer.isPresent()) {
			Optional<Texture> texture = opRenderer.get().texture.apply(Direction.values()[side]);
			if (texture.isPresent()) {
				return RenderUtility.instance.getTexture(texture.get());
			}
		}
		return null;
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		Optional<StaticBlockRenderer> opRenderer = block.getOp(StaticBlockRenderer.class);
		if (opRenderer.isPresent()) {
			Optional<Texture> texture = opRenderer.get().texture.apply(Direction.values()[side]);
			if (texture.isPresent()) {
				return RenderUtility.instance.getTexture(texture.get());
			}
		}
		return null;
	}

	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, net.minecraft.block.Block neighborBlock) {
		Block blockInstance = getBlockInstance(world, new Vector3D(pos.getX(), pos.getY(), pos.getZ()));
		// Minecraft does not provide the neighbor :(
		Block.NeighborChangeEvent evt = new Block.NeighborChangeEvent(Optional.empty());
		blockInstance.events.publish(evt);
	}

	@Override
	public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
		Block blockInstance = getBlockInstance(world, new Vector3D(pos.getX(), pos.getY(), pos.getZ()));
		Block.RemoveEvent evt = new Block.RemoveEvent(Optional.of(Game.natives().toNova(player)));
		blockInstance.events.publish(evt);
		if (evt.result) {
			return super.removedByPlayer(world, pos, player, willHarvest);
		}
		return false;
	}

	@Override
	public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {
		Block blockInstance = getBlockInstance(world, new Vector3D(pos.getX(), pos.getY(), pos.getZ()));
		MovingObjectPosition mop = player.rayTrace(10, 1);
		Block.LeftClickEvent evt = new Block.LeftClickEvent(Game.natives().toNova(player), Direction.fromOrdinal(mop.field_178784_b.ordinal()), new Vector3D(mop.hitVec.xCoord, mop.hitVec.yCoord, mop.hitVec.zCoord));
		blockInstance.events.publish(evt);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
		Block blockInstance = getBlockInstance(world, new Vector3D(pos.getX(), pos.getY(), pos.getZ()));
		Block.RightClickEvent evt = new Block.RightClickEvent(Game.natives().toNova(player), Direction.fromOrdinal(side.ordinal()), new Vector3D(hitX, hitY, hitZ));
		blockInstance.events.publish(evt);
		return evt.result;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, Entity entity) {
		Block blockInstance = getBlockInstance(world, new Vector3D(pos.getX(), pos.getY(), pos.getZ()));
		blockInstance.getOp(Collider.class).ifPresent(collider -> collider.collideEvent.publish(new Collider.CollideEvent(Game.natives().toNova(entity))));
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
		Block blockInstance = getBlockInstance(access, new Vector3D(pos.getX(), pos.getY(), pos.getZ()));
		if (blockInstance.has(Collider.class)) {
			Cuboid cuboid = blockInstance.get(Collider.class).boundingBox.get();
			setBlockBounds((float) cuboid.min.getX(), (float) cuboid.min.getY(), (float) cuboid.min.getZ(), (float) cuboid.max.getX(), (float) cuboid.max.getY(), (float) cuboid.max.getZ());
		}
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBox(World world, BlockPos pos) {
		Block blockInstance = getBlockInstance(world, new Vector3D(pos.getX(), pos.getY(), pos.getZ()));

		if (blockInstance.has(Collider.class)) {
			Cuboid cuboid = blockInstance.get(Collider.class).boundingBox.get();
			return Game.natives().toNative(cuboid.add(new Vector3D(pos.getX(), pos.getY(), pos.getZ())));
		}
		return super.getSelectedBoundingBox(world, pos);
	}

	@Override
	public void addCollisionBoxesToList(World world, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity entity) {
		Block blockInstance = getBlockInstance(world, new Vector3D(pos.getX(), pos.getY(), pos.getZ()));
		blockInstance.getOp(Collider.class).ifPresent(
			collider -> {
				Set<Cuboid> boxes = collider.occlusionBoxes.apply(Optional.ofNullable(entity != null ? Game.natives().toNova(entity) : null));

				list.addAll(
					boxes
						.stream()
						.map(c -> c.add(new Vector3D(pos.getX(), pos.getY(), pos.getZ())))
						.filter(c -> c.intersects((Cuboid) Game.natives().toNova(mask)))
						.map(cuboid -> Game.natives().toNative(cuboid))
						.collect(Collectors.toList())
				);
			}
		);
	}

	@Override
	public boolean isOpaqueCube() {
		if (block == null) {
			// Superconstructor fix. -10 style points.
			return true;
		}

		Optional<Collider> blockCollider = block.getOp(Collider.class);

		if (blockCollider.isPresent()) {
			return blockCollider.get().isOpaqueCube.get();
		} else {
			return super.isOpaqueCube();
		}
	}

	@Override
	public boolean isNormalCube() {
		Optional<Collider> blockCollider = block.getOp(Collider.class);

		if (blockCollider.isPresent()) {
			return blockCollider.get().isCube.get();
		} else {
			return super.isNormalCube();
		}
	}

	@Override
	public int getLightValue(IBlockAccess access, BlockPos pos) {
		Block blockInstance = getBlockInstance(access, new Vector3D(pos.getX(), pos.getY(), pos.getZ()));
		Optional<LightEmitter> opEmitter = blockInstance.getOp(LightEmitter.class);

		if (opEmitter.isPresent()) {
			return Math.round(opEmitter.get().emittedLevel.get() * 15.0F);
		} else {
			return 0;
		}
	}

	@Override
	public boolean canConnectRedstone(IBlockAccess access, BlockPos pos, EnumFacing side) {
		Block blockInstance = getBlockInstance(access, new Vector3D(pos.getX(), pos.getY(), pos.getZ()));
		WrapperEventManager.RedstoneConnectEvent event = new WrapperEventManager.RedstoneConnectEvent(blockInstance.world(), blockInstance.position(), Direction.fromOrdinal(side.ordinal()));
		NovaMinecraft.eventManager.onCanConnect.publish(event);
		return event.canConnect;
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess access, BlockPos pos, IBlockState state, EnumFacing side) {
		Block blockInstance = getBlockInstance(access, new Vector3D(pos.getX(), pos.getY(), pos.getZ()));
		WrapperEventManager.RedstoneEvent event = new WrapperEventManager.RedstoneEvent(blockInstance.world(), blockInstance.position(), Direction.fromOrdinal(side.ordinal()));
		NovaMinecraft.eventManager.onWeakPower.publish(event);
		return event.power;
	}

	@Override
	public int isProvidingStrongPower(IBlockAccess access, BlockPos pos, IBlockState state, EnumFacing side) {
		Block blockInstance = getBlockInstance(access, new Vector3D(pos.getX(), pos.getY(), pos.getZ()));
		WrapperEventManager.RedstoneEvent event = new WrapperEventManager.RedstoneEvent(blockInstance.world(), blockInstance.position(), Direction.fromOrdinal(side.ordinal()));
		NovaMinecraft.eventManager.onStrongPower.publish(event);
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

	@SideOnly(Side.CLIENT)
	@Override
	public void renderInventoryBlock(net.minecraft.block.Block block, int metadata, int modelId, RenderBlocks renderer) {
		Optional<ItemRenderer> opRenderer = this.block.getOp(ItemRenderer.class);
		if (opRenderer.isPresent()) {
			GL11.glPushAttrib(GL_TEXTURE_BIT);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glPushMatrix();
			Tessellator.getInstance().startDrawingQuads();
			BWModel model = new BWModel();
			opRenderer.get().onRender.accept(model);
			model.render();
			Tessellator.getInstance().draw();
			GL11.glPopMatrix();
			GL11.glPopAttrib();
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, net.minecraft.block.Block block, int modelId, RenderBlocks renderer) {
		Block blockInstance = getBlockInstance(world, new Vector3D(pos.getX(), pos.getY(), pos.getZ()));
		Optional<StaticRenderer> opRenderer = blockInstance.getOp(StaticRenderer.class);
		if (opRenderer.isPresent()) {
			BWModel model = new BWModel();
			model.matrix = new MatrixStack().translate(x + 0.5, y + 0.5, z + 0.5);
			opRenderer.get().onRender.accept(model);
			model.renderWorld(world);

			return Tessellator.getInstance().rawBufferIndex != 0; // Returns true if Tesselator is not empty. Avoids crash on empty Tesselator buffer.
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
		// TODO: Maybe do something with these parameters.
		return (float) getBlockInstance(world, new Vector3D(pos.getX(), pos.getY(), pos.getZ())).getResistance() * 30;
	}

	@Override
	public float getBlockHardness(World world, int x, int y, int z) {
		return (float) getBlockInstance(world, new Vector3D(x, y, z)).getHardness() * 2;
	}
}
