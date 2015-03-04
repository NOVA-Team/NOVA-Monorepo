package nova.core.gui.nativeimpl;

import nova.core.entity.Entity;
import nova.core.gui.ComponentEvent;
import nova.core.gui.Gui;
import nova.core.gui.GuiEvent;
import nova.core.gui.GuiEvent.KeyEvent;
import nova.core.gui.GuiEvent.KeyEvent.EnumKeyState;
import nova.core.gui.GuiEvent.MouseEvent;
import nova.core.gui.GuiEvent.MouseEvent.EnumMouseButton;
import nova.core.gui.GuiEvent.MouseEvent.EnumMouseState;
import nova.core.gui.GuiEvent.MouseWheelEvent;
import nova.core.gui.KeyManager.Key;
import nova.core.gui.Outline;
import nova.core.gui.factory.GuiEventFactory;
import nova.core.gui.render.TextMetrics;
import nova.core.network.Packet;
import nova.core.util.transform.Vector3i;

public interface NativeGui extends NativeContainer {

	public void dispatchNetworkEvent(Packet packet);

	public TextMetrics getTextMetrics();

	public default void bind(Entity entity, Vector3i pos) {

	}

	public default void unbind() {

	}

	/**
	 * Called when the GUI was resized and the child components need to
	 * re-validate their layout. The new size has to be set before calling this.
	 *
	 * @param oldOutline Old {@link Outline}
	 */
	public default void onResized(Outline oldOutline) {
		getComponent().onEvent(new GuiEvent.ResizeEvent(oldOutline));
	}

	@SuppressWarnings("deprecation")
	public default void onNetworkEvent(Packet packet) {
		Gui gui = (Gui) getComponent();
		ComponentEvent event = GuiEventFactory.instance.constructEvent(packet, gui);
		event.reduceTarget();
		event.component.triggerEvent(event);
	}

	public default void onMousePressed(int mouseX, int mouseY, EnumMouseButton button, boolean state) {
		// TODO Post events for CLICK and DOUBLECLICK
		if (getComponent().isActive()) {
			getComponent().onEvent(new MouseEvent(mouseX, mouseY, button, state ? EnumMouseState.DOWN : EnumMouseState.UP));
		}
	}

	public default void onMouseWheelTurned(int scrollAmount) {
		if (getComponent().isActive()) {
			getComponent().onEvent(new MouseWheelEvent(scrollAmount));
		}
	}

	public default void onKeyPressed(Key key, char character, boolean state) {
		// TODO Post events for TYPE
		if (getComponent().isActive()) {
			getComponent().onEvent(new KeyEvent(key, character, state ? EnumKeyState.DOWN : EnumKeyState.UP));
		}
	}
}
