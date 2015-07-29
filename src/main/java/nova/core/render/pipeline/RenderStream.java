package nova.core.render.pipeline;

import nova.core.render.model.Model;

import java.util.function.Consumer;

/**
 * Handles model render pipeline.
 *
 * @author Calclavia
 */
public class RenderStream extends RenderTransmutation {
	public Consumer<Model> result = model -> {
	};

	public static <T extends RenderTransmutation> T of(T transmutator) {
		return new RenderStream().transmute(transmutator);
	}

	public RenderStream() {
		renderStream = this;
	}

	/**
	 * Applies an action to the result model
	 *
	 * @param consumer The action to apply.
	 * @return The RenderStream
	 */
	public RenderStream apply(Consumer<Model> consumer) {
		result.andThen(consumer);
		return this;
	}

	/**
	 * Applies an action to the result model
	 *
	 * @param transmutator The action to apply.
	 * @return The RenderStream
	 */
	public <T extends RenderTransmutation> T transmute(T transmutator) {
		transmutator.accept(this);
		return transmutator;
	}
}
