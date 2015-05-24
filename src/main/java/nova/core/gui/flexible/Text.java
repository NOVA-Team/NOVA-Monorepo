package nova.core.gui.flexible;

import nova.core.component.ComponentProvider;

/**
 * An image specifies a texture to be rendered
 * @author Calclavia
 */
public class Text extends ComponentUI {

	private String text;

	public Text(ComponentProvider provider, String text) {
		super(provider);
		this.text = text;
	}

	@Override
	public String getID() {
		return "text";
	}
}
