package nova.core.gui;

/**
 * Event created by {@link GuiElement}
 */
public abstract class GuiElementEvent {

	public final GuiElement<?> element;

	public GuiElementEvent(GuiElement<?> element) {
		this.element = element;
	}

	public static class ActionEvent extends GuiElementEvent {

		public ActionEvent(GuiElement<?> element) {
			super(element);
		}
	}
}
