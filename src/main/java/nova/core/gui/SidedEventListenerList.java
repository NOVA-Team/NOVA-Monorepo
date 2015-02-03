package nova.core.gui;

import java.util.HashSet;

import nova.core.event.EventListener;
import nova.core.event.EventListenerHandle;
import nova.core.event.EventListenerList;
import nova.core.gui.GuiEvent.SidedEvent;
import nova.core.network.NetworkManager;
import nova.core.network.NetworkTarget.Side;

public class SidedEventListenerList<T> extends EventListenerList<T> {

	private NetworkEventProcessor eventProcessor;
	private boolean checkListenedBeforeSend = true;
	private HashSet<Class<?>> listenedNetworkEvents = new HashSet<Class<?>>();

	public SidedEventListenerList(NetworkEventProcessor eventProcessor) {
		this.eventProcessor = eventProcessor;
	}

	@Override
	public EventListenerHandle<T> add(EventListener<T> listener) {
		// Disables the checking mechanism as now there is a listener that
		// listens for everything on every side. Whoever called this clearly
		// doesn't care about network load or anything else.
		checkListenedBeforeSend = false;
		return super.add(listener);
	}

	@Override
	public <E extends T> EventListenerHandle<T> add(EventListener<E> listener, Class<E> clazz) {
		listenedNetworkEvents.add(clazz);
		return super.add(listener, clazz);
	}

	public <E extends T> EventListenerHandle<T> add(EventListener<E> listener, Class<E> clazz, Side sideToListen) {
		listenedNetworkEvents.add(clazz);
		return add(new SidedEventListener<E, T>(listener, clazz, sideToListen));
	}

	@Override
	public void publish(T event) {
		if (event instanceof SidedEvent) {
			SidedEvent sidedEvent = (SidedEvent) event;
			Side currentSide = NetworkManager.instance.get().getSide();

			// Check if the event targets the current side.
			if (sidedEvent.getTarget().targets(currentSide)) {
				super.publish(event);
			}

			// Check if the event needs to be sent over the network.
			if (currentSide.targets(sidedEvent.getTarget())) {
				boolean send = !checkListenedBeforeSend;
				Class<?> clazz = event.getClass();
				if (!send) {
					while (true) {
						if (listenedNetworkEvents.contains(clazz)) {
							send = true;
							break;
						}
						if (clazz == Object.class) {
							break;
						}
						clazz = clazz.getSuperclass();
					}
				}

				if (send) {
					listenedNetworkEvents.add(clazz);
					eventProcessor.handleEvent(sidedEvent);
				}
			}
		} else {
			super.publish(event);
		}
	}

	protected static class SidedEventListener<E extends T, T> extends SingleEventListener<E, T> {

		public final Side side;

		public SidedEventListener(EventListener<E> wrappedListener, Class<E> eventClass, Side side) {
			super(wrappedListener, eventClass);
			this.side = side;
		}

		@Override
		public void onEvent(T value) {
			if (value instanceof SidedEvent) {
				SidedEvent sidedEvent = (SidedEvent) value;
				if (sidedEvent.getTarget().targets(side)) {
					onEvent(value);
				}
			} else {
				onEvent(value);
			}
		}
	}

	@FunctionalInterface
	public static interface NetworkEventProcessor {
		/**
		 * Gets called if the parent {@link SidedEventListener} received an
		 * event that needs to be sent over the network.
		 * 
		 * @param event
		 */
		public void handleEvent(SidedEvent event);
	}
}
