package nova.core.gui;

/**
 * Event created by {@link GuiComponent}
 */
public abstract class ComponentEvent<T extends GuiComponent<?, ?>> {

	public final T component;

	public ComponentEvent(T component) {
		this.component = component;
	}

	public static class ActionEvent<T extends GuiComponent<?, ?>> extends ComponentEvent<T> {

		public ActionEvent(T component) {
			super(component);
		}
	}
}
