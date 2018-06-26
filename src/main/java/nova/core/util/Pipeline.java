package nova.core.util;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * A series of methods that transform an object as it is passed through the {@link Pipeline}.
 * @author Calclavia
 * @param <O> The pipeline type.
 */
public class Pipeline<O> {
	protected Optional<Pipeline<O>> prev = Optional.empty();

	protected Consumer<O> consumer = model -> {};

	/**
	 * Constructs an empty pipeline
	 */
	public Pipeline() {
	}

	/**
	 * Constructs a pipeline with a given function to act upon an object
	 * @param consumer The object
	 */
	public Pipeline(Consumer<O> consumer) {
		this.consumer = consumer;
	}

	/**
	 * Sets the given pipeline to be piped after this render stream.
	 * This method essentially allow you to switch between processes in the pipeline.
	 * @param <T> The new pipeline type.
	 * @param stream The stream to apply.
	 * @return The new RenderStream
	 */
	public <T extends Pipeline<O>> T apply(T stream) {
		stream.prev = Optional.of(this);
		return stream;
	}

	/**
	 * Builds the pipeline, concatenating the mutations into one single function.
	 * @return The pipeline function.
	 */
	public Consumer<O> build() {
		if (prev.isPresent()) {
			return prev.get().build().andThen(consumer);
		}

		return this.consumer;
	}
}