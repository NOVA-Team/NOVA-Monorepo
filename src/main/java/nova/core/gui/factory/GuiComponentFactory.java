package nova.core.gui.factory;

import java.util.HashMap;
import java.util.function.Supplier;

import nova.core.gui.GuiComponent;
import nova.core.gui.nativeimpl.NativeGuiComponent;

public abstract class GuiComponentFactory {

	private HashMap<Class<? extends NativeGuiComponent>, Supplier<? extends NativeGuiComponent>> map = new HashMap<>();

	@SuppressWarnings("unchecked")
	public <T extends NativeGuiComponent> void applyNativeComponent(GuiComponent<?, T> component, Class<T> clazz) {
		component.setNativeComponent((T) map.get(clazz).get());
	}

	public <T extends NativeGuiComponent> void registerNativeComponent(Class<T> clazz, Supplier<T> supplier) {
		map.put(clazz, supplier);
	}
}
