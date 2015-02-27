package nova.core.event;

import java.util.HashSet;

import nova.core.network.NetworkTarget;
import nova.core.network.NetworkTarget.Side;
import nova.core.network.PacketHandler;

/**
 * {@link EventBus} that can differentiate {@link NetworkTarget NetworkTargets}
 * and allows registration of handlers that only listen on a specific
 * {@link Side}.
 *
 * @param <T> -Describe me-
 * @author Vic Nightfall
 */
public class SidedEventBus<T extends Cancelable> extends CancelableEventBus<T> {

	private NetworkEventProcessor eventProcessor;
	private boolean checkListenedBeforeSend = true;
	private HashSet<Class<?>> listenedNetworkEvents = new HashSet<Class<?>>();

	public SidedEventBus(NetworkEventProcessor eventProcessor) {
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
	public EventListenerHandle<T> add(EventListener<T> listener, int priority) {
		checkListenedBeforeSend = false;
		return super.add(listener, priority);
	}

	@Override
	public <E extends T> EventListenerHandle<T> add(EventListener<E> listener, Class<E> clazz) {
		listenedNetworkEvents.add(clazz);
		return super.add(listener, clazz);
	}

	@Override
	public <E extends T> EventListenerHandle<T> add(EventListener<E> listener, Class<E> clazz, int priority) {
		listenedNetworkEvents.add(clazz);
		return super.add(listener, clazz, priority);
	}

	public <E extends T> EventListenerHandle<T> add(EventListener<E> listener, Class<E> clazz, Side sideToListen) {
		listenedNetworkEvents.add(clazz);
		return add(new SidedEventListener<E, T>(listener, clazz, sideToListen));
	}

	public <E extends T> EventListenerHandle<T> add(EventListener<E> listener, Class<E> clazz, Side sideToListen, int priority) {
		listenedNetworkEvents.add(clazz);
		return add(new SidedEventListener<E, T>(listener, clazz, sideToListen), priority);
	}

	@Override
	public void publish(T event) {
		if (event instanceof SidedEventBus.SidedEvent) {
			SidedEventBus.SidedEvent sidedEvent = (SidedEventBus.SidedEvent) event;
			Side currentSide = Side.get();

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
							listenedNetworkEvents.add(event.getClass());
							break;
						}
						if (clazz == Object.class) {
							break;
						}
						clazz = clazz.getSuperclass();
					}
				}

				if (send) {
					eventProcessor.handleEvent(sidedEvent);
				}
			}
		} else {
			super.publish(event);
		}
	}

	@FunctionalInterface
	public static interface NetworkEventProcessor {

		/**
		 * Gets called if the parent {@link nova.core.event.SidedEventBus.SidedEventListener} received an
		 * event that needs to be sent over the network.
		 *
		 * @param event The event
		 */
		public void handleEvent(SidedEventBus.SidedEvent event);
	}

	/**
	 * An event that specifies a {@link NetworkTarget}. Set the target by either
	 * overriding {@link #getTarget()} or by using the annotation
	 * {@link NetworkTarget} on the inherited class.
	 *
	 * @author Vic Nightfall
	 */
	public static interface SidedEvent extends PacketHandler {

		public default Side getTarget() {
			NetworkTarget target = getClass().getAnnotation(NetworkTarget.class);
			return target != null ? target.value() : Side.BOTH;
		}
	}

	protected static class SidedEventListener<E extends T, T> extends SingleEventListener<E, T> {

		public final Side side;

		public SidedEventListener(EventListener<E> wrappedListener, Class<E> eventClass, Side side) {
			super(wrappedListener, eventClass);
			this.side = side;
		}

		@Override
		public void onEvent(T event) {
			if (event instanceof SidedEventBus.SidedEvent) {
				SidedEventBus.SidedEvent sidedEvent = (SidedEventBus.SidedEvent) event;
				if (sidedEvent.getTarget().targets(side)) {
					super.onEvent(event);
				}
			} else {
				super.onEvent(event);
			}
		}
	}
}
