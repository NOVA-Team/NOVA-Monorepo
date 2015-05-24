package nova.core.gui.flexible;

import nova.core.component.ComponentProvider;
import nova.core.render.texture.Texture;

/**
 * An image specifies a texture to be rendered
 * @author Calclavia
 */
public class Image extends ComponentUI {

	private final Texture texture;

	public Image(ComponentProvider provider, Texture texture) {
		super(provider);
		this.texture = texture;
	}

	@Override
	public String getID() {
		return "image";
	}
}
