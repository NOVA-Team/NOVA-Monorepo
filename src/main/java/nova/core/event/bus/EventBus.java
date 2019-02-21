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

package nova.core.event.bus;

import nova.internal.core.util.TopologicalSort;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * A general purpose event bus. This class is thread-safe and listeners can be
 * added or removed concurrently, no external locking is ever needed.
 * @param <T> event type
 * @author Stan Hebben, Calclavia
 */
public class EventBus<T> {
	public static final int PRIORITY_HIGH = 100;
	public static final int PRIORITY_DEFAULT = 0;
	public static final int PRIORITY_LOW = -100;

	// TODO: actually test concurrency
	protected final List<EventListenerNode> unsortedListeners = new ArrayList<>();
	private List<EventListenerNode> sortedListeners;

	/**
	 * Builds an ordered list cachedListeners. Sorts using topological sort algorithm.
	 */
	protected synchronized void buildCache() {
		TopologicalSort.DirectedGraph<EventListenerNode> graph = new TopologicalSort.DirectedGraph<>();

		unsortedListeners.forEach(graph::addNode);

		//Create directed graph edges.
		unsortedListeners.forEach(
			node -> {
				//Sort "after"
				node.after.forEach(
					name ->
						unsortedListeners
							.stream()
							.filter(node2 -> node2.name.equals(name))
							.findFirst()
							.ifPresent(dependent -> graph.addEdge(dependent, node))
				);

				//Sort "before"
				node.before.forEach(
					name ->
						unsortedListeners
							.stream()
							.filter(node2 -> node2.name.equals(name))
							.findFirst()
							.ifPresent(dependent -> graph.addEdge(node, dependent))
				);

				//Priority check
				unsortedListeners
					.stream()
					.filter(compare -> node.priority < compare.priority)
					.forEach(compare -> graph.addEdge(compare, node));
			}
		);

		sortedListeners = TopologicalSort.topologicalSort(graph);
	}

	/**
	 * Invalidates the sorted listeners. Call this after tweaking with unsortedListeners.
	 */
	protected synchronized void invalidateCache() {
		sortedListeners = null;
	}

	/**
	 * Retrieves the sorted listeners. Calls buildCache() if the listeners aren't sorted.
	 * @return The sorted listeners.
	 */
	protected synchronized List<EventListenerNode> getSortedListeners() {
		if (sortedListeners == null) {
			buildCache();
		}

		return sortedListeners;
	}

	public synchronized void clear() {
		unsortedListeners.clear();
		invalidateCache();
	}

	/**
	 * Removes an EventListener from the list.
	 * @param listener listener to be removed
	 * @return true if the listener was removed, false it it wasn't there
	 */
	public synchronized boolean remove(EventListener<T> listener) {
		boolean didRemove = unsortedListeners.removeIf(node -> node.getListener().equals(listener));

		if (didRemove) {
			invalidateCache();
		}
		return didRemove;
	}

	/**
	 * Checks if there are any listeners in this list.
	 * @return true if empty
	 */
	public boolean isEmpty() {
		return count() == 0;
	}

	public int count() {
		return unsortedListeners.size();
	}

	/**
	 * Publishes an event by calling all of the registered listeners.
	 * @param event event to be published
	 */
	public void publish(T event) {
		getSortedListeners()
			.stream()
			.forEachOrdered(node -> node.getListener().onEvent(event));
	}

	/**
	 * Retrieves the EventBinder object to bind an EventListener to ths EventBus that only accepts a specific subclass of &lt;T&gt;
	 * @param <E> The event type
	 * @return event listener's handle
	 */
	public <E extends T> EventBinder<E> on() {
		return new EventBinder<>(Optional.empty());
	}

	public <E extends T> EventBinder<E> on(Class<E> clazz) {
		return new EventBinder<>(Optional.of(clazz));
	}

	public class EventBinder<E extends T> {
		private final Optional<Class<E>> clazz;
		private int priority = PRIORITY_DEFAULT;
		private String name;
		private Set<String> before = new HashSet<>();
		private Set<String> after = new HashSet<>();

		public EventBinder(Optional<Class<E>> clazz) {
			this.clazz = clazz;
		}

		/**
		 * Sets the event's numeric priority.
		 * Numeric priority overrules named priority.
		 * @param priority An integer. The higher the number, the higher the priority.
		 * @return This
		 */
		public EventBinder<E> withPriority(int priority) {
			this.priority = priority;
			return this;
		}

		/**
		 * Sets the event to have a name.
		 * @param name The event name.
		 * @return This
		 */
		public EventBinder<E> withName(String name) {
			this.name = name;
			return this;
		}

		/**
		 * Sets the event to occur before another event with given name.
		 * @param name The other event name
		 * @return This
		 */
		public EventBinder<E> before(String name) {
			before.add(name);
			return this;
		}

		/**
		 * Sets the event to occur after another event with given name.
		 * @param name The other event name
		 * @return This
		 */
		public EventBinder<E> after(String name) {
			after.add(name);
			return this;
		}

		/**
		 * Binds the event to the {@link EventBus}, finalizing all modifiers on the event.
		 * @param list Event listener
		 * @return The event handler
		 */
		public synchronized EventListenerHandle<T> bind(EventListener<E> list) {
			@SuppressWarnings("unchecked")
			EventListener<T> listener = clazz.isPresent() ? new TypedEventListener<>(list, clazz.get()) : (EventListener) list;

			if (name != null && unsortedListeners.stream().filter(node -> node.name != null).anyMatch(node -> node.name.equals(name))) {
				throw new EventException("Duplicate event listener name: " + name);
			}

			EventListenerNode node = new EventListenerNode(listener, name, priority, before, after);

			unsortedListeners.add(node);
			invalidateCache();

			return node;
		}
	}

	// #########################
	// ### Protected classes ###
	// #########################

	/**
	 * A wrapper for an event listener that only accepts a specific type of
	 * event.
	 * @param <E> event type
	 * @param <T> super type
	 * @author Vic Nightfall
	 */
	protected static class TypedEventListener<E extends T, T> implements EventListener<T> {
		private final Class<E> eventClass;
		private final EventListener<E> wrappedListener;

		/**
		 * Constructs a new single typed Event listener.
		 * @param wrappedListener The listener which gets called when the event
		 * was accepted.
		 * @param eventClass The event to listen for, Any posted event that is
		 * an instance of said class will get passed through to the
		 * wrapped listener instance.
		 */
		public TypedEventListener(EventListener<E> wrappedListener, Class<E> eventClass) {
			this.eventClass = eventClass;
			this.wrappedListener = wrappedListener;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void onEvent(T event) {
			if (eventClass.isInstance(event)) {
				wrappedListener.onEvent((E) event);
			}
		}
	}

	protected class EventListenerNode implements EventListenerHandle<T> {
		protected final EventListener<T> listener;
		protected final int priority;
		protected final String name;
		protected final Set<String> before;
		protected final Set<String> after;

		public EventListenerNode(EventListener<T> handler, String name, int priority, Set<String> before, Set<String> after) {
			this.listener = handler;
			this.name = name;
			this.priority = priority;
			this.before = before;
			this.after = after;
		}

		@Override
		public EventListener<T> getListener() {
			return listener;
		}

		@Override
		public void close() {
			synchronized (EventBus.this) {
				unsortedListeners.remove(this);
				invalidateCache();
			}
		}
	}
}
