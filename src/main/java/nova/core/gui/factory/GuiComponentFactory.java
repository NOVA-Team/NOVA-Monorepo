package nova.core.gui.factory;

import nova.core.gui.GuiComponent;
import nova.core.gui.nativeimpl.NativeGuiComponent;

import java.util.HashMap;
import java.util.function.Function;

// TODO verify completeness, support for optional native components.
public abstract class GuiComponentFactory {

	private HashMap<Class<? extends NativeGuiComponent>, Function<GuiComponent<?, ?>, ? extends NativeGuiComponent>> map = new HashMap<>();

	@SuppressWarnings("unchecked")
	public <T extends NativeGuiComponent> void applyNativeComponent(GuiComponent<?, T> component, Class<T> clazz) {
		component.setNativeComponent((T) map.get(clazz).apply(component));
	}

	public <T extends NativeGuiComponent> void registerNativeComponent(Class<T> clazz, Function<GuiComponent<?, ?>, T> supplier) {
		map.put(clazz, supplier);
	}
}
