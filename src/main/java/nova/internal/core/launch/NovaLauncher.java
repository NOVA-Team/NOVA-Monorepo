package nova.internal.core.launch;

import nova.bootstrap.DependencyInjectionEntryPoint;
import nova.core.deps.Dependencies;
import nova.core.deps.Dependency;
import nova.core.deps.MavenDependency;
import nova.core.loader.NovaMod;
import nova.core.util.NovaException;
import nova.internal.core.Game;

import java.util.*;
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

		TopologicalSort.DirectedGraph<NovaMod> modGraph = new TopologicalSort.DirectedGraph<>();

		mods.keySet().forEach(modGraph::addNode);

		//Create directed graph edges.
		mods.keySet().forEach(
			mod -> {
				Map<String, String> depMap = dependencyToMap(mod.dependencies());
				depMap.forEach((id, version) -> {
					Optional<NovaMod> dependent = mods.keySet()
						.stream()
						.filter(m2 -> m2.id().equals(id))
						.findFirst();

					// TODO: Compare version requirements.
					if (dependent.isPresent()) {
						modGraph.addEdge(dependent.get(), mod);
					}
				});
			}
		);

		orderedMods.clear();

		TopologicalSort.topologicalSort(modGraph)
			.stream()
			.map(mods::get)
			.forEachOrdered(orderedMods::add);

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

	//TODO: Separate into Util library
	public static class TopologicalSort {
		/**
		 * Sort the input graph into a topologically sorted list
		 *
		 * Uses the reverse depth first search as outlined in ...
		 * @param graph
		 * @return The sorted mods list.
		 */
		public static <T> List<T> topologicalSort(DirectedGraph<T> graph) {
			DirectedGraph<T> rGraph = reverse(graph);
			List<T> sortedResult = new ArrayList<T>();
			Set<T> visitedNodes = new HashSet<T>();
			// A list of "fully explored" nodes. Leftovers in here indicate cycles in the graph
			Set<T> expandedNodes = new HashSet<T>();

			for (T node : rGraph) {
				explore(node, rGraph, sortedResult, visitedNodes, expandedNodes);
			}

			return sortedResult;
		}

		public static <T> DirectedGraph<T> reverse(DirectedGraph<T> graph) {
			DirectedGraph<T> result = new DirectedGraph<T>();

			for (T node : graph) {
				result.addNode(node);
			}

			for (T from : graph) {
				for (T to : graph.edgesFrom(from)) {
					result.addEdge(to, from);
				}
			}

			return result;
		}

		public static <T> void explore(T node, DirectedGraph<T> graph, List<T> sortedResult, Set<T> visitedNodes, Set<T> expandedNodes) {
			// Have we been here before?
			if (visitedNodes.contains(node)) {
				// And have completed this node before
				if (expandedNodes.contains(node)) {
					// Then we're fine
					return;
				}

				throw new NovaException("There was a cycle detected in the input graph, sorting is not possible", node) {};
			}

			// Visit this node
			visitedNodes.add(node);

			// Recursively explore inbound edges
			for (T inbound : graph.edgesFrom(node)) {
				explore(inbound, graph, sortedResult, visitedNodes, expandedNodes);
			}

			// Add ourselves now
			sortedResult.add(node);
			// And mark ourselves as explored
			expandedNodes.add(node);
		}

		public static class DirectedGraph<T> implements Iterable<T> {
			private final Map<T, SortedSet<T>> graph = new HashMap<T, SortedSet<T>>();
			private List<T> orderedNodes = new ArrayList<T>();

			public boolean addNode(T node) {
				// Ignore nodes already added
				if (graph.containsKey(node)) {
					return false;
				}

				orderedNodes.add(node);
				graph.put(node, new TreeSet<>((o1, o2) -> orderedNodes.indexOf(o1) - orderedNodes.indexOf(o2)));
				return true;
			}

			public void addEdge(T from, T to) {
				if (!(graph.containsKey(from) && graph.containsKey(to))) {
					throw new NoSuchElementException("Missing nodes from graph: " + from + " to " + to);
				}

				graph.get(from).add(to);
			}

			public void removeEdge(T from, T to) {
				if (!(graph.containsKey(from) && graph.containsKey(to))) {
					throw new NoSuchElementException("Missing nodes from graph: " + from + " to " + to);
				}

				graph.get(from).remove(to);
			}

			public boolean edgeExists(T from, T to) {
				if (!(graph.containsKey(from) && graph.containsKey(to))) {
					throw new NoSuchElementException("Missing nodes from graph: " + from + " to " + to);
				}

				return graph.get(from).contains(to);
			}

			public Set<T> edgesFrom(T from) {
				if (!graph.containsKey(from)) {
					throw new NoSuchElementException("Missing node from graph");
				}

				return Collections.unmodifiableSortedSet(graph.get(from));
			}

			@Override
			public Iterator<T> iterator() {
				return orderedNodes.iterator();
			}

			public int size() {
				return graph.size();
			}

			public boolean isEmpty() {
				return graph.isEmpty();
			}

			@Override
			public String toString() {
				return graph.toString();
			}
		}
	}

}
