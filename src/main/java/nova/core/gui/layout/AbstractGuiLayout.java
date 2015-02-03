package nova.core.gui.layout;

import nova.core.gui.AbstractGuiContainer;
import nova.core.gui.GuiElement;

public abstract class AbstractGuiLayout<T extends LayoutConstraints> implements GuiLayout {

	private final Class<T> constraintsClass;

	public AbstractGuiLayout(Class<T> constraintClass) {
		this.constraintsClass = constraintClass;
	}

	/**
	 * Adds a {@link GuiElement} to this layout. The parameters have to match
	 * the {@link LayoutConstraints} required by this layout. (See generic type
	 * arguments for convenience) If there is only one argument passed and it is
	 * an instance of the needed constraint type, it will be passed directly to
	 * the layout. Otherwise, it will try to create one based on the supplied
	 * argument list.
	 * 
	 * @param element The element to add to this layout.
	 * @param parent The parent container of this layout.
	 * @param parameters arguments passed to the {@link LayoutConstraints} or a
	 *        fitting instance of &lt;T&gt;
	 * 
	 * @throws IllegalArgumentException if the Object array couldn't be
	 *         converted to the required constraint type.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void add(GuiElement<?> element, AbstractGuiContainer<?> parent, Object... parameters) {
		if (parameters.length == 1) {
			if (constraintsClass.isInstance(parameters[0])) {
				addImpl(element, parent, (T) parameters[0]);
			}
		}
		add(element, parent, LayoutConstraints.createConstraints(constraintsClass, parameters));
	}

	protected abstract void addImpl(GuiElement<?> element, AbstractGuiContainer<?> parent, T constraints);
}
