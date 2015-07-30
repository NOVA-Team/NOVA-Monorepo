package nova.core.render.pipeline;

import nova.core.render.model.Model;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * A render transmutation transforms a {@link RenderStream}.
 *
 * @author Calclavia
 */
public class RenderStream {

	protected Optional<RenderStream> prev = Optional.empty();

	protected Consumer<Model> consumer = model -> {
	};

	/**
	 * Applies a new RenderStream to the current stream.
	 * This method essentially allow you to switch between render streams in the rendering pipeline.
	 *
	 * @param stream The stream to apply.
	 * @return The new RenderStream
	 */
	public <T extends RenderStream> T apply(T stream) {
		stream.prev = Optional.of(this);
		return stream;
	}

	public Consumer<Model> build() {
		if (prev.isPresent()) {
			return prev.get().build().andThen(consumer);
		}

		return this.consumer;
	}
}
