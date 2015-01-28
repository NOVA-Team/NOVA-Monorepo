package nova.wrapper.mc1710.forward.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import nova.core.block.Block;
import nova.core.block.BlockChanger;
import nova.core.block.Stateful;
import nova.core.util.components.Storable;
import nova.core.util.components.Updater;
import nova.core.util.transform.Vector3i;
import nova.wrapper.mc1710.backward.entity.EntityWrapper;
import nova.wrapper.mc1710.backward.world.BlockAccessWrapper;

/**
 * A Minecraft to Nova block wrapper
 * @author Calclavia
 */
//TODO: How should blockAccess and World be injected?
public class BlockWrapper extends net.minecraft.block.Block {

	/**
	 * Reference to the wrapped Nova block
	 */
	private Class<? extends Block> blockClass;

	public BlockWrapper(Material material) {
		super(material);
	}

	public Block newBlockInstance(net.minecraft.world.IBlockAccess access, Vector3i position) {
		return newBlockInstance(new BlockAccessWrapper(access), position);
	}

	public Block newBlockInstance(nova.core.block.BlockAccess access, Vector3i position) {
		try {
			return blockClass.getConstructor(nova.core.block.BlockAccess.class, Vector3i.class).newInstance(access, position);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//TODO: Should this be null?
		return null;
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
		newBlockInstance(access, new Vector3i(x, y, z)).onNeighborChange(new Vector3i(tileX, tileY, tileZ));
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
		newBlockInstance(world, new Vector3i(x, y, z)).onPlaced(new BlockChanger.Entity(new EntityWrapper(entity)));
	}
}
