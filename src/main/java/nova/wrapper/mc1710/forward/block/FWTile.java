package nova.wrapper.mc1710.forward.block;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import nova.core.block.Block;
import nova.core.block.BlockFactory;
import nova.core.block.components.Stateful;
import nova.core.game.Game;
import nova.core.network.PacketSender;
import nova.core.util.components.Storable;
import nova.core.util.components.Updater;
import nova.core.util.transform.Vector3i;
import nova.wrapper.mc1710.backward.world.BWWorld;
import nova.wrapper.mc1710.launcher.NovaMinecraft;
import nova.wrapper.mc1710.util.NBTUtility;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * A Minecraft TileEntity to Nova block wrapper
 *
 * @author Calclavia
 */
public class FWTile extends TileEntity {

	private final ExecutorService executor = Executors.newFixedThreadPool(2);

	private String blockID;
	private Block block;
	private Map<String, Object> cacheData = null;

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
				Optional<BlockFactory> blockFactory = Game.instance.get().blockManager.getBlockFactory(blockID);
				if (blockFactory.isPresent()) {
					block = blockFactory.get().makeBlock(new BWWorld(getWorldObj()), new Vector3i(xCoord, yCoord, zCoord));

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
		if (block instanceof PacketSender) {
			return NovaMinecraft.networkManager.toMCPacket(NovaMinecraft.networkManager.getBlockPacket(0, (PacketSender) block));
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
	 *
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
	 *
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
				Map<String, Object> data = new HashMap<>();
				((Storable) block).save(data);
				nbt.setTag("nova", NBTUtility.mapToNBT(data));
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
		cacheData = NBTUtility.nbtToMap(nbt.getCompoundTag("nova"));
	}
}
