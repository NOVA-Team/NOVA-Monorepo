package nova.internal.launch;

import nova.bootstrap.DependencyInjectionEntryPoint;
import nova.core.deps.Dependencies;
import nova.core.deps.Dependency;
import nova.core.deps.MavenDependency;
import nova.core.game.Game;
import nova.core.loader.NovaMod;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The main class that launches NOVA mods.
 *
 * Correct order to call the methods is this:
 * <ol>
 * <li>{@link #generateDependencies()}</li>
 * <li>{@link #preInit()}</li>
 * <li>{@link #init()}</li>
 * <li>{@link #postInit()}</li>
 * </ol>
 * @author Calclavia, Kubuxu
 */
public class NovaLauncher extends ModLoader<NovaMod> {

	private Map<NovaMod, List<MavenDependency>> neededDeps;

	/**
	 * Creates NovaLauncher.
	 * @param modClasses mods to instantialize.
	 * @param diep is required as we are installing additional modules to it.
	 */
	public NovaLauncher(DependencyInjectionEntryPoint diep, Set<Class<?>> modClasses) {
		super(NovaMod.class, diep, modClasses);

		/**
		 * Install all DI modules
		 */
		javaClasses.keySet().stream()
			.flatMap(mod -> Arrays.stream(mod.modules()))
			.forEach(diep::install);
	}

	@Override
	public void load() {
		super.load();

		Collections.sort(orderedMods, (o1, o2) -> {
			NovaMod anno1 = o1.getClass().getAnnotation(NovaMod.class);
			NovaMod anno2 = o2.getClass().getAnnotation(NovaMod.class);

			// Split string by @ and versions
			Map<String, String> loadAfter1 = dependencyToMap(anno1.dependencies());

			// TODO: Compare version requirements.
			if (loadAfter1.entrySet().stream().anyMatch(entry -> anno2.id().equals(entry.getKey()))) {
				return 1;
			}

			// Split string by @ and versions
			Map<String, String> loadAfter2 = dependencyToMap(anno2.dependencies());

			if (loadAfter2.entrySet().stream().anyMatch(entry -> anno1.id().equals(entry.getKey()))) {
				return -1;
			}

			return 0;
		});
		Game.logger().info("NOVA Mods Loaded: " + mods.size());
	}

	public Map<String, String> dependencyToMap(String[] dependencies) {
		return Arrays.stream(dependencies)
			.map(s -> s.split("@", 1))
			.collect(Collectors.toMap(s -> s[0], s -> s.length > 1 ? s[1] : ""));
	}

	@Override
	public void preInit() {
		// Test integrity of the GuiFactory
		Game.guiComponent().validate();
		super.preInit();
	}

	public Map<NovaMod, List<MavenDependency>> getNeededDeps() {
		if (neededDeps == null) {
			throw new IllegalStateException("Dependencies have not been generated");
		}
		return neededDeps;
	}

	/**
	 * Get the dependencies. Separated from preInit due to issues with ordering in case mods need to download mods before the preInit method is called.
	 * The wrapper just needs to call this method right before it downloads the dependencies.
	 */
	public void generateDependencies() {
		neededDeps = new HashMap<>(); // This should be cleaned every time this method is run.

		Stream.concat(javaClasses.values().stream(), scalaClasses.values().stream())
			.forEach(this::generateAndAddDependencies);
	}

	private void generateAndAddDependencies(Class<?> mod) {
		List<MavenDependency> deps;
		if (mod.isAnnotationPresent(Dependency.class)) {
			Dependency dependency = mod.getAnnotation(Dependency.class);
			deps = Collections.singletonList(new MavenDependency(dependency));
		} else if (mod.isAnnotationPresent(Dependencies.class)) {
			Dependency[] dependencies = mod.getAnnotation(Dependencies.class).value();
			deps = Arrays.stream(dependencies).map(MavenDependency::new).collect(Collectors.toList());
		} else {
			return;
		}

		neededDeps.put(mod.getAnnotation(NovaMod.class), deps);
	}

}
