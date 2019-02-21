package nova.internal.core.util;

import nova.core.util.exception.NovaException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class TopologicalSort {
	/**
	 * Sort the input graph into a topologically sorted list
	 *
	 * Uses the reverse depth first search as outlined in ...
	 * @param <T> The type
	 * @param graph The graph to sort
	 * @return The sorted list.
	 */
	public static <T> List<T> topologicalSort(DirectedGraph<T> graph) {
		DirectedGraph<T> rGraph = reverse(graph);
		List<T> sortedResult = new ArrayList<>();
		Set<T> visitedNodes = new HashSet<>();
		// A list of "fully explored" nodes. Leftovers in here indicate cycles in the graph
		Set<T> expandedNodes = new HashSet<>();

		for (T node : rGraph) {
			explore(node, rGraph, sortedResult, visitedNodes, expandedNodes);
		}

		return sortedResult;
	}

	public static <T> DirectedGraph<T> reverse(DirectedGraph<T> graph) {
		DirectedGraph<T> result = new DirectedGraph<>();

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

			throw new NovaException("There was a cycle detected in the input graph, sorting is not possible", node) {
				private static final long serialVersionUID = 1L;
			};
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
		private final Map<T, SortedSet<T>> graph = new HashMap<>();
		private List<T> orderedNodes = new ArrayList<>();

		public boolean addNode(T node) {
			// Ignore nodes already added
			if (graph.containsKey(node)) {
				return false;
			}

			orderedNodes.add(node);
			graph.put(node, new TreeSet<>((o1, o2) -> orderedNodes.indexOf(o1) - orderedNodes.indexOf(o2)));
			return true;
		}

		public boolean removeNode(T node) {
			// Ignore nodes not added
			if (!graph.containsKey(node)) {
				return false;
			}

			orderedNodes.remove(node);
			graph.remove(node);
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

		public Stream<T> stream() {
			return StreamSupport.stream(spliterator(), false);
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