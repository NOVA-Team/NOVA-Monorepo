package nova.wrapper.mc1710.forward.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import nova.core.block.Block;
import nova.core.block.BlockChanger;
import nova.core.block.Stateful;
import nova.core.util.components.Storable;
import nova.core.util.components.Updater;
import nova.core.util.transform.Cuboid;
import nova.core.util.transform.Vector3d;
import nova.core.util.transform.Vector3i;
import nova.wrapper.mc1710.backward.entity.EntityBackwardWrapper;
import nova.wrapper.mc1710.backward.util.CuboidBackwardWrapper;
import nova.wrapper.mc1710.backward.world.BlockAccessWrapper;
import nova.wrapper.mc1710.forward.util.CuboidForwardWrapper;

import java.util.List;
import java.util.Set;

/**
 * A Minecraft to Nova block wrapper
 * @author Calclavia
 */
public class BlockWrapper extends net.minecraft.block.Block {

	/**
	 * Reference to the wrapped Nova block
	 */
	private Class<? extends Block> blockClass;

	//TODO: Resolve unknown material issue
	public BlockWrapper() {
		super(Material.piston);
	}

	public Block getBlockInstance(net.minecraft.world.IBlockAccess access, Vector3i position) {

		/**
		 * If this block has a TileEntity, forward the method into the Stateful block.
		 * Otherwise, create a new instance of the block and forward the methods over.
		 */
		if (hasTileEntity()) {
			return ((TileWrapper) access.getTileEntity(position.x, position.y, position.z)).block;
		} else {
			return getBlockInstance(new BlockAccessWrapper(access), position);
		}
	}

	public Block getBlockInstance(nova.core.block.BlockAccess access, Vector3i position) {
		try {
			return blockClass.getConstructor(nova.core.block.BlockAccess.class, Vector3i.class).newInstance(access, position);
		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	@Override
	public boolean hasTileEntity(int metadata) {
		//A block requires a TileEntity if it stores data or if it ticks.
		return Storable.class.isAssignableFrom(blockClass) || Stateful.class.isAssignableFrom(blockClass) || Updater.class.isAssignableFrom(blockClass);
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new TileWrapper();
	}

	//TODO: This method seems to only be invoked when a TileEntity changes, not when blocks change!
	@Override
	public void onNeighborChange(IBlockAccess access, int x, int y, int z, int tileX, int tileY, int tileZ) {
		getBlockInstance(access, new Vector3i(x, y, z)).onNeighborChange(new Vector3i(tileX, tileY, tileZ));
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
		getBlockInstance(world, new Vector3i(x, y, z)).onPlaced(new BlockChanger.Entity(new EntityBackwardWrapper(entity)));
		//TODO: Should we consider onBlockPlaced also?
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, net.minecraft.block.Block block, int i) {
		getBlockInstance(world, new Vector3i(x, y, z)).onRemoved(new BlockChanger.Unknown());
		super.breakBlock(world, x, y, z, block, i);
	}

	@Override
	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
		//TODO: Check this traytrace.
		MovingObjectPosition mop = player.rayTrace(10, 1);
		getBlockInstance(world, new Vector3i(x, y, z)).onLeftClick(new EntityBackwardWrapper(player), mop.sideHit, new Vector3d(mop.hitVec.xCoord, mop.hitVec.yCoord, mop.hitVec.zCoord));
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		return getBlockInstance(world, new Vector3i(x, y, z)).onRightClick(new EntityBackwardWrapper(player), side, new Vector3d(hitX, hitY, hitZ));
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		getBlockInstance(world, new Vector3i(x, y, z)).onEntityCollide(new EntityBackwardWrapper(entity));
	}

	@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity entity) {
		Set<Cuboid> boxes = getBlockInstance(world, new Vector3i(x, y, z)).getCollidingBoxes(new CuboidBackwardWrapper(aabb), new EntityBackwardWrapper(entity));
		boxes
			.stream()
			.map(c -> c.add(new Vector3i(x, y, z)))
			.map(c -> new CuboidForwardWrapper(c))
			.forEach(c -> list.add(c));
	}
}
