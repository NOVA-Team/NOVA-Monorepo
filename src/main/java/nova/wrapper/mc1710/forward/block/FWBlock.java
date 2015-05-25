package nova.wrapper.mc1710.forward.block;

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
import net.minecraft.entity.EntityLivingBase;
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
import nova.core.component.Updater;
import nova.core.event.EventManager;
import nova.core.game.Game;
import nova.core.render.texture.Texture;
import nova.core.retention.Storable;
import nova.core.util.Direction;
import nova.core.util.transform.matrix.MatrixStack;
import nova.core.util.transform.shape.Cuboid;
import nova.core.util.transform.vector.Vector3d;
import nova.core.util.transform.vector.Vector3i;
import nova.wrapper.mc1710.backward.BackwardProxyUtil;
import nova.wrapper.mc1710.backward.render.BWModel;
import nova.wrapper.mc1710.backward.util.BWCuboid;
import nova.wrapper.mc1710.backward.world.BWWorld;
import nova.wrapper.mc1710.forward.util.FWCuboid;
import nova.wrapper.mc1710.item.ItemWrapperRegistry;
import nova.wrapper.mc1710.render.RenderUtility;
import nova.wrapper.mc1710.util.WrapperEventManager;
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
	public final Block block;
	/**
	 * Reference to the wrapper Nova block
	 */
	private final BlockFactory factory;
	private final Class<? extends Block> blockClass;

	@SideOnly(Side.CLIENT)
	private int blockRenderingID;

	private Map<BlockPosition, Block> harvestedBlocks = new HashMap<>();

	//TODO: Resolve unknown material issue
	public FWBlock(BlockFactory factory) {
		super(Material.piston);
		this.factory = factory;
		this.block = factory.getDummy();
		this.blockClass = block.getClass();
		this.setBlockName(block.getID());

		// Recalculate super constructor things after loading the block properly
		this.opaque = isOpaqueCube();
		this.lightOpacity = isOpaqueCube() ? 255 : 0;

		if (FMLCommonHandler.instance().getSide().isClient()) {
			blockRenderingID = RenderingRegistry.getNextAvailableRenderId();
		}
	}

	public Block getBlockInstance(net.minecraft.world.IBlockAccess access, Vector3i position) {
		/**
		 * If this block has a TileEntity, forward the method into the Stateful block.
		 * Otherwise, create a new instance of the block and forward the methods over.
		 */
		if (hasTileEntity(0)) {
			FWTile tileWrapper = ((FWTile) access.getTileEntity(position.x, position.y, position.z));
			if (tileWrapper != null && tileWrapper.getBlock() != null) {
				return ((FWTile) access.getTileEntity(position.x, position.y, position.z)).getBlock();
			}

			System.out.println("Error: Block in TileWrapper is null.");
		}
		return getBlockInstance(new BWWorld(access), position);

	}

	public Block getBlockInstance(nova.core.world.World world, Vector3i position) {
		//TODO: Implement obj args
		return factory.makeBlock(new MCBlockWrapper(world, position));
	}

	@Override
	public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer player) {
		// HACK: called before block is destroyed by the player prior to the player getting the drops. Determine drops here.
		// hack is needed because the player sets the block to air *before* getting the drops. woo good logic from mojang.
		if (!player.capabilities.isCreativeMode) {
			harvestedBlocks.put(new BlockPosition(world, x, y, z), getBlockInstance(world, new Vector3i(x, y, z)));
		}
	}

	@Override
	public boolean hasTileEntity(int metadata) {
		//A block requires a TileEntity if it stores data or if it ticks.
		return Storable.class.isAssignableFrom(blockClass) || Stateful.class.isAssignableFrom(blockClass) || Updater.class.isAssignableFrom(blockClass);
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new FWTile(factory.getID());
	}

	@Override
	public IIcon getIcon(IBlockAccess access, int x, int y, int z, int side) {
		Optional<Texture> texture = getBlockInstance(access, new Vector3i(x, y, z)).getTexture(Direction.values()[side]);
		if (texture.isPresent()) {
			return RenderUtility.instance.getIcon(texture.get());
		}
		return null;
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		Optional<Texture> texture = block.getTexture(Direction.values()[side]);
		if (texture.isPresent()) {
			return RenderUtility.instance.getIcon(texture.get());
		}
		return null;
	}

	@Override
	public void onNeighborBlockChange(World access, int x, int y, int z, net.minecraft.block.Block otherBlock) {
		//TODO: This method seems to only be invoked when a TileEntity changes, not when blocks change!
		Block blockInstance = getBlockInstance(access, new Vector3i(x, y, z));
		EventManager.BlockNeighborChangeEvent event = new EventManager.BlockNeighborChangeEvent(blockInstance.world(), blockInstance.position(), Optional.empty());
		Game.instance.eventManager.blockNeighborChange.publish(event);
		if (!event.isCanceled()) {
			//TODO: This technically shouldn't be null!
			blockInstance.onNeighborChange(null);
		}
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
		getBlockInstance(world, new Vector3i(x, y, z)).onPlaced(BackwardProxyUtil.getEntityWrapper(entity));
		//TODO: Should we consider onBlockPlaced also?
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, net.minecraft.block.Block block, int i) {
		getBlockInstance(world, new Vector3i(x, y, z)).onRemoved(Optional.empty());
		super.breakBlock(world, x, y, z, block, i);
	}

	@Override
	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
		//TODO: Check this raytrace.
		MovingObjectPosition mop = player.rayTrace(10, 1);
		getBlockInstance(world, new Vector3i(x, y, z)).onLeftClick(BackwardProxyUtil.getEntityWrapper(player), mop.sideHit, new Vector3d(mop.hitVec.xCoord, mop.hitVec.yCoord, mop.hitVec.zCoord));
	}

	@Override
	public void registerBlockIcons(IIconRegister ir) {

	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		return getBlockInstance(world, new Vector3i(x, y, z)).onRightClick(BackwardProxyUtil.getEntityWrapper(player), side, new Vector3d(hitX, hitY, hitZ));
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		getBlockInstance(world, new Vector3i(x, y, z)).onEntityCollide(BackwardProxyUtil.getEntityWrapper(entity));
	}

	@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity entity) {
		Set<Cuboid> boxes = getBlockInstance(world, new Vector3i(x, y, z))
			.getCollidingBoxes(new BWCuboid(aabb), entity != null ? Optional.of(BackwardProxyUtil.getEntityWrapper(entity)) : Optional.empty());

		list.addAll(
			boxes
				.stream()
				.map(c -> c.add(new Vector3i(x, y, z)))
				.map(FWCuboid::new)
				.collect(Collectors.toList())
		);
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		Block block;

		// see onBlockHarvested for why the harvestedBlocks hack exists
		// this method will be called exactly once after destroying the block
		BlockPosition position = new BlockPosition(world, x, y, z);
		if (harvestedBlocks.containsKey(position)) {
			block = harvestedBlocks.remove(position);
		} else {
			block = getBlockInstance(world, new Vector3i(x, y, z));
		}

		return block.getDrops()
					.stream()
					.map(ItemWrapperRegistry.instance::getMCItemStack)
					.collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public boolean isOpaqueCube() {
		if (block == null) {
			// Superconstructor fix. -10 style points.
			return true;
		}
		return block.isOpaqueCube();
	}

	@Override
	public boolean isNormalCube() {
		return block.isCube();
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
		return new FWCuboid(getBlockInstance(world, new Vector3i(x, y, z)).getBoundingBox().add(new Vector3i(x, y, z)));
	}

	@Override
	public int getLightValue(IBlockAccess access, int x, int y, int z) {
		if (block instanceof LightEmitter) {
			return Math.round(((LightEmitter) getBlockInstance(access, new Vector3i(x, y, z))).getEmittedLightLevel() * 15.0F);
		} else {
			return 0;
		}
	}

	@Override
	public boolean canConnectRedstone(IBlockAccess access, int x, int y, int z, int side) {
		Block blockInstance = getBlockInstance(access, new Vector3i(x, y, z));
		WrapperEventManager.RedstoneConnectEvent event = new WrapperEventManager.RedstoneConnectEvent(blockInstance.world(), blockInstance.position(), Direction.fromOrdinal(side));
		WrapperEventManager.instance.onCanConnect.publish(event);
		return event.canConnect;
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess access, int x, int y, int z, int side) {
		Block blockInstance = getBlockInstance(access, new Vector3i(x, y, z));
		WrapperEventManager.RedstoneEvent event = new WrapperEventManager.RedstoneEvent(blockInstance.world(), blockInstance.position(), Direction.fromOrdinal(side));
		WrapperEventManager.instance.onWeakPower.publish(event);
		return event.power;
	}

	@Override
	public int isProvidingStrongPower(IBlockAccess access, int x, int y, int z, int side) {
		Block blockInstance = getBlockInstance(access, new Vector3i(x, y, z));
		WrapperEventManager.RedstoneEvent event = new WrapperEventManager.RedstoneEvent(blockInstance.world(), blockInstance.position(), Direction.fromOrdinal(side));
		WrapperEventManager.instance.onStrongPower.publish(event);
		return event.power;
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
		if (this.block instanceof ItemRenderer) {
			GL11.glPushAttrib(GL_TEXTURE_BIT);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glPushMatrix();
			Tessellator.instance.startDrawingQuads();
			BWModel model = new BWModel();
			((ItemRenderer) this.block).renderItem(model);
			model.render();
			Tessellator.instance.draw();
			GL11.glPopMatrix();
			GL11.glPopAttrib();
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, net.minecraft.block.Block block, int modelId, RenderBlocks renderer) {
		Block blockInstance = getBlockInstance(world, new Vector3i(x, y, z));
		if (blockInstance instanceof StaticRenderer) {
			BWModel model = new BWModel();
			model.matrix = new MatrixStack().translate(x + 0.5, y + 0.5, z + 0.5).getMatrix();
			((StaticRenderer) blockInstance).renderStatic(model);
			model.renderWorld(world);
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
		//TODO: Use this
	}

	@Override
	public float getExplosionResistance(Entity expEntity, World world, int x, int y, int z, double explosionX, double p_explosionresistance, double explosionY) {
		//TODO: Maybe do something with these parameters.
		return (float) getBlockInstance(world, new Vector3i(x, y, z)).getResistance() * 30;
	}

	@Override
	public float getBlockHardness(World world, int x, int y, int z) {
		return (float) getBlockInstance(world, new Vector3i(x, y, z)).getHardness() * 2;
	}
}
