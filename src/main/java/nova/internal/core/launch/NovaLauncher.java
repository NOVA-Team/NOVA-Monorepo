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
import nova.core.loader.Mod;
import nova.core.util.ProgressBar;
import nova.internal.core.Game;
import nova.internal.core.bootstrap.DependencyInjectionEntryPoint;
import nova.internal.core.util.TopologicalSort;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The main class that launches NOVA mods.
 *
 * Correct order to call the methods is this:
 * <ol>
 * <li>{@link #generateDependencies()}</li>
 * <li>{@link #load()}</li>
 * </ol>
 * @author Calclavia, Kubuxu
 */
public class NovaLauncher extends ModLoader<Mod> {

	private static Optional<NovaLauncher> INSTANCE = Optional.empty();

	private Map<Mod, List<MavenDependency>> neededDeps;
	private Map<Mod, Set<String[]>> missingDeps;
	private Map<Mod, Set<String[][]>> mismatchedDeps;
	private Map<String, Set<Mod>> duplicatedIDs;
	private boolean loadingErrored = false;

	public static Optional<NovaLauncher> instance() {
		return INSTANCE;
	}

	/**
	 * Creates NovaLauncher.
	 * @param modClasses mods to instantialize.
	 * @param diep is required as we are installing additional modules to it.
	 */
	public NovaLauncher(DependencyInjectionEntryPoint diep, Set<Class<?>> modClasses) {
		super(Mod.class, diep, modClasses);
		INSTANCE = Optional.of(this);

		/**
		 * Install all DI modules
		 */
		javaClasses.keySet().stream()
			.flatMap(mod -> Arrays.stream(mod.modules()))
			.forEach(diep::install);
	}

	@Override
	public void load() {
		this.load(new ProgressBar.NullProgressBar());
	}

	@Override
	public void load(ProgressBar progressBar) {
		this.load(progressBar, true);
	}

	@Override
	public void load(ProgressBar progressBar, boolean finish) {
		super.load(progressBar, false);

		TopologicalSort.DirectedGraph<Mod> modGraph = new TopologicalSort.DirectedGraph<>();

		mods.keySet().forEach(modGraph::addNode);

		missingDeps = new HashMap<>();
		mismatchedDeps = new HashMap<>();
		duplicatedIDs = new HashMap<>();

		// Ensure we don't try to load two mods with the same ID.
		mods.keySet().forEach(
			mod -> {
				Class<?> modClass = getModClasses().getOrDefault(mod, getScalaClassesMap().get(mod));
				if (mods.keySet().stream().filter(m -> m != mod &&
					getModClasses().getOrDefault(m, getScalaClassesMap().get(m)) != modClass)
					.anyMatch(m -> mod.id().equals(m.id()))) {
					Game.logger().error("Found duplicate mod id '" + mod.id() + '@' + mod.version() + "' (" + mod.name() + "), class: " + modClass.getCanonicalName());
					Set<Mod> duplicatedIDs = this.duplicatedIDs.get(mod.id());
					if (duplicatedIDs == null) {
						duplicatedIDs = new HashSet<>();
						this.duplicatedIDs.put(mod.id(), duplicatedIDs);
					}
					duplicatedIDs.add(mod);
				}
			}
		);

		if (!duplicatedIDs.isEmpty()) {
			throw new InitializationException("Mods with duplicate IDs");
		}

		// Create directed graph edges.
		mods.keySet().forEach(
			mod -> {
				Map<String, String> depMap = dependencyToMap(mod.dependencies());
				depMap.forEach((id, version) -> {
					Optional<Mod> dependent = mods.keySet()
						.stream()
						.filter(m2 -> m2.id().equals(id))
						.findFirst();

					final boolean forced = version.endsWith("f") || version.endsWith("F");
					if (forced) version = version.substring(0, version.length() - 1);

					// TODO: Compare NOVA version requirements. (can be done with a Semantic Versioning APO)
					if (dependent.isPresent()) {
						try {
							if (!versionMatches(version, dependent.get().version())) {
								// TODO: Display mod version patterns in a more user friendly manner.
								Game.logger().error("Mod '" + mod.id() + '@' + mod.version() + "' (" + mod.name()
									+ ") needs mod '" + dependent.get().id() + '@' + dependent.get().version()
									+ "' (" + dependent.get().name() + ") with version " + version);

								Set<String[][]> mismatchedDeps = this.mismatchedDeps.get(mod);
								if (mismatchedDeps == null) {
									mismatchedDeps = new HashSet<>();
									this.mismatchedDeps.put(mod, mismatchedDeps);
								}
								mismatchedDeps.add(new String[][]{{dependent.get().id(), dependent.get().version()}, {id, version}});
							}
						} catch (Exception ex) {
							Game.logger().error(ex.getMessage(), ex);
							loadingErrored = true;
						}
						modGraph.addEdge(dependent.get(), mod);
					} else if (forced) {
						// TODO: Display mod version patterns in a more user friendly manner.
						Game.logger().error("Mod '" + mod.id() + '@' + mod.version() + "' (" + mod.name()
							+ ") needs mod '" + id + (version.isEmpty() ? '\'' : '@' + version + '\''));

						Set<String[]> missingDeps = this.missingDeps.get(mod);
						if (missingDeps == null) {
							missingDeps = new HashSet<>();
							this.missingDeps.put(mod, missingDeps);
						}
						missingDeps.add(new String[]{id, version});
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

		if (loadingErrored || !duplicatedIDs.isEmpty() || !missingDeps.isEmpty() || !mismatchedDeps.isEmpty()) {
			throw new InitializationException("Errors during mod loading.");
		}

		orderedMods.clear();

		TopologicalSort.topologicalSort(modGraph)
			.stream()
			.map(mods::get)
			.forEachOrdered(orderedMods::add);

		Game.logger().info("NOVA mods loaded: " + mods.size());
		if (finish) progressBar.finish();
	}

	public Map<String, String> dependencyToMap(String[] dependencies) {
		return Arrays.stream(dependencies)
			.map(s -> {
				if (s.contains("@")) {
					String[] ret = new String[2];
					ret[0] = s.substring(0, s.lastIndexOf('@'));
					ret[1] = s.substring(s.lastIndexOf('@') + 1);
					return ret;
				} else {
					return new String[]{s};
				}
			})
			.collect(Collectors.toMap(s -> s[0], s -> s.length > 1 ? s[1] : ""));
	}

	public Map<Mod, List<MavenDependency>> getNeededDeps() {
		if (neededDeps == null) {
			throw new IllegalStateException("Dependencies have not been generated");
		}
		return neededDeps;
	}

	/**
	 * Gets the mods with duplicate IDs.
	 *
	 * @return The mods with duplicate IDs.
	 */
	public Map<String, Set<Mod>> getDuplicateIDs() {
		return Collections.unmodifiableMap(this.duplicatedIDs.entrySet().stream()
			.collect(Collectors.toMap(e -> e.getKey(), e -> Collections.unmodifiableSet(e.getValue()))));
	}

	/**
	 * Gets the mods with missing required dependencies.
	 *
	 * The String array is formatted as such:
	 * <code>{id, version}</code>
	 *
	 * @return The mods with missing required dependencies.
	 */
	public Map<Mod, Set<String[]>> getMissingDeps() {
		return Collections.unmodifiableMap(this.missingDeps.entrySet().stream()
			.collect(Collectors.toMap(e -> e.getKey(), e -> Collections.unmodifiableSet(e.getValue()))));
	}

	/**
	 * Gets the mods with mismatched dependencies.
	 *
	 * The 2D String array is formatted as such:
	 * <code>{{id, actualVersion}, {id, expectedVersion}}</code>
	 *
	 * @return The mods with mismatched dependencies.
	 */
	public Map<Mod, Set<String[][]>> getMismatchedDeps() {
		return Collections.unmodifiableMap(this.mismatchedDeps.entrySet().stream()
			.collect(Collectors.toMap(e -> e.getKey(), e -> Collections.unmodifiableSet(e.getValue()))));
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

	private static boolean versionMatches(String versionPattern, String version) {
		String[] versionPatternSplit = versionPattern.split("\\d");
		long[] v = Arrays.stream(version.split("\\.")).mapToLong(Long::parseUnsignedLong).toArray();
		int cutTo = 0;
		for (int i = 0; i < versionPatternSplit.length; i++)
			if (!"x".equalsIgnoreCase(versionPatternSplit[i]))
				cutTo = i;

		if (cutTo == 0)
			return true;
		if (cutTo > v.length)
			return false;

		String[] m = Arrays.copyOf(versionPatternSplit, cutTo);

		for (int i = 0; i < m.length; i++) {
			String pattern = m[i];
			long l = v[i];

			if ("x".equalsIgnoreCase(pattern))
				continue;

			try {
				long k = Long.parseUnsignedLong(pattern);
				if (Long.compareUnsigned(l, k) < 0)
					return false;
				else
					continue;
			} catch (NumberFormatException e) {
			}

			if (VERSION_RANGE.matcher(pattern).matches()) {
				OptionalLong[] data = rangeToInts(pattern);
				OptionalLong min = data[0];
				OptionalLong max = data[1];
				if (!min.isPresent() && !max.isPresent())
					throw new IllegalArgumentException(pattern);

				if (min.isPresent() && Long.compareUnsigned(l, min.getAsLong()) < 0) {
					return false;
				}
				if (max.isPresent() && Long.compareUnsigned(l, max.getAsLong()) > 0) {
					return false;
				}
				continue;
			}

			return false;
		}

		return true;
	}

	private static OptionalLong[] rangeToInts(String string) {
		Matcher m = VERSION_RANGE.matcher(string);
		if (m.matches()) {
			return new OptionalLong[]{
				m.group(1).isEmpty() ? OptionalLong.empty() : OptionalLong.of(Long.parseUnsignedLong(m.group(1))),
				m.group(2).isEmpty() ? OptionalLong.empty() : OptionalLong.of(Long.parseUnsignedLong(m.group(2)))
			};
		} else {
			throw new IllegalArgumentException(string);
		}
	}

	private static final Pattern VERSION_RANGE = Pattern.compile("^\\{(\\d*)-(\\d*)\\}$", Pattern.CASE_INSENSITIVE);
}
