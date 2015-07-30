package nova.core.render.model;

import nova.core.util.math.MatrixStack;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * A Model where the render method has no specific definition, except that it's a method.
 * Essentially this is a black box model where the definition is unspecified and may depend on the game.
 * The render method will not be called until it is actually being rendered at the instant.
 *
 * @author Calclavia
 */
public class CustomModel extends Model {

	public final Runnable render;

	public CustomModel(String name, Runnable render) {
		super(name);
		this.render = render;
	}

	public CustomModel(Runnable render) {
		this.render = render;
	}

	@Override
	protected Model newModel(String name) {
		return new CustomModel(name, render);
	}

	@Override
	public Set<Model> flatten(MatrixStack matrixStack) {
		return null;
	}
}
