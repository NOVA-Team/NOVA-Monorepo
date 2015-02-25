package nova.wrapper.mc1710.backward.world;

import net.minecraft.init.Blocks;
import nova.core.block.Block;
import nova.core.entity.Entity;
import nova.core.entity.EntityFactory;
import nova.core.util.transform.Vector3i;
import nova.core.world.World;
import nova.wrapper.mc1710.forward.block.BlockWrapperRegistry;
import nova.wrapper.mc1710.forward.entity.FWEntity;
import nova.wrapper.mc1710.launcher.NovaMinecraft;

import java.util.Optional;

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
	public String getID() {
		return world.provider.getDimensionName();
	}
}
