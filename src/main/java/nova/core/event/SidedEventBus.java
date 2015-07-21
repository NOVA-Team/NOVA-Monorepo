package nova.core.event;

import nova.core.network.NetworkTarget;
import nova.core.network.NetworkTarget.Side;
import nova.core.network.Syncable;

import java.util.HashMap;

/**
 * {@link EventBus} that can differentiate {@link NetworkTarget NetworkTargets}
 * and allows registration of handlers that only listen on a specific
 * {@link Side}. <b>Remember to {@link Side#reduce() reduce} the scope of any
 * {@link SidedEvent} that was sent over the network!</b>
 *
 * @param <T> -Describe me-
 * @author Vic Nightfall
 */
public class SidedEventBus<T extends CancelableEvent> extends CancelableEventBus<T> {

	private NetworkEventProcessor eventProcessor;
	private boolean checkListenedBeforeSend = true;
	private HashMap<Class<?>, Side> listenedNetworkEvents = new HashMap<>();

	public SidedEventBus(NetworkEventProcessor eventProcessor) {
		this.eventProcessor = eventProcessor;
	}

	private void add(Class<?> clazz, Side side) {
		if (side == Side.NONE)
			throw new IllegalArgumentException("Can't specify a sided event without a scope!");
		Side side2 = listenedNetworkEvents.get(clazz);
		if (side2 != null) {
			if (side2 != Side.BOTH && side2 != side) {
				listenedNetworkEvents.put(clazz, Side.BOTH);
			}
		} else {
			listenedNetworkEvents.put(clazz, side);
		}
	}

	private boolean contains(Class<?> clazz, Side side) {
		Class<?> clazz2 = clazz;
		while (true) {
			Side side2 = listenedNetworkEvents.get(clazz2);
			if (side2 == side || side2 == Side.BOTH) {
				listenedNetworkEvents.put(clazz, side2);
				return true;
			}
			if (clazz2 == Object.class) {
				break;
			}
			clazz2 = clazz2.getSuperclass();
		}
		return false;
	}

	@Override
	@Deprecated
	public EventListenerHandle<T> add(EventListener<T> listener) {
		// Disables the checking mechanism as now there is a listener that
		// listens for everything on every side. Whoever called this clearly
		// doesn't care about network load or anything else.
		checkListenedBeforeSend = false;
		return super.add(listener);
	}

	@Override
	@Deprecated
	public EventListenerHandle<T> add(EventListener<T> listener, int priority) {
		checkListenedBeforeSend = false;
		return super.add(listener, priority);
	}

	@Override
	public <E extends T> EventListenerHandle<T> add(EventListener<E> listener, Class<E> clazz) {
		add(clazz, Side.BOTH);
		return super.add(listener, clazz);
	}

	@Override
	public <E extends T> EventListenerHandle<T> add(EventListener<E> listener, Class<E> clazz, int priority) {
		add(clazz, Side.BOTH);
		return super.add(listener, clazz, priority);
	}

	public <E extends T> EventListenerHandle<T> add(EventListener<E> listener, Class<E> clazz, Side sideToListen) {
		add(clazz, sideToListen);
		return super.add(new SidedEventListener<E, T>(listener, clazz, sideToListen), PRIORITY_DEFAULT);
	}

	public <E extends T> EventListenerHandle<T> add(EventListener<E> listener, Class<E> clazz, Side sideToListen, int priority) {
		add(clazz, sideToListen);
		return super.add(new SidedEventListener<E, T>(listener, clazz, sideToListen), priority);
	}

	@Override
	public void publish(T event) {
		if (event instanceof SidedEventBus.SidedEvent) {
			SidedEventBus.SidedEvent sidedEvent = (SidedEventBus.SidedEvent) event;
			Side currentSide = Side.get();

			if (sidedEvent.getTarget().opposite().targets(currentSide)) {
				super.publish(event);
			}

			// Check if the event needs to be sent over the network.
			if (currentSide.targets(sidedEvent.getTarget())) {
				boolean send = !checkListenedBeforeSend;
				if (!send) {
					send = contains(event.getClass(), currentSide.opposite());
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
	public interface NetworkEventProcessor {

		/**
		 * Gets called if the parent {@link SidedEventBus.SidedEventListener}
		 * received an event that needs to be sent over the network.
		 *
		 * @param event The event
		 */
		void handleEvent(SidedEvent event);
	}

	/**
	 * An event that specifies a {@link NetworkTarget}. Set the target by either
	 * overriding {@link #getTarget()} or by using the annotation
	 * {@link NetworkTarget} on the inherited class.
	 *
	 * @author Vic Nightfall
	 */
	public interface SidedEvent extends Syncable {

		default Side getTarget() {
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
				if (sidedEvent.getTarget().opposite().targets(side)) {
					super.onEvent(event);
				}
			} else {
				super.onEvent(event);
			}
		}
	}
}
