package nova.wrapper.mc1710.wrapper.block.world;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import nova.core.block.Block;
import nova.core.block.BlockFactory;
import nova.core.entity.Entity;
import nova.core.entity.EntityFactory;
import nova.core.game.Game;
import nova.core.item.Item;
import nova.core.sound.Sound;
import nova.core.util.transform.shape.Cuboid;
import nova.core.util.transform.vector.Vector3d;
import nova.core.util.transform.vector.Vector3i;
import nova.core.world.World;
import nova.wrapper.mc1710.wrapper.entity.BWEntity;
import nova.wrapper.mc1710.wrapper.entity.forward.FWEntity;
import nova.wrapper.mc1710.wrapper.entity.forward.MCEntityWrapper;
import nova.wrapper.mc1710.launcher.NovaMinecraft;
import nova.wrapper.mc1710.wrapper.block.backward.BWBlock;
import nova.wrapper.mc1710.wrapper.block.forward.FWBlock;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * The backwards world wrapper.
 * @author Calclavia
 */
public class BWWorld extends World {
	public final net.minecraft.world.IBlockAccess access;

	public BWWorld(net.minecraft.world.IBlockAccess blockAccess) {
		this.access = blockAccess;
	}

	public net.minecraft.world.World world() {
		// Trying to access world from a IBlockAccess object!
		assert access instanceof World;
		return (net.minecraft.world.World) access;
	}

	@Override
	public void markStaticRender(Vector3i position) {
		world().markBlockForUpdate(position.x, position.y, position.z);
	}

	@Override
	public void markChange(Vector3i position) {
		world().notifyBlockChange(position.x, position.y, position.z, access.getBlock(position.x, position.y, position.z));
	}

	@Override
	public Optional<Block> getBlock(Vector3i position) {
		net.minecraft.block.Block mcBlock = access.getBlock(position.x, position.y, position.z);
		if (mcBlock == null || mcBlock == Blocks.air) {
			return Optional.of(Game.instance.blockManager.getAirBlock());
		} else if (mcBlock instanceof FWBlock) {
			return Optional.of(((FWBlock) mcBlock).getBlockInstance(access, position));
		} else {
			return Optional.of(new BWBlock(mcBlock, this, position));
		}
	}

	@Override
	public boolean setBlock(Vector3i position, BlockFactory blockFactory, Object... args) {
		//TODO: Implement object arguments
		net.minecraft.block.Block mcBlock = Game.instance.nativeManager.toNative(blockFactory.getDummy());
		return world().setBlock(position.x, position.y, position.z, mcBlock != null ? mcBlock : Blocks.air);
	}

	@Override
	public boolean removeBlock(Vector3i position) {
		return world().setBlockToAir(position.x, position.y, position.z);
	}

	@Override
	public Entity addEntity(EntityFactory factory, Object... args) {
		FWEntity bwEntity = new FWEntity(world(), factory, args);
		world().spawnEntityInWorld(bwEntity);
		return bwEntity.wrapped;
	}

	@Override
	public Entity addClientEntity(EntityFactory factory) {
		return NovaMinecraft.proxy.spawnParticle(world(), factory);
	}

	@Override
	public Entity addClientEntity(Entity entity) {
		return NovaMinecraft.proxy.spawnParticle(world(), entity);
	}

	@Override
	public void removeEntity(Entity entity) {
		world().removeEntity(entity.get(MCEntityWrapper.class).wrapper);
	}

	@Override
	public Set<Entity> getEntities(Cuboid bound) {
		return new HashSet(world().getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(bound.min.x, bound.min.y, bound.min.z, bound.max.x, bound.max.y, bound.max.z)));
	}

	@Override
	public Entity addEntity(Vector3d position, Item item) {
		EntityItem entityItem = new EntityItem(world(), position.x, position.y, position.z, Game.instance.nativeManager.toNative(item));
		world().spawnEntityInWorld(entityItem);
		return new BWEntity(entityItem);
	}

	@Override
	public String getID() {
		return world().provider.getDimensionName();
	}

	@Override
	public void playSoundAtPosition(Vector3d position, Sound sound) {
		//TODO: This may not work!
		world().playSound(position.x, position.y, position.z, sound.getID(), sound.pitch, sound.volume, false);
	}
}
