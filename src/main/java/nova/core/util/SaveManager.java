package nova.core.util;

import nova.core.util.components.Storable;

import java.util.HashMap;
import java.util.Map;

/**
 * A manager that handles external file saving.
 * @author Calclavia
 */
public abstract class SaveManager {

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
}
