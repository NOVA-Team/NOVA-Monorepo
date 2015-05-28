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
import nova.core.util.collection.Tuple2;
import nova.core.util.transform.shape.Cuboid;

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
	public final int[] sideRotMap = new int[] { 3, 4, 2, 5, 3, 5, 2, 4, 1, 5, 0, 4, 1, 4, 0, 5, 1, 2, 0, 3, 1, 3, 0, 2 };
	private final Block block;
	/**
	 * The mask the represents which sides the block should render its connected texture.
	 */
	public Supplier<Integer> sideMask;

	public ConnectedTextureRenderer(Block provider, Texture edgeTexture) {
		super(provider);
		this.block = provider;
		sideMask = () -> Arrays.stream(Direction.DIRECTIONS)
			.map(d -> new Tuple2<>(d, block.world().getBlock(block.position().add(d.toVector()))))
			.filter(kv -> kv._2.isPresent() && kv._2.get().getID().equals(block.getID()))
			.reduce(0, (b, a) -> b | (1 << a._1.ordinal()), (b, a) -> a | b);

		onRender = this::renderStatic;
		this.edgeTexture = edgeTexture;
	}

	protected void renderStatic(Model model) {
		//Render the block face
		BlockModelUtil.drawBlock(model, block);

		//Render the block edge
		Cuboid bounds = provider.get(Collider.class).boundingBox.get();

		for (int r = 0; r < 4; r++) {
			Direction relativeDir = Direction.DIRECTIONS[r];

			if ((sideMask.get() & (1 << relativeDir.ordinal())) == 0) {
				Direction absDir = Direction.fromOrdinal(sideRotMap[relativeDir.ordinal() << 2 | r]);

				if ((sideMask.get() & (1 << absDir.ordinal())) == 0) {
					Face face = BlockModelUtil.drawDir(absDir, model, bounds.min.x, bounds.min.y, bounds.min.z, bounds.max.x, bounds.max.y, bounds.max.z, StaticCubeTextureCoordinates.instance);
					face.texture = Optional.of(edgeTexture);
				}
			}
		}
	}
}
