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

package nova.internal.core.launch;

import nova.core.deps.Dependencies;
import nova.core.deps.Dependency;
import nova.core.deps.MavenDependency;
import nova.core.loader.Loadable;
import nova.core.loader.Mod;
import nova.core.util.ProgressBar;
import nova.internal.core.Game;
import nova.internal.core.bootstrap.DependencyInjectionEntryPoint;
import nova.internal.core.util.TopologicalSort;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
public class NovaLauncher extends ModLoader<Mod> {

	private Map<Mod, List<MavenDependency>> neededDeps;

	/**
	 * Creates NovaLauncher.
	 * @param modClasses mods to instantialize.
	 * @param diep is required as we are installing additional modules to it.
	 */
	public NovaLauncher(DependencyInjectionEntryPoint diep, Set<Class<?>> modClasses) {
		super(Mod.class, diep, modClasses);

		/**
		 * Install all DI modules
		 */
		javaClasses.keySet().stream()
			.flatMap(mod -> Arrays.stream(mod.modules()))
			.forEach(diep::install);
	}

	@Override
	public void load() {
		this.load(ProgressBar.NULL_PROGRESS_BAR);
	}

	@Override
	public void load(ProgressBar progressBar) {
		super.load(progressBar);

		TopologicalSort.DirectedGraph<Mod> modGraph = new TopologicalSort.DirectedGraph<>();

		mods.keySet().forEach(modGraph::addNode);

		//Create directed graph edges.
		mods.keySet().forEach(
			mod -> {
				Map<String, String> depMap = dependencyToMap(mod.dependencies());
				depMap.forEach((id, version) -> {
					Optional<Mod> dependent = mods.keySet()
						.stream()
						.filter(m2 -> m2.id().equals(id))
						.findFirst();

					// TODO: Compare version requirements.
					if (dependent.isPresent()) {
						modGraph.addEdge(dependent.get(), mod);
					}
				});

				//Priority check
				mods.keySet().forEach(
					compareMod -> {
						if (mod.priority() < compareMod.priority()) {
							modGraph.addEdge(compareMod, mod);
						}
					});
			}
		);

		orderedMods.clear();

		TopologicalSort.topologicalSort(modGraph)
			.stream()
			.map(mods::get)
			.filter(mod -> mod instanceof Loadable)
			.map(mod -> (Loadable) mod)
			.forEachOrdered(orderedMods::add);

		Game.logger().info("NOVA mods loaded: " + mods.size());
	}

	public Map<String, String> dependencyToMap(String[] dependencies) {
		return Arrays.stream(dependencies)
			.map(s -> s.split("@", 1))
			.collect(Collectors.toMap(s -> s[0], s -> s.length > 1 ? s[1] : ""));
	}

	@Override
	public void preInit() {
		super.preInit();
	}

	public Map<Mod, List<MavenDependency>> getNeededDeps() {
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

		neededDeps.put(mod.getAnnotation(Mod.class), deps);
	}
}
