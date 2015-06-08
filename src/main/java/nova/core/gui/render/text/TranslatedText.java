package nova.core.gui.render.text;

import nova.internal.core.Game;

import java.util.function.Consumer;

public final class TranslatedText extends FormattedText {

	public TranslatedText(String langKey) {
		super(langKey);
	}

	public TranslatedText(String langKey, TextFormat format) {
		super(langKey, format);
	}

	@Override
	public String getText() {
		return Game.language().translate(super.getText());
	}

	@Override
	public FormattedText add(FormattedText other) {
		child = other;
		other.first = this.first;
		return other;
	}

	@Override
	public FormattedText add(String text) {
		return add(new FormattedText(text));
	}

	@Override
	public FormattedText add(String text, Consumer<TextFormat> consumer) {
		TextFormat format = getFormat().clone();
		consumer.accept(format);
		return add(new FormattedText(text, format));
	}
}
