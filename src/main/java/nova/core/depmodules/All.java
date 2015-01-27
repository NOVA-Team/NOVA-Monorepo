package nova.core.depmodules;

import com.google.inject.Module;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class All {
	private static Set<Supplier<Module>> coreModules = new HashSet<>();

	public static Set<Module> getAllCoreModules() {
		return coreModules.stream().map(Supplier::get).collect(Collectors.toSet());
	}

	private static void add(Supplier<Module> module) {
		coreModules.add(module);
	}

	static {
		add(BlockModule::new);
		add(ItemModule::new);
		add(UtilModule::new);
		add(WorldModule::new);
	}

}
