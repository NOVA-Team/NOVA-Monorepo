package nova.core.gui;

import nova.core.event.SidedEventListenerList.SidedEvent;
import nova.core.network.Sync;

/**
 * Event created by {@link GuiComponent}, is also a {@link SidedEvent}. Needs to
 * be registered with the {@link GuiFactory}.
 */
public abstract class ComponentEvent<T extends GuiComponent<?, ?>> implements SidedEvent {

	public final T component;

	public ComponentEvent(T component) {
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

	public static class ActionEvent<T extends GuiComponent<?, ?>> extends ComponentEvent<T> {

		public ActionEvent(T component) {
			super(component);
		}
	}
}
