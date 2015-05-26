package nova.wrapper.mc1710.wrapper.block.forward;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import nova.core.block.Block;
import nova.core.block.BlockFactory;
import nova.core.block.Stateful;
import nova.core.component.Updater;
import nova.core.game.Game;
import nova.core.retention.Data;
import nova.core.retention.Storable;
import nova.core.util.transform.vector.Vector3i;
import nova.wrapper.mc1710.network.netty.MCNetworkManager;
import nova.wrapper.mc1710.wrapper.block.world.BWWorld;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * A Minecraft TileEntity to Nova block wrapper
 * @author Calclavia
 */
public class FWTile extends TileEntity {

	private final ExecutorService executor = Executors.newFixedThreadPool(2);

	private String blockID;
	private Block block;
	private Data cacheData = null;

	public FWTile() {

	}

	public FWTile(String blockID) {
		this.blockID = blockID;
	}

	public Block getBlock() {
		/**
		 * If a block does not exist, create one.
		 */
		if (block == null) {
			waitForWorld(() ->
			{
				Optional<BlockFactory> blockFactory = Game.instance.blockManager.getFactory(blockID);
				if (blockFactory.isPresent()) {
					block = blockFactory.get().makeBlock(new MCBlockWrapper(new BWWorld(getWorldObj()), new Vector3i(xCoord, yCoord, zCoord)));
					block.add(new MCBlockTransform(block));

					if (cacheData != null && block instanceof Storable) {
						((Storable) block).load(cacheData);
						cacheData = null;
					}
					if (block instanceof Stateful) {
						((Stateful) block).load();
					}
				} else {
					System.out.println("Error! Invalid NOVA block ID");
				}
			});
		}
		return block;
	}

	@Override
	public Packet getDescriptionPacket() {
		if (block instanceof nova.core.network.PacketHandler) {
			return ((MCNetworkManager) Game.instance.networkManager).toMCPacket(((MCNetworkManager) Game.instance.networkManager).getBlockPacket(0, (nova.core.network.PacketHandler) block));
		}
		return null;
	}

	@Override
	public void validate() {
		super.validate();
		getBlock();
		waitForBlock(() -> {
			if (block instanceof Stateful) {
				((Stateful) block).awake();
			}
		});
	}

	@Override
	public void invalidate() {
		if (block instanceof Stateful) {
			((Stateful) block).unload();
		}
		super.invalidate();
	}

	/**
	 * Waits for the world object to be available, then executes a specific action immediately after it is available.
	 */
	//TODO: Dump this back into the server thread. Threads also cannot printline.
	private void waitForWorld(Runnable action) {
		if (getWorldObj() != null) {
			action.run();
		} else {
			FutureTask<Object> future = new FutureTask<>(() ->
			{
				while (getWorldObj() == null) {
					Thread.sleep(1);
				}

				action.run();
				return null;
			});
			executor.execute(future);
		}

	}

	/**
	 * Waits for when the block instance is not null.
	 * @param action
	 */
	//TODO: Dump this back into the server thread. Threads also cannot printline.
	private void waitForBlock(Runnable action) {
		if (getBlock() != null) {
			action.run();
		} else {
			FutureTask<Object> future = new FutureTask<>(() ->
			{
				while (getBlock() == null) {
					Thread.sleep(1);
				}

				action.run();
				return null;
			});
			executor.execute(future);
		}
	}

	/**
	 * Updates the block.
	 */
	@Override
	public void updateEntity() {
		((Updater) block).update(0.05);
	}

	/**
	 * Only register tile updates if the block is an instance of Updater.
	 * @return Whether can update
	 */
	@Override
	public boolean canUpdate() {
		return block instanceof Updater;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		nbt.setString("novaID", blockID);

		if (block != null) {
			if (block instanceof Storable) {
				Data data = new Data();
				((Storable) block).save(data);
				nbt.setTag("nova", Game.instance.nativeManager.toNative(data));
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		/**
		 * Because World and Position do not exist during NBT read time,
		 * we must wait until the block is injected with World and Position data using Future.
		 */
		blockID = nbt.getString("novaID");
		cacheData = Game.instance.nativeManager.toNova(nbt.getCompoundTag("nova"));
	}
}
