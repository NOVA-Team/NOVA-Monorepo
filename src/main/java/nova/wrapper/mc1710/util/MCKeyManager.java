package nova.wrapper.mc1710.util;

import nova.core.gui.KeyManager;
import org.lwjgl.input.Keyboard;

/**
 * The MC KeyManager
 * @author Calclavia
 */
//TODO: Does not work yet. Need a full map between the keys and LWGL keys.
public class MCKeyManager extends KeyManager {
	@Override
	public boolean isKeyDown(Key key) {
		//TODO: Sync this with server side for server-side events. Need a packet manager
		return Keyboard.isKeyDown(getNativeKeyCode(key));
	}
}
