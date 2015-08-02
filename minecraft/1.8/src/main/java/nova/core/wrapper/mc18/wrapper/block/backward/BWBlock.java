package nova.core.wrapper.mc18.wrapper.block.backward;

import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import nova.core.block.Block;
import nova.core.block.component.BlockProperties;
import nova.core.block.component.LightEmitter;
import nova.core.component.misc.Collider;
import nova.core.component.transform.BlockTransform;
import nova.core.item.ItemFactory;
import nova.core.retention.Data;
import nova.core.retention.Storable;
import nova.core.retention.Store;
import nova.core.util.shape.Cuboid;
import nova.core.world.World;
import nova.core.wrapper.mc18.wrapper.block.world.BWWorld;
import nova.internal.core.Game;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BWBlock extends Block implements Storable {
	public final net.minecraft.block.Block mcBlock;
	@Store
	public int metadata;
	private TileEntity mcTileEntity;

	public BWBlock(net.minecraft.block.Block block) {
		this.mcBlock = block;
	}

	public BWBlock(net.minecraft.block.Block block, World world, Vector3D pos) {
		this.mcBlock = block;

		BlockTransform transform = add(new BlockTransform());
		transform.setWorld(world);
		transform.setPosition(pos);

		BlockProperties blockMaterial = add(new BlockProperties());
		blockMaterial.setLightTransmission(!mcBlock.getMaterial().blocksLight());
		blockMaterial.setBlockSound(BlockProperties.BlockSoundTrigger.PLACE, mcBlock.stepSound.getPlaceSound());
		blockMaterial.setBlockSound(BlockProperties.BlockSoundTrigger.BREAK, mcBlock.stepSound.getBreakSound());
		blockMaterial.setBlockSound(BlockProperties.BlockSoundTrigger.WALK, mcBlock.stepSound.getStepSound());

		add(new LightEmitter()).setEmittedLevel(() -> mcBlock.getLightValue(getMcBlockAccess(), new BlockPos(x(), y(), z())) / 15.0F);
		add(new Collider(this))
			.setBoundingBox(() -> new Cuboid(mcBlock.getBlockBoundsMinX(), mcBlock.getBlockBoundsMinY(), mcBlock.getBlockBoundsMinZ(), mcBlock.getBlockBoundsMaxX(), mcBlock.getBlockBoundsMaxY(), mcBlock.getBlockBoundsMaxZ()))
			.setOcclusionBoxes(entity -> {
				List<AxisAlignedBB> aabbs = new ArrayList<>();
				mcBlock.addCollisionBoxesToList(
					Game.natives().toNative(world()),
					new BlockPos(x(), y(), z()),
					blockState(),
					Game.natives().toNative(entity.isPresent() ? entity.get().get(Collider.class).boundingBox.get() : Cuboid.ONE.add(pos)),
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
	public void onRegister() {
	}

	@Override
	public ItemFactory getItemFactory() {
		return Game.natives().toNova(new ItemStack(Item.getItemFromBlock(mcBlock)));
	}

	private IBlockAccess getMcBlockAccess() {
		return ((BWWorld) world()).access;
	}

	private IBlockState blockState() {
		return getMcBlockAccess().getBlockState(new BlockPos(x(), y(), z()));
	}

	private TileEntity getTileEntity() {
		if (mcTileEntity == null && mcBlock.hasTileEntity(blockState())) {
			mcTileEntity = getMcBlockAccess().getTileEntity(new BlockPos(x(), y(), z()));
		}
		return mcTileEntity;
	}

	@Override
	public boolean canReplace() {
		return mcBlock.canPlaceBlockAt((net.minecraft.world.World) getMcBlockAccess(), new BlockPos(x(), y(), z()));
	}

	@Override
	public boolean shouldDisplacePlacement() {
		if (mcBlock == Blocks.snow_layer && ((int) blockState().getValue(BlockSnow.LAYERS) < 1)) {
			return false;
		}

		if (mcBlock == Blocks.vine || mcBlock == Blocks.tallgrass || mcBlock == Blocks.deadbush || mcBlock.isReplaceable(Game.natives().toNative(world()), new BlockPos(x(), y(), z()))) {
			return false;
		}
		return super.shouldDisplacePlacement();
	}

	@Override
	public String getID() {
		return net.minecraft.block.Block.blockRegistry.getNameForObject(mcBlock).toString();
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
