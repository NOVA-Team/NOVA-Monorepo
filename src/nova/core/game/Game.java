package nova.core.game;

import nova.core.util.Identifiable;
import nova.core.util.Registry;

import java.util.HashMap;
import java.util.Map;

public class Game {
	public static Game instance;
	private Map<Class, Registry> registryMap = new HashMap<Class, Registry>();

	public Registry<? extends Identifiable> getRegistry(Class<? extends Identifiable> c) {
		if (!registryMap.containsKey(c)) {
			registryMap.put(c, new Registry());
		}
		return registryMap.get(c);
	}
}
