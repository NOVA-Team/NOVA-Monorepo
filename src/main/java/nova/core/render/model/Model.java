package nova.core.render.model;

import nova.core.render.texture.Texture;
import nova.core.util.transform.Matrix4x4;
import nova.core.util.transform.MatrixStack;
import nova.core.util.transform.Vector2d;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A model is capable of containing multiple faces.
 *
 * @author Calclavia
 */
public class Model implements Cloneable {

	//The name of the model
	public final String name;
	/**
	 * A list of all the shapes drawn.
	 */
	public final Set<Face> faces = new HashSet<>();
	public final Set<Model> children = new HashSet<>();

	public Matrix4x4 matrix = Matrix4x4.IDENTITY;

	public Vector2d textureOffset = Vector2d.zero;

	public Model(String name) {
		this.name = Objects.requireNonNull(name, "Model name cannot be null!");
	}

	public Model() {
		this("");
	}

	/**
	 * Binds all the faces and all child models with this texture.
	 *
	 * @param texture The texture
	 */
	public void bind(Texture texture) {
		faces.forEach(f -> f.texture = Optional.of(texture));
		children.forEach(m -> m.bind(texture));
	}

	/**
	 * Starts drawing, by returning an Face for the Model to work on.
	 * Add vertices to this Face and finish it by calling drawFace()
	 *
	 * @return new {@link Face}
	 */
	public Face createFace() {
		return new Face();
	}

	/**
	 * Finish drawing the Face by adding it into the list of Faces.
	 *
	 * @param Face - The finished masterpiece.
	 */
	public void drawFace(Face Face) {
		faces.add(Face);
	}

	public Set<Model> flatten() {
		return flatten(new MatrixStack());
	}

	/**
	 * Flattens the model into a set of models with no additional transformations,
	 * applying all the transformations into the individual vertices.
	 *
	 * @param matrixStack transformation matrix.
	 * @return Resulting set of models
	 */
	public Set<Model> flatten(MatrixStack matrixStack) {
		Set<Model> models = new HashSet<>();

		matrixStack.pushMatrix();
		matrixStack.transform(matrix);
		//Create a new model with transformation applied.
		Model transformedModel = clone();
		transformedModel.faces.stream().forEach(f -> {
				f.normal = f.normal; //TODO normal matrix;
				f.vertices.forEach(v -> v.vec = matrixStack.transform(v.vec));
			}
		);

		models.add(transformedModel);
		//Flatten child models
		models.addAll(children.stream().flatMap(m -> m.flatten(matrixStack).stream()).collect(Collectors.toSet()));
		matrixStack.popMatrix();
		return models;
	}

	@Override
	protected Model clone() {
		Model model = new Model(name);
		model.faces.addAll(faces.stream().map(Face::clone).collect(Collectors.toSet()));
		model.children.addAll(children.stream().map(Model::clone).collect(Collectors.toSet()));
		model.matrix = matrix;
		model.textureOffset = textureOffset;
		return model;
	}
}
