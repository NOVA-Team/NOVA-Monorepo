package nova.core.gui;

import nova.core.event.CancelableEvent;
import nova.core.event.EventListener;
import nova.core.event.SidedEventBus.SidedEvent;
import nova.core.gui.factory.GuiFactory;
import nova.core.network.Sync;

/**
 * Event created by {@link GuiComponent}, is also a {@link SidedEvent}. Needs to
 * be registered with the {@link GuiFactory}.
 */
public abstract class ComponentEvent extends CancelableEvent implements SidedEvent {

	/**
	 * Not type-safe, use {@link ComponentEventListener} instead.
	 */
	@Deprecated
	public final GuiComponent<?, ?> component;

	public ComponentEvent(GuiComponent<?, ?> component) {
		this.component = component;
	}

	/**
	 * Specify to indicate which id {@link Sync} will use when getting
	 * serialized to a packet.
	 * 
	 * @return sync id
	 */
	public int getSyncID() {
		return 0;
	}

	/**
	 * {@link EventListener} interface used by {@link GuiComponent} to specify
	 * the component type. Preferred over using {@link #component} directly.
	 * 
	 * @author Vic Nightfall
	 * 
	 * @param <EVENT>
	 * @param <T>
	 * @see GuiComponent#onEvent(ComponentEventListener, Class)
	 * @see GuiComponent#onEvent(ComponentEventListener, Class,
	 *      nova.core.network.NetworkTarget.Side)
	 */
	@FunctionalInterface
	public static interface ComponentEventListener<EVENT extends ComponentEvent, T extends GuiComponent<?, ?>> extends EventListener<EVENT> {

		public void onEvent(EVENT event, T component);

		@SuppressWarnings("unchecked")
		@Override
		public default void onEvent(EVENT event) {
			onEvent(event, (T) event.component);
		}
	}

	@Cancelable
	public static class ActionEvent extends ComponentEvent {

		public ActionEvent(GuiComponent<?, ?> component) {
			super(component);
		}
	}
}
