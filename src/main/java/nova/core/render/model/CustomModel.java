package nova.core.render.model;

import nova.core.util.math.MatrixStack;
import nova.core.util.math.TransformUtil;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * A Model where the render method has no specific definition, except that it's a method.
 * Essentially this is a black box model where the definition is unspecified and may depend on the game.
 * The render method will not be called until it is actually being rendered at the instant.
 *
 * @author Calclavia
 */
public class CustomModel extends Model {

	public final Consumer<CustomModel> render;

	public CustomModel(String name, Consumer<CustomModel> render) {
		super(name);
		this.render = render;
	}

	public CustomModel(Consumer<CustomModel> render) {
		this.render = render;
	}

	@Override
	protected Model newModel(String name) {
		return new CustomModel(name, render);
	}

	@Override
	public Set<Model> flatten(MatrixStack matrixStack) {
		Set<Model> models = new HashSet<>();

		matrixStack.pushMatrix();
		matrixStack.transform(matrix.getMatrix());
		//Create a new model with transformation applied.
		Model transformedModel = clone();
		transformedModel.matrix.loadMatrix(matrixStack.getMatrix());
		models.add(transformedModel);

		//Flatten child models
		models.addAll(children.stream().flatMap(m -> m.flatten(matrixStack).stream()).collect(Collectors.toSet()));
		matrixStack.popMatrix();
		return models;
	}
}
