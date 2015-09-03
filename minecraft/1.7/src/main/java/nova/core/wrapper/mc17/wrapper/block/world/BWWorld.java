package nova.core.wrapper.mc17.wrapper.block.world;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import nova.core.block.Block;
import nova.core.block.BlockFactory;
import nova.core.entity.Entity;
import nova.core.entity.EntityFactory;
import nova.core.item.Item;
import nova.core.sound.Sound;
import nova.core.util.shape.Cuboid;
import nova.core.world.World;
import nova.core.wrapper.mc17.launcher.NovaMinecraft;
import nova.core.wrapper.mc17.wrapper.block.backward.BWBlock;
import nova.core.wrapper.mc17.wrapper.block.forward.FWBlock;
import nova.core.wrapper.mc17.wrapper.entity.backward.BWEntity;
import nova.core.wrapper.mc17.wrapper.entity.forward.FWEntity;
import nova.core.wrapper.mc17.wrapper.entity.forward.MCEntityTransform;
import nova.internal.core.Game;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
	public void markStaticRender(Vector3D position) {
		world().markBlockForUpdate((int) position.getX(), (int) position.getY(), (int) position.getZ());
	}

	@Override
	public void markChange(Vector3D position) {
		world().notifyBlockChange((int) position.getX(), (int) position.getY(), (int) position.getZ(), access.getBlock((int) position.getX(), (int) position.getY(), (int) position.getZ()));
	}

	@Override
	public Optional<Block> getBlock(Vector3D position) {
		net.minecraft.block.Block mcBlock = access.getBlock((int) position.getX(), (int) position.getY(), (int) position.getZ());
		if (mcBlock == null || mcBlock == Blocks.air) {
			return Optional.of(Game.blocks().getAirBlock());
		} else if (mcBlock instanceof FWBlock) {
			return Optional.of(((FWBlock) mcBlock).getBlockInstance(access, position));
		} else {
			return Optional.of(new BWBlock(mcBlock, this, position));
		}
	}

	@Override
	public boolean setBlock(Vector3D position, BlockFactory blockFactory, Object... args) {
		//TODO: Implement object arguments
		net.minecraft.block.Block mcBlock = Game.natives().toNative(blockFactory.getDummy());
		return world().setBlock((int) position.getX(), (int) position.getY(), (int) position.getZ(), mcBlock != null ? mcBlock : Blocks.air);
	}

	@Override
	public boolean removeBlock(Vector3D position) {
		return world().setBlockToAir((int) position.getX(), (int) position.getY(), (int) position.getZ());
	}

	@Override
	public Entity addEntity(EntityFactory factory, Object... args) {
		FWEntity bwEntity = new FWEntity(world(), factory, args);
		bwEntity.forceSpawn = true;
		world().spawnEntityInWorld(bwEntity);
		return bwEntity.getWrapped();
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
		net.minecraft.entity.Entity wrapper = entity.get(MCEntityTransform.class).wrapper;
		wrapper.setDead();
		world().removeEntity(wrapper);
	}

	@Override
	public Set<Entity> getEntities(Cuboid bound) {
		return (Set) world().getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(bound.min.getX(), bound.min.getY(), bound.min.getZ(), bound.max.getX(), bound.max.getY(), bound.max.getZ()))
			.stream()
			.map(mcEnt -> Game.natives().getNative(Entity.class, net.minecraft.entity.Entity.class).toNova((net.minecraft.entity.Entity) mcEnt))
			.collect(Collectors.toSet());
	}

	@Override
	public Entity addEntity(Vector3D position, Item item) {
		EntityItem entityItem = new EntityItem(world(), position.getX(), position.getY(), position.getZ(), Game.natives().toNative(item));
		world().spawnEntityInWorld(entityItem);
		return new BWEntity(entityItem);
	}

	@Override
	public Optional<Entity> getEntity(String uniqueID) {
		return Optional.ofNullable(Game.natives().toNova(world().getEntityByID(Integer.parseInt(uniqueID))));
	}

	@Override
	public String getID() {
		return world().provider.getDimensionName();
	}

	@Override
	public void playSoundAtPosition(Vector3D position, Sound sound) {
		world().playSound(position.getX(), position.getY(), position.getZ(), sound.getID(), sound.pitch, sound.volume, false);
	}
}
