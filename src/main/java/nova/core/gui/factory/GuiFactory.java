package nova.core.gui.factory;

import nova.core.gui.Gui;
import nova.core.util.Factory;

import java.util.function.Function;
import java.util.function.Supplier;

public class GuiFactory extends Factory<Gui> {

	public GuiFactory(Function<Object[], Gui> constructor) {
		super(constructor);
	}

	public GuiFactory(Supplier<Gui> supplier) {
		super(o -> supplier.get());
	}

	public Gui makeGUI(Object... args) {
		return constructor.apply(args);
	}
}
