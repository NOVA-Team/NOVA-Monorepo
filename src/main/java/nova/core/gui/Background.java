package nova.core.gui;

import nova.core.gui.layout.Constraints;
import nova.core.gui.layout.RelativePosition;
import nova.core.gui.render.Canvas;
import nova.core.gui.render.Graphics;
import nova.core.render.Color;
import nova.core.render.texture.Texture;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.Optional;

// TODO Document me!
/**
 * A background to apply to any {@link GuiComponent}.
 * 
 * @author Vic Nightfall
 * @see Constraints
 */
public class Background extends Constraints<Background> {

	public EnumFill xFill;
	public EnumFill yFill;
	public Optional<Texture> texture;
	public Color color = Color.white;
	public RelativePosition position;

	public Background(Texture texture, EnumFill xFill, EnumFill yFill, RelativePosition position) {
		this.texture = Optional.of(texture);
		this.xFill = xFill;
		this.yFill = yFill;
		this.position = position;
	}

	public Background(Texture texture, EnumFill xFill, EnumFill yFill) {
		this(texture, xFill, yFill, new RelativePosition(Vector2D.ZERO));
	}

	public Background(Texture texture, EnumFill xFill) {
		this(texture, xFill, EnumFill.DEFAULT);
	}

	public Background(Texture texture, RelativePosition position) {
		this.texture = Optional.of(texture);
		this.position = position;
	}

	public Background(Texture texture) {
		this(texture, EnumFill.DEFAULT, EnumFill.DEFAULT);
	}

	public Background(Color color) {
		this.texture = Optional.empty();
		this.xFill = this.yFill = EnumFill.DEFAULT;
		this.color = color;
		this.position = new RelativePosition(Vector2D.ZERO);
	}

	// TODO Test me!
	/**
	 * Draws the background to the given Graphics object. You might have to
	 * translate the underlying {@link Canvas} to the position of the component
	 * you want to draw the background onto.
	 * 
	 * @param graphics {@link Graphics} object to draw onto
	 * @param size boundaries of the background
	 */
	public void draw(Graphics graphics, Vector2D size) {
		// TODO Need to alter Canvas to save state and consider the fill enum
		if (texture.isPresent()) {
			// Draw texture
			Vector2D position = this.position.getPositionOf(size);
			graphics.drawTexture(position.getX(), position.getY(), size.getX() - position.getX(), size.getY() - position.getY(), texture.get(), color);
		} else {
			// Draw solid background color
			graphics.setColor(color);
			graphics.fillRect(0, 0, size.getX(), size.getY());
		}
	}

	// TODO Couldn't come up with a better name, might want to change this
	// before release
	public static enum EnumFill {
		REPEAT, FILL, DEFAULT
	}
}
