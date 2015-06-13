package nova.core.block.component;

import nova.core.block.Block;
import nova.core.component.renderer.StaticRenderer;
import nova.core.render.Color;
import nova.core.render.model.BlockModelUtil;
import nova.core.render.texture.Texture;
import nova.core.util.Direction;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * A static block renderer for blocks.
 * @author Calclavia
 */
public class StaticBlockRenderer extends StaticRenderer {
	/**
	 * Called to get the texture of this block for a certain side.
	 * side - The side of the block that the texture is for.
	 * Returns -  An optional of the texture.
	 */
	public Function<Direction, Optional<Texture>> texture = (dir) -> Optional.empty();
	/**
	 * Called when this block is to be rendered.
	 * Direction - The direction to render
	 * Returns -  true if the side should render
	 */
	public Function<Direction, Boolean> renderSide = (dir) -> true;

	/**
	 * Gets the color of a specific face. This is called by the default block
	 * renderer.
	 * direction - The side of the block.
	 * Returns the color
	 */
	public Function<Direction, Color> colorMultiplier = (dir) -> Color.white;

	public StaticBlockRenderer(Block provider) {
		super(provider);
		onRender = model -> BlockModelUtil.drawBlock(model, (Block) provider);
	}

	public StaticBlockRenderer setTexture(Function<Direction, Optional<Texture>> texture) {
		this.texture = texture;
		return this;
	}

	public StaticBlockRenderer setTexture(Texture t) {
		Objects.requireNonNull(t, "Texture is null, please initiate the texture before the block");
		this.texture = (dir) -> Optional.of(t);
		return this;
	}

	public StaticBlockRenderer setRenderSide(Function<Direction, Boolean> renderSide) {
		this.renderSide = renderSide;
		return this;
	}

	public StaticBlockRenderer setColorMultiplier(Function<Direction, Color> colorMultiplier) {
		this.colorMultiplier = colorMultiplier;
		return this;
	}
}
