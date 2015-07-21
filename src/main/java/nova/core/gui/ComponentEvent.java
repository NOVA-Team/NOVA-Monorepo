package nova.core.gui;

import nova.core.event.CancelableEvent;
import nova.core.event.EventListener;
import nova.core.event.SidedEventBus.SidedEvent;
import nova.core.gui.factory.GuiEventFactory;
import nova.core.network.NetworkTarget.Side;
import nova.core.network.Sync;

/**
 * Event created by {@link GuiComponent}. These events, opposed to
 * {@link GuiEvent}, aren't propagated to the child components.
 * 
 * @see SidedComponentEvent
 * @see ComponentEventListener
 * @see GuiComponent#triggerEvent(ComponentEvent)
 * @see GuiComponent#onEvent(ComponentEventListener, Class)
 */
public abstract class ComponentEvent extends CancelableEvent {

	/**
	 * Not type-safe, use {@link ComponentEventListener} instead.
	 */
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

	/**
	 * {@link ComponentEvent} which can be sent over the network. Has to be
	 * registered with
	 * {@link GuiEventFactory#registerNetworkEvent(java.util.function.Function)}
	 * 
	 * @author Vic Nightfall
	 */
	public static class SidedComponentEvent extends ComponentEvent implements SidedEvent {

		public SidedComponentEvent(GuiComponent<?, ?> component) {
			super(component);
		}

		private Side target = SidedEvent.super.getTarget();

		@Override
		public Side getTarget() {
			return target;
		}

		public void reduceTarget() {
			target = target.reduce();
		}
	}

	public static class ActionEvent extends SidedComponentEvent {

		public ActionEvent(GuiComponent<?, ?> component) {
			super(component);
		}
	}

	public static class ResizeEvent extends ComponentEvent {

		public final Outline oldOutline;

		public ResizeEvent(GuiComponent<?, ?> component, Outline oldOutline) {
			super(component);
			this.oldOutline = oldOutline;
		}
	}

	public static class AddEvent extends ComponentEvent {

		public final AbstractGuiContainer<?, ?> container;

		public AddEvent(GuiComponent<?, ?> component, AbstractGuiContainer<?, ?> container) {
			super(component);
			this.container = container;
		}
	}

	public static class RemoveEvent extends ComponentEvent {

		public final AbstractGuiContainer<?, ?> container;

		public RemoveEvent(GuiComponent<?, ?> component, AbstractGuiContainer<?, ?> container) {
			super(component);
			this.container = container;
		}
	}
}
