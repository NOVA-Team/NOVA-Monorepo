package nova.core.block.component;

import nova.core.block.Block;
import nova.core.component.Require;
import nova.core.component.misc.Collider;
import nova.core.render.model.BlockModelUtil;
import nova.core.render.model.Face;
import nova.core.render.model.Model;
import nova.core.render.model.StaticCubeTextureCoordinates;
import nova.core.render.texture.Texture;
import nova.core.util.Direction;
import nova.core.util.math.RotationUtil;
import nova.core.util.shape.Cuboid;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Renders connected texture for a block.
 * @author Calclavia
 */
@Require(Collider.class)
public class ConnectedTextureRenderer extends StaticBlockRenderer {

	public final Texture edgeTexture;
	private final Block block;

	/**
	 * The mask the represents which sides the block should render its connected texture.
	 * E.g: 000000 will render all directions
	 */
	public Supplier<Integer> connectMask;

	/**
	 * A mask of which sides the connected texture renderer should render.
	 * Each bit corresponds to a direction.
	 * E.g: 000011 will render top and bottom
	 */
	public int faceMask = 0xff;

	public ConnectedTextureRenderer(Block provider, Texture edgeTexture) {
		super(provider);
		this.block = provider;
		connectMask = () ->
			Arrays.stream(Direction.DIRECTIONS)
				.filter(d -> block.world().getBlock(block.position().add(d.toVector())).get().sameType(block))
				.map(d -> 1 << d.ordinal())
				.reduce(0, (b, a) -> a | b);

		onRender = this::renderStatic;
		this.edgeTexture = edgeTexture;
	}

	protected void renderStatic(Model model) {
		//Render the block face
		BlockModelUtil.drawBlock(model, block);

		//Render the block edge
		for (Direction dir : Direction.DIRECTIONS)
			if ((faceMask & (1 << dir.ordinal())) != 0) {
				renderFace(dir, model);
			}
	}

	public ConnectedTextureRenderer setFaceMask(int faceMask) {
		this.faceMask = faceMask;
		return this;
	}

	//Apply connected texture on top face
	protected void renderFace(Direction direction, Model model) {
		for (int r = 0; r < 4; r++) {
			Cuboid bound = provider.get(Collider.class).boundingBox.get()
				.subtract(0.5) //Correct translation
				.add(direction.toVector().scalarMultiply(r * 0.0001d)); //Lift up texture slightly, preventing z-fighting

			Direction absDir = Direction.fromOrdinal(RotationUtil.rotateSide(direction.opposite().ordinal(), r));

			int mask = connectMask.get();
			if ((mask & (1 << absDir.ordinal())) == 0) {
				Model innerModel = new Model();
				innerModel.matrix.rotate(direction.toVector(), Math.PI / 2 * r);
				Face face = BlockModelUtil.drawDir(direction, innerModel, bound.min.getX(), bound.min.getY(), bound.min.getZ(), bound.max.getX(), bound.max.getY(), bound.max.getZ(), StaticCubeTextureCoordinates.instance);
				face.texture = Optional.of(edgeTexture);
				//TODO: Support colors
				model.children.add(innerModel);
			}
		}
	}
}
