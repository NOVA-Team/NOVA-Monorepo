package nova.wrapper.mc1710.wrapper.block.backward;

import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import nova.core.block.Block;
import nova.core.block.component.LightEmitter;
import nova.core.component.misc.Collider;
import nova.core.component.transform.BlockTransform;
import nova.core.game.Game;
import nova.core.item.ItemFactory;
import nova.core.retention.Data;
import nova.core.retention.Storable;
import nova.core.retention.Stored;
import nova.core.util.transform.shape.Cuboid;
import nova.core.util.transform.vector.Vector3i;
import nova.core.world.World;
import nova.wrapper.mc1710.wrapper.block.world.BWWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BWBlock extends Block implements Storable {
	public final net.minecraft.block.Block mcBlock;
	@Stored
	public int metadata;
	private TileEntity mcTileEntity;

	public BWBlock(net.minecraft.block.Block block) {
		this.mcBlock = block;
	}

	public BWBlock(net.minecraft.block.Block block, World world, Vector3i pos) {
		this.mcBlock = block;
		BlockTransform transform = add(new BlockTransform());
		transform.setWorld(world);
		transform.setPosition(pos);
		add(new LightEmitter()).setEmittedLevel(() -> mcBlock.getLightValue(getMcBlockAccess(), x(), y(), z()) / 15.0F);
		add(new Collider())
			.setBoundingBox(() -> new Cuboid(mcBlock.getBlockBoundsMinX(), mcBlock.getBlockBoundsMinY(), mcBlock.getBlockBoundsMinZ(), mcBlock.getBlockBoundsMaxX(), mcBlock.getBlockBoundsMaxY(), mcBlock.getBlockBoundsMaxZ()))
			.setOcclusionBoxes(entity -> {
				List<AxisAlignedBB> aabbs = new ArrayList<>();
				mcBlock.addCollisionBoxesToList(
					Game.natives().toNative(world()),
					position().x,
					position().y,
					position().z,
					Game.natives().toNative(entity.isPresent() ? entity.get().get(Collider.class).boundingBox.get() : Cuboid.one.add(pos)),
					aabbs,
					entity.isPresent() ? Game.natives().toNative(entity.get()) : null
				);

				return aabbs.stream()
					.map(aabb -> (Cuboid) Game.natives().toNova(aabb))
					.map(cuboid -> cuboid.subtract(pos))
					.collect(Collectors.toSet());
			});
		//TODO: Set selection bounds
	}

	@Override
	public ItemFactory getItemFactory() {
		return Game.natives().toNova(Item.getItemFromBlock(mcBlock));
	}

	private IBlockAccess getMcBlockAccess() {
		return ((BWWorld) world()).access;
	}

	private int getMetadata() {
		return getMcBlockAccess().getBlockMetadata(x(), y(), z());
	}

	private TileEntity getTileEntity() {
		if (mcTileEntity == null && mcBlock.hasTileEntity(getMetadata())) {
			mcTileEntity = getMcBlockAccess().getTileEntity(x(), y(), z());
		}
		return mcTileEntity;
	}

	@Override
	public String getID() {
		return net.minecraft.block.Block.blockRegistry.getNameForObject(mcBlock);
	}

	@Override
	public void save(Data data) {
		Storable.super.save(data);

		TileEntity tileEntity = getTileEntity();
		if (tileEntity != null) {
			NBTTagCompound nbt = new NBTTagCompound();
			tileEntity.writeToNBT(nbt);
			data.putAll(Game.natives().toNova(nbt));
		}
	}

	@Override
	public void load(Data data) {
		Storable.super.load(data);

		TileEntity tileEntity = getTileEntity();
		if (tileEntity != null) {
			tileEntity.writeToNBT(Game.natives().toNative(data));
		}
	}
}
