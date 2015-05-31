package nova.wrapper.mc1710.manager;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.world.WorldEvent;
import nova.core.game.Game;
import nova.core.retention.Data;
import nova.core.retention.Storable;
import nova.core.util.SaveManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * A manager that handles external file saving.
 * @author Calclavia
 */
public class MCSaveManager extends SaveManager {

	/**
	 * Last time that the queueSave manager tried to queueSave a file
	 */
	private long lastSaveMills = 0;

	/**
	 * Save all storable queued
	 */
	public void saveAll() {
		saveQueue.forEach(this::save);
		saveQueue.clear();
	}

	@Override
	public void save(String filename, Storable storable) {
		Data saveMap = new Data();
		storable.save(saveMap);
		saveFile(filename, Game.instance().nativeManager().toNative(saveMap));
	}

	@Override
	public void load(String filename, Storable storable) {
		NBTTagCompound nbt = loadFile(filename);
		storable.load(Game.instance().nativeManager().toNova(nbt));
	}

	/**
	 * Saves NBT data in the world folder.
	 * @param file File to save data to
	 * @param data Data to save
	 * @return True on success.
	 */
	public boolean saveFile(File file, NBTTagCompound data) {
		try {
			File tempFile = new File(file.getParent(), file.getName() + "_tmp.dat");

			CompressedStreamTools.writeCompressed(data, new FileOutputStream(tempFile));

			if (file.exists()) {
				file.delete();
			}

			tempFile.renameTo(file);
			return true;
		} catch (Exception e) {
			System.out.println("Failed to queueSave " + file.getName() + ".dat!");
			e.printStackTrace();
			return false;
		}
	}

	public boolean saveFile(File saveDirectory, String filename, NBTTagCompound data) {
		return saveFile(new File(saveDirectory, filename + ".dat"), data);
	}

	public boolean saveFile(String filename, NBTTagCompound data) {
		return saveFile(getSaveDirectory(MinecraftServer.getServer().getFolderName()), filename, data);
	}

	public NBTTagCompound loadFile(File file) {
		try {
			if (file.exists()) {
				return CompressedStreamTools.readCompressed(new FileInputStream(file));
			} else {
				return new NBTTagCompound();
			}
		} catch (Exception e) {
			System.out.println("Failed to load " + file.getName() + ".dat!");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Reads NBT data from the world folder.
	 * @param saveDirectory Directory in which the file resides
	 * @param filename Name of the file
	 * @return The NBT data
	 */
	public NBTTagCompound loadFile(File saveDirectory, String filename) {
		return loadFile(new File(saveDirectory, filename + ".dat"));
	}

	public NBTTagCompound loadFile(String filename) {
		return loadFile(getSaveDirectory(MinecraftServer.getServer().getFolderName()), filename);
	}

	@Override
	public File getSaveDirectory() {
		return getSaveDirectory(MinecraftServer.getServer().getFolderName());
	}

	public File getSaveDirectory(String worldName) {
		File parent = getBaseDirectory();

		if (FMLCommonHandler.instance().getSide().isClient()) {
			parent = new File(getBaseDirectory(), "saves" + File.separator);
		}

		return new File(parent, worldName + File.separator);
	}

	public File getBaseDirectory() {
		if (FMLCommonHandler.instance().getSide().isClient()) {
			FMLClientHandler.instance().getClient();
			return FMLClientHandler.instance().getClient().mcDataDir;
		} else {
			return new File(".");
		}
	}

	@SubscribeEvent
	public void worldSave(WorldEvent evt) {
		//Current time milli-seconds is used to prevent the files from saving 20 times when the world loads
		if (System.currentTimeMillis() - lastSaveMills > 2000) {
			lastSaveMills = System.currentTimeMillis();
			saveAll();
		}
	}
}
