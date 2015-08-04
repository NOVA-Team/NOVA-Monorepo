package nova.core.render.pipeline;

import nova.core.block.Block;
import nova.core.component.transform.BlockTransform;
import nova.core.render.model.Face;
import nova.core.render.model.Model;
import nova.core.render.model.MeshModel;
import nova.core.render.texture.Texture;
import nova.core.util.Direction;
import nova.core.util.math.RotationUtil;
import nova.core.util.shape.Cuboid;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;

public class ConnectedTextureRenderStream extends BlockRenderStream {
	public final Texture edgeTexture;

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

	public ConnectedTextureRenderStream(Block block, Texture edgeTexture) {
		super(block);
		this.edgeTexture = edgeTexture;

		connectMask = () -> {
			if (this.block.has(BlockTransform.class)) {
				return Arrays.stream(Direction.VALID_DIRECTIONS)
					.filter(d -> this.block.world().getBlock(this.block.position().add(d.toVector())).get().sameType(this.block))
					.map(d -> 1 << d.ordinal())
					.reduce(0, (b, a) -> a | b);
			}
			return 0x0;
		};

		consumer = model -> {
			//Render the block face
			MeshModel vModel = new MeshModel();
			draw(vModel);
			model.addChild(vModel);

			//Render the block edge
			for (Direction dir : Direction.VALID_DIRECTIONS)
				if ((faceMask & (1 << dir.ordinal())) != 0) {
					renderFace(dir, model);
				}
		};
	}

	public ConnectedTextureRenderStream withConnectMask(Supplier<Integer> connectMask) {
		this.connectMask = connectMask;
		return this;
	}

	public ConnectedTextureRenderStream withFaceMask(int faceMask) {
		this.faceMask = faceMask;
		return this;
	}

	//Apply connected texture on top face
	protected void renderFace(Direction direction, Model model) {
		for (int r = 0; r < 4; r++) {
			Cuboid bound = bounds.get()
				.subtract(0.5) //Correct translation
				.add(direction.toVector().scalarMultiply(r * 0.0001d)); //Lift up texture slightly, preventing z-fighting

			Direction absDir = Direction.fromOrdinal(RotationUtil.rotateSide(direction.opposite().ordinal(), r));

			int mask = connectMask.get();
			if ((mask & (1 << absDir.ordinal())) == 0) {
				MeshModel innerModel = new MeshModel();
				innerModel.matrix.rotate(direction.toVector(), Math.PI / 2 * r);
				Face face = drawDir(direction, innerModel, bound.min.getX(), bound.min.getY(), bound.min.getZ(), bound.max.getX(), bound.max.getY(), bound.max.getZ(), StaticCubeTextureCoordinates.instance);
				face.texture = Optional.of(edgeTexture);
				//TODO: Support colors
				model.children.add(innerModel);
			}
		}
	}
}