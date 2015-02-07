package nova.wrapper.mc1710.forward.block;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_BIT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
import nova.core.block.BlockChanger;
import nova.core.block.BlockFactory;
import nova.core.block.components.LightEmitter;
import nova.core.block.components.Stateful;
import nova.core.render.texture.Texture;
import nova.core.util.Direction;
import nova.core.util.components.Storable;
import nova.core.util.components.Updater;
import nova.core.util.transform.Cuboid;
import nova.core.util.transform.Vector3d;
import nova.core.util.transform.Vector3i;
import nova.wrapper.mc1710.backward.BackwardProxyUtil;
import nova.wrapper.mc1710.backward.render.ModelWrapper;
import nova.wrapper.mc1710.backward.util.BWCuboid;
import nova.wrapper.mc1710.backward.world.BWBlockAccess;
import nova.wrapper.mc1710.forward.util.CuboidForwardWrapper;
import nova.wrapper.mc1710.render.RenderUtility;
import nova.wrapper.mc1710.util.WrapUtility;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * A Minecraft to Nova block wrapper
 * @author Calclavia
 */
public class BlockWrapper extends net.minecraft.block.Block implements ISimpleBlockRenderingHandler, IItemRenderer {
	public final Block block;
	/**
	 * Reference to the wrapped Nova block
	 */
	private final BlockFactory factory;
	private final Class<? extends Block> blockClass;
	@SideOnly(Side.CLIENT)
	private final int blockRenderingID = RenderingRegistry.getNextAvailableRenderId();

	private Map<BlockPosition, Block> harvestedBlocks = new HashMap<>();

	//TODO: Resolve unknown material issue
	public BlockWrapper(BlockFactory factory) {
		super(Material.piston);
		this.factory = factory;
		this.block = factory.getDummy();
		this.blockClass = block.getClass();
		this.setBlockName(block.getID());

		// Recalculate super constructor things after loading the block properly
		this.opaque = isOpaqueCube();
		this.lightOpacity = isOpaqueCube() ? 255 : 0;
	}

	public Block getBlockInstance(net.minecraft.world.IBlockAccess access, Vector3i position) {
		/**
		 * If this block has a TileEntity, forward the method into the Stateful block.
		 * Otherwise, create a new instance of the block and forward the methods over.
		 */
		if (hasTileEntity(0)) {
			TileWrapper tileWrapper = ((TileWrapper) access.getTileEntity(position.x, position.y, position.z));
			if (tileWrapper != null && tileWrapper.getBlock() != null) {
				return ((TileWrapper) access.getTileEntity(position.x, position.y, position.z)).getBlock();
			}

			System.out.println("Error: Block in TileWrapper is null.");
		}
		return getBlockInstance(new BWBlockAccess(access), position);

	}

	public Block getBlockInstance(nova.core.block.BlockAccess access, Vector3i position) {
		return factory.makeBlock(access, position);
	}


	@Override
	public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer player)
	{
		// HACK: called before block is destroyed by the player prior to the player getting the drops. Determine drops here.
		// hack is needed because the player sets the block to air *before* getting the drops. woo good logic from mojang.
		if (!player.capabilities.isCreativeMode)
			harvestedBlocks.put(new BlockPosition(world, x, y, z), getBlockInstance(world, new Vector3i(x, y, z)));
	}

	@Override
	public boolean hasTileEntity(int metadata) {
		//A block requires a TileEntity if it stores data or if it ticks.
		return Storable.class.isAssignableFrom(blockClass) || Stateful.class.isAssignableFrom(blockClass) || Updater.class.isAssignableFrom(blockClass);
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new TileWrapper(factory.getID());
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

	//TODO: This method seems to only be invoked when a TileEntity changes, not when blocks change!
	@Override
	public void onNeighborChange(IBlockAccess access, int x, int y, int z, int tileX, int tileY, int tileZ) {
		getBlockInstance(access, new Vector3i(x, y, z)).onNeighborChange(new Vector3i(tileX, tileY, tileZ));
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
		getBlockInstance(world, new Vector3i(x, y, z)).onPlaced(new BlockChanger.Entity(BackwardProxyUtil.getEntityWrapper(entity)));
		//TODO: Should we consider onBlockPlaced also?
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, net.minecraft.block.Block block, int i) {
		getBlockInstance(world, new Vector3i(x, y, z)).onRemoved(new BlockChanger.Unknown());
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
				.map(CuboidForwardWrapper::new)
				.collect(Collectors.toList())
		);
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		Block block;

		// see onBlockHarvested for why the harvestedBlocks hack exists
		// this method will be called exactly once after destroying the block
		BlockPosition position = new BlockPosition(world, x, y, z);
		if (harvestedBlocks.containsKey(position))
			block = harvestedBlocks.remove(position);
		else
			block = getBlockInstance(world, new Vector3i(x, y, z));

		return block.getDrops()
			.stream()
			.map(WrapUtility::wrapItemStack)
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
		return new CuboidForwardWrapper(getBlockInstance(world, new Vector3i(x, y, z)).getBoundingBox().add(new Vector3i(x, y, z)));
	}

	@Override
	public int getLightValue(IBlockAccess access, int x, int y, int z) {
		if (block instanceof LightEmitter) {
			return Math.round(((LightEmitter) getBlockInstance(access, new Vector3i(x, y, z))).getEmittedLightLevel() * 15.0F);
		} else {
			return 0;
		}
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
		//TODO: We should use the item renderer.
		GL11.glPushAttrib(GL_TEXTURE_BIT);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glPushMatrix();
		Tessellator.instance.startDrawingQuads();
		ModelWrapper artist = new ModelWrapper();
		this.block.renderItem(artist);
		artist.renderItem();
		Tessellator.instance.draw();
		GL11.glPopMatrix();
		GL11.glPopAttrib();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, net.minecraft.block.Block block, int modelId, RenderBlocks renderer) {
		ModelWrapper model = new ModelWrapper();
		getBlockInstance(world, new Vector3i(x, y, z)).renderStatic(model);
		model.renderWorld(world, new Vector3d(x + 0.5, y + 0.5, z + 0.5));
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
}
