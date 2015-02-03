package nova.core.gui;

/**
 * Event created by {@link GuiComponent}
 */
public abstract class ComponentEvent {

	public final GuiComponent<?> component;

	public ComponentEvent(GuiComponent<?> element) {
		this.component = element;
	}

	public static class ActionEvent extends ComponentEvent {

		public ActionEvent(GuiComponent<?> component) {
			super(component);
		}
	}
}
