package nova.core.gui;

public interface GuiLayout {

	public void reposition(GuiContainer container);

	public void add(GuiComponent element, GuiContainer parent, Object... properties);

	public void remove(GuiComponent element);

	public static class BorderLayout implements GuiLayout {

		@Override
		public void reposition(GuiContainer parent) {
			// TODO Auto-generated method stub

		}

		@Override
		public void add(GuiComponent element, GuiContainer parent, Object... properties) {
			// property = property == null ? BorderPropery.CENTER : property;
		}

		@Override
		public void remove(GuiComponent element) {
			// TODO Auto-generated method stub

		}

		public static enum BorderPropery {
			PAGE_START, PAGE_END, LINE_START, LINE_END, CENTER;
		}
	}
}
