package nova.core.gui.render.text;

import java.util.function.Consumer;

import nova.core.game.Game;

public final class TranslatedText extends FormattedText {

	public TranslatedText(String langKey) {
		super(langKey);
	}

	public TranslatedText(String langKey, TextFormat format) {
		super(langKey, format);
	}

	@Override
	public String getText() {
		return Game.getInstance().languageManager().translate(super.getText());
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
