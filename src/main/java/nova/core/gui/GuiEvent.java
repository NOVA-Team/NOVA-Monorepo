package nova.core.gui;

import nova.core.block.Block;
import nova.core.entity.Entity;
import nova.core.event.CancelableEvent;
import nova.core.gui.KeyStroke.Key;
import nova.core.player.Player;
import nova.core.util.transform.Vector3i;
import nova.core.world.World;

import java.util.Optional;

/**
 * Event for GUI, like mouse click
 */
public class GuiEvent extends CancelableEvent {

	// TODO Document.
	// TODO Split this up into multiple events maybe?
	@Cancelable
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

	@Cancelable
	public static class MouseWheelEvent extends GuiEvent {

		public final int scrollAmount;

		public MouseWheelEvent(int scrollAmount) {
			this.scrollAmount = scrollAmount;
		}
	}

	@Cancelable
	public static class KeyEvent extends GuiEvent {

		public final Key key;
		public final int character;
		public final EnumKeyState state;

		public KeyEvent(Key key, char character, EnumKeyState state) {
			this.key = key;
			this.character = character;
			this.state = state;
		}

		public static enum EnumKeyState {
			UP, DOWN, TYPE;
		}
	}

	public static class ResizeEvent extends GuiEvent {

		public final Outline oldOutline;

		public ResizeEvent(Outline oldOutline) {
			this.oldOutline = oldOutline;
		}
	}

	public static class BindEvent extends GuiEvent {

		public final Gui gui;
		public final Entity entity;
		public final Player player;
		public final Vector3i position;
		public final World world;
		public final Optional<Block> block;

		public BindEvent(Gui gui, Entity entity, Vector3i position) {
			this.gui = gui;
			this.entity = entity;
			this.position = position;
			this.player = (Player) entity;
			this.world = entity.world();
			this.block = world.getBlock(position);
		}
	}

	public static class UnBindEvent extends GuiEvent {

		public final Gui gui;

		public UnBindEvent(Gui gui) {
			this.gui = gui;
		}
	}
}