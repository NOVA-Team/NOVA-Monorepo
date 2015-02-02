package nova.core.gui;

import nova.core.network.PacketReceiver;
import nova.core.network.PacketSender;

/**
 * Event for GUI, like mouse click
 */
public class GuiEvent {

	public static class MouseEvent extends GuiEvent {

		public final int mouseX;
		public final int mouseY;
		public final EnumMouseButton button;
		public final EnumMouseState state;

		public MouseEvent(int mouseX, int mouseY, EnumMouseButton button, EnumMouseState state) {
			this.mouseX = mouseX;
			this.mouseY = mouseY;
			this.button = button;
			this.state = state;
		}

		public static enum EnumMouseButton {
			LEFT, RIGHT, MIDDLE;
		}

		public static enum EnumMouseState {
			UP, DOWN, CLICK, DOUBLECLICK;
		}
	}

	public static class MouseWheelEvent extends GuiEvent {

		public final int scrollAmount;

		public MouseWheelEvent(int scrollAmount) {
			this.scrollAmount = scrollAmount;
		}
	}

	public static class KeyEvent extends GuiEvent {

		public final int keyCode;
		public final int character;
		public final EnumKeyState state;

		public KeyEvent(int keyCode, char character, EnumKeyState state) {
			this.keyCode = keyCode;
			this.character = character;
			this.state = state;
		}

		public static enum EnumKeyState {
			UP, DOWN, TYPE;
		}
	}

	public static abstract class GuiEventSynced extends GuiEvent implements PacketReceiver, PacketSender {

	}
}