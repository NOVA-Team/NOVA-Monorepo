package nova.wrapper.mc1710.asm.lib;

import java.util.Collection;

import nova.core.component.Component;
import nova.core.component.ComponentProvider;

public class ComponentInjector {

	private final Collection<? extends Component> componenets;

	public ComponentInjector(ComponentProvider provider) {
		this.componenets = provider.components();
	}

	public <T> Class<? extends T> inject(Class<T> clazz) {
		return clazz;
	}
}
