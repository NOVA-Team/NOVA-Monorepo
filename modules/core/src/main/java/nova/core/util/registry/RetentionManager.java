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

package nova.core.util.registry;

import nova.core.retention.Storable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * A manager that handles external file saving.
 * @author Calclavia
 */
public abstract class RetentionManager extends Manager<RetentionManager> {

	/**
	 * A map of objects to be saved to disk.
	 */
	protected Map<String, Storable> saveQueue = new HashMap<>();

	/**
	 * Saves a storable into the appropriate directory.
	 * @param filename - The file name to save the data as.
	 * @param storable - The storable object instance.
	 */
	public abstract void save(String filename, Storable storable);

	/**
	 * Loads a specific storable data based on the filename.
	 * @param filename - The file name the data was saved.
	 * @param storable - The storable object instance.
	 */
	public abstract void load(String filename, Storable storable);

	/**
	 * Queues the save until the next game save event occurs.
	 * @param filename - The file name to save the data as.
	 * @param storable - The storable object instance.
	 */
	public void queueSave(String filename, Storable storable) {
		saveQueue.put(filename, storable);
	}

	/**
	 * @return Gets the default directory in where game files are saved.
	 */
	public abstract File getSaveDirectory();
}
