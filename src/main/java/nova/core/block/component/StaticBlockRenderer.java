package nova.core.block.component;

import nova.core.block.Block;
import nova.core.component.renderer.StaticRenderer;
import nova.core.render.Color;
import nova.core.render.model.BlockModelUtil;
import nova.core.render.model.Model;
import nova.core.render.texture.Texture;
import nova.core.util.Direction;

import java.util.Optional;

/**
 * @author Calclavia
 */
public class StaticBlockRenderer extends StaticRenderer {

	public StaticBlockRenderer(Block provider) {
		super(provider);
	}

	/**
	 * Called when this block is to be rendered.
	 * @param model A {@link nova.core.render.model.Model} to use.
	 */
	public void renderStatic(Model model) {
		BlockModelUtil.drawBlock(model, (Block) provider);

	}

	/**
	 * Called to get the texture of this block for a certain side.
	 * @param side The side of the block that the texture is for.
	 * @return An optional of the texture.
	 */
	public Optional<Texture> getTexture(Direction side) {
		return Optional.empty();
	}

	public boolean shouldRenderSide(Direction side) {
		return true;
	}

	/**
	 * Gets the color of a specific face. This is called by the default block
	 * renderer.
	 * @param side - The side of the block.
	 * @return The color
	 */
	public Color colorMultiplier(Direction side) {
		return Color.white;
	}
}
