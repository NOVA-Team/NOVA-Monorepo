package nova.wrapper.mc1710.backward.world;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import nova.core.block.Block;
import nova.core.entity.Entity;
import nova.core.entity.EntityFactory;
import nova.core.item.Item;
import nova.core.util.transform.Cuboid;
import nova.core.util.transform.Vector3d;
import nova.core.util.transform.Vector3i;
import nova.core.world.World;
import nova.wrapper.mc1710.backward.entity.BWEntity;
import nova.wrapper.mc1710.forward.block.BlockWrapperRegistry;
import nova.wrapper.mc1710.forward.entity.FWEntity;
import nova.wrapper.mc1710.item.ItemWrapperRegistry;
import nova.wrapper.mc1710.launcher.NovaMinecraft;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * The backwards world wrapper.
 * @author Calclavia
 */
//TODO: Consider Blocks.air compatibility?
public class BWWorld extends World {
	private final BWBlockAccess blockAccess;
	private final net.minecraft.world.World world;

	public BWWorld(net.minecraft.world.World world) {
		this.world = world;
		this.blockAccess = new BWBlockAccess(world);
	}

	@Override
	public void markStaticRender(Vector3i position) {
		world.markBlockForUpdate(position.x, position.y, position.z);
	}

	@Override
	public void markChange(Vector3i position) {
		world.notifyBlockChange(position.x, position.y, position.z, world.getBlock(position.x, position.y, position.z));
	}

	@Override
	public Optional<Block> getBlock(Vector3i position) {
		return blockAccess.getBlock(position);
	}

	@Override
	public boolean setBlock(Vector3i position, Block block) {
		net.minecraft.block.Block mcBlock = BlockWrapperRegistry.instance.getMCBlock(block);
		return world.setBlock(position.x, position.y, position.z, mcBlock != null ? mcBlock : Blocks.air);
	}

	@Override
	public boolean removeBlock(Vector3i position) {
		return world.setBlockToAir(position.x, position.y, position.z);
	}

	@Override
	public Entity createEntity(EntityFactory factory) {
		FWEntity bwEntity = new FWEntity(world, factory);
		world.spawnEntityInWorld(bwEntity);
		return bwEntity.wrapped;
	}

	@Override
	public Entity createClientEntity(EntityFactory factory) {
		return NovaMinecraft.proxy.spawnParticle(world, factory);
	}

	@Override
	public void destroyEntity(Entity entity) {
		world.removeEntity((FWEntity) entity.wrapper);
	}

	@Override
	public Set<Entity> getEntities(Cuboid bound) {
		return new HashSet(world.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(bound.min.x, bound.min.y, bound.min.z, bound.max.x, bound.max.y, bound.max.z)));
	}

	@Override
	public Entity createEntity(Vector3d position, Item item) {
		EntityItem entityItem = new EntityItem(world, position.x, position.y, position.z, ItemWrapperRegistry.instance.getMCItemStack(item));
		world.spawnEntityInWorld(entityItem);
		return new BWEntity(entityItem);
	}

	@Override
	public String getID() {
		return world.provider.getDimensionName();
	}
}
