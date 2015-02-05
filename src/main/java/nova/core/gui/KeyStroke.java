package nova.core.gui;

import java.util.Optional;

import com.google.common.collect.HashBiMap;

// TODO Make this a module.
/**
 * Maps native key strokes to the internal NOVA key enum.
 * 
 * @see nova.core.gui.KeyStroke.Key
 * @author Vic Nightfall
 */
public class KeyStroke {
	
	// TODO Hi there, NPE.
	public static KeyStroke instance;
	
	protected HashBiMap<Integer, Key> keys = HashBiMap.create(Key.values().length);
	
	/**
	 * Extend this in order to map native key codes to NOVA's key enum.
	 */
	public void mapKeys() {
		Key[] keyEnum = Key.values();
		for (int i = 0; i < keyEnum.length; i++) {
			keys.put(i, keyEnum[i]);
		}
	}
	
	public Key getKey(int nativeKeyCode) {
		return keys.getOrDefault(nativeKeyCode, Key.KEY_NONE);
	}
	
	public int getNativeKeyCode(Key key) {
		return keys.inverse().getOrDefault(key, 0);
	}
	
	public static enum Key {
		
		KEY_NONE, KEY_ESCAPE, 
		
		KEY_1, KEY_2, KEY_3, KEY_4, KEY_5, 
		KEY_6, KEY_7, KEY_8, KEY_9, KEY_0,
		
		KEY_Q, KEY_W, KEY_E, KEY_R, KEY_T,
		KEY_Y, KEY_U, KEY_I, KEY_O, KEY_P,
		KEY_A, KEY_S, KEY_D, KEY_F, KEY_G,
		KEY_H, KEY_J, KEY_K, KEY_L, KEY_Z, 
		KEY_X, KEY_C, KEY_V, KEY_B, KEY_N, 
		KEY_M,
		
		KEY_F1, KEY_F2, KEY_F3, KEY_F4, KEY_F5,
		KEY_F6, KEY_F7, KEY_F8, KEY_F9, KEY_F10,
		KEY_F11, KEY_F12,
		
		KEY_NUMPAD7, KEY_NUMPAD8, KEY_NUMPAD9, 
		KEY_NUMPAD4, LEY_NUMPAD5, KEY_NUMPAD6, 
		KEY_NUMPAD1, KEY_NUMPAD2, KEY_NUMPAD3,
		
		KEY_LEFT, KEY_UP, KEY_DOWN, KEY_RIGHT,
		
		KEY_RETURN, KEY_BACK, KEY_TAB, KEY_HOME,
		
		/**
		 * left alt key
		 */
		KEY_LMENU, 
		
		/**
		 * right alt key
		 */
		KEY_RMENU,
		
		/**
		 * left windows key / meta key
		 */
		KEY_LMETA, 
		
		/**
		 * right windows key / meta key
		 */
		KEY_RMETA,
		
		KEY_LSHIFT, KEY_RSHIFT,
		KEY_LCONTROL, KEY_RCONTROL,
		KEY_LBRACKET, KEY_RBRACKET,
		KEY_PRIOR, KEY_NEXT,
		
		KEY_COMMA, KEY_PERIOD, KEY_MINUS, 
		KEY_EQUALS, KEY_CAPITAL, KEY_SPACE,
		KEY_SEMICOLON, KEY_APOSTROPHE, KEY_GRAVE, 
		KEY_BACKSLASH, KEY_SLASH,
		KEY_MULTIPLY, KEY_NUMLOCK,
		KEY_SCROLL, KEY_ADD, KEY_SUBSTRACT
	}
}
