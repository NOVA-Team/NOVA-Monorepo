package nova.core.gui.layout;

import nova.core.gui.AbstractGuiContainer;
import nova.core.gui.GuiComponent;
import nova.core.gui.Outline;
import nova.core.util.math.Vector2DUtil;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.Objects;

public abstract class AbstractGuiLayout<T extends Constraints<T>> implements GuiLayout {

	private final Class<T> constraintsClass;

	public AbstractGuiLayout(Class<T> constraintClass) {
		this.constraintsClass = constraintClass;
	}

	/**
	 * Adds a {@link GuiComponent} to this layout. The parameters have to match
	 * the {@link Constraints} required by this layout. (See generic type
	 * arguments for convenience) If there is only one argument passed and it is
	 * an instance of the needed constraint type, it will be passed directly to
	 * the layout. Otherwise, it will try to create one based on the supplied
	 * argument list.
	 *
	 * @param component The component to add to this layout.
	 * @param parent The parent container of this layout.
	 * @param parameters arguments passed to the {@link Constraints} or a
	 *        fitting instance of &lt;T&gt;
	 * @throws IllegalArgumentException if the Object array couldn't be
	 *         converted to the required constraint type.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public final void add(GuiComponent<?, ?> component, AbstractGuiContainer<?, ?> parent, Object... parameters) {
		Objects.requireNonNull(component);
		if (parameters.length == 1) {
			if (constraintsClass.isInstance(parameters[0])) {
				addImpl(component, parent, ((T) parameters[0]).clone());
			}
		}
		addImpl(component, parent, Constraints.createConstraints(constraintsClass, parameters));
	}

	protected abstract void addImpl(GuiComponent<?, ?> component, AbstractGuiContainer<?, ?> parent, T constraints);

	// TODO Document as needed by possible custom layouts.
	protected final Vector2D getPreferredSizeOf(GuiComponent<?, ?> component) {
		return component != null ? component.getPreferredSize().orElse(component.getMinimumSize().orElse(Vector2D.ZERO)) : Vector2D.ZERO;
	}

	protected final Vector2D getMaximumSizeOf(GuiComponent<?, ?> component) {
		return component != null ? component.getPreferredSize().orElse(component.getMaximumSize().orElse(Vector2DUtil.ONE)) : Vector2D.ZERO;
	}

	protected final Vector2D getMiniumSizeOf(GuiComponent<?, ?> component) {
		return component != null ? component.getMinimumSize().orElse(Vector2D.ZERO) : Vector2D.ZERO;
	}

	@SuppressWarnings("deprecation")
	protected final void setSizeOf(GuiComponent<?, ?> component, Vector2D size) {
		if (component != null) {
			component.setOutlineNative(component.getOutline().setDimension(size));
		}
	}

	@SuppressWarnings("deprecation")
	protected final void setPositionOf(GuiComponent<?, ?> component, Vector2D position) {
		if (component != null) {
			component.setOutlineNative(component.getOutline().setPosition(position));
		}
	}

	@SuppressWarnings("deprecation")
	protected final void setOutlineOf(GuiComponent<?, ?> component, Outline outline) {
		if (component != null) {
			component.setOutlineNative(outline);
		}
	}

	public T constraints() {
		try {
			return constraintsClass.newInstance();
		} catch (Exception e) {
			throw new LayoutException(e);
		}
	}
}
