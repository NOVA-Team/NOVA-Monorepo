package nova.core.gui;

import nova.core.gui.layout.Constraints;
import nova.core.gui.layout.RelativePosition;
import nova.core.render.texture.Texture;
import nova.core.util.transform.Vector2i;

public class Background extends Constraints<Background> {

	public boolean xRepeat;
	public boolean yRepeat;
	public Texture texture;
	public RelativePosition position;

	public Background(Texture texture, boolean xRepeat, boolean yRepeat, RelativePosition position) {
		this.texture = texture;
		this.xRepeat = xRepeat;
		this.yRepeat = yRepeat;
		this.position = position;
	}

	public Background(Texture texture, boolean xRepeat, boolean yRepeat) {
		this(texture, xRepeat, yRepeat, new RelativePosition(Vector2i.zero));
	}

	public Background(Texture texture, boolean xRepeat) {
		this(texture, xRepeat, false);
	}

	public Background(Texture texture, RelativePosition position) {
		this.texture = texture;
		this.position = position;
	}

	public Background(Texture texture) {
		this.texture = texture;
	}
}
