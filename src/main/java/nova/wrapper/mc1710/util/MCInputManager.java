package nova.wrapper.mc1710.util;

import nova.core.gui.InputManager;

import org.lwjgl.input.Keyboard;

/**
 * The MC KeyManager
 * 
 * @author Calclavia
 */
// TODO: Does not work yet. Need a full map between the input and LWGL input.
public class MCInputManager extends InputManager {

	@Override
	public void mapKeys() {
		// Map jlwgl input to NOVA Keys, slightly hacky but functional.
		for (Key key : Key.values()) {
			try {
				keys.put(Keyboard.class.getDeclaredField(key.name()).getInt(null), key);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean isKeyDown(Key key) {
		// TODO: Sync this with server side for server-side events. Need a
		// packet manager
		return Keyboard.isKeyDown(getNativeKeyCode(key));
	}
}
