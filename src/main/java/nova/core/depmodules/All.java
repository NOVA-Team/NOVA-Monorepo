package nova.core.depmodules;


import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.inject.Module;

public class All {
	private static Set<Supplier<Module>> coreModules = new HashSet<>();
	
	public static Set<Module> getAllCoreModules(){
		return coreModules.stream().map(Supplier<Module>::get).collect(Collectors.toSet());
	}
	
	private static void add(Supplier<Module> module){
		coreModules.add(module);
	}
	
	static{
		add(BlockModule::new);
		add(ItemModule::new);
		add(RegistryModule::new);
		add(WorldModule::new);
	}
	
}
