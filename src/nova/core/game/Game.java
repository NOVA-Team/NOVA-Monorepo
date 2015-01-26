package nova.core.game;

import nova.core.util.Identifiable;
import nova.core.util.Registry;

import java.util.HashMap;
import java.util.Map;

public class Game {
	public static Game instance;
	private Map<Class<? extends Named>, Registry> registryMap = new HashMap<>();

	public <T extends Identifiable> Registry<T> getRegistry(Class<T> type) {
		if (!registryMap.containsKey(type)) {
			registryMap.put(type, new Registry<T>());
		}
		return registryMap.get(type);
	}
}
