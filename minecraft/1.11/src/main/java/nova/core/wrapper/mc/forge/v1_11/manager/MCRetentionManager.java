/*
 * Copyright (c) 2015 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License version 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
 */

package nova.core.wrapper.mc.forge.v1_11.manager;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nova.core.retention.Data;
import nova.core.retention.Storable;
import nova.core.util.registry.RetentionManager;
import nova.internal.core.Game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import net.minecraftforge.fml.server.FMLServerHandler;

/**
 * A manager that handles external file saving.
 * @author Calclavia
 */
public class MCRetentionManager extends RetentionManager {

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
		saveFile(filename, Game.natives().toNative(saveMap));
	}

	@Override
	public void load(String filename, Storable storable) {
		NBTTagCompound nbt = loadFile(filename);
		storable.load(Game.natives().toNova(nbt));
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
		return saveFile(getSaveDirectory(FMLServerHandler.instance().getServer().getFolderName()), filename, data);
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
		return loadFile(getSaveDirectory(FMLServerHandler.instance().getServer().getFolderName()), filename);
	}

	@Override
	public File getSaveDirectory() {
		return getSaveDirectory(FMLServerHandler.instance().getServer().getFolderName());
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

	@Override
	public void init() {

	}
}
