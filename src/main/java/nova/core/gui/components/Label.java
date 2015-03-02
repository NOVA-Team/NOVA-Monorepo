package nova.core.gui.components;

import java.util.Optional;

import nova.core.gui.GuiComponent;
import nova.core.gui.nativeimpl.NativeGuiComponent;
import nova.core.gui.render.FormattedText;
import nova.core.gui.render.Graphics;
import nova.core.util.transform.Vector2i;

/**
 * A label is a component that defines a single piece of text. Labels have their
 * minimum size set to the boundaries of the provided {@link FormattedText}.
 * 
 * @author Vic Nightfall
 */
public class Label extends GuiComponent<Label, NativeGuiComponent> {

	private FormattedText text;

	public Label(String uniqueID, FormattedText text) {
		super(uniqueID, NativeGuiComponent.class);
		setText(text);
	}

	public Label(String uniqueID, String text) {
		this(uniqueID, FormattedText.parse(text));
	}

	public Label setText(String text) {
		this.text = FormattedText.parse(text);
		return this;
	}

	public Label setText(FormattedText text) {
		this.text = text;
		return this;
	}

	public FormattedText getText() {
		return text;
	}

	@Override
	public Optional<Vector2i> getMinimumSize() {
		Optional<Vector2i> inherited = super.getMinimumSize();
		if (inherited.isPresent())
			return inherited;
		if (getParentGui().isPresent()) {
			return Optional.of(getParentGui().get().getTextMetrics().getBounds(getText()));
		}
		return inherited;
	}

	@Override
	public void render(int mouseX, int mouseY, Graphics graphics) {
		super.render(mouseX, mouseY, graphics);
		graphics.drawString(0, 0, text);
	}
}
